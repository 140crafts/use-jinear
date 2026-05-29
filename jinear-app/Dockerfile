# syntax=docker/dockerfile:1.7

# ─── Build the static bundle ─────────────────────────────────────────────────
FROM node:22-alpine AS builder
WORKDIR /app

COPY package.json yarn.lock ./
RUN yarn install --frozen-lockfile

COPY . .
# Produces dist/ with hashed assets and a SW (src/sw.ts compiled by vite-plugin-pwa).
# import.meta.env.VITE_* references in the source become placeholders in dist/
# (handled by @import-meta-env/unplugin in vite.config.ts).
RUN yarn build


# ─── Serve via Caddy, with a Node-powered entrypoint ─────────────────────────
FROM caddy:2-alpine AS runtime

# @import-meta-env/cli rewrites placeholders in dist/ at container start using
# the real process env, so the same image works on any self-hoster's domain.
# It needs Node — add it once, keep the layer small.
RUN apk add --no-cache nodejs npm \
    && npm install -g @import-meta-env/cli@0.7.4 \
    && npm cache clean --force

WORKDIR /srv
COPY --from=builder /app/dist .
# Schema file telling the CLI which env vars to substitute. Values come from process env, not this file.
COPY --from=builder /app/.env.example .

COPY Caddyfile /etc/caddy/Caddyfile
COPY docker-entrypoint.sh /usr/local/bin/docker-entrypoint.sh
RUN chmod +x /usr/local/bin/docker-entrypoint.sh

EXPOSE 80

ENTRYPOINT ["docker-entrypoint.sh"]
CMD ["caddy", "run", "--config", "/etc/caddy/Caddyfile", "--adapter", "caddyfile"]
