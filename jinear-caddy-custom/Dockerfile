FROM --platform=linux/arm64 caddy:2.8.4-builder AS builder
RUN xcaddy build v2.8.4 \
    --with github.com/caddy-dns/cloudflare
FROM --platform=linux/arm64 caddy:2.8.4
COPY --from=builder /usr/bin/caddy /usr/bin/caddy