# launch opencode
ollama launch opencode --config

# launch pi
ollama launch pi --config

# launch claude
export CLAUDE_CODE_USE_FOUNDRY=0 && ollama launch claude --config

# opencode in container
container run --cpus 6 --memory 1024m --rm --name opencode --dns 8.8.8.8 -v "$HOME/.config/opencode:/root/.config/opencode" -v "$HOME/.local/share/opencode:/root/.local/share/opencode" -v "${PWD}:/workspace" -w /workspace \
-it ghcr.io/anomalyco/opencode:1.18.8 --model anthropic/claude-sonnet-4-6-1
  
# TurboFieldFare
https://github.com/Pummelchen/NVMAI/wiki/Getting-Started

defaults write TinyTitan model qwen36

~/IdeaProjects/xplayground/tinytitan/TinyTitanServer \
--model ~/Library/Application\ Support/TinyTitan/qwen3.6_35B_A3B_4Bit --port 8080 --max-context 65536 --ram-budget 5g
