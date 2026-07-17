# Running Jinear Behind nginx

This guide explains how to deploy Jinear behind an existing nginx reverse proxy — the
common case where nginx already terminates TLS on your server (with real or self-signed
certificates) and you don't want the bundled Caddy binding ports 80/443.

The recommended layout keeps the bundled Caddy as Jinear's single internal ingress and
puts nginx in front of it:

```
Internet → nginx (TLS termination) → Caddy (HTTP routing) → Jinear services
```

This is the easiest and least error-prone setup: Caddy already knows how to route to the
app, API, and MinIO, so nginx only forwards three subdomains to one upstream. You don't
configure anything storage-specific.

## Two directives that matter

nginx's defaults break file storage in two ways. The provided example already handles
both — if you copy it, you don't need to think about them, but here's why they're there:

- **`proxy_set_header Host $host;`** — Caddy routes by the Host header, and MinIO
  validates S3 presigned-URL signatures against it. nginx's default `proxy_pass`
  rewrites Host to the upstream name, which makes file uploads and downloads fail with
  **403 Forbidden**. (The app and API are largely Host-insensitive, so they can appear to
  work even when this is wrong — don't use "the app loads" as proof it's set correctly.)
- **`client_max_body_size 0;`** — nginx rejects request bodies over **1 MB** by default,
  so without this, uploading anything larger fails with **413 Request Entity Too Large**.
  `0` means unlimited; set an explicit cap (e.g. `512m`) if you prefer.

## Setup Guide

### Step 1: Install Jinear in "behind a TLS proxy" mode

Run the installer and, at the networking prompt:

- Answer **No** to *"Enable automatic HTTPS via Let's Encrypt?"* (nginx handles TLS).
- Answer **Yes** to *"Will Jinear run behind a proxy that terminates HTTPS?"*
- Pick the HTTP port Caddy should listen on, e.g. `HTTP_PORT=8080`.

This leaves external URLs as `https://` (secure cookies unchanged) while the bundled
Caddy serves plain HTTP on your chosen port. See
[jinear-installation-scripts/README.md](../../jinear-installation-scripts/README.md#running-behind-your-own-reverse-proxy).

### Step 2: Point nginx at the bundled Caddy

Copy [`nginx/jinear.conf`](./nginx/jinear.conf) into your nginx config (e.g.
`/etc/nginx/conf.d/jinear.conf` or `sites-available` + a symlink), then:

- Replace `jinear.example.com` with your domain across all three server blocks.
- Set the `ssl_certificate` / `ssl_certificate_key` paths to your certs (self-signed is
  fine — only browsers need to trust them; the Jinear core never connects to these names).
- Set the `upstream jinear_caddy` address to wherever Caddy's HTTP port is reachable from
  nginx (e.g. `127.0.0.1:8080` if nginx runs on the same host as the Jinear stack).

### Step 3: Reload nginx and start Jinear

```bash
sudo nginx -t && sudo systemctl reload nginx

cd /path/to/jinear
docker compose up -d
```

## Troubleshooting

### File uploads fail with 403 (progress bar stuck)

The browser's `PUT https://files.<your-domain>/...` returns **403**. The `Host` header is
being changed before it reaches MinIO — confirm the `files.` server block has
`proxy_set_header Host $host;` (present in the example) and that no other proxy in front
of nginx is rewriting it. The same mismatch also breaks image/attachment **downloads**,
so fixing it restores both.

### File uploads fail with 413

`client_max_body_size` is too low on the `files.` server block. Set it to `0` (unlimited)
or a large explicit value.

### Alternative: connecting nginx directly to services

If you'd rather bypass the bundled Caddy and proxy each subdomain straight to its service
(`jinear-app:80`, `jinear-core:8008`, `jinear-minio:9000`), the same two directives apply
to the `files.` → MinIO block. The Caddy-in-front layout above is recommended because it
keeps MinIO's Host handling inside Jinear's own config.

## Example Files

- [`nginx/jinear.conf`](./nginx/jinear.conf) — complete nginx server blocks for the
  recommended nginx → Caddy topology.
