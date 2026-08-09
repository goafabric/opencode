# service setup
when writing new services build them with quarkus and kotlin based on the following
- follow the build.gradle.kts inside `./spec/simple/build.gradle.kts` 
                                                                       
# requirements
- follow technical requirements inside `./spec/simple/technical-requirements.md`
- follow the requirements inside `./spec/simple/functional-requirements.md`

# phased approach with PLAN.md
- please create a PLAN.md upfront to separate the approach into multiple phases
- this PLAN.md could be verified by the human in the loop upfront
- it should also be possible to resume the code generation after a session was closed and restarted
                                           
# verification
- when you are finished with code generation execute `gradlew clean build` to verify everything