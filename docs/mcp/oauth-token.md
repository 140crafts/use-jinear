# Getting an OAuth token

Jinear is its own OAuth 2.1 authorization server. An assistant or a script gets an access
token from it, then sends that token to the MCP server at `POST /mcp`.

This page lists the calls in order, with curl for each one.

## Before you start

Two properties must be on:

- `jinear.oauth.enabled=true` runs the authorization server.
- `jinear.mcp.enabled=true` runs the MCP server, which is what the token opens.

An administrator must also turn on **AI Assistant Connections** in the admin panel. Without
it, no new connection can be authorized.

A real client does steps 3 to 5 in a browser. The calls below do the same thing from a
shell, which is useful for testing an instance.

## Set up your shell

Every command below keeps its result in a variable, so nothing is written to disk.

```bash
ROOT=http://localhost:8085
REDIRECT_URI=http://127.0.0.1:9876/callback
```

Make the PKCE pair now. The verifier must be 43 to 128 characters, and the challenge is the
URL safe SHA-256 of it with no padding:

```bash
CODE_VERIFIER=$(openssl rand -hex 32)
CODE_CHALLENGE=$(printf '%s' "$CODE_VERIFIER" \
  | openssl dgst -sha256 -binary \
  | openssl base64 \
  | tr '+/' '-_' \
  | tr -d '=\n')
```

The examples read values back with `sed`, so they work on a plain shell. If you have `jq`,
`| jq -r .token` is easier to read than the `sed` line.

## The order

### 1. Log in

`POST /v1/auth/password/email`

This gives you a session JWT. You need it only for step 5, because the consent step reads
the account from the session. All the other OAuth endpoints are public.

```bash
SESSION=$(curl -s -X POST "$ROOT/v1/auth/password/email" \
  -H 'Content-Type: application/json' \
  -d '{"email":"you@example.com","password":"your-password"}' \
  | sed -n 's/.*"token":"\([^"]*\)".*/\1/p')
```

The login also sets a `JWT` cookie. The header is used here because it keeps the shell free
of a cookie file.

### 2. Register a client

`POST /v1/oauth/register`

Answers `201` with a `client_id`. Send JSON, not form data.

```bash
CLIENT_ID=$(curl -s -X POST "$ROOT/v1/oauth/register" \
  -H 'Content-Type: application/json' \
  -d "{
        \"client_name\": \"curl\",
        \"redirect_uris\": [\"$REDIRECT_URI\"],
        \"grant_types\": [\"authorization_code\", \"refresh_token\"],
        \"token_endpoint_auth_method\": \"none\"
      }" \
  | sed -n 's/.*"client_id":"\([^"]*\)".*/\1/p')
```

Put the redirect URI you plan to use in `redirect_uris`. Step 3 refuses a redirect URI that
is not registered.

A client can also identify itself with a Client ID Metadata Document: if the `client_id` is
an `https` URL, the server fetches its metadata instead of storing a registration. Claude
uses this. Registration is then unnecessary.

### 3. Ask for authorization

`GET /v1/oauth/authorize`

| Parameter | Necessary | Notes |
|---|---|---|
| `response_type` | yes | `code` only |
| `client_id` | yes | From step 2 |
| `redirect_uri` | yes | Must be registered |
| `code_challenge` | yes | PKCE, see below |
| `code_challenge_method` | yes | `S256` only |
| `scope` | no | Empty grants every scope |
| `state` | no | Given back to you in step 5 |
| `resource` | no | If sent, must equal `jinear.mcp.resource-url` |

Answers `302` to the consent screen. `-G` makes curl encode the query for you, and `-D -`
prints the headers so the redirect can be read without following it:

```bash
REQUEST_ID=$(curl -s -o /dev/null -D - -G "$ROOT/v1/oauth/authorize" \
  --data-urlencode "response_type=code" \
  --data-urlencode "client_id=$CLIENT_ID" \
  --data-urlencode "redirect_uri=$REDIRECT_URI" \
  --data-urlencode "scope=workspace:read tasks:read tasks:write offline_access" \
  --data-urlencode "state=xyz" \
  --data-urlencode "code_challenge=$CODE_CHALLENGE" \
  --data-urlencode "code_challenge_method=S256" \
  | tr -d '\r' \
  | sed -n 's/.*[?&]request_id=\([^&[:space:]]*\).*/\1/p')
```

### 4. Read what is asked (optional)

`GET /v1/oauth/authorize/info/{requestId}`

Shows the client name, its verified host and the requested scopes. This is what the consent
screen renders.

```bash
curl -s "$ROOT/v1/oauth/authorize/info/$REQUEST_ID"
```

### 5. Approve

`POST /v1/oauth/authorize/consent`

This is the one call that needs the session from step 1.

```bash
REDIRECT=$(curl -s -X POST "$ROOT/v1/oauth/authorize/consent" \
  -H "Authorization: Bearer $SESSION" \
  -H 'Content-Type: application/json' \
  -d "{\"requestId\": \"$REQUEST_ID\", \"approved\": true}" \
  | sed -n 's/.*"data":"\([^"]*\)".*/\1/p')

CODE=$(printf '%s' "$REDIRECT" | sed -n 's/.*[?&]code=\([^&]*\).*/\1/p')
```

