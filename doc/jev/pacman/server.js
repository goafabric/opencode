#!/usr/bin/env node
"use strict";

/**
 * Tiny zero-dependency HTTP server that:
 *  - serves pacman.html
 *  - receives a live state snapshot pushed by the running page (POST /api/sync)
 *  - exposes that snapshot for external readers, e.g. a jev-style decision
 *    model (GET /api/state, GET /api/decision)
 *  - lets an external caller drive Pac-Man via plain curl (POST /api/move,
 *    POST /api/control), without touching the keyboard controls at all
 *
 * Usage:
 *   node server.js            # listens on http://localhost:8787
 *   PORT=9000 node server.js  # custom port
 */

const http = require("http");
const fs = require("fs");
const path = require("path");
const { URL } = require("url");

const PORT = process.env.PORT ? Number(process.env.PORT) : 8787;
const HTML_PATH = path.join(__dirname, "pacman.html");

const DIRECTIONS = new Set(["up", "down", "left", "right"]);
const ACTIONS = new Set(["pause", "resume", "restart"]);

// -------------------------------------------------------- autoplay config
// By default the server itself drives Pac-Man: it repeatedly asks the
// jev/ollama decision model (POST OLLAMA_URL) for the next move and applies
// it. There is no built-in fallback — if ollama is unreachable, autoplay
// simply queues no move until it answers again. Toggle with POST
// /api/autoplay or the on-page button; keyboard input always still works
// alongside it.
const OLLAMA_URL = process.env.OLLAMA_URL || "http://localhost:11434/v1/systemone";
const OLLAMA_MODEL = process.env.OLLAMA_MODEL || "tev1:4b";
const OLLAMA_TIMEOUT_MS = process.env.OLLAMA_TIMEOUT_MS ? Number(process.env.OLLAMA_TIMEOUT_MS) : 1500;
const AUTOPLAY_INTERVAL_MS = process.env.AUTOPLAY_INTERVAL_MS ? Number(process.env.AUTOPLAY_INTERVAL_MS) : 400;

let autoplayEnabled = process.env.AUTOPLAY !== "off";
let autoplayInfo = { lastDirection: null, lastError: null, lastAt: 0 };

// ---------------------------------------------------------------- state
let latestState = null;      // last snapshot pushed by the browser via /api/sync
let lastSyncAt = 0;          // Date.now() of that push
let pendingDirection = null; // next direction command for the browser to pick up
let pendingActions = [];     // queued control actions (pause/resume/restart)

// ---------------------------------------------------------------- helpers
function send(res, status, body, contentType) {
  const payload = typeof body === "string" ? body : JSON.stringify(body, null, 2);
  res.writeHead(status, {
    "Content-Type": contentType || "application/json",
    "Access-Control-Allow-Origin": "*",
    "Access-Control-Allow-Methods": "GET, POST, OPTIONS",
    "Access-Control-Allow-Headers": "Content-Type"
  });
  res.end(payload);
}

function readJsonBody(req) {
  return new Promise((resolve, reject) => {
    let data = "";
    req.on("data", chunk => {
      data += chunk;
      if (data.length > 2_000_000) {
        req.destroy();
        reject(new Error("payload too large"));
      }
    });
    req.on("end", () => {
      if (!data) return resolve({});
      try { resolve(JSON.parse(data)); }
      catch (e) { reject(e); }
    });
    req.on("error", reject);
  });
}

// Builds a request body shaped like the jev / ollama "systemone" examples,
// ready to be curled straight at a decision model. The model only ever sees
// currently-legal moves as choice criteria.
function buildDecisionPrompt() {
  if (!latestState) return null;
  const s = latestState;
  const legal = s.pacman.legalMoves || [];

  const ghostLines = (s.ghosts || []).map(g => {
    const tag = g.status === "frightened" ? "EDIBLE"
      : g.status === "eaten" ? "returning to house (harmless)"
      : g.status === "in_house" ? "still in house (harmless)"
      : "DANGEROUS";
    return `${g.name} at tile (${g.tileX},${g.tileY}), ${tag}, ` +
      `${g.distanceToPacman.toFixed(1)} tiles away, moving ${g.direction}`;
  }).join("; ");

  const stateText =
    `Pac-Man is at tile (${s.pacman.tileX},${s.pacman.tileY}) moving ${s.pacman.direction}. ` +
    `Game phase: ${s.gameState}. Score ${s.score}, lives ${s.lives}, level ${s.level}, ` +
    `${s.pelletsRemaining} pellets remaining. ` +
    `Legal moves right now: ${legal.join(", ") || "none"}. ` +
    `Ghosts: ${ghostLines || "none"}. ` +
    `Local 7x7 view centered on Pac-Man (@):\n${(s.surroundings && s.surroundings.rows || []).join("\n")}`;

  const criteria = {};
  for (const dir of (legal.length ? legal : ["up", "down", "left", "right"])) {
    criteria[dir] = `Move ${dir}`;
  }

  return {
    model: "tev1:4b",
    state: stateText,
    questions: {
      move: {
        type: "choice",
        instructions: "Which direction should Pac-Man move next? Prefer eating pellets " +
          "and power pellets, avoid DANGEROUS ghosts, chase EDIBLE ghosts when safe.",
        criteria
      }
    }
  };
}

