---
name: callee
description: Interact with the callee MCP server running at localhost:50900. Use this skill whenever you need to call sayMyName or sayMyOtherName on the callee service.
---

# Callee MCP Skill

Use the callee MCP tools to call the local callee service. The server runs at `http://localhost:50900/mcp`.

## Tool reference

| Tool | Description | Required parameter |
|------|-------------|-------------------|
| `mcp__callee__sayMyName` | Say the given name | `name` (string) |
| `mcp__callee__sayMyOtherName` | Say the given other name | `name` (string) |

## Standard workflow

1. **Load schemas** — call `ToolSearch` with `select:mcp__callee__sayMyName,mcp__callee__sayMyOtherName` before invoking either tool, as their schemas are deferred.
2. **Call the tool** — pass the `name` string parameter.
3. **Surface the result** — return the tool's response to the user.

## Notes

- The callee server must be running locally on port 50900 before these tools will succeed.
- Both tools accept a single required `name` parameter of type string.
