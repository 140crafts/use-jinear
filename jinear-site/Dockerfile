# syntax=docker/dockerfile:1.7

# Base images are overridable so CI can route them through the GitLab dependency
# proxy (avoids Docker Hub rate limits). Defaults work for local `docker build`.
ARG BUILDER_IMAGE=node:20-alpine
ARG RUNTIME_IMAGE=caddy:2-alpine

# ─── Build the static site ───────────────────────────────────────────────────
FROM ${BUILDER_IMAGE} AS builder
WORKDIR /app

COPY package.json package-lock.json ./
RUN npm ci

COPY . .

# Public config baked into the static export at build time. Override per
# deployment with --build-arg (the hosted jinear.co sets app.jinear.co here).
ARG NEXT_PUBLIC_SITE_URL=https://jinear.co
ARG NEXT_PUBLIC_APP_URL=https://app.jinear.co
ENV NEXT_PUBLIC_SITE_URL=${NEXT_PUBLIC_SITE_URL}
ENV NEXT_PUBLIC_APP_URL=${NEXT_PUBLIC_APP_URL}

# `output: "export"` emits a fully static site to out/.
RUN npm run build

# ─── Serve the static files via Caddy ────────────────────────────────────────
FROM ${RUNTIME_IMAGE} AS runtime
WORKDIR /srv
COPY --from=builder /app/out .
COPY Caddyfile /etc/caddy/Caddyfile

EXPOSE 80
CMD ["caddy", "run", "--config", "/etc/caddy/Caddyfile", "--adapter", "caddyfile"]
