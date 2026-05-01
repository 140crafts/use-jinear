# Jinear — Project Context

Use this as background context. Do not implement anything from this section — it only describes the existing system. The actual task is described separately after this context block.

## Project Responsibilities

**Jinear** is a project management suite, tasks, calendar, team chat, and file storage. It is open source.
Jinear consists of several projects that work together. Projects lives within single monorepo.

### docs
- Documents and tutorials about jinear setup

### jinear-caddy-custom
- caddy with module: dns.providers.cloudflare
- handles traffic and reroutes it. Manages ssl certificates etc.

### jinear-core (main backend)
- Auth (login, registration, sessions)
- Workspace & tier management (free/pro)
- Media storage (S3)
- Generic features reusable across all Jinear-based projects

### jinear-installation-scripts
- Installation script for self hosted option.
- Helps user to fill config files easyly 

### jinear-message-service
- Handles websockets for real time messaging.
- account ids are rooms, jinear-core sends messages through internal endpoint to here and then message send to related account's room.
- Users trigger jinear-core's restfull api, that api stores message then calls message-service to send message to receiving account. 

### jinear-pages
- Each project on jinear gets project feed which can be public or restricted to project members only. It's like projects twitter profile. Can also be used for blogs etc. 

### jinear-shared-libs
- Contains shared libraries that can be used across Jinear applications.
- If user wants to create an another service to live within existing stack anything might be shared goes here.
- jinear-rate-limiter: Adds rate limiting with given config. 

### jinear-web (main frontend)
- Jinear's main frontend.

### jinear-webview-mobile
- Jinear's WebView mobile wrapper
- Uses app ↔ web postMessage patterns

## Tech Stack

| Layer            | Tech                          |
|------------------|-------------------------------|
| Backend services | Java, Spring Boot             |
| jinear-web       | Next.js (React)               |
| jinear-mobile    | Expo (React Native)           |
| Gateway          | Caddy                         |
| Storage          | Google Cloud Storage or MinIO |
| Payments         | Paddle (web)                  |

## Dev Configuration

- jinear-core uses `application-dev.properties` (per-project dev config)
- On prod, properties files are provided externally

## Constraints

- Prefer adding to existing services over creating new ones; avoid adding new infrastructure dependencies; don't introduce new libraries for problems already solved by the current stack.
- Any new property, docker compose change etc. should also be added to installation-scripts, docs and if necessary README.md.

## How To Use This Context

Pair this file with a task-specific prompt structured like:
