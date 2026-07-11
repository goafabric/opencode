# AGENTS.md — container-ui

A Docker Desktop-like UI for Apple Containers, built with Quarkus (Kotlin) and plain HTML/JavaScript.

---

## Running the service

```bash
# Dev mode with live reload
./gradlew quarkusDev

# Open in browser
open http://localhost:8080
```

## Running tests

```bash
./gradlew test
```

---

## Configuration

`src/main/resources/application.properties`

| Property      | Default                                              | Description                        |
|---------------|------------------------------------------------------|------------------------------------|
| `docker.host` | `unix://${user.home}/.socktainer/container.sock`     | Docker Engine Unix socket path     |
| `quarkus.http.port` | `8080`                                         | HTTP port                          |

To connect to a different socket (e.g. standard Docker):
```properties
docker.host=unix:///var/run/docker.sock
```

---

## Project structure

```
container-ui/
├── build.gradle.kts
├── src/main/kotlin/org/goafabric/containerui/
│   ├── Application.kt
│   ├── adapter/
│   │   └── DockerSocketAdapter.kt       # Unix domain socket → Docker Engine API (raw HTTP/1.0)
│   ├── controller/
│   │   ├── ContainerController.kt       # GET /api/containers, POST start/stop, DELETE, GET logs
│   │   ├── ImageController.kt           # GET /api/images, DELETE /api/images/{id}
│   │   ├── VolumeController.kt          # GET /api/volumes, DELETE /api/volumes/{name}
│   │   └── dto/                         # Container, ContainerLog, Image, Volume
│   ├── logic/
│   │   ├── ContainerLogic.kt            # List, start, stop, delete, fetch logs + stats
│   │   ├── ImageLogic.kt                # List, delete
│   │   └── VolumeLogic.kt              # List, delete
│   └── extensions/
│       └── ExceptionHandler.kt
└── src/main/resources/META-INF/resources/
    ├── index.html                        # SPA shell: header, sidebar, view area
    ├── css/bootstrap.min.css
    └── js/
        ├── app.js                        # Navigation, fetch wrapper, shared utilities
        ├── containers.js                 # Container list: name/image/ports/cpu/mem/state + actions
        ├── container-detail.js           # Container detail: scrollable log view
        ├── images.js                     # Image list: name/tag/id/created/size + delete
        └── volumes.js                    # Volume list: name/created/size + delete
```

---

## Architecture

Strict layered package structure — no layer may skip a level:

```
controller  →  logic  →  adapter
```

- `controller/` calls `logic/`; never touches `adapter/` directly
- `logic/` orchestrates business logic and calls `adapter/`
- `adapter/` (DockerSocketAdapter) speaks raw HTTP over the Unix domain socket
- DTOs in `controller/dto/` are the shared data contract between `controller` and `logic`

---

## API endpoints

| Method | Path                            | Description              |
|--------|---------------------------------|--------------------------|
| GET    | `/api/containers`               | List all containers      |
| POST   | `/api/containers/{id}/start`    | Start a container        |
| POST   | `/api/containers/{id}/stop`     | Stop a container         |
| DELETE | `/api/containers/{id}`          | Delete a container       |
| GET    | `/api/containers/{id}/logs`     | Fetch container logs     |
| GET    | `/api/images`                   | List all images          |
| DELETE | `/api/images/{id}`              | Delete an image          |
| GET    | `/api/volumes`                  | List all volumes         |
| DELETE | `/api/volumes/{name}`           | Delete a volume          |

## Build

| Property        | Value    |
|-----------------|----------|
| Quarkus version | 3.37.0   |
| Gradle version  | 9.5.1    |
| Java version    | 25       |
| Kotlin version  | 2.4.0    |
