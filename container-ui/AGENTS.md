# AGENTS.md — container-ui

A Docker Desktop-like UI for Apple Containers, built with Quarkus (Kotlin) and plain HTML/JavaScript.

---

## Running the service

```bash
# Dev mode with live reload
./gradlew quarkusDev

# Open in browser
open http://localhost:8081
```

## Running tests

```bash
./gradlew test
```

---

## Configuration

`src/main/resources/application.properties`

| Property              | Default | Description  |
|-----------------------|---------|--------------|
| `quarkus.http.port`   | `8081`  | HTTP port    |

No socket configuration is required. The adapter shells out to the `container` CLI which must be available on `$PATH`.

---

## Project structure

```
container-ui/
├── build.gradle.kts
├── src/main/kotlin/org/goafabric/containerui/
│   ├── Application.kt
│   ├── adapter/
│   │   ├── AppleContainerAdapter.kt     # Shells out to `container` CLI, parses JSON output
│   │   └── model/                       # Raw JSON model classes (ContainerModels, ImageModels, VolumeModels)
│   ├── controller/
│   │   ├── ContainerController.kt       # GET /api/containers, POST start/stop, DELETE, GET logs
│   │   ├── ImageController.kt           # GET /api/images, DELETE /api/images/{id}
│   │   ├── VolumeController.kt          # GET /api/volumes, DELETE /api/volumes/{name}
│   │   └── dto/                         # Container, ContainerLog, Image, Volume
│   ├── logic/
│   │   ├── ContainerLogic.kt            # List, start, stop, delete, fetch logs + stats
│   │   ├── ImageLogic.kt                # List, delete (container image rm)
│   │   └── VolumeLogic.kt              # List, delete (container volume rm)
│   └── extensions/
│       └── ExceptionHandler.kt
└── src/main/resources/META-INF/resources/
    ├── index.html                        # SPA shell: header, resizable sidebar, view area
    ├── css/bootstrap.min.css
    └── js/
        ├── app.js                        # Navigation, fetch wrapper, shared utilities
        ├── containers.js                 # Container list: name/image/ports/cpu/mem/state + actions, auto-refresh every 2s
        ├── container-detail.js           # Container detail: log view with live search filter
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
- `adapter/` (`AppleContainerAdapter`) shells out to the `container` CLI and parses JSON responses
- DTOs in `controller/dto/` are the shared data contract between `controller` and `logic`

### Key adapter commands

| Operation        | CLI command                              |
|------------------|------------------------------------------|
| List containers  | `container list --format json`           |
| Start container  | `container start <id>`                   |
| Stop container   | `container stop <id>`                    |
| Delete container | `container delete <id>`                  |
| Container logs   | `container logs <id>`                    |
| Container stats  | `container stats --format json`          |
| List images      | `container image list --format json`     |
| Delete image     | `container image delete <name:tag>`      |
| List volumes     | `container volume list --format json`    |
| Delete volume    | `container volume rm <name>`             |

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
