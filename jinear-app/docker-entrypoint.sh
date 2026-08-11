#!/bin/sh
# Substitute runtime env vars into the built bundle, then hand off to Caddy.
#
# How it works:
#   - @import-meta-env/unplugin replaced every `import.meta.env.VITE_*`
#     reference at build time with a placeholder string.
#   - This step (re)scans dist/ and rewrites those placeholders using the
#     real process env (the keys to substitute come from .env.example).
#   - Same image, any domain: self-hosters just set env vars on the container.

set -e

import-meta-env \
    --example /srv/.env.example \
    --path '/srv/**/*.{js,html}'

exec "$@"
