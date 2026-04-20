# basics
## artificial intelligence
very generic termin that can mean a lot of different things

- machine learning
- usage of LLMs to generate code
- usage of LLMs inside code

## machine learning
- discipline in the field of ai of training and creating (large language) models with training data

# #large language model (LLM)
- a computational model designed to perform natural language processing tasks
- pretrained by data provided via MACHINE LEARNING
- basically returns results based on the statistically most propable matching result between input and training data
- if there is no training data, models can start to hallucinate

## context window
- information provided to the LLM
- usually consists of system prompt, tools, local code base
- context window is limited, will fill up over time, which can lead to context exhaustion
  
# skills and tools
          
## skill
- basically a markdown file describing additional functionality
                           
## agent.md
- basically a markdown file describing a specific behaviour

## system prompt
- basically a markdown file describing the overall behaviour 

## mcp server
- a piece of code / deployable that adds additional functionality and provides them via a standardized MCP contract
- can be called locally (e.g. npx -y) or remote (agent2agent)

## tools
- additional context that can provide additional functionality to the LLM
- often skills, mcp ....
           
# coding

## coding cli
- options: claude code, opencode, codex
- how they work: 
  - often a bunch of typescript code, essentially doing rest calls against the LLM
  - adding a system prompt and tools (e.g. read, write, bash) to the CONTEXT WINDOW


