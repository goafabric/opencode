# intro

- ollama can be downloaded from the internet  
- is based on popular ollama cpp cli              

ollama ls

- explain the models 
  - different sizes qwen3:1.7b, qwen3.5:9b
  - qwen is currently best for coding
  
# run qwen3:1.7b

ollama run qwen3:1.7b

ollama ps

- explain the size in memory
- explain the 100% gpu vs 100% cpu
- context window of 40960, short term memory, will be explained later
- explain the advantage of apple silicon 
  - macbook m5 with 24gb ram and 10 core cpu
  - unified memory for cpu and gpu => 24gb ram for a 
  - vs buying a dedicated graphics card with 16gb ram
  - => model fits perfectly into our gpu ram 
  - could be double cheked with activity monitor

# chat
"please create a simple html file"

"write the file to a local index.html"

- explain why its not written to disk
- explain tools like read, write

# api calls
- explain that ollama is also an llm server

curl http://localhost:11434/api/chat -d '{
"model": "qwen3:1.7b",
"messages": [{ "role": "user", "content": "Please create me a simple html" }]
}' | jq '.eval_count / (.eval_duration / 1e9)'


# opencode

- explain opencode cli
  - basically doing rest calls
  - and providing tools like read, write out of the box
  - claude caude is similar and could also be used


ollama launch opencode --config

"please create a simple html file"

- explain why its automatically written to disk

# run qwen3.5:9b

ollama stop qwen3:1.7b

ollama run qwen3.5:9b 

ollama ps

# opencode 

ollama launch opencode --config

"please create a simple html file"

- explain why its now basically running forever
  - Time to First Token (TTFT) 
  - all the tools need to be put inside the context window 
  - which is now talking much longer with the more powerful model
  - after first initialization, the model will perform faster upon every subsequent request

# pi

ollama launch pi --config

"please create a simple html file"

- explain why its now answering much faster
  - only 3 tools inside the context windows (read, write, bash)
                           
- explain to theoretically build an entire service with spring boot

- explain the downside, e.g. missing planning tool that will divide the coding into multiple steps
         
- explain context window config in ollam