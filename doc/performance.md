# ollama
curl http://localhost:11434/api/v1/chat -H "Content-Type: application/json" -d '{
"model": "Qwen3.5-9B",
"messages": [{ "role": "user", "content": "Please create me a simple html" }]
}' | jq '.eval_count / (.eval_duration / 1e9)'


# lm studio
curl http://localhost:11434/v1/chat/completions -H "Content-Type: application/json" -d '{
"model": "Qwen3.5-9B-MLX",
"messages": [{ "role": "user", "content": "Please create me a simple html" }]
}' | jq '.eval_count / (.eval_duration / 1e9)'
