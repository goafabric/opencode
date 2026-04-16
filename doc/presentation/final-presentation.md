# expectation

- please create a presentation with marp mcp from `doc/presentation/final-presentation.md` and convert the output to a pdf
- the presentation should include all 3 sections and subsections for `basics`, `scenario 1`, `scenario 2`
- you should ommit the `expectation` section
- for the subsections of `basics` (eg. skills, hooks) please choose a matching image as company
- and we should also have a fitting image for the sections `aproach`, `result`, `takeaway`
- for the section `demo with simpsons agent` you should use the following image, but feel free to adjust the size `doc/presentation/homer-agent.png'
- the visual style should be rich, but the text segements should be small, rather generate more slides than having too much info on one page
- you may compact the text if desired

# basics

## agent.md
Defines the agent’s identity, goals, behavior, and operating instructions

## hooks
Event-driven triggers that let you run logic before or after specific actions

## permissions
Controls what the agent is allowed to access or execute

## agents
Modular, reusable AI units with specific roles or capabilities

## skills
Packaged abilities the agent can use to perform specialized tasks

## slash commands
Shortcut commands that invoke predefined actions via simple text inputs

# mcp
Model Context Protocol - A standard for connecting agents to external tools, data sources, and services
This presentation was generated with marp-mcp ;-)

## harness
Packaging all of the above

# demo with simpsons agent

# scenario 1

## description

- Building a Laboratory Observation Service with Quarkus
                                             
## aproach

pick the right model: claude sonet  

create a folder specs with
- a zipped example service that the model can infer
- provide brief technical requirements by describing the service
- provide functional requirements
- ask the model to do a phased approach, by creating a PLAN.md first

## result

- generation of a first running vertical slice was done after about 20 mins
- but after ommitting the "extensions" in phase 1, the model had troubles getting the test to work
- the reason: instead of using PanacheRepository from the examples it used Jakarta Repositories that come with limitations

## takeaway
- adding an explicit directive to always using "PanacheRepository" to the technical requirements
- this refinement for future runs is called "harness engineering"

# scenario 2

## description

conversion of spring boot services to quarkus

## approach

- very similar approach to scenario 1 with a specs folder
- one example just converting the person service
- another example converting our simple contact service

## result

- the conversion of person-service worked rather good
- which is no wander given the fact that the model basically already had the target at hand as example.zip

- the conversion of contact service worked less smooth
- while the code conversion itself worked
- the model struggled a lot getting the tests running, i finally paused it after 60 mins of not being successfull

## takeway

- the model can help a lot with the conversion of rest controller and persistence
- but the final result and also the tests may still need manual human in the loop fixes