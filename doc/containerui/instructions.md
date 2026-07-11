# basics
- please create a new service with quarkus
- follow the build.gradle.kts inside `./spec/quarkus/build.gradle.kts`
- follow the example service files inside the zipped file, which needs to be unzipped `./spec/quarkus/example.zip`
- follow technical requirements inside `./spec/quarkus/technical-requirements.md`
- anything persistence is most likely not needed
- please create a plan.md upfront

# scope
- scope of the new service a docker desktop like ui for connecting to apple containers
- please create a new subdirectory in this workspace ./container-ui
- it should be a quarkus backend application with plain html / javascript frontend
- in the first step simply served from quarkus resources/META-INF/resources folder
- still there should be a clean separation in multiple html /javascript files where required, e.g multiple views
- connection should be configuriable in application.propties and go through sockets
- e.g DOCKER_HOST=unix://$HOME/.socktainer/container.sock

# ui
- the UI should consist of a sidebar to the left, 
- and views that show specific content to the right
- the views could be seperated by "register cards"
- please use a clean and simple lool

## sidebar ui
- the sidebar should have these entries
  - containers
  - images
  - volumes

## views

### container view
- a simple table with these columns
  - name, image, port, cpu%, memory
- an action column, with a play and stop and delete button to start/stop/delete containers
- when clicking one entry, it should switch to a view with one "register card" logs
- this should show the logs in a scrollable view

### images view
- a simple table with these columngs
  - name, tag, id, create, size
- an action column with a delete button

# volumes
- a simple table with these columns
  - name, create, size 
- an action column with a delete button
  



