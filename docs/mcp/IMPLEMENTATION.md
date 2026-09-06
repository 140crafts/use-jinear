# Jinear MCP: Implementation and Review Guide

This document describes everything the `poc-mcp` branch adds, project by project and flow
by flow. It is written for a reviewer who has not worked with MCP before, so it starts
with a primer and only then goes into the code.

If you already know MCP and OAuth 2.1, skip to [section 2](#2-jinear-core-the-map).

---

## 0. What is on this branch

The branch has two layers.

**Layer 1, commit `bb2aee94`.** The whole `jinear-core` MCP server: the transport, the
tool catalog, the OAuth 2.1 authorization server, the resource server, the data model, the
analytics, and 12 test classes. 114 files.

**Layer 2, the working tree.** Everything that layer 1 needed but did not have: the
frontend screens, the instance flag wiring, the installation script, and the
documentation. Without layer 2 the OAuth flow dead ends, because `jinear-core` redirects
to a consent screen that did not exist.

| Project | Layer 1 (committed) | Layer 2 (working tree) |
|---------|---------------------|------------------------|
| `jinear-core` | The entire server | Server info endpoint, instance flag gate, one public route, 2 test classes |
| `jinear-app` | Nothing | Consent screen, profile section, admin page, instance flag, 2 API slices |
| `jinear-site` | Generated tool manifest (unused) | `/mcp` page, sitemap, llms.txt, footer |
| `jinear-installation-scripts` | Nothing | Prompt, secret, env, compose and properties templates, README |
| `docs` | Nothing | `docs/mcp/README.md`, this file, root README |

---

## 1. MCP in five minutes

### 1.1 The problem

An AI assistant such as Claude or ChatGPT can only answer from what it is given. If a user
wants to ask "what is assigned to me this week", the assistant needs to read their Jinear
tasks. Before MCP, every assistant and every application had to agree on a bespoke
integration.

**MCP, the Model Context Protocol, is a standard for that connection.** An application
exposes an MCP server; any assistant that speaks MCP can then use it. Jinear now exposes
one.

### 1.2 The three roles

- **Host**: the product the person is using. Claude, ChatGPT, an IDE.
- **Client**: the connector inside the host that talks to one server. One per server.
- **Server**: what we built. It offers **tools** the assistant may call.

### 1.3 Transport

Two transports exist:

- **stdio**: the server is a local process the host launches. No authentication needed,
  because the process already runs as the user.
- **Streamable HTTP**: the server is a web service at a URL. This is what a hosted product
  must use, and it is what Jinear implements at `POST /mcp`.

Streamable HTTP allows the server to keep a session and stream server-initiated messages
over Server Sent Events. **Jinear does neither.** Every tool answers in one response, so
the endpoint is stateless: no session id is issued, `GET /mcp` answers `405`, and
`DELETE /mcp` answers `204` because there is no session to tear down. A stateless server
also has no session table to lose when an instance restarts behind the gateway.

### 1.4 The protocol

MCP is **JSON-RPC 2.0** over that transport. The methods that matter here:

| Method | Meaning |
|--------|---------|
| `initialize` | Handshake. The client states its protocol version, the server answers with the version it will speak, its capabilities and free-text `instructions`. |
| `ping` | Liveness. |
| `tools/list` | "What can you do?" Returns every tool with its JSON Schema. |
| `tools/call` | "Do this." Names a tool and passes arguments. |
| notifications | Messages with no `id`. The specification says answer nothing. |

A **tool** is a name, a human description, an `inputSchema` (JSON Schema), an optional
`outputSchema`, and **annotations** that are hints to the host:

- `readOnlyHint`: changes nothing, so the host may run it without asking the user.
- `destructiveHint`: removes or irreversibly changes data, so the host prompts every time.
- `idempotentHint`: calling twice is the same as calling once.
- `openWorldHint`: touches systems outside this server.

The description and the schema are the entire interface the model sees. They are
documentation for a reader who cannot ask a follow-up question, which is why the tool
definitions in this branch are so verbose.

### 1.5 Why OAuth is involved

The server holds one person's private data, and the assistant is a third party acting on
their behalf. That is exactly what OAuth exists for. The MCP specification pins down which
parts of OAuth 2.1 a server must implement, and it splits the work in two:

- The **resource server** is the MCP endpoint. It reads a bearer token and refuses without
  one. It never issues tokens.
- The **authorization server** issues tokens. It may be a separate product. In Jinear it is
  the same Spring application, because Jinear already owns the login page and the session
  layer.

The specifications involved, and what each one does for us:

| Spec | What it gives us |
|------|------------------|
| **RFC 9728** Protected Resource Metadata | `/.well-known/oauth-protected-resource`. Tells a client which authorization server to use for this resource. This is the entry point: the client is given only a URL, and this is how it discovers everything else. |
| **RFC 8414** Authorization Server Metadata | `/.well-known/oauth-authorization-server`. Lists the authorize, token, register and revoke endpoints, the supported scopes, and the PKCE methods. |
| **RFC 7591** Dynamic Client Registration | `POST /oauth/register`. A client the server has never seen registers itself and receives a `client_id`. |
| **CIMD** (Client ID Metadata Document, draft) | The client's `client_id` **is** an https URL that serves its own metadata. The server fetches it instead of storing a registration. Claude prefers this. |
| **RFC 7636** PKCE | The client proves at the token endpoint that it is the same party that started the authorization. Mandatory in OAuth 2.1, and `S256` only here. |
| **RFC 8707** Resource Indicators | The client says which resource the token is for, and the token carries that as its audience. Stops a token minted for one server being replayed at another. |

### 1.6 The whole handshake, once, in words

1. The user pastes `https://api.jinear.co/mcp` into Claude.
2. Claude calls `POST /mcp` with `tools/call` (or gets there some other way) and receives
   **401** with a `WWW-Authenticate` header naming a `resource_metadata` URL.
3. Claude fetches that metadata, learns the authorization server, and fetches the
   authorization server metadata.
4. Claude identifies itself, either by CIMD or by registering dynamically.
5. Claude opens a browser at `/v1/oauth/authorize` with a PKCE challenge.
6. Jinear validates, parks the request, and redirects to its own consent screen.
7. The user signs in if needed, reads what is being asked for, and allows.
8. Jinear redirects back to Claude's callback with a one-time authorization code.
9. Claude posts the code plus its PKCE verifier to `/v1/oauth/token` and gets an access
   token and a refresh token.
10. Claude calls `tools/list` and `tools/call` with `Authorization: Bearer ...`.

Every step of that is implemented in this branch. Section 3 walks each one through the
code.

---

## 2. jinear-core: the map

### 2.1 Why it is hand written

The plan this work started from said to use the Spring AI MCP server starter. It is not
used, and `pom.xml` is unchanged. Two reasons, both worth challenging in review:

1. **The refusal has to be visible at the HTTP layer.** A client only offers the user a
   "connect" prompt when the HTTP request itself fails with `401` and a `WWW-Authenticate`
   header. A `200` carrying a tool error that says "please sign in" reads to the host as a
   tool that failed, and the user is never asked to connect. Per-tool scope enforcement
   with that refusal shape is not something the starter models.
2. **No new dependency tree** in `jinear-core`.

The cost is that protocol maintenance is ours: version negotiation, batching, notification
handling, and the result envelope.

### 2.2 Package layout

Two layers, kept apart by package. Everything under `oauth` is a plain OAuth 2.1
authorization server with no MCP protocol knowledge; everything under `mcp` is the
resource it protects. The names say which is which, but the split is naming and packaging
only: the server still issues tokens for exactly one resource. Section 2.2.1 names the
three places that encode that.

Note the `provider` segment. `controller/oauth` and `manager/oauth` already held Google
OAuth **client** code, where Jinear is the client of somebody else's server. This is the
**server** side, so it lives one level deeper and the Google classes did not move.

```
co.jinear.core
├── config
│   ├── properties/OauthProperties.java        all jinear.oauth.* settings
│   ├── properties/McpProperties.java          all jinear.mcp.* settings
│   └── security/OauthBearerAuthenticationFilter turns bearer -> Authentication, /mcp only
├── controller/oauth/provider
│   ├── OauthAuthorizeController               /v1/oauth/authorize + consent
│   ├── OauthTokenController                   /v1/oauth/token, /register, /revoke
│   ├── OauthConnectionController              /v1/oauth/connection/*  (member facing)
│   ├── OauthAdminController                   /v1/admin/oauth/*       (instance admin)
│   └── OauthDiscoveryController               /.well-known/*
├── controller/mcp
│   ├── McpController                          POST/GET/DELETE /mcp
│   ├── McpManagementController                /v1/mcp/*       (member facing)
│   └── McpAdminController                     /v1/admin/mcp/* (instance admin)
├── manager/oauth/provider
│   ├── OauthAuthorizationManager              authorize + consent business logic
│   ├── OauthTokenManager                      token, register, revoke
│   ├── OauthConnectionManager                 a member's granted apps, and disconnect
│   └── OauthAdminManager                      instance wide client list and revoke
├── manager/mcp
│   ├── McpManagementManager                   server info, workspace tool call reads
│   └── McpAdminManager                        instance wide tool call reads
├── service/oauth/provider                     client, code, request, refresh, PKCE,
│                                              redirect matching, CIMD, scopes,
│                                              bearer resolution, AS metadata
├── service/mcp
│   ├── McpProtocolService                     JSON-RPC dispatch
│   ├── McpDiscoveryService                    protected resource metadata
│   ├── McpToolCallLogService                  async audit writes
│   ├── analytics/                             usage rollup, retention
│   └── tool/                                  registry, builder, schema, argument access
│       └── config/                            the tool definitions, grouped by resource
├── system/oauth/OauthTokenHelper              mints and validates access tokens
├── system/mcp/McpPaths                        the two path constants used in 3 places
├── model/{dto,entity,enumtype,request,response,vo}/oauth
└── model/{dto,entity,enumtype,mcp,response}/mcp
```

#### 2.2.1 Where the server is still bound to MCP

Three places, all documented in the `OauthAuthorizationManager` class javadoc. A second
resource, a public REST API for third party apps being the likely one, means changing
these and nothing else.

1. **The audience is fixed.** `OauthTokenHelper.generateAccessToken` sets `aud` from
   `jinear.mcp.resource-url`, and `OauthAuthorizationManager.isResourceAcceptable` compares
   the RFC 8707 `resource` parameter against that one value. Multi resource means deriving
   the audience per request.
2. **The resource server filter is path bound.** `OauthBearerAuthenticationFilter` skips
   every path except `McpPaths.MCP_ENDPOINT`, so a bearer authenticates nothing else.
3. **The admin flag is MCP's.** `assertEnabled` now reads `jinear.oauth.enabled`, but the
   authorization path also checks the `MCP_SERVER` instance flag, because MCP is the only
   resource a new grant can be for. That check becomes per resource on the same day.

`scopes_supported` in both discovery documents follows from the same fact: it is one flat
`OauthScope` set today, and becomes per resource on the same day.

### 2.3 Request routing

Three paths sit outside the usual `/v1` API surface, and each is handled differently.

| Path | Security | JwtRequestFilter | OauthBearerAuthenticationFilter |
|------|----------|------------------|-------------------------------|
| `/mcp` | `permitAll`, controller refuses | **skipped** | **runs** |
| `/.well-known/**` | `permitAll` | **skipped** | skipped |
| `/v1/oauth/authorize`, `/token`, `/register`, `/revoke` | `permitAll` | runs | skipped |
| `/v1/oauth/authorize/info/{requestId}` | `permitAll` (layer 2) | runs | skipped |
| `/v1/oauth/authorize/consent` | `ROLE_USER` | runs | skipped |
| `/v1/oauth/connection/**` | `ROLE_USER` | runs | skipped |
| `/v1/mcp/**` | `ROLE_USER` | runs | skipped |
| `/v1/admin/oauth/**` | `ROLE_ADMIN` | runs | skipped |
| `/v1/admin/mcp/**` | `ROLE_ADMIN` | runs | skipped |

Filter order in `SecurityConfiguration` is deliberate:

```
OauthBearerAuthenticationFilter -> JwtRequestFilter -> RateLimitingFilter
```

The bearer filter runs first so that an authenticated tool call is rate limited per account
rather than falling into the shared public allowance keyed on the gateway IP.

`JwtRequestFilter` gained a `shouldNotFilter` that skips `/mcp` and `/.well-known/`. This
matters: an MCP bearer is signed with a **different key**, so handing it to the session
filter would produce a signature failure, and letting it through would be worse. It would
turn a scoped tool credential into a full browser session.

`permitAll` on `/mcp` is not a hole. The controller itself decides, per message in the
request body, whether credentials are required, and answers `401` or `403` with a
challenge. It is done there because whether a JSON-RPC message needs credentials depends
on the method **inside the body**, and reading the body in a filter would consume it
before the controller could parse it.

---

## 3. jinear-core: flow by flow

### 3.1 Discovery

**Entry point.** `OauthDiscoveryController`, which delegates to `OauthDiscoveryService`
for the authorization server document and to `McpDiscoveryService` for the protected
resource document. The authorization server describes itself; the resource describes
itself.

Two documents, both plain `Map` so they serialize exactly as the RFCs specify and stay out
of the generated frontend type file.

`GET /.well-known/oauth-protected-resource` and the path-suffixed twin
`/.well-known/oauth-protected-resource/mcp`. Both are served, because a client whose
resource URL has a path component tries the suffixed form first and falls back to the root
form. Serving both removes a round trip and one class of misconfiguration.

```json
{
  "resource": "https://api.jinear.co/mcp",
  "authorization_servers": ["https://api.jinear.co"],
  "scopes_supported": ["workspace:read", "..."],
  "bearer_methods_supported": ["header"],
  "resource_documentation": "https://jinear.co/mcp/"
}
```

`GET /.well-known/oauth-authorization-server`, also aliased at
`/.well-known/openid-configuration`:

```json
{
  "issuer": "https://api.jinear.co",
  "authorization_endpoint": "https://api.jinear.co/v1/oauth/authorize",
  "token_endpoint": "https://api.jinear.co/v1/oauth/token",
  "revocation_endpoint": "https://api.jinear.co/v1/oauth/revoke",
  "registration_endpoint": "https://api.jinear.co/v1/oauth/register",
  "response_types_supported": ["code"],
  "grant_types_supported": ["authorization_code", "refresh_token"],
  "token_endpoint_auth_methods_supported": ["none"],
  "code_challenge_methods_supported": ["S256"],
  "client_id_metadata_document_supported": true
}
```

**Two fields decide how Claude registers itself.** CIMD is used only when
`client_id_metadata_document_supported` is `true` **and** `"none"` appears in
`token_endpoint_auth_methods_supported`, because a CIMD client authenticates as a public
client with PKCE and no secret. Drop either and every connection falls back to dynamic
registration.

`registration_endpoint` is omitted entirely when `jinear.oauth.dcr-enabled` is false.

Note the paths: the endpoints live under `/v1/oauth/...`, not `/oauth/...`. That differs
from the original plan but is not a defect, because the metadata advertises the real paths
and clients read them from there.

Both documents are cached for five minutes.

### 3.2 Client registration

**Entry point.** `OauthClientService.resolveForAuthorization(clientId)`.

Three registration types exist, modelled as `OauthClientRegistrationType`: `CIMD`, `DCR`,
`STATIC`.

**CIMD.** If the `client_id` starts with `https://`, it is treated as a metadata document
URL and fetched by `CimdResolver`. This is the path Claude takes.

The document is **re-fetched on every authorization** rather than trusting a stored copy,
so a client that changes its redirect URIs cannot be authorized against a stale
registration. A shadow row is still upserted so the management screens and the call log
have a name to show, but that row is never the source of truth for authorization.

`CimdResolver` is the most security-sensitive class on the branch, because it takes a
URL from an unauthenticated caller and fetches it. Its guards, in order:

1. The `client_id` must be `https`, have a host, have a non-empty path that is not `/`, and
   have no fragment.
2. **Host allowlist**, if `jinear.oauth.cimd-allowed-hosts` is set. Empty means any public
   https host, which is the open policy the draft describes.
3. **SSRF guard.** Every resolved address of the host is checked, and loopback, site-local,
   link-local, any-local, multicast and IPv6 unique-local addresses are refused.
4. Redirects are **not** followed (`Redirect.NEVER`).
5. Connect and request timeouts from `jinear.oauth.cimd-fetch-timeout-millis`.
6. Non-`200` is refused. The body is capped at 64 KB.
7. The document must be **self-referential**: its `client_id` must equal the URL it was
   fetched from.
8. `redirect_uris` must be non-empty, and each one must be either loopback or **same origin
   with the metadata document**. Without rule 8, an attacker could host a legitimate
   looking `client_id` and point its callback at their own server.

**DCR.** `POST /v1/oauth/register`, RFC 7591, JSON body. Gated by
`jinear.oauth.dcr-enabled`. Redirect URIs must be `https` or loopback, and must have no
fragment. The issued client is public: `token_endpoint_auth_method` is `none`, and no
secret is returned. PKCE is what protects the exchange.

**STATIC** exists in the enum for a client seeded by hand. Nothing on the branch creates
one.

### 3.3 Authorization and consent

This is the flow that needed frontend work, so it is worth reading closely.

**Step 1, `GET /v1/oauth/authorize`.** `OauthAuthorizeController` ->
`OauthAuthorizationManager.authorize`.

Validation happens in two groups, and the split is the interesting part:

- **Errors we cannot report to the client**: an unknown `client_id`, or a `redirect_uri`
  that is not registered. There is no address we trust to send the user to, so these throw
  and the controller renders a plain HTML refusal page. That page deliberately links
  nowhere the caller supplied and says nothing about the account.
- **Everything after that** is reported as an **OAuth error redirect** to the
  already-validated `redirect_uri`, which is what the RFC requires. This covers a wrong
  `response_type`, a missing PKCE challenge, a non-`S256` challenge method, and a `resource`
  that does not match this server.

`resource` (RFC 8707) is compared after normalising case and a trailing slash. An **absent**
`resource` is accepted, because a few clients still do not send it; only a **mismatched**
one is refused.

Scopes are parsed with `OauthScopeService.parse`, which silently drops anything this server
does not define. Failing the whole authorization would be worse, because a generic client
may ask for scopes it read from another server's metadata. An empty result falls back to
every scope.

The request is then **parked** as an `oauth_authorization_request` row with a 10 minute
expiry, and the endpoint returns a redirect to
`fe.oauth-consent-url` with `{requestId}` substituted. The endpoint never renders the consent
screen itself. It redirects to `jinear-app`, which is where the user is already signed in
and where the sign-in redirect already works.

**Step 2, the consent screen.** Implemented in `jinear-app`, see [section 7.1](#71-the-consent-screen).

It reads `GET /v1/oauth/authorize/info/{requestId}`, which returns `OauthConsentInfoDto`:

```
requestId, clientDisplayHost, clientName, clientUri, logoUri,
policyUri, tosUri, redirectHost, loopbackOnly, requestedScopes[]
```

`clientDisplayHost` is the **host of the `client_id` URL**, not the self-asserted
`client_name`. A metadata document is written by whoever hosts it, so the name in it is a
claim and the host is the only part we verified. The screen shows the host prominently and
the name as secondary.

**Layer 2 made this endpoint public.** Without that, a signed-out visitor arriving from
Claude sees a sign-in form with no idea what they are signing in for. The endpoint carries
client metadata only, is reachable only with an unguessable short-lived request id, and the
consent submission behind it still requires a session.

**Step 3, `POST /v1/oauth/authorize/consent`.** Requires `ROLE_USER`.

- **Denied**: the request is completed and the user is redirected back to the client with
  `error=access_denied`.
- **Allowed**: `OauthConnectionService.grant` creates or widens the connection, an
  authorization code is issued, the request is completed, and the user is redirected back
  with `code=...` plus the original `state`.

`grant` keeps **one connection per account and client**. Re-consenting widens the existing
grant rather than piling up rows, so the management screen shows one entry per client and
revoking it really does end that client's access.

`grant` also opens a real session: `sessionInfoService.initialize(ProviderType.MCP,
accountId)`. Write managers stamp the session id onto every workspace activity row, so an
agent's edits are attributable exactly as a browser login's are. `ProviderType.MCP` was
added for this.

### 3.4 Token issue

**Entry point.** `POST /v1/oauth/token`, form encoded, `OauthTokenManager.token`.

The content types matter and are easy to get wrong: RFC 6749 requires the **token**
endpoint to accept `application/x-www-form-urlencoded`, and RFC 7591 requires the
**registration** endpoint to accept `application/json`. A single body parser will not serve
both, and getting it wrong shows up as a `415` on the very first connection attempt.

For `grant_type=authorization_code`:

1. `OauthAuthorizationCodeService.redeem` consumes the code **exactly once**. The code handed
   to the client is `{rowId}.{secret}`; only a BCrypt hash of the secret is stored, so a
   database leak yields no usable codes, and the row id keeps the lookup indexable. A
   replay finds `consumedAt` set and is refused as `invalid_grant`. Codes expire in 60
   seconds by default.
2. `client_id`, if sent, must match the code's.
3. `redirect_uri` must match the authorization request's **exactly**.
4. PKCE: `PkceValidator.verify` recomputes `BASE64URL(SHA256(verifier))` and compares
   with `MessageDigest.isEqual`, a constant-time comparison. The verifier length is checked
   against the RFC's 43 to 128 characters. `plain` is not accepted at all.
5. `resource`, if sent, must match.
6. The connection must still exist.

The response is built by `OauthTokenHelper.generateAccessToken`: an HS512 JWT with
`sub` = account id, `aud` = the resource URL, `iss` = the issuer URL, plus `scope`,
`client_id` and `oauth_connection_id` claims. A refresh token is included **only if
`offline_access` is among the granted scopes**.

Every failure at this endpoint is mapped to an RFC 6749 error shape by
`OauthErrorMapper`, and the exact code matters operationally. Claude only treats a refresh
failure as "this grant is gone, start over" when the response says `invalid_grant`.
Returning `invalid_request` or a custom code there leaves users stuck on a connection that
can never recover. `invalid_client` is the only code answered with 401; everything else is
400. Both the success and the error response carry `Cache-Control: no-store`, so a token
never lands in an intermediary cache.

**The signing key is `jwt.oauth.secret`, deliberately different from `jwt.secret`.** An MCP
bearer must never authenticate a browser session, and a session cookie must never
authenticate a tool call, so the two key spaces are kept apart rather than relying on a
claim to tell them apart.

### 3.5 Refresh and rotation

`grant_type=refresh_token`, `OauthRefreshTokenService`.

A refresh token is also `{rowId}.{secret}` with only a BCrypt hash stored. Default validity
is 30 days.

**Rotation with reuse detection.** OAuth 2.1 requires rotation for public clients, and both
CIMD and DCR register an MCP client as public. On redeem:

- Unknown id, wrong secret, or expired: `invalid_grant`.
- **Already consumed**: a copy leaked. The whole connection is revoked, every refresh token
  under it is passivated, and the caller gets `invalid_grant`. Refusing only that one token
  would leave the thief's copy working.

`rotate` marks the presented token consumed, records `rotatedTo`, and returns the
successor in one step.

A refresh **may narrow** the scope set but never widen it: the requested scopes must be a
subset of what the connection was granted.

### 3.6 An authenticated tool call

This is the hot path.

1. **`OauthBearerAuthenticationFilter`** (only on `/mcp`) reads `Authorization`, and if it
   resolves, puts a `UsernamePasswordAuthenticationToken` in the security context with the
   account id as principal, `ROLE_USER` as authority, and the `OauthAccessTokenVo` as
   **details**. The credential slot is left empty on purpose: that is where a browser
   session keeps its parseable JWT, and an MCP token is not one.
   The filter **never refuses** a request. It only authenticates.

2. **`OauthAccessTokenResolver.resolve`** does the checking:
   - `OauthTokenHelper.parseAccessToken` verifies the HS512 signature and expiry, then checks
     the **audience** equals `jinear.mcp.resource-url` and the **issuer** equals
     `jinear.oauth.issuer-url`. The audience check is the one the MCP specification calls out
     as mandatory: without it a token minted for a different resource would be accepted.
   - The **connection row must still exist**. Revocation cannot be carried in the token, and
     a user who disconnects a client expects that to take effect now, not when the access
     token expires an hour later.
   - `lastUsedAt` is refreshed only if older than five minutes, so a row update does not sit
     in front of every read.

3. **`McpController.handle`** parses the body, which may be a single message or a batch
   array. Before dispatching anything it walks the batch looking for the first tool call
   the caller may not make:
   - `initialize`, `ping`, notifications and `tools/list` are **deliberately open**, so a
     client can connect and read the whole catalog before anyone signs in.
   - An unknown tool name is left to the dispatcher, so the model learns the name was wrong
     rather than being told it is unauthorized.
   - No token and the tool requires scopes: **401** with
     `WWW-Authenticate: Bearer error="invalid_token", resource_metadata="...", scope="..."`.
   - Token present but missing a scope: **403** with `error="insufficient_scope"`. The
     challenge names **granted plus missing** scopes together, because naming only the
     missing ones would have the user re-consent to a narrower set than they already had,
     silently dropping permissions they were relying on.
   - Both cases are written to the call log as `UNAUTHORIZED` or `FORBIDDEN`.

4. **`McpProtocolService.handle`** dispatches per message. `initialize` echoes the client's
   protocol version when we speak it (`2025-11-25`, `2025-06-18`, `2025-03-26`) and answers
   with our preferred one otherwise. It returns `capabilities.tools.listChanged = false`,
   because the catalog is fixed at build time, and a 512 character `instructions` string
   that tells the model to call `list_workspaces` first.

5. **`tools/call`** looks the tool up, calls it, and wraps the result. Three failure modes,
   and the distinction is deliberate:
   - `McpToolException` (bad arguments): returned as a **tool error**, not a protocol error,
     so the model can correct it and retry.
   - Any other `RuntimeException`: logged, and translated by `describe` into something
     actionable. `NoAccessException` becomes "You do not have access to that resource in
     this workspace." A generic error body is one of the documented reasons a connector
     fails directory review.
   - Success: the result is returned **both** as `structuredContent` and serialized into a
     text block, because the specification asks for both so that clients predating
     `structuredContent` still see the data.

6. **The call is logged** asynchronously. See [3.9](#39-logging-analytics-retention).

### 3.7 Tool definitions

Tools are `@Bean McpTool` builders declared in eight grouped configuration classes rather
than one class each, because a class per tool would bury the part that matters, which is
the description and the schema the model reads.

`SimpleMcpTool.named("create_task").title(...).description(...).input(...).output(...)
.write().scopes(TASKS_WRITE).handler((context, args) -> ...)`

Three annotation shorthands: `readOnly()` (also sets idempotent), `write()`, and
`destructive()`. **Nothing on the branch calls `destructive()`**, because there are no
delete tools.

`McpJsonSchema` and `McpShapes` build the input and output schemas. `McpToolArguments`
gives typed access with error text a model can act on: every failure names the field, says
what was expected and says what was received. `"taskId is required"` is retryable;
`"Invalid request"` is not.

**Handlers call the existing manager layer**, never repositories. That is what makes
workspace permissions, validation, localized errors and workspace activity behave exactly
as they do for the app. A handler sets `context.setWorkspaceId(...)` as soon as it resolves
one, so the call log can attribute the call to a workspace.

`McpToolRegistry` indexes tools at startup and **fails the boot** on: a name that does not
match `^[a-zA-Z0-9_.-]{1,64}$`, a duplicate name, a missing title, a missing description, a
non-object input schema, a tool that is both read only and destructive, or a tool marked
read only that requires a write scope. Each of those is a directory rejection, and it is
cheaper to fail the boot than to find out during review.

**The catalog, grouped by the scope it requires:**

| Scope | Tools |
|-------|-------|
| `workspace:read` | `get_workspace`, `list_workspaces`, `list_teams`, `list_topics`, `list_workflow_statuses`, `list_workspace_members`, `search`, `fetch` |
| `tasks:read` | `get_task`, `list_tasks`, `search_tasks`, `list_task_boards`, `list_task_comments`, `search`, `fetch` |
| `tasks:write` | `create_task`, `update_task`, `set_task_status`, `add_task_comment`, `create_task_board`, `add_task_to_board` |
| `calendar:read` | `list_calendar_events` |
| `notes:read` | `get_note`, `list_notebooks`, `search_notes`, `search`, `fetch` |
| `files:read` | `list_files`, `get_file_link` |

25 tools, 19 of them read only, **0 destructive**.

There is no projects scope and there are no project tools. Projects are a deprecated
feature, so the connector leaves them out entirely, down to the `projectId` and
`milestoneId` fields on the task tools. A token issued before the removal may still carry
`projects:read` or `projects:write`; nothing requires them, and `OauthScopeService.parse`
drops any scope `OauthScope` does not define.

`search` and `fetch` are the generic retrieval pair a host uses when it wants to cite
sources: `search` returns identifiers with user-openable URLs, `fetch` returns the full
text behind one of them. They cover every workspace the account belongs to, because a
citing host has no workspace to pass in. They are what makes the connector usable in
ChatGPT.

**Deliberate absences**, documented in `OauthScope`:

- **No delete tools at all.**
- **Notes are read only.** A note body is a CRDT document authored by the editor, and
  seeding one from plain text would produce a note whose title and body disagree the moment
  somebody opens it.
- **Calendar is read only.** Jinear has no calendar events of its own; the calendar is
  tasks with dates plus events synced from Google. Writing an event would mean writing to
  Google on the user's behalf, which this connector deliberately does not proxy. Scheduling
  work is `create_task`.

### 3.8 Revocation and disconnect

Three different scopes of revocation exist, and they are not interchangeable.

| Who | Endpoint | Effect |
|-----|----------|--------|
| The client itself | `POST /v1/oauth/revoke` (RFC 7009) | Redeems the refresh token, passivates every refresh token for the connection, passivates the connection. Answers `200` even for an unknown token, as the RFC requires. |
| The member | `DELETE /v1/oauth/connection/{id}` | Same, for one of their own connections. **Owner only, not admin**: a connection is a grant one person made from their own account, and nobody else's role should let them speak for it. |
| The instance admin | `DELETE /v1/admin/oauth/client?clientId=` | Passivates the client registration, which cuts every connection made through it for every account. |

In all cases the effect on a live access token is immediate, because
`OauthAccessTokenResolver` re-checks the connection row on every call.

### 3.9 Logging, analytics, retention

`McpToolCallLogService` records **what** a client did, never **what it said**. Arguments
and response bodies are never stored. They come from the user's conversation, and both
directory policies limit a connector to the data its function needs. Tool name, outcome,
duration and response size answer the operational questions on their own.

Writes are `@Async` and every failure is swallowed, so a slow or broken log never becomes a
slow or broken tool call.

`McpAnalyticsService.summarize(workspaceId, windowDays)` produces the numbers behind both
management screens. Per-tool totals come from the raw log, which only reaches back as far
as the retention window; the daily series comes from the rollup table, which survives
pruning, so a chart keeps its history after the rows behind it are gone.

`McpRetentionService`, driven by a 6-hourly `@Scheduled` job in `ScheduledJobManager`:

1. `rollUpYesterday()` writes yesterday's per-tool totals into `mcp_usage_daily`, guarded by
   an existence check so a second run is a no-op.
2. `pruneExpired()` deletes call logs older than `jinear.mcp.log-retention-days`, plus
   authorization codes and pending authorization requests older than one day.

**The order matters**: the rollup is written before the rows it summarizes are deleted, or
a chart loses the day it was about to gain.

### 3.10 Session integration

`SessionCarrier` is a new interface. A browser request carries its session id inside the
JWT cookie, so `SessionInfoService` reads it back out of the token. A machine caller
authenticated some other way has no such token to parse, so it attaches a `SessionCarrier`
as the authentication **details** instead, and that wins over the credential when present.

`OauthAccessTokenVo` implements it. The result is that every existing manager which calls
`sessionInfoService.currentAccountId()` or reads the session id works unchanged under an
MCP call, and workspace activity rows are attributed correctly.

---

## 4. Data model

Seven tables, all in `changelog-v22.xml`, plus one seeded flag row.

| Table | Holds | Notable columns |
|-------|-------|-----------------|
| `oauth_client` | One row per registered or seen client | `client_id`, `redirect_uris` (newline joined), `registration_type` (DCR/CIMD/STATIC), `client_id_issued_at` |
| `oauth_authorization_request` | A parked `/authorize` call awaiting consent | `scope`, `state`, `code_challenge`, `resource`, `expires_at`, `completed_at`. Indexed on `expires_at` |
| `oauth_connection` | One grant, per account per client | `account_id`, `client_id`, `granted_scopes`, `session_info_id`, `last_used_at`. Indexed on `account_id` |
| `oauth_authorization_code` | A one-time code | `hashed_code` (BCrypt), `consumed_at`, `expires_at`. Indexed on `expires_at` |
| `oauth_refresh_token` | A rotating refresh token | `hashed_token` (BCrypt), `consumed_at`, `rotated_to`. Indexed on connection id |
| `mcp_tool_call_log` | One row per tool call or rejection | `tool_name`, `call_status`, `error_code`, `duration_ms`, `response_bytes`. **No arguments, no responses.** Indexed on date, workspace, connection |
| `mcp_usage_daily` | Rolled-up per-day per-tool totals | `usage_date`, `tool_name`, `call_count`, `error_count`, `total_duration_ms` |

Plus `INSTANCE_FLAG` row `flag_type = 7` (`MCP_SERVER`), seeded `false`.

All tables follow the existing soft-delete convention: `passive_id` is set rather than rows
being deleted, except in the retention job which really does delete.

---

## 5. Configuration

Split in two, the same way the code is.

`OauthProperties`, prefix `jinear.oauth`.

| Property | Default | Notes |
|----------|---------|-------|
| `enabled` | `false` | With it off, every `/v1/oauth/*` endpoint refuses with `temporarily_unavailable` |
| `issuer-url` | none, **required** | Must be the origin that serves `/.well-known/*`, that is the API origin |
| `documentation-url` | `https://jinear.co/mcp/` | Advertised as `service_documentation` |
| `access-token-validity-minutes` | 60 | |
| `refresh-token-validity-days` | 30 | |
| `authorization-code-validity-seconds` | 60 | |
| `authorization-request-validity-minutes` | 10 | How long a consent screen stays answerable |
| `dcr-enabled` | `true` | When false, `registration_endpoint` is omitted from the metadata too |
| `cimd-allowed-hosts` | empty | Empty means any public https host. The SSRF guard applies either way |
| `cimd-fetch-timeout-millis` | 4000 | |

`McpProperties`, prefix `jinear.mcp`.

| Property | Default | Notes |
|----------|---------|-------|
| `enabled` | `false` | With it off, `/mcp` returns 404. Does not gate the OAuth endpoints |
| `resource-url` | none, **required** | The RFC 8707 audience **and** the string a user types into their client. RFC 9728 requires the two to match exactly, so it is configured rather than derived |
| `documentation-url` | `https://jinear.co/mcp/` | Advertised in the protected resource metadata |
| `log-retention-days` | 30 | |
| `max-page-size` | 50 | Cap on any list tool |

Plus `jwt.oauth.secret` (no default, required) and `fe.oauth-consent-url` (no default,
required, must contain `{requestId}`).

**Three switches gate the feature**, and the split is deliberate:

- `jinear.oauth.enabled` runs the authorization server. With it off, `/v1/oauth/*` refuses
  with `oauth.error.disabled`, mapped to `temporarily_unavailable`, so nobody can authorize
  a new connection and no existing connection can refresh.
- `jinear.mcp.enabled` runs the resource server. With it off, `/mcp` answers 404. A member
  needs both properties on; the installer writes both from one question.
- `InstanceFlagType.MCP_SERVER` is the administrator's switch, checked **only on the
  authorization path** (`OauthAuthorizationManager.assertEnabled`). Turning it off stops
  anybody granting fresh access while the connections people already made keep working
  until they disconnect them. It is not checked on the transport, because that would put a
  database read in front of every tool call.

---

## 6. Security decisions worth a close read

These are the places where a mistake would be expensive. Each is deliberate, and each is
worth challenging.

1. **Separate signing key** (`jwt.oauth.secret` vs `jwt.secret`), with `JwtRequestFilter`
   skipping `/mcp`. This is the main isolation between an agent credential and a browser
   session.
2. **Audience and issuer are both checked** on every token parse, not just the signature.
3. **Revocation is checked per call** by re-reading the connection row, so disconnect is
   immediate rather than eventual.
4. **CIMD SSRF guard** in `CimdResolver`: allowlist, address family checks, no redirect
   following, timeouts, size cap, self-reference check, and the same-origin rule on redirect
   URIs. The same-origin rule is the one that stops a hostile metadata document.
5. **Loopback redirect matching** in `RedirectUriMatcher`. RFC 8252 section 7.3 says a
   loopback redirect is compared with the **port ignored**, because a native client binds an
   ephemeral port. This implementation extends that to the `localhost` name and not only the
   IP literal, because Claude Code declares both and listens on a random port.
   `localhost` and `127.0.0.1` are still treated as **distinct hosts**. Everything
   non-loopback is matched exactly.
6. **PKCE is S256 only**, compared in constant time, with the verifier length enforced.
7. **Codes and refresh tokens are BCrypt hashed** at rest, in the `{rowId}.{secret}` shape
   already used by the robot token.
8. **Refresh reuse revokes the connection**, not just the token.
9. **The authorize endpoint refuses to redirect** to an unvalidated address, and renders a
   page that leaks nothing instead.
10. **Consent names the client by `client_id` host**, never by the self-asserted
    `client_name`.
11. **The call log stores no arguments and no responses.**
12. **`permitAll` on `/mcp`** with refusal in the controller. Confirm you agree with the
    reasoning in [section 2.3](#23-request-routing).
13. **`/v1/oauth/authorize/info/{requestId}` is public** (layer 2). Confirm you agree that
    client metadata behind an unguessable short-lived id is acceptable to expose.

---

## 7. jinear-app changes

All of this is layer 2. Before it, the OAuth flow dead ended.

### 7.1 The consent screen

**New**: `src/pages/oauth/consent/page.tsx`, `index.module.css`.
**Route**: `/oauth/consent`, added in `App.tsx` outside the `/:workspaceName` tree.

Reads `request_id` from the query string, then `GET /v1/oauth/authorize/info/{requestId}`.
Renders:

- the verified client host, prominently, with `clientName` secondary;
- one line per requested scope, in plain language, from `SCOPE_LABEL_KEYS`;
- a note about where the user will be sent back to, or that the application runs on their
  own computer when `loopbackOnly` is true;
- the client's own site, privacy and terms links when it declared them;
- **Allow** and **Deny**, both posting to `POST /v1/oauth/authorize/consent`.

On success it sets `window.location.href` to the returned redirect URL. That has to be a
full navigation, not a router navigate, because the destination belongs to the client.

**The signed-out case is the subtle part.** A user arriving from Claude may have no
session. Three pieces make that work:

1. `AuthCheck.tsx` gained `/oauth/consent` in
   `PATHS_EVERYONE_CAN_VISIT_INREGARD_OF_THEIR_LOGIN_STATUS`, so it is not bounced to `/`.
2. The page renders `LoginWithMailForm` in place, under the client and scope information,
   so the user knows what they are signing in for.
3. `src/util/oauthConsent.ts` parks the request id in `sessionStorage`, and
   `src/components/oauthConsentReturnListener/OauthConsentReturnListener.tsx` (mounted at the
   top of `App.tsx`) navigates back to the consent screen once `authState` becomes
   `LOGGED_IN`.

**Why parked storage and not a redirect parameter.** Every sign-in method lands on the app
root: the password form navigates there, and the Google and Apple flows leave the app
entirely and come back. Only the password form could have carried a query parameter
through. `sessionStorage` rather than `localStorage`, so an abandoned request cannot
resurface days later in a different session. Every access is wrapped in try/catch, because
private mode and blocked site data make it throw.

### 7.2 Profile: AI Assistants

**New**: `src/components/profile-screen/mcpConnectionsSection/`.
**Mounted in**: `src/pages/profile/page.tsx`.

Two jobs in one section:

- **Setup.** The instance's own MCP server URL with a copy button, then Claude and ChatGPT
  step lists and the `mcp-remote` note for a private-network instance. The URL comes from
  the new `GET /v1/mcp/info`, so nobody has to work out their own address.
- **Management.** The connection list with last-used time and a Disconnect action, backed
  by `GET /v1/oauth/connection/list` and `DELETE /v1/oauth/connection/{id}`.

The whole section returns `null` when the instance says MCP is off, so a member never sees
setup steps for something they cannot connect to.

### 7.3 Admin: AI Assistants

**New**: `src/components/adminScreen/mcpScreen/`, `src/pages/admin/mcp/`.
**Route**: `/admin/mcp`. **Side menu entry** added to `InstanceSettingsSideMenu.tsx`.

Three blocks: usage stats over the 30 day window, the most-used tools, the registered
client applications with a Revoke action, and the recent call log. Both lists are
paginated with the existing `Pagination` component.

### 7.4 The instance flag

`InstanceFlagType.MCP_SERVER` was added in layer 1 and the row is seeded by
`changelog-v22.xml`, but nothing read it and the admin screen did not list it.

- `InstanceFlagsSection.tsx` gained an `MCP_SERVER` entry in `FLAG_COPY` and in the
  integration group. **This was a compile error**, not a nicety: `FLAG_COPY` is typed
  `Record<InstanceFlagType, FlagCopy>`, so the app would not build against the new enum
  value.
- The copy states the behaviour honestly: assistants already connected keep working when it
  is turned off.

### 7.5 API layer

**New slices**, split the same way the backend is: `src/store/api/oauthApi.ts` (consent
info, consent submit, connection list, revoke), `src/store/api/mcpApi.ts` (server info),
`src/store/api/adminOauthApi.ts` (client list, revoke) and
`src/store/api/adminMcpApi.ts` (analytics, logs
client).

**New tag types** in `api.ts`: `v1/oauth/authorize/info/{requestId}`,
`v1/oauth/connection/list`, `v1/mcp/info`, `v1/admin/mcp/analytics`,
`v1/admin/oauth/client/list`, `v1/admin/mcp/log/list`.

**`src/model/be/jinear-core.ts` was regenerated** from `jinear-core/jinear-core.ts`. The
app's copy was stale and had no MCP types at all. Apart from the MCP additions the diff is
field reordering from the generator.

**New strings** in `src/locales/strings.ts`, EN and TR, for the consent screen, the profile
section, the admin page and the instance flag.

New components render `currentData` rather than gating on `isFetching`, following the app's
existing convention, so a refetch never blanks a screen the user is part way through.

---

## 8. jinear-site changes

**New**: `app/mcp/page.tsx` and `index.module.scss`. A static page at `/mcp` that is both
the marketing page for the feature and the target of `jinear.mcp.documentation-url`, for
the hosted instance and every self-hosted one.

It renders the **generated manifest**, `lib/mcp-tools.generated.json`, grouped by the scope
each tool requires, so the published tool list cannot drift from what the server actually
offers.

**`McpToolManifestExportTest` was extended** to emit a `scopes` map alongside `tools`. The
`tools` array stays exactly what `tools/list` returns, so it cannot drift from the protocol
shape; scopes are not part of that shape, so they ride alongside rather than inside.

**A latent bug in that test was fixed.** Tool scopes came from `Set.of`, whose iteration
order changes per JVM run, so the committed manifest and the regenerated one would differ
on a second run and the comparison would flap. Scope values are now sorted.

Also: `/mcp` added to `app/sitemap.ts`, to `app/llms.txt/route.ts` (both the feature list
and the key pages index), and a footer link in `BareFooter.tsx`. `MCP_DOCS_URL` added to
`utils/constants.ts`.

---

## 9. jinear-installation-scripts changes

**One question**, asked next to the other optional features: "Enable the MCP server now?"
It warns when `EXTERNAL_SCHEME` is not `https`, because Claude and ChatGPT refuse to
connect over plain HTTP.

**`OAUTH_JWT_SECRET` is generated unconditionally**, whether or not MCP is turned on, so
enabling it later needs no new secret. It uses `generate_secret`, the same 64 character
generator as `JWT_SECRET`, and is deliberately a different value.

**The four URLs are derived, not asked for.** `templates/docker-compose.yaml` builds them
from `DOMAIN` and `API_DOMAIN`, which the script already collects:

```yaml
OAUTH_ISSUER_URL:   ${EXTERNAL_SCHEME}://${API_DOMAIN}${PUBLIC_PORT_SUFFIX}
MCP_RESOURCE_URL: ${EXTERNAL_SCHEME}://${API_DOMAIN}${PUBLIC_PORT_SUFFIX}/mcp
OAUTH_CONSENT_URL:  ${EXTERNAL_SCHEME}://${DOMAIN}${PUBLIC_PORT_SUFFIX}/oauth/consent?request_id={requestId}
```

A mismatch between these three is the failure that would generate every support question,
and it fails inside Claude where there is nothing to read. Deriving them removes the
possibility.

`templates/application.properties.template` gained the `jinear.mcp.*` block,
`jwt.oauth.secret` and `fe.oauth-consent-url`. `.env.template` and the installation `README.md`
gained an MCP section. The completion summary prints the server address when MCP is on.

**No gateway change was needed.** Both sample Caddyfiles proxy the whole `api.*` host to
`jinear-core`, so `/mcp` and `/.well-known/*` already pass through. The docs name those two
paths anyway, for operators running their own path-based proxy.

---

## 10. docs changes

- **`docs/mcp/README.md`**, new. The user-facing setup guide: the public HTTPS requirement,
  the `mcp-remote` fallback for private networks, the four URL variables and why each must
  be what it is, the two switches, the Claude and ChatGPT steps, the permission table, how
  to turn it off at four different scopes, and troubleshooting.
- **`docs/mcp/IMPLEMENTATION.md`**, this file.
- **Root `README.md`**: a feature bullet, a "Connecting Claude or ChatGPT" section, and a
  line in Configuration.

---

## 11. Known gaps

Be aware of these before signing off.

1. **The end-to-end OAuth round trip has never been run against a real client.** The unit
   and slice tests cover the pieces, but no Claude or ChatGPT connector has completed the
   flow. This is the biggest untested surface, and it needs a publicly reachable HTTPS
   instance to test at all.
2. **`jinear.mcp.documentation-url` defaults to `https://jinear.co/mcp/`**, which only
   exists once `jinear-site` is deployed from this branch.
3. **No directory submission.** This was dropped on purpose. Every instance, hosted or self
   hosted, is added as a custom connector. Some code comments still refer to directory
   review as the reason for a decision; those reasons remain valid as quality bars, but the
   submission is not planned.
4. **Dev experience**: `application-dev.properties` sets `jinear.mcp.enabled=true`, but the
   seeded instance flag is `false`. A developer must turn on **AI Assistants** in the admin
   panel before the OAuth flow will run locally.
5. **No workspace-scoped MCP screens in the app.** `McpManagementController` exposes
   `/v1/mcp/log/list/workspace/{id}` and `/v1/mcp/analytics/workspace/{id}` for workspace
   owners, and nothing in `jinear-app` calls them yet.
6. **`OauthClientRegistrationType.STATIC`** exists in the enum and nothing creates one.

---

## 12. Suggested review order

If you read the code in this order it should make sense without jumping around.

1. `McpProperties`, `McpPaths`, `SecurityConfiguration`, `JwtRequestFilter` -> how requests
   are routed and what is open.
2. `McpDiscoveryService` -> what we advertise.
3. `CimdResolver`, `RedirectUriMatcher`, `PkceValidator` -> the three classes where
   a mistake is a vulnerability.
4. `OauthAuthorizationManager`, `OauthAuthorizationCodeService`, `OauthTokenManager`,
   `OauthRefreshTokenService` -> the OAuth flow end to end.
5. `OauthTokenHelper`, `OauthAccessTokenResolver`, `OauthBearerAuthenticationFilter` -> the
   resource server.
6. `McpController`, `McpProtocolService` -> the transport and the refusal shape.
7. `McpToolRegistry`, `SimpleMcpTool`, `McpToolArguments`, then one config class such as
   `TaskMcpTools` -> the tool pattern.
8. `jinear-app`: `pages/oauth/consent/page.tsx`, then `OauthConsentReturnListener` and
   `util/oauthConsent.ts`, then the two profile and admin components.
9. `jinear-installation-scripts`: the derived URLs in `templates/docker-compose.yaml`.

### Test map

363 tests, all green. `./mvnw test`.

| Class | Covers |
|-------|--------|
| `McpControllerTest` (19) | Handshake, batching, notifications, the 401 and 403 challenge shapes |
| `McpToolCatalogTest` (284) | Every tool's name, title, description, schema and annotation consistency |
| `RedirectUriMatcherTest` (10) | Loopback port-agnostic matching, exact matching otherwise |
| `OauthRefreshTokenServiceTest` (7) | Issue, rotate, reuse detection |
| `OauthTokenHelperTest` (6) | Signature, expiry, audience, issuer |
| `OauthAccessTokenResolverTest` (6) | Bearer extraction, revoked connection, last-used throttle |
| `OauthAuthorizationCodeServiceTest` (6) | Single use, expiry, hash mismatch |
| `PkceValidatorTest` (6) | S256 only, length bounds |
| `McpDiscoveryServiceTest` (6) | Both metadata documents |
| `OauthTokenControllerTest` (5) | OAuth error bodies and status codes |
| `McpServerInfoTest` (4) | **Layer 2.** Both switches, and that the URL is withheld when off |
| `McpAuthorizationGateTest` (3) | **Layer 2.** The instance flag refusal happens before a request is parked |
| `McpToolManifestExportTest` (1) | The published manifest matches the live catalog |

Frontend: `yarn tsc -b` and `yarn build` are clean in both `jinear-app` and `jinear-site`.
`yarn lint` produces only the same `react-hooks/exhaustive-deps` warning the sibling
components already have.
