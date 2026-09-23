# Upgrading Jinear

Newest release first. Each entry says whether anything has to change before you pull.

Jinear images are tagged `latest`, so an upgrade is:

```bash
cd ~/jinear
docker compose pull && docker compose up -d
```

`docker compose pull` never rewrites your `docker-compose.yaml` or your
`.config/application.properties`. Those files stay exactly as the installer wrote them, so
a new setting has to carry a default in the software. Configuration you add by hand is for
turning a new feature on, not for keeping the server running.

---

## v0.1.1682

Two features land in this release, both optional and both off by default:

- **AI assistant connections (MCP)**, with an OAuth authorization server.
- **Update checks and anonymous statistics.**

### Upgrading is safe to do unattended

There is nothing to change first. Every new setting has a default, so jinear-core starts
with your existing files untouched.

```bash
cd ~/jinear
docker exec jinear-db-backup /backup.sh
docker compose pull && docker compose up -d
```

**The database updates itself.** This release adds seven tables and one instance flag row.
Liquibase applies them on boot. The change is additive: nothing existing is altered or
dropped.

If you do not want assistant connections, you are finished. The feature stays off, the tool
endpoint answers as disabled, and nothing else about your instance changes.

### Turning on AI assistant connections

Only if you want members to connect Claude or ChatGPT. Your instance must be reachable from
the internet over HTTPS with a valid certificate, because those assistants connect from
their own servers rather than from a member's browser.

#### 1. Add the properties

Open `.config/application.properties` and paste this at the end:

```properties
fe.oauth-consent-url=${OAUTH_CONSENT_URL:not-configured}

# ===============================
# = OAUTH AUTHORIZATION SERVER
# ===============================
# Issues the tokens a connected app carries, and runs the consent screen. Switched
# independently of the MCP server below, but a member needs both on to connect an
# assistant: this half authorizes, that half serves the tools.
# jinear.oauth.issuer-url must be the API origin that serves /.well-known/*.
jinear.oauth.enabled=${OAUTH_ENABLED:false}
jinear.oauth.issuer-url=${OAUTH_ISSUER_URL:not-configured}
jinear.oauth.documentation-url=${MCP_DOCUMENTATION_URL:https://jinear.co/mcp/}
jinear.oauth.access-token-validity-minutes=60
jinear.oauth.refresh-token-validity-days=30
jinear.oauth.authorization-code-validity-seconds=60
jinear.oauth.authorization-request-validity-minutes=10
jinear.oauth.dcr-enabled=${OAUTH_DCR_ENABLED:true}
jinear.oauth.cimd-allowed-hosts=${OAUTH_CIMD_ALLOWED_HOSTS:}
jinear.oauth.cimd-fetch-timeout-millis=4000
jinear.oauth.discovery-cache-minutes=5
# Signs the access tokens. Deliberately a different key from jwt.secret, so a
# connected app's token can never authenticate a browser session.
jwt.oauth.secret=${OAUTH_JWT_SECRET:}

# ===============================
# = MCP (AI assistant connections)
# ===============================
# jinear.mcp.resource-url must be exactly the address a member pastes into their
# client, path included: the specification compares the two character for character.
jinear.mcp.enabled=${MCP_ENABLED:false}
jinear.mcp.resource-url=${MCP_RESOURCE_URL:not-configured}
jinear.mcp.documentation-url=${MCP_DOCUMENTATION_URL:https://jinear.co/mcp/}
jinear.mcp.log-retention-days=${MCP_LOG_RETENTION_DAYS:30}
jinear.mcp.max-page-size=50
```

For the update check and usage report, also add:

```properties
jinear.telemetry.update-check.enabled=${TELEMETRY_UPDATE_CHECK:false}
jinear.telemetry.usage-report.enabled=${TELEMETRY_USAGE_REPORT:false}
```

#### 2. Add the environment variables

Open `docker-compose.yaml` and paste this into the `environment:` block of the
`jinear-core` service, next to the `MANAGEMENT_*` lines:

