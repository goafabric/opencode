# service setup
when writing new services build them with spring and kotlin based on the following
- follow the build.gradle.kts inside `./specs/spring/build.gradle.kts` 
- follow the example service files inside the zipped file, which needs to be unzipped `./specs/spring/example.zip`
                                                                       
# requirements
- follow technical requirements inside `./specs/spring/technical-requirements.md`
- follow the requirements inside `./specs/spring/functional-requirements.md`

# phased approach with PLAN.md
- please create a PLAN.md upfront to separate the approach into multiple phases
- this PLAN.md could be verified by the human in the loop upfront
- it should also be possible to resume the code generation after a session was closed and restarted
