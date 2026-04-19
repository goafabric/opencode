# ollama
curl http://localhost:11434/api/chat -d '{
"model": "qwen3.5:9b-nvfp4",
"messages": [{ "role": "user", "content": "Please create me a simple html" }]
}' | jq '.eval_count / (.eval_duration / 1e9)'

# llm studio
just do it from the chat and reveal the file in finder, it will contain token/s

# results
                                                    
## ollama
qwen3.5:9b       : 18 token/s, 10+ GB Ram
qwen3.5:9b-nvfp4 : 20,3 token/s , 7,7 GB Ram

## lm studio
qwen3.5:mlx      : 26,6 token/s, 6,5 GB Ram