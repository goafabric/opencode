# service setup
when writing new services build them with quarkus and kotlin based on the following
- follow the instructions inside `./specs/quarkus/spec-simple.md`
- follow the build.gradle.kts inside `./specs/quarkus/build.gradle.kts` 
- follow the example service files inside the zipped file, which needs to be unzipped `./specs/quarkus/example.zip`

please build me a service based on the scenario

# functional requirements 
- a service named "observation-service" inside the same workspace here
- it should contain laboratory values based on FHIR, as an inspiration: https://build.fhir.org/observation.html
- all encodings and metrics should be fit for the german market
- as a starting point we should include the most important laboratory values from the german "laborblatt"
- for example glucose, hemoglobin, cholesterol  ...

# technical requirements 
- we should have controller, logic, persistence connected to an H2 Database to store these laboratory values
- additionally we also need functionality to import so called "ldt" files, that contain the laboratory values
- and it would be nice to have a simple frontend with html and javascript to visualize, for quarkus this goes to src/main/resources/META-INF/
                                
# agentic approach
- it would make sense to separate the whole approach into individual steps, maybe also with multiple (sub) agents
- and to do a multi phase approach, the frontend should only be added at a later phase
- it should be possible to stop the process and resume it later
- if desired you may also create a plan first, to be reviewed by me the human