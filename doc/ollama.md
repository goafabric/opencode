# ollama docker (3x slower due to missing m1 gpu support)
docker run --rm -v ~/.ollama:/root/.ollama -p 11434:11434 --name ollama ollama/ollama:0.21.2
docker exec -it ollama ollama run qwen3:1.7b   