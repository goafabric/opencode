# launch opencode
ollama launch opencode --config

# launch pi
ollama launch pi --config

# launch claude
export CLAUDE_CODE_USE_FOUNDRY=0 && ollama launch claude --config

# opencode in container
container run --cpus 6 --memory 1024m --rm --name opencode -v "$HOME/.config/opencode:/root/.config/opencode" -v "$HOME/.local/share/opencode:/root/.local/share/opencode" -v "${PWD}:/workspace" \
-it ghcr.io/anomalyco/opencode:1.14.30