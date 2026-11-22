# Jinear: Coding Style

House rules for writing code in this monorepo. Each rule names a real file you can open, so
the standard stays checkable instead of aspirational.

This is the first version. It covers release compatibility for the whole repo, and the Java
backend. Frontend sections come later.

## Release compatibility

Applies to every change that ships in a release: database changesets, properties, compose
files, mail templates and the API between `jinear-app` and `jinear-core`.

A self-hosted instance upgrades with `docker compose pull && docker compose up -d` and
rolls back by pinning the previous image tag. Nobody rewrites the files of an install, and
most people do not read the notes. The rules below keep those two commands safe.

### An upgrade must start with no manual change

- An existing install must start on the new version with its current
  `docker-compose.yaml`, `.env` and `.config/application.properties`.
- A new property carries a default in code: `@Value("${some.property:default}")`. A new
  environment variable carries a default in the compose template.
- A feature that needs configuration stays off until it is configured. It never stops the
  server from starting.

Reference: `config/properties/MailProperties.java` gives `spring.mail.host` an empty
default and treats an unset host as "mail is not configured".

### A release must roll back to the previous release

The previous version must run correctly on the database that the new version migrated,
with no database restore. A changeset is therefore additive. In the release that stops
using something, do not:

- drop or rename a table or a column;
- delete rows that the previous version reads;
- change the format or the meaning of data that the previous version reads;
- add a constraint that the writes of the previous version break.

Remove the unused thing one release later, when the version that needed it is no longer
the rollback target. The same applies to an API field or a property that the previous
version of another service in this repo still uses.

Reference: the "Rolling back" section of `v0.1.1682` in `docs/upgrading.md`. That release
added seven tables, and the older image ignores them.

### A one-way change needs explicit approval

A change that cannot meet the two rules above is a one-way change: after it, an instance
cannot start without manual work, or cannot go back to the previous version without a
database restore.

- Do not write a one-way change on your own judgment. Stop, say what becomes irreversible
  and why an additive change cannot do the job, and wait for the explicit approval of the
  maintainer.
- Approval is for one change. It does not carry to the next one.
- An approved one-way change is the first thing in its `docs/upgrading.md` entry and in
  its release post, together with the backup step.

## Backend - Java

Applies to `jinear-core` and any future Spring Boot service in this repo.

### Layering

```
controller  ->  manager  ->  service  ->  repository
```

Every arrow points one way. There are no shortcuts and no back edges.

- A controller injects managers, and nothing else.
- A manager injects services, validators and converters. Never a repository.
- A service injects its repository and its converters. Never a manager.
- A repository is Spring Data only.

Reference: `manager/task/TaskInitializeManager.java` calls only services and validators;
`service/task/TaskRetrieveService.java` owns `TaskRepository` and nothing above it.

### Controllers

A controller maps HTTP to a manager call. One line per endpoint.

```java
@PostMapping
@ResponseStatus(HttpStatus.CREATED)
public TaskResponse initializeTask(@Valid @RequestBody TaskInitializeRequest request) {
    return taskInitializeManager.initializeTask(request);
}
```

Canonical example: `controller/task/TaskController.java`.

Rules:

- Return the typed `*Response`. `ResponseEntity` is not a preferred return value; if a
  response object can carry the answer, use the response object. Set the status code with
  `@ResponseStatus(HttpStatus.X)` on the method.
- Request bodies arrive as `@Valid @RequestBody XxxRequest`. If the receiving type is known,
  declare it. Do not accept a `Map` or a `JsonNode` and pick fields out of it.
- No try/catch. `BusinessException` and friends are mapped globally in
  `controller/advice/GeneralApiAdvice.java`. Catching in a controller bypasses the localized
  error envelope every other endpoint returns.
- No logic, no validation, no conversion, no session lookup. The current account is resolved
  inside the manager through `SessionInfoService`, which is why
  `OauthConnectionController.listMyConnections()` takes no argument.
- Class level `@RequestMapping("v1/...")`; paging params are
  `@RequestParam(required = false, defaultValue = "0") Integer page`.

### Managers

A manager is the orchestration layer: it resolves the caller, delegates checks, runs the
flow through services, and assembles the response. It is annotated `@Service`.

Canonical example: `manager/task/TaskInitializeManager.java`.

Rules:

- Manage services. Do as little yourself as possible.
- Simple mappings and one line guards are fine here. When a mapping or a check grows long,
  gets reused, or needs its own tests, it earns its own class: a converter in
  `converter/<domain>/` or a validator in `validator/<domain>/`.
- Never inject a repository. If a manager needs a query, the service layer is missing a
  method.
- Never hold an entity. Managers work in DTOs.
- A multi step write flow gets a `@Transactional` boundary, and that boundary belongs in a
  service, not in the manager.

### Services

A service owns one repository and one concern. Split by verb rather than growing one god
class: `TaskRetrieveService`, `TaskUpdateService`, `TaskListingService`,
`TaskInitializeService`, `TaskSearchService`. Sub domains get sub packages
(`service/task/board`, `service/task/comment`).

Rules:

- Services accept VOs, ids and primitives. They return DTOs.
- Entity to DTO conversion happens in the service, through an injected converter.
- Entities do not exit a service. Services in the same domain may share an entity, but this
  is rarely needed, and when it happens it goes through a single method whose name announces
  it: `retrieveEntity(...)`, `retrieveWorkspaceEntityWithId(...)`. That is the only
  entity typed method a service exposes, and only same domain services may call it.

Canonical example: `service/task/TaskRetrieveService.java` - one `retrieveEntity(String)`
next to three DTO returning `retrieve(...)` overloads.

### Converters and validators

- Converters live in `converter/<domain>/` and are pure mappers: a `@Component` for
  hand written mapping (`converter/task/TaskDtoDetailedConverter.java`) or a MapStruct
  `@Mapper(componentModel = "spring")` interface for field for field mapping
  (`converter/task/TaskInitializeVoConverter.java`). A converter holds no orchestration.
- Validators live in `validator/<domain>/`, take ids or VOs, and throw. They hold no
  orchestration either.

### Model types

| Package | Role |
|---|---|
| `model/request/` | what the wire sends in, bean validated |
| `model/vo/` | what a service accepts |
| `model/dto/` | what a service returns |
| `model/response/` | what the wire sends out, extends `BaseResponse` with `@JsonProperty("data")` |
| `model/entity/` | JPA rows, never leave their domain's services |

Request to VO conversion is a converter's job, called from the manager.

### Types

- Always write the object type. Never `var`.
- No `Map<String, Object>`, `Map<Object, Object>`, `JsonNode` or bare `Object` as a
  parameter, field or return type when the shape is known. Define a class and wrap it.

Maps are not banned. What is banned is using a map as an escape hatch for an object you did
not define. The test is simple: if every key is a string literal written in the same method,
it is a class in disguise.

- Legitimate: `system/oauth/OauthTokenHelper.java` builds `Map<String, Object>` JWT claims,
  because that is the JJWT library's own API and the keys are open ended.
- Legitimate: a `Map<TeamWorkflowStateGroup, List<TeamWorkflowStatusDto>>` grouping, where
  the key is data.
- Not legitimate: a method that creates a `LinkedHashMap` and puts eleven literal keys into
  it before returning it. That is a response class. `OauthDiscoveryService` used to do exactly
  that and now returns `OauthServerMetadataResponse`.

The same applies to `JsonNode` and `ObjectNode`. Raw Jackson trees belong at a parse boundary
and must not travel past it. The MCP tool layer shows the shape: `McpToolArguments` holds the
one raw tree and hands out typed values, and every tool payload is a class in
`model/mcp/view/` whose JSON Schema is generated from it, rather than a tree built by hand
next to a schema written by hand.

### Naming and imports

- Wildcard imports are for framework packages only, matching what the codebase already does:
  `org.springframework.web.bind.annotation.*` in controllers (98 of 110 use it),
  `jakarta.persistence.*` in entities, `lombok.*`, `org.mapstruct.*`. Never wildcard a
  `co.jinear.core.*` package or `java.util`: name the types you use, so a reader can see
  where a class comes from without opening the IDE.
- No fully qualified class names written inline. Import the class.
- No dead code: an unused helper or an unreferenced schema is a maintenance cost with no
  reader.

### External protocol endpoints

Some endpoints have a body shape fixed by a specification we do not control: JSON-RPC for
MCP, and RFC 6749 / 7591 / 8414 / 9728 for OAuth. These are the one carve out:

- They do not extend `BaseResponse`, because the specification defines the envelope.
- They keep the specification's `snake_case` field names through `@JsonProperty`.
- `@RestControllerAdvice` error mapping does not apply; the specification defines the error
  body, so a dedicated error response type and its own advice carry it, as
  `controller/advice/McpApiAdvice.java` does for RFC 6750 challenges.
- `ResponseEntity` is allowed here, and only here, when the status code or a response header
  is part of the contract and therefore cannot travel in the body. `controller/mcp/McpController.java`
  needs it for JSON-RPC's 202 Accepted. It still holds no logic: the manager decides, the
  controller maps.

The carve out is about the envelope, not about types. A spec mandated body is still a typed
class. `Map<String, Object>` is not more standards compliant than
`OauthTokenResponse`, it is only less checked.

### Published documents

A public endpoint whose body is copied whole into another project is a published document.
`GET /v1/mcp/manifest` is one: its body is pasted into `jinear-site/lib/mcp-tools.generated.json`.

- It does not extend `BaseResponse`, so the body can be copied without stripping an envelope.
- It is still a typed class (`model/response/mcp/McpToolManifestResponse.java`), and the
  controller returns it directly, without `ResponseEntity`.
- It is pretty printed, so the copied file stays readable in a diff. Add the type to
  `config/jackson/PublishedDocumentJsonConfiguration.java`; never turn on indentation on the
  shared `ObjectMapper`.
