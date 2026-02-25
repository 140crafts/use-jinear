ARG BUILDER_IMAGE=caddy:2.10.0-builder
ARG BASE_IMAGE=caddy:2.10.0
FROM ${BUILDER_IMAGE} AS builder
RUN xcaddy build v2.10.0 \
    --with github.com/caddy-dns/cloudflare
FROM ${BASE_IMAGE}
COPY --from=builder /usr/bin/caddy /usr/bin/caddy