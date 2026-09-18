# Running Jinear Behind Traefik

This guide explains how to deploy Jinear behind a Traefik reverse proxy.

> **Note:** The example in this guide assumes Traefik is running in its own `docker-compose` setup. If you run Traefik in another way (systemd, bare Docker, Kubernetes, etc.), you must adapt the configuration to match the same network and routing principles described here.

## Architecture Options

There are two approaches to run Jinear behind Traefik:

### Option 1: Direct Service Connection

Connect Traefik directly to each Jinear service (web, api, files). This requires configuring individual routes for each service.

See the [main architecture documentation](../../README.md#architecture) for service details and ports.

### Option 2: Traefik → Caddy (Recommended)

Route all traffic through Caddy as an intermediate proxy. This is easier to configure since Caddy already handles the internal routing logic.

```
Internet → Traefik (TLS termination) → Caddy (HTTP routing) → Jinear Services
```

---

## Setup Guide (Option 2)

### Step 1: Create Docker Network

Both Traefik and Jinear containers need to communicate:

```bash
docker network create traefik-net
```

### Step 2: Configure Traefik

Create `docker-compose.yaml` for Traefik:

```yaml
services:
  traefik:
    image: traefik:v3.6
    command:
#      - "--api.insecure=true" # Enable Traefik dashboard and API without authentication (remove for production; for testing/debugging only)
      - "--providers.docker=true"
      - "--providers.file.filename=/etc/traefik/dynamic.yaml"
      - "--providers.file.watch=true"
      - "--entrypoints.web.address=:80"
      - "--entrypoints.websecure.address=:443"
      - "--certificatesresolvers.letsencrypt.acme.tlschallenge=true"
      - "--certificatesresolvers.letsencrypt.acme.email=your-email@example.com"
      - "--certificatesresolvers.letsencrypt.acme.storage=/letsencrypt/acme.json"
    ports:
      - "80:80"
      - "443:443"
      - "8080:8080"
    volumes:
      - /var/run/docker.sock:/var/run/docker.sock
      - ./letsencrypt:/letsencrypt
      - ./dynamic.yaml:/etc/traefik/dynamic.yaml:ro
    networks:
      - traefik-net

networks:
  traefik-net:
    external: true
```

Create `dynamic.yaml` for routing:

```yaml
http:
  routers:
    jinear-main:
      rule: "Host(`jinear.example.com`)"
      entryPoints:
        - websecure
      service: jinear-caddy
      tls:
        certResolver: letsencrypt
    jinear-api:
      rule: "Host(`api.jinear.example.com`)"
      entryPoints:
        - websecure
      service: jinear-caddy
      tls:
        certResolver: letsencrypt
    jinear-files:
      rule: "Host(`files.jinear.example.com`)"
      entryPoints:
        - websecure
      service: jinear-caddy
      tls:
        certResolver: letsencrypt

  services:
    jinear-caddy:
      loadBalancer:
        servers:
          - url: "http://jinear-caddy:80"
```

### Step 3: Configure Jinear

Add `traefik-net` network to the Caddy container in your Jinear `docker-compose.yaml`:

```yaml
jinear-caddy:
  container_name: jinear-caddy
  image: caddy:2.10.0
  restart: unless-stopped
  cap_add:
    - NET_ADMIN
  volumes:
    - ./.data/caddy/conf:/etc/caddy
    - ./.data/caddy/site:/srv
    - ./.data/caddy/data:/data
    - ./.data/caddy/config:/config
  networks:
    - jinear-default
    - traefik-net
  labels:
    - "traefik.enable=false"

networks:
  jinear-default:
    driver: bridge
  traefik-net:
    external: true
```

### Step 4: Modify Caddyfile

Since Traefik handles TLS termination, Caddy must listen on HTTP only.

**Key changes:**
- Add `auto_https off` to disable Caddy's automatic HTTPS
- Change all `https://` site blocks to `http://`
- Declare `trusted_proxies` and rewrite `X-Forwarded-For` on the API host

The last one is not cosmetic. jinear-core rate limits unauthenticated callers by the
leftmost value of `X-Forwarded-For`. With two proxies in front and no `trusted_proxies`
setting, that leftmost value is whatever the caller sent, so anyone can defeat the public
rate limit by varying the header. Declaring the proxies as trusted lets `{client_ip}`
resolve to the real caller, and rewriting the header hands jinear-core that value alone.

Do not use `{remote_host}` here. In this topology that is Traefik's address, which would
put every visitor in one shared rate limit bucket.

Example `Caddyfile`:

```text
{
    debug
    auto_https off
    servers {
        trusted_proxies static private_ranges
    }
}

http://jinear.example.com {
    reverse_proxy http://jinear-app:80
}

http://api.jinear.example.com {
    reverse_proxy http://jinear-core:8008 {
        header_up X-Real-IP {client_ip}
        header_up X-Forwarded-For {client_ip}
        header_up X-Forwarded-Proto {scheme}
    }
}

http://files.jinear.example.com {
    reverse_proxy http://jinear-minio:9000
}
```

`private_ranges` covers the Docker network Traefik sits on. If your clients also reach
Jinear from a private range, replace it with the explicit subnet Traefik uses, otherwise
`{client_ip}` can skip past a real caller. Confirm it by watching the `[OAUTH]` and rate
limit lines in `docker compose logs jinear-core`: they should show visitor addresses, not
a single gateway address.

### Step 5: Start Services

```bash
# Start Traefik
cd /path/to/traefik
docker compose up -d

# Start Jinear
cd /path/to/jinear
docker compose up -d
```

---

## Troubleshooting

### 502 Bad Gateway

Caddy can reach the upstream but receives an error.

```bash
# Check Caddy logs
docker logs jinear-caddy --tail 100
```

**Common cause:** Caddyfile still uses `https://` instead of `http://`.

### 504 Gateway Timeout

Traefik can't get a response from Caddy in time.

```bash
# Test connectivity from Caddy to upstream
docker exec jinear-caddy wget -qO- --timeout=5 http://jinear-core:8008

# Check if service is running
docker ps | grep jinear-core
docker logs jinear-core --tail 50
```

**Common causes:**
- Upstream service not running or still starting
- Service listening on different port
- Network connectivity issues between containers

### Connection Refused

```bash
# Verify container is on correct network
docker network inspect jinear-default | grep -A5 "jinear-core"

# Check what port the service is listening on
docker exec jinear-core netstat -tlnp
```

---

## Example Files

Complete example configurations are available in this directory:
- [`traefik/docker-compose.yaml`](./traefik/docker-compose.yaml) - Traefik configuration
- [`traefik/dynamic.yaml`](./traefik/dynamic.yaml) - Traefik routing rules
- [`jinear/Caddyfile`](./jinear/Caddyfile) - Modified Caddy configuration for HTTP
