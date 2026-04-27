# ollama docker (3x slower due to missing m1 gpu support)
docker run --rm -v ~/.ollama:/root/.ollama -p 11434:11434 --name ollama ollama/ollama:0.21.2
docker exec -it ollama ollama run qwen3:1.7b   

# docker model runner
docker model run ai/smollm2
docker model ls
docker model ps

=> Models run accelerated on apple silicon, Models are spawned in a separate proccess

# docker sandboxes
sbx run opencode
sbx ls
sbx ps
                   
=> Currently highly unstable