The `data` field holds the redirect URL, with `?code=...&state=...` on the end.

### 6. Exchange the code for a token

`POST /v1/oauth/token`, form encoded. Do this at once: the code lives 60 seconds.

```bash
TOKEN_RESPONSE=$(curl -s -X POST "$ROOT/v1/oauth/token" \
  --data-urlencode "grant_type=authorization_code" \
  --data-urlencode "code=$CODE" \
  --data-urlencode "redirect_uri=$REDIRECT_URI" \
  --data-urlencode "client_id=$CLIENT_ID" \
  --data-urlencode "code_verifier=$CODE_VERIFIER")

ACCESS_TOKEN=$(printf '%s' "$TOKEN_RESPONSE" | sed -n 's/.*"access_token":"\([^"]*\)".*/\1/p')
REFRESH_TOKEN=$(printf '%s' "$TOKEN_RESPONSE" | sed -n 's/.*"refresh_token":"\([^"]*\)".*/\1/p')
```

Answers `access_token`, `token_type`, `expires_in`, `scope` and, when `offline_access` was
granted, `refresh_token`.

### 7. Use the token

List the tools:

```bash
curl -s -X POST "$ROOT/mcp" \
  -H "Authorization: Bearer $ACCESS_TOKEN" \
  -H 'Content-Type: application/json' \
  -d '{"jsonrpc":"2.0","id":1,"method":"tools/list"}'
```

Call one:

```bash
curl -s -X POST "$ROOT/mcp" \
  -H "Authorization: Bearer $ACCESS_TOKEN" \
  -H 'Content-Type: application/json' \
  -d '{
        "jsonrpc": "2.0",
        "id": 2,
        "method": "tools/call",
        "params": { "name": "list_workspaces", "arguments": {} }
      }'
```

Call `list_workspaces` first. Nearly every other tool needs a `workspaceId` from it, and the
task tools also need a `teamId` from `list_teams`.

### Refreshing

```bash
curl -s -X POST "$ROOT/v1/oauth/token" \
  --data-urlencode "grant_type=refresh_token" \
  --data-urlencode "refresh_token=$REFRESH_TOKEN" \
  --data-urlencode "client_id=$CLIENT_ID"
```

Each refresh returns a new refresh token and retires the old one. Keep the new one.

## Things that go wrong

**The code expires in 60 seconds.** Run step 6 straight after step 5. The parked
authorization request from step 3 lives 10 minutes.

**PKCE is mandatory.** A missing `code_challenge` is refused, and `S256` is the only
accepted method. The verifier must be 43 to 128 characters.

**No refresh token without `offline_access`.** The server issues one only when that scope is
among the granted scopes.

**An empty `scope` grants everything.** The authorize endpoint falls back to every scope it
knows. Unknown scopes are dropped quietly rather than refusing the whole request.

**A refresh can narrow the scopes but never widen them.**

**Reusing a refresh token kills the connection.** A second use of an already spent refresh
token is read as a leak, so every token for that connection is revoked and the client must
start again at step 3.

**`resource` is optional.** If you send it, it must match `jinear.mcp.resource-url` or you
get `invalid_target`. Real MCP clients send it.

**An empty variable usually means the step before failed.** Print the whole response without
the `sed` line to see the error.

## Scopes

| Scope | What it opens |
|---|---|
| `workspace:read` | Workspaces, teams, members, topics, workflow statuses |
| `tasks:read` | Tasks, boards, comments, attachments |
| `tasks:write` | Creating and updating tasks, boards and comments |
| `calendar:read` | Calendar events |
| `notes:read` | Notebooks and notes |
| `files:read` | Files, folders and links |
| `offline_access` | A refresh token, so the connection lasts |

If a tool answers `403` with `insufficient_scope`, the token is missing a scope that the
tool declares. The `WWW-Authenticate` header names the scopes to ask for.

## Lifetimes

| Item | Default |
|---|---|
| Authorization request | 10 minutes |
| Authorization code | 60 seconds |
| Access token | 60 minutes |
| Refresh token | 30 days |

These come from the `jinear.oauth.*` properties.

## Ending access

| To stop | Do this |
|---|---|
| One client, by itself | `POST /v1/oauth/revoke` |
| One assistant, for one member | `DELETE /v1/oauth/connection/{id}`, or the profile screen |
| One client, for everybody | `DELETE /v1/admin/oauth/client?clientId=` |
| New connections only | Turn off **AI Assistant Connections** in the admin panel |

```bash
curl -s -X POST "$ROOT/v1/oauth/revoke" \
  --data-urlencode "token=$REFRESH_TOKEN" \
  --data-urlencode "token_type_hint=refresh_token"
```

Revoking takes effect at once. The server re-reads the connection on every call, so it does
not wait for the access token to expire.
