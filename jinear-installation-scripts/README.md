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
2. **Prompts for configuration** - Asks for your domain, HTTP/HTTPS ports, HTTPS mode (automatic Let's Encrypt, behind your own TLS proxy, or plain HTTP), timezone, and optional email settings
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
- **Ports**: 80 and 443 by default (configurable via `HTTP_PORT` / `HTTPS_PORT` — see [Running behind your own reverse proxy](#running-behind-your-own-reverse-proxy))

## Configuration

After installation, you can modify the configuration:

| File | Purpose |
|------|---------|
| `.env` | Environment variables (domains, passwords, etc.) |
| `.config/application.properties` | Spring Boot application settings |
| `.data/caddy/conf/Caddyfile` | Reverse proxy and SSL configuration |
| `.secrets` | Generated credentials (keep secure!) |

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
```

Point your proxy's virtual hosts for the main, `api.` and `files.` domains at
`http://<this-host>:${HTTP_PORT}`. For a worked Traefik example, see
[`docs/behind-traefik`](../docs/behind-traefik/README.md).

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
add the line to `docker-compose.yaml` (keep the quotes — unquoted `off` is a YAML
boolean and MinIO ignores it) and apply it:

```bash
cd <install-dir>
docker compose up -d jinear-minio
```

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

