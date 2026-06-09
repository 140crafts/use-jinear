# Jinear — Project Context

Use this as background context. Do not implement anything from this section — it only describes the existing system. The actual task is described separately after this context block.

## Project Responsibilities

**Jinear** is a project management suite, tasks, calendar, and file storage. It is open source.
Jinear consists of several projects that work together. Projects lives within single monorepo.

### docs
- Documents and tutorials about jinear setup

### jinear-core (main backend)
- Auth (login, registration, sessions)
- Workspace & tier management (free/pro)
- Media storage (S3)
- Generic features reusable across all Jinear-based projects

### jinear-installation-scripts
- Installation script for self hosted option.
- Helps user to fill config files easyly 

### jinear-shared-libs
- Contains shared libraries that can be used across Jinear applications.
- If user wants to create an another service to live within existing stack anything might be shared goes here.
- jinear-rate-limiter: Adds rate limiting with given config. 

### jinear-app (main frontend)
- Jinear's current main frontend — a Vite + React (React 19) single-page app
  served as an installable PWA (vite-plugin-pwa / Workbox, app splash screens +
  manifest). Replaces `jinear-web`, and the PWA install also supersedes the old
  WebView mobile wrapper.
- Routing via react-router-dom; state via Redux Toolkit + redux-persist; rich
  text via TipTap; forms via react-hook-form.
- Push notifications via Firebase (FCM); product analytics via PostHog.
- Runtime env injected with `@import-meta-env` (env values resolved at container
  start, not baked at build). Deployed as a static site behind its own
  `Caddyfile` (see `jinear-app/Dockerfile`).
- Scope vs the old `jinear-web`: chat and project Pages features are intentionally
  dropped (their backing services are archived).

### jinear-site (marketing site + blog)
- Public-facing content site: marketing pages, the pricing page, and the blog
  (the blog moved here from the old `blog.jinear.co` subdomain to `/blog`).
- Next.js 14 App Router with `output: "export"` → a fully static site (real
  pre-rendered HTML per route, for SEO + AI-bot friendliness). Served behind
  its own `Caddyfile` like `jinear-app` (no SPA fallback).
- Blog is file-based (no CMS): one Markdown/MDX file per post under
  `jinear-site/content/blog/`, frontmatter parsed with `gray-matter`, rendered
  with `next-mdx-remote`. Add a file → commit → CI rebuilds.
- SEO/AI-bot infra: per-route metadata, JSON-LD (Organization / Product /
  BlogPosting), `app/sitemap.ts`, `app/robots.ts` (allows AI crawlers),
  `/llms.txt`, and `/blog/rss.xml`.
- Reuses jinear-web's design system (`styles/*`) and homepage/pricing components,
  with the Redux/auth coupling stripped; English-only for now. CTAs link to the
  app via `NEXT_PUBLIC_APP_URL`.
- Hosted topology: apex `jinear.co` → jinear-site, `app.jinear.co` → jinear-app.
  Self-host default is unchanged (app stays on the apex); the marketing site is
  an optional, hosted-oriented add-on.

### jinear-web (legacy frontend — being phased out)
- The original Next.js (React) frontend. Still in the repo and currently the
  service wired into the default deployment, but slated for removal once
  `jinear-app` and `jinear-site` fully replace it. Prefer `jinear-app` for new
  app work and `jinear-site` for new marketing/blog content.

## Archived / Deprecated Projects (`archive/`)

These projects have been removed from the active build and deployment. They are
kept under `archive/` for git history and future code reference only — they are
**not** built (no CI job) or run (no compose service). Do not modify or depend
on them; use them as reference when building their replacements.

### archive/jinear-caddy-custom
- caddy with module: dns.providers.cloudflare; handled traffic, rerouting, ssl certificates.
- Removed because chat and project Pages were removed, so the gateway now runs
  stock `caddy` (the cloudflare DNS module / on-demand TLS existed only to serve
  Pages' custom project domains).

### archive/jinear-message-service
- Removed with the chat feature.
- Previously: handled websockets for real time messaging. Account ids are rooms;
  jinear-core stored messages then called this service's internal endpoint to
  deliver them to the receiving account's room.

### archive/jinear-pages
- Removed with the project Pages feature.
- Previously: each project got a public/restricted project feed (like a project's
  twitter profile, also usable for blogs).

### archive/jinear-webview-mobile
- Jinear's WebView mobile wrapper. Used app ↔ web postMessage patterns.

## Tech Stack

| Layer                       | Tech                                                  |
|-----------------------------|-------------------------------------------------------|
| Backend services            | Java, Spring Boot                                     |
| jinear-app (main frontend)  | Vite + React 19 (PWA), react-router, Redux Toolkit    |
| jinear-site (marketing/blog)| Next.js 14 static export (`output: export`) + MDX     |
| jinear-web (legacy)         | Next.js (React) — being phased out                    |
| Gateway                     | Caddy                                                 |
| Storage                     | Google Cloud Storage or MinIO                         |
| Payments                    | Paddle (web)                                          |
| Push notifications          | Firebase (FCM)                                         |
| Product analytics           | PostHog                                                |

## Dev Configuration

- jinear-core uses `application-dev.properties` (per-project dev config)
- On prod, properties files are provided externally

## Constraints

- Prefer adding to existing services over creating new ones; avoid adding new infrastructure dependencies; don't introduce new libraries for problems already solved by the current stack.
- Any new property, docker compose change etc. should also be added to installation-scripts, docs and if necessary README.md.

## How To Use This Context

Pair this file with a task-specific prompt structured like PROMPT_TEMPLATE.md
