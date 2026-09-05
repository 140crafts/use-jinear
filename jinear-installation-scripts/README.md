# Jinear Installation Scripts

This folder contains everything needed to easily self-host Jinear on your own server.

## Quick Start

Download and run the installer on your server:

```bash
curl -sSL https://gitlab.com/140crafts/use-jinear/-/raw/main/jinear-installation-scripts/install.sh -o install.sh && chmod +x install.sh && ./install.sh
```

Or download and run manually:

```bash
# Download the installer
curl -sSL https://gitlab.com/140crafts/use-jinear/-/raw/main/jinear-installation-scripts/install.sh -o install.sh

# Make it executable
chmod +x install.sh

# Run the installer
./install.sh
```

## What the Installer Does

1. **Checks prerequisites** - Verifies Docker, Docker Compose, and other requirements
2. **Prompts for configuration** - Asks for your domain, HTTP/HTTPS ports, HTTPS mode (automatic Let's Encrypt, behind your own TLS proxy, or plain HTTP), timezone, optional email settings, and optional instance management (the admin panel)
3. **Generates secure credentials** - Automatically creates strong passwords and secrets
4. **Creates directory structure** - Sets up all necessary folders
5. **Generates configuration files** - Creates all config files from templates
6. **Starts services** - Pulls Docker images and starts all containers

## Requirements

- **Operating System**: Linux (Ubuntu 20.04+ recommended), macOS, or Windows with WSL2
- **Docker**: Version 20.10 or higher
- **Docker Compose**: Version 2.0 or higher (or docker-compose v1.29+)
- **Disk Space**: Minimum 5GB free (10GB+ recommended)
- **Memory**: Minimum 2GB RAM (4GB+ recommended)
- **Ports**: 80 and 443 by default (configurable via `HTTP_PORT` / `HTTPS_PORT`; see [Running behind your own reverse proxy](#running-behind-your-own-reverse-proxy))

## Configuration

After installation, you can modify the configuration:

| File | Purpose |
|------|---------|
| `.env` | Environment variables (domains, passwords, etc.) |
| `.config/application.properties` | Spring Boot application settings |
| `.data/caddy/conf/Caddyfile` | Reverse proxy and SSL configuration |
| `.secrets` | Generated credentials (keep secure!) |

## Instance Management (Admin Panel)

Jinear can expose an instance admin panel at `https://your-domain.com/admin`, where a
single admin account manages workspaces, teams, accounts and instance flags. It is
optional and disabled by default; the installer offers to set it up.

If you enabled it during install, the login is written to `.secrets` under
**INSTANCE ADMIN**. To enable, disable or change it afterwards, edit `.env` and restart:

```bash
cd ~/jinear
nano .env          # set MANAGEMENT_ENABLED / MANAGEMENT_ADMIN_EMAIL / MANAGEMENT_ADMIN_PASSWORD
docker compose up -d
```

| Variable | Purpose |
|----------|---------|
| `MANAGEMENT_ENABLED` | `true` enables the admin account, `false` disables it |
| `MANAGEMENT_ADMIN_EMAIL` | Email of the admin account (created if it does not exist) |
| `MANAGEMENT_ADMIN_PASSWORD` | Password for that account |

Things worth knowing:

- **`.env` is the source of truth.** On every startup jinear-core creates or promotes the
  account for `MANAGEMENT_ADMIN_EMAIL` and re-applies `MANAGEMENT_ADMIN_PASSWORD`. To
  rotate the password, change it in `.env` and run `docker compose up -d`; changing it
  from inside the app will not survive the next restart.
- **There is exactly one admin.** Any other account holding the admin role has it revoked
  on the next startup. Point `MANAGEMENT_ADMIN_EMAIL` at an existing account to promote it
  instead of creating a new one.
- **Disabling revokes access.** Setting `MANAGEMENT_ENABLED=false` and restarting strips
  the admin role from every account; the account itself and its data stay intact.
- **Keep all three keys present** in `.env`, even when disabled. Leave the email and
  password blank rather than deleting the lines, otherwise jinear-core fails to start.
- The password is written to `.env` unquoted and passed through Docker Compose, so avoid
  spaces, dollar signs, hash signs, curly braces, backslashes, quotes and backticks in it.
  The installer rejects those and can generate a safe password for you.

## AI Assistant Connections (MCP)

Jinear can run an MCP server, which lets a member connect Claude or ChatGPT to this
instance and work with their tasks, projects, notes and files from inside the assistant.
It is optional and disabled by default; the installer offers to turn it on.

**Your instance must be reachable from the internet over HTTPS with a valid certificate.**
Claude and ChatGPT connect from their own servers, not from the member's browser, so an
instance on a private network cannot be added as a custom connector at all. Members of a
private instance can still use Claude Desktop with the `mcp-remote` proxy, which runs on
their own machine.

Two switches have to agree before anybody can connect:

1. `MCP_ENABLED=true` in `.env`. This decides whether the server exists.
2. **AI Assistants** turned on in the admin panel, under instance flags. This is the
   switch an administrator keeps.

Turning either off stops new connections. Assistants that are already connected keep
working until a member disconnects them from their profile page.

| Variable | Purpose |
|----------|---------|
| `MCP_ENABLED` | `true` runs the MCP server and the OAuth server in front of it, `false` turns both off |
| `MCP_LOG_RETENTION_DAYS` | How long the call log is kept before it is rolled up and pruned |
| `OAUTH_JWT_SECRET` | Signs the access tokens the OAuth server issues. Generated by the installer |
| `OAUTH_DCR_ENABLED` | `true` lets a client register itself. Leave on unless you pin clients |
| `OAUTH_CIMD_ALLOWED_HOSTS` | Comma separated hosts allowed to describe a client. Empty accepts any public https host |

Things worth knowing:

- **The URLs are derived, not asked for.** `docker-compose.yaml` builds the issuer, the
  resource and the consent URL from `DOMAIN` and `API_DOMAIN`. A mismatch between them is
  what breaks a connection, so change those two and all three follow.
- **The server address a member needs** is `https://api.your-domain.com/mcp`. It is shown
  on every member's profile page with a copy button, so nobody has to work it out.
- **`OAUTH_JWT_SECRET` is not `JWT_SECRET`.** They are separate on purpose: a connected
  app's token must never be able to open a browser session, and a session cookie must
  never be able to call a tool.
- **There is one switch, not two.** The OAuth authorization server has no enabled flag of
  its own. MCP is the only resource its tokens open, so `MCP_ENABLED` turns both halves on
  and off together.
- **Two paths must reach jinear-core**: `/mcp` and `/.well-known/*`, both on the API
  domain. The bundled Caddy configuration proxies the whole API host, so this is already
  true. If you front Jinear with your own path-based proxy, do not block them.
- **What an assistant can do** is decided by the member on the consent screen, per
  resource and split between reading and writing. Notes, files and the calendar are read
  only, and there are no delete tools.

## Running Behind Your Own Reverse Proxy

By default the installer lets Caddy bind ports **80/443** and issue Let's Encrypt
certificates automatically. If you already run your own reverse proxy (nginx,
Traefik, another Caddy, a load balancer, …), answer **No** to *"Enable automatic
HTTPS via Let's Encrypt?"* and Caddy will serve **plain HTTP** instead. You then
pick the HTTP port your proxy forwards to (e.g. `8080`), and the `443` mapping is
dropped so Caddy never competes for it.

When automatic HTTPS is disabled you choose between two topologies:

| Topology | You pick | Effect |
|----------|----------|--------|
| **Behind a TLS-terminating proxy** | *Yes* to "runs behind a proxy that terminates HTTPS?" | External URLs stay `https://`; secure cookies unchanged. Your proxy handles certificates and forwards HTTP to Caddy. |
| **Plain HTTP (no TLS)** | *No* | External URLs become `http://` and secure/`SameSite=None` cookies are relaxed to `Lax` so login works over http. Intended for internal/LAN use only. |

These map to the following `.env` values (also editable by hand afterwards):

```bash
HTTP_PORT=8080          # port your proxy forwards to
HTTPS_PORT=443          # ignored when AUTO_HTTPS=false (443 not published)
AUTO_HTTPS=false        # Caddy serves plain HTTP, no Let's Encrypt
EXTERNAL_SCHEME=https   # https behind a TLS proxy, http for plain HTTP
JWT_IS_SECURE=true      # false for plain HTTP
JWT_SAME_SITE=None      # Lax for plain HTTP
PUBLIC_PORT_SUFFIX=     # auto-set (e.g. :8080); empty for standard ports 80/443
```

If you serve Jinear directly on a **non-standard port** (e.g. plain HTTP on `8080`),
the installer derives `PUBLIC_PORT_SUFFIX` and embeds it in every generated URL
(`VITE_API_URL`, `CORS_ORIGINS`, `MINIO_BASE_PATH`, …) so the browser origin matches
CORS automatically, no manual URL edits needed. It stays empty for standard ports, so
default installs are unaffected. Hand-set it only if a TLS proxy in front of Jinear
listens on a non-standard port.

Point your proxy's virtual hosts for the main, `api.` and `files.` domains at
`http://<this-host>:${HTTP_PORT}`. For a worked Traefik example, see
[`docs/behind-traefik`](../docs/behind-traefik/README.md).

### If your proxy is another container on the same host

`HTTP_PORT` only matters when the proxy reaches Caddy over a **published host port**. If
your reverse proxy is itself a container on the same Docker host, it can talk to Caddy
directly over a shared network and Jinear doesn't need to publish anything at all.

In the generated `docker-compose.yaml`, remove the whole `ports:` block from
`jinear-caddy` and attach the service to your proxy's existing network:

```yaml
services:
  jinear-caddy:
    # ports: block deleted, nothing is exposed to the host
    networks:
      - jinear-default
      - proxy

networks:
  jinear-default:
    driver: bridge
  proxy:
    external: true          # the network your proxy already runs on
```

Then point your proxy's three virtual hosts at `http://jinear-caddy:80`, i.e. the
**container** port, which is always `80` regardless of `HTTP_PORT`.

Leave `PUBLIC_PORT_SUFFIX` empty in this topology: your proxy owns the public port, so it
is the one that decides whether a port shows up in the browser's `Origin`. Set it only if
that proxy listens on a non-standard port.

> **Recommended: proxy to the bundled Caddy, not to individual services.** Point your
> reverse proxy at the bundled Caddy as a single upstream for the main, `api.` and
> `files.` domains. Caddy is already configured to talk to MinIO correctly, so you
> configure nothing storage-specific; this is the topology used in the
> [Traefik example](../docs/behind-traefik/README.md).
>
> This matters because file storage uses S3 presigned URLs, whose signature is bound to
> the host the browser connects to (`files.<domain>`). The proxy must forward that
> original `Host` header to MinIO unchanged; rewriting it to an internal service name
> makes MinIO reject uploads **and** downloads with **403 Forbidden**. Caddy and Traefik
> preserve `Host` by default. **nginx does not**; its default `proxy_pass` rewrites the
> Host, and because the app and API are largely Host-insensitive they can still appear to
> work, so "the app loads" is not proof the files domain is configured right. For a
> copy-paste-correct nginx config (with the `Host` header and upload body size already
> handled), see [docs/behind-nginx](../docs/behind-nginx/README.md).

## Directory Structure

After installation, your Jinear directory will look like:

```
jinear/
├── docker-compose.yaml      # Docker services configuration
├── .env                     # Environment variables
├── .secrets                 # Generated credentials (chmod 600)
├── .config/
│   ├── application.properties
│   └── db-backup.sh
├── .data/
│   ├── postgres/            # Database files
│   ├── redis/               # Redis data
│   ├── minio/               # File storage
│   └── caddy/               # SSL certificates & config
├── .logs/
│   └── jinear-core/
└── .backups/                # Database backups
```

## Useful Commands

```bash
# Navigate to installation directory
cd jinear

# View all logs
docker compose logs -f

# View specific service logs
docker compose logs -f jinear-core

# Restart all services
docker compose restart

# Stop all services
docker compose down

# Update to latest version
docker compose pull && docker compose up -d

# Check service status
docker compose ps

# Manual database backup
docker exec jinear-db-backup /backup.sh
```

## DNS Configuration

Point these DNS records to your server's IP address:

| Record Type | Name | Value |
|-------------|------|-------|
| A | jinear.yourdomain.com | YOUR_SERVER_IP |
| A | api.jinear.yourdomain.com | YOUR_SERVER_IP |
| A | files.jinear.yourdomain.com | YOUR_SERVER_IP |

SSL certificates are automatically issued via Let's Encrypt.

## Troubleshooting

### Services won't start
```bash
# Check Docker logs
docker compose logs

# Ensure ports are free
sudo lsof -i :80
sudo lsof -i :443
```

### SSL certificate issues

**Staging certificates (untrusted by browsers):**
If you see warnings about untrusted certificates, Caddy might be using the staging server. Fix:
```bash
# Edit Caddyfile to ensure production server is used
# It should have: acme_ca https://acme-v02.api.letsencrypt.org/directory

# Restart Caddy
cd <install-dir>
docker compose restart jinear-caddy
```

**Let's Encrypt rate limits:**
If you've been testing multiple times, you may hit rate limits:
- **50 certificates per domain per week**
- **5 duplicate certificates per week** (same set of domains)

Solutions:
1. **Wait it out** - Rate limits reset after 7 days
2. **Use staging for testing** - Edit Caddyfile global config:
   ```
   {
       acme_ca https://acme-staging-v02.api.letsencrypt.org/directory
   }
   ```
   Then switch back to production when ready:
   ```
   {
       acme_ca https://acme-v02.api.letsencrypt.org/directory
   }
   ```
3. **Check your rate limit status**: https://crt.sh (search your domain)

**Other SSL issues:**
- Ensure DNS is properly configured and propagated
- Check Caddy logs: `docker compose logs jinear-caddy`
- Certificates are stored in `.data/caddy/data/`
- Delete certificates to force renewal: `rm -rf .data/caddy/data/`

### Files subdomain redirects to `jinear-minio:9001`

If opening `https://files.<your-domain>/` redirects your browser to
`http://jinear-minio:9001/` (an unreachable internal address), MinIO's console
redirect is enabled. The `jinear-minio` service must set:

```yaml
    environment:
      MINIO_BROWSER_REDIRECT: "off"
```

This is already in the current template. If you're upgrading an older install,
add the line to `docker-compose.yaml` (keep the quotes; unquoted `off` is a YAML
boolean and MinIO ignores it) and apply it:

```bash
cd <install-dir>
docker compose up -d jinear-minio
```

### File uploads fail with 403 (progress bar stuck)

If picking a file starts an upload that never completes and the browser's network
tab shows a **403 Forbidden** on the `PUT https://files.<your-domain>/...` request,
something between the browser and MinIO is changing the `Host` header. Presigned
URLs are signed for the public files host, and the request must reach MinIO with that
host intact.

The bundled Caddy handles this for you, so the simplest fix is to proxy the `files.`
domain to the bundled Caddy rather than straight to MinIO (see
[Running Behind Your Own Reverse Proxy](#running-behind-your-own-reverse-proxy)). On
nginx, make sure the `files.` server block sets `proxy_set_header Host $host;`; its
default `proxy_pass` rewrites the Host and causes this exact 403. A copy-paste-correct
nginx config is in [docs/behind-nginx](../docs/behind-nginx/README.md). The same
mismatch also breaks image/attachment **downloads**, so fixing it restores both.

### "You're offline" page and CORS `No 'Access-Control-Allow-Origin'` errors

If the app shows the **"You're offline"** page and devtools reports a CORS error like
`Access to fetch at 'http://api.<domain>:8080/v1/account' from origin
'http://<domain>:8080' has been blocked by CORS policy: No 'Access-Control-Allow-Origin'
header`, your generated URLs are missing the port. This is fixed for new installs (the
installer now sets `PUBLIC_PORT_SUFFIX`). For an `.env` generated before the fix, add it
and re-create the containers:

```bash
cd <install-dir>
echo 'PUBLIC_PORT_SUFFIX=:8080' >> .env   # use your actual public port
docker compose up -d
```

(The offline page is a side effect: the blocked `/v1/account` request makes the app
think it is offline. Fixing CORS clears both.)

### Database connection issues
```bash
# Check if database is running
docker compose ps jinear-db

# View database logs
docker compose logs jinear-db
```

### Reset installation
```bash
# Stop and remove everything
docker compose down -v

# Remove data (WARNING: This deletes all data!)
rm -rf .data .logs .backups

# Re-run installer
./install.sh
```

## Files in This Directory

| File | Description |
|------|-------------|
| `install.sh` | Main interactive installation script |
| `.env.template` | Template for environment variables |
| `templates/docker-compose.yaml` | Docker Compose configuration |
| `templates/Caddyfile.template` | Caddy reverse proxy template (automatic HTTPS) |
| `templates/Caddyfile.http.template` | Caddy reverse proxy template (plain HTTP / behind your own proxy) |
| `templates/application.properties.template` | Spring Boot config template |
| `templates/db-backup.sh` | Database backup script |

## Support

- 📖 Documentation: [GitLab Repository](https://gitlab.com/140crafts/use-jinear)
- 🐛 Issues: [Issue Tracker](https://gitlab.com/140crafts/use-jinear/-/issues)
- 💬 Contact: [@cagdasplus on X](https://x.com/cagdasplus)

## License

This project is licensed under [AGPL-3.0](https://gitlab.com/140crafts/use-jinear/-/raw/main/LICENCE).

