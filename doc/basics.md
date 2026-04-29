# basics
## artificial intelligence
very generic termin that can mean a lot of different things

- machine learning
- reinforcement learning
- usage of LLMs to generate code
- usage of LLMs inside code

## machine learning
- discipline in the field of ai of training and creating (large language) models with training data

## reinforcement learning
- discipline in the field of ai where the model "trains itself" by executing tasks with a reward / penalty system to figure out an optimal strategy
- simple example : super mario, reward: collecting coins, shortest time, penalty: "death"
- complex example : training autonomous driving or robotic movement in a virtual environment

## large language model (LLM)
- a computational model designed to perform natural language processing tasks
- pretrained by data provided via MACHINE LEARNING
- basically returns results based on the statistically most propable matching result between input and training data
- if there is no training data, models can start to hallucinate
                                                                     
## small language model (SLM)
- similar to LLMs just trained with much less data, so they can be executed on local hardware
- usually less powerfull then LLMs in the cloud aka "frontier models"

## context window
- information provided to the LLM
- usually consists of system prompt, tools, local code base
- context window is limited, will fill up over time, which can lead to context exhaustion
  
# skills and tools (mostly markdown files)
          
## skill
- basically a markdown file describing additional functionality

## command
- basically a skill that can be executed via "/command"

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
           
# coding (aka harness)

## coding cli
- options: claude code, opencode, codex, pi
- how they work: 
  - often a bunch of typescript code, essentially doing rest calls against the LLM
  - adding a system prompt and tools (e.g. read, write, bash) to the CONTEXT WINDOW, and that's the main difference between all of them

## coding desktop app
- options: windsurf, copilot, claude desktop
- how they work:
  - essentially in the same way as coding cli
  - just live inside your IDE as a plugin or inside their own IDE (often just a VSCode fork)