// --------------------------------------------------------- decision engine
async function callOllama(prompt) {
  const controller = new AbortController();
  const timer = setTimeout(() => controller.abort(), OLLAMA_TIMEOUT_MS);
  try {
    const res = await fetch(OLLAMA_URL, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ ...prompt, model: OLLAMA_MODEL }),
      signal: controller.signal
    });
    if (!res.ok) throw new Error(`ollama http ${res.status}`);
    const data = await res.json();
    const choice = data && data.answers && data.answers.move && data.answers.move.choice;
    if (!DIRECTIONS.has(choice)) throw new Error("ollama response had no usable move.choice");
    return choice;
  } finally {
    clearTimeout(timer);
  }
}

async function autoplayTick() {
  if (autoplayEnabled && latestState) {
    const prompt = buildDecisionPrompt();
    let direction = null, error = null;

    if (prompt) {
      try {
        direction = await callOllama(prompt);
      } catch (err) {
        error = String((err && err.message) || err);
      }
    }

    if (direction) pendingDirection = direction;
    if (latestState.gameState === "gameover" && !pendingActions.includes("restart")) {
      pendingActions.push("restart"); // keep the self-play demo going indefinitely
    }

    autoplayInfo = { lastDirection: direction, lastError: error, lastAt: Date.now() };
    // One line per move: ollama was called, and what came back.
    console.log(error ? `[ollama] call failed: ${error}` : `[ollama] -> ${direction}`);
  }
  setTimeout(autoplayTick, AUTOPLAY_INTERVAL_MS);
}

// ---------------------------------------------------------------- server
const server = http.createServer(async (req, res) => {
  const url = new URL(req.url, `http://${req.headers.host || "localhost"}`);
  const { pathname } = url;

  if (req.method === "OPTIONS") return send(res, 204, "");

  try {
    if (req.method === "GET" && (pathname === "/" || pathname === "/pacman.html")) {
      const html = fs.readFileSync(HTML_PATH, "utf8");
      return send(res, 200, html, "text/html; charset=utf-8");
    }

    // Internal: the page pushes its live state and picks up queued commands.
    if (req.method === "POST" && pathname === "/api/sync") {
      const body = await readJsonBody(req);
      latestState = body;
      lastSyncAt = Date.now();
      const cmd = {
        direction: pendingDirection,
        actions: pendingActions,
        autoplay: { enabled: autoplayEnabled, error: autoplayInfo.lastError }
      };
      pendingDirection = null;
      pendingActions = [];
      return send(res, 200, cmd);
    }

    // External: read-only game state for a human or a decision model.
    if (req.method === "GET" && pathname === "/api/state") {
      if (!latestState) return send(res, 503, { ok: false, error: "no game page connected yet" });
      return send(res, 200, { ok: true, ageMs: Date.now() - lastSyncAt, state: latestState });
    }

    // External: ready-to-curl jev/ollama-style decision request body.
    if (req.method === "GET" && pathname === "/api/decision") {
      const prompt = buildDecisionPrompt();
      if (!prompt) return send(res, 503, { ok: false, error: "no game page connected yet" });
      return send(res, 200, prompt);
    }

    // External: drive Pac-Man's next direction, e.g.
    //   curl -X POST localhost:8787/api/move -d '{"direction":"up"}'
    if (req.method === "POST" && pathname === "/api/move") {
      const body = await readJsonBody(req);
      const dir = body.direction;
      if (!DIRECTIONS.has(dir)) {
        return send(res, 400, { ok: false, error: "direction must be one of up/down/left/right" });
      }
      pendingDirection = dir;
      return send(res, 200, { ok: true, queued: dir });
    }

    // External: pause/resume/restart, e.g.
    //   curl -X POST localhost:8787/api/control -d '{"action":"restart"}'
    if (req.method === "POST" && pathname === "/api/control") {
      const body = await readJsonBody(req);
      const action = body.action;
      if (!ACTIONS.has(action)) {
        return send(res, 400, { ok: false, error: "action must be one of pause/resume/restart" });
      }
      pendingActions.push(action);
      return send(res, 200, { ok: true, queued: action, pending: pendingActions.slice() });
    }

    // Toggle/inspect the self-play loop, e.g.
    //   curl -X POST localhost:8787/api/autoplay -d '{"enabled":false}'
    if (req.method === "GET" && pathname === "/api/autoplay") {
      return send(res, 200, { enabled: autoplayEnabled, ...autoplayInfo, ollamaUrl: OLLAMA_URL, model: OLLAMA_MODEL });
    }
    if (req.method === "POST" && pathname === "/api/autoplay") {
      const body = await readJsonBody(req);
      if (typeof body.enabled === "boolean") autoplayEnabled = body.enabled;
      return send(res, 200, { enabled: autoplayEnabled });
    }

    return send(res, 404, { ok: false, error: "not found" });
  } catch (err) {
    return send(res, 500, { ok: false, error: String((err && err.message) || err) });
  }
});

server.listen(PORT, () => {
  /* eslint-disable no-console */
  console.log(`Pac-Man server running at http://localhost:${PORT}`);
  console.log(`  Open the game (it plays itself by default): http://localhost:${PORT}/`);
  console.log(`  Query state:            curl http://localhost:${PORT}/api/state`);
  console.log(`  Get decision prompt:    curl http://localhost:${PORT}/api/decision`);
  console.log(`  Move Pac-Man:           curl -X POST http://localhost:${PORT}/api/move -d '{"direction":"up"}'`);
  console.log(`  Pause/resume/restart:   curl -X POST http://localhost:${PORT}/api/control -d '{"action":"pause"}'`);
  console.log(`  Toggle autoplay:        curl -X POST http://localhost:${PORT}/api/autoplay -d '{"enabled":false}'`);
  console.log(`  Autoplay requires ollama at ${OLLAMA_URL} (model ${OLLAMA_MODEL}). No moves are queued while it is unreachable.`);
});

autoplayTick();