```yaml
      # OAuth authorization server - issues the tokens a connected app carries.
      # The URLs are derived here rather than asked for, because they have to agree:
      # the issuer is the API origin that serves /.well-known, and the consent screen
      # is on the app.
      OAUTH_ENABLED: ${OAUTH_ENABLED:-false}
      OAUTH_ISSUER_URL: ${EXTERNAL_SCHEME:-https}://${API_DOMAIN}${PUBLIC_PORT_SUFFIX:-}
      OAUTH_CONSENT_URL: ${EXTERNAL_SCHEME:-https}://${DOMAIN}${PUBLIC_PORT_SUFFIX:-}/oauth/consent?request_id={requestId}
      OAUTH_JWT_SECRET: ${OAUTH_JWT_SECRET:-}
      OAUTH_DCR_ENABLED: ${OAUTH_DCR_ENABLED:-true}
      OAUTH_CIMD_ALLOWED_HOSTS: ${OAUTH_CIMD_ALLOWED_HOSTS:-}
      # MCP server (optional) - lets members connect Claude or ChatGPT.
      # MCP_RESOURCE_URL is exactly what a member pastes into their client.
      MCP_ENABLED: ${MCP_ENABLED:-false}
      MCP_RESOURCE_URL: ${EXTERNAL_SCHEME:-https}://${API_DOMAIN}${PUBLIC_PORT_SUFFIX:-}/mcp
      MCP_LOG_RETENTION_DAYS: ${MCP_LOG_RETENTION_DAYS:-30}
      # Updates and anonymous statistics (optional, see telemetry.md).
      TELEMETRY_UPDATE_CHECK: ${TELEMETRY_UPDATE_CHECK:-false}
      TELEMETRY_USAGE_REPORT: ${TELEMETRY_USAGE_REPORT:-false}
      DO_NOT_TRACK: ${DO_NOT_TRACK:-}
```

The three URLs are built from `DOMAIN` and `API_DOMAIN`, which are already in your `.env`.
They have to agree with each other, which is why they are derived rather than typed. Change
those two values and all three follow.

#### 3. Add the values to .env

```bash
cd ~/jinear
nano .env
```

```bash
# AI assistant connections
MCP_ENABLED=true
MCP_LOG_RETENTION_DAYS=30
OAUTH_ENABLED=true
OAUTH_JWT_SECRET=
OAUTH_DCR_ENABLED=true
OAUTH_CIMD_ALLOWED_HOSTS=

# Updates and anonymous statistics
TELEMETRY_UPDATE_CHECK=false
TELEMETRY_USAGE_REPORT=false
DO_NOT_TRACK=
```

**`OAUTH_JWT_SECRET` can stay empty.** When it is not set, the server derives its signing
key from `JWT_SECRET`, which you already have, and writes a line in the log saying so. The
derived key is a digest, not `JWT_SECRET` itself, so the two key spaces stay separate: a
token minted for an assistant still cannot authenticate a browser session.

Set it explicitly if you would rather control or rotate that key yourself:

```bash
openssl rand -base64 64 | tr -dc 'a-zA-Z0-9' | head -c 64
```

Changing it later is not disruptive. Access tokens last an hour and stop validating, but
refresh tokens live in the database and are unaffected, so connected assistants get a new
access token on their next refresh without anyone reconnecting.

#### 4. Restart, then turn it on for members

```bash
docker compose up -d
```

Then sign in as the instance admin, open `/admin`, and turn on **AI Assistant Connections**
under instance flags. Both that flag and the two `.env` switches must be on.

Members then find their server address in their own profile, under **AI Assistants**, with
a copy button.

### If assistant connections do not appear

**Everything reports off.** The server treats "switched on but not configured" as off, on
purpose, rather than half running. If `OAUTH_ENABLED` or `MCP_ENABLED` is `true` but the
matching URL is missing, the feature stays off and members see nothing. Check that
`.config/application.properties` has `jinear.oauth.issuer-url` and
`jinear.mcp.resource-url`, and that both resolve to real addresses rather than
`not-configured`.

**A member's client cannot reach the server.** Check that
`https://api.your-domain.com/.well-known/oauth-protected-resource/mcp` answers in a
browser. Two paths must reach jinear-core on the API domain: `/mcp` and `/.well-known/*`.

### Rolling back

Pin the previous image and restart:

```yaml
image: registry.gitlab.com/140crafts/use-jinear/jinear-core:v0.1.1681
```

The new tables are additive, so the older version ignores them. There is no need to restore
the database to roll back the image.