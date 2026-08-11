# jinear-app

Jinear's web client: a Vite + React PWA (with an offline service worker and
Firebase Cloud Messaging push). It is packaged as a single Docker image and
distributed to self-hosters, who point it at their own API/host and (optionally)
their own Firebase project **without rebuilding**.

## Development

```bash
yarn install
yarn dev      # Vite dev server
yarn build    # tsc -b && vite build -> dist/
```

Local dev values live in `.env` (gitignored). Only the **keys** in `.env.example`
are part of the public config contract; see below.

## Runtime configuration

The same built image serves any domain. Configuration is injected **at container
start**, not at build time, via [`import-meta-env`](https://github.com/iendeavor/import-meta-env):

1. **Build time**: `@import-meta-env/unplugin` (in `vite.config.ts`) rewrites
   every `import.meta.env.VITE_*` reference into a placeholder. `index.html`
   carries the bootstrap expression
   `globalThis.import_meta_env = JSON.parse('"import_meta_env_placeholder"')`.
2. **Container start**: `docker-entrypoint.sh` runs `import-meta-env`, which
   scans `dist/**/*.{js,html}` and substitutes the placeholders with the real
   process env (from docker-compose), then hands off to Caddy.

So **docker-compose `environment:` is the source of truth.**

> The service worker (`src/sw.ts`) can't read the app's `globalThis.import_meta_env`
> (it never loads `index.html`), so it carries its own copy of the placeholder,
> which the same entrypoint pass substitutes inside `dist/sw.js`. The SW is
> minified with terser (see `vite.config.ts`) because esbuild rewrites the
> placeholder's quotes to backticks, which the substitution CLI won't match.

### Environment variables

Every key below is declared in `.env.example`. **All of them must be present at
container start (empty string is allowed) or the container won't boot**.
`import-meta-env` treats any listed-but-undefined key as a fatal error.

| Variable | Required | Description |
|---|---|---|
| `VITE_API_URL` | yes | Jinear backend API origin, e.g. `https://api.example.com` |
| `VITE_HOST` | yes | Public origin this web app is served on, e.g. `https://app.example.com` |
| `VITE_APPLE_CLIENT_ID` | optional | Apple Sign In Service ID (blank disables) |
| `VITE_APPLE_REDIRECT_URI` | optional | Apple Sign In return URL |
| `VITE_FIREBASE_API_KEY` | optional | Firebase web app config (blank disables web push) |
| `VITE_FIREBASE_AUTH_DOMAIN` | optional | Firebase web app config |
| `VITE_FIREBASE_PROJECT_ID` | optional | Firebase web app config |
| `VITE_FIREBASE_STORAGE_BUCKET` | optional | Firebase web app config |
| `VITE_FIREBASE_MESSAGING_SENDER_ID` | optional | Firebase web app config |
| `VITE_FIREBASE_APP_ID` | optional | Firebase web app config |
| `VITE_FIREBASE_MEASUREMENT_ID` | optional | Firebase web app config |
| `VITE_FIREBASE_VAPID_KEY` | optional | Web Push certificate key pair (Firebase Console → Cloud Messaging) |
| `VITE_POSTHOG_KEY` | optional | PostHog project API key (blank disables analytics) |
| `VITE_POSTHOG_HOST` | optional | PostHog API host, e.g. `https://us.i.posthog.com` |

"Optional" means push / Apple Sign In / analytics are disabled when left blank, but the
**key must still be present** in the environment (compose uses `${VAR:-}` to pass
an empty string). Web push requires a self-hoster's *own* Firebase project; the
service worker only initializes messaging when the Firebase config is non-empty,
so an unconfigured deployment still gets a working offline/cached app.

## Docker

```bash
docker build -t jinear-app .
docker run -p 8080:80 \
  -e VITE_API_URL=https://api.example.com/ \
  -e VITE_HOST=https://app.example.com/ \
  -e VITE_APPLE_CLIENT_ID= -e VITE_APPLE_REDIRECT_URI= \
  -e VITE_FIREBASE_API_KEY= -e VITE_FIREBASE_AUTH_DOMAIN= \
  -e VITE_FIREBASE_PROJECT_ID= -e VITE_FIREBASE_STORAGE_BUCKET= \
  -e VITE_FIREBASE_MESSAGING_SENDER_ID= -e VITE_FIREBASE_APP_ID= \
  -e VITE_FIREBASE_MEASUREMENT_ID= -e VITE_FIREBASE_VAPID_KEY= \
  -e VITE_POSTHOG_KEY= -e VITE_POSTHOG_HOST=https://us.i.posthog.com \
  jinear-app
```

The container serves the static bundle via Caddy on port `80`. In the full
Jinear stack it sits behind the Caddy gateway, which reverse-proxies the main
domain to `jinear-app:80`.

## Adding a new runtime variable

1. Reference it as `import.meta.env.VITE_FOO` in code.
2. Add the key to `.env.example` (and `.env` for dev) and type it in
   `src/vite-env.d.ts`.
3. Add it to the `jinear-app` service in the docker-compose files, the
   installation-scripts template/`install.sh`, and this table.
