# jinear-web (archived)

> ⚠️ **Archived.** This was the original Jinear frontend, a Next.js (React) app.
> It is no longer built or deployed and is kept here for git history and code
> reference only. Do not depend on this folder from live projects.

## What replaced it

`jinear-web` has been split into two active, root-level projects:

- **[`jinear-app`](../../jinear-app/)** — the main frontend, a Vite + React 19
  installable PWA served behind Caddy. This is what now serves the application
  UI in every compose file, Caddyfile, and install template.
- **[`jinear-site`](../../jinear-site/)** — the public marketing site, pricing
  page, and blog, as a statically-exported Next.js site.

Chat and project Pages — which `jinear-web` also hosted — were dropped along the
way; their backing services live under `archive/jinear-message-service` and
`archive/jinear-pages`.

Git history is preserved: this folder was moved with `git mv`, so
`git log --follow archive/jinear-web/<file>` traces each file back through its
original `jinear-web/` path.
