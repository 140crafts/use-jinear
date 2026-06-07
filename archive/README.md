# Archived Projects

These projects were once part of the Jinear stack but have been **removed from
the active build and deployment**. They are kept here for **git history and
future code reference only**.

> ⚠️ Nothing in `archive/` is built (no CI job) or run (no compose service).
> Do not depend on these folders from live projects. Treat them as read-only
> reference when building their replacements.

Git history is fully preserved — these folders were moved here with `git mv`, so
`git log --follow archive/<folder>/<file>` traces each file back through its
original top-level path.

## Contents

| Folder                 | What it was                                                                 | Why archived                                                                                 |
|------------------------|-----------------------------------------------------------------------------|----------------------------------------------------------------------------------------------|
| `jinear-caddy-custom`  | Caddy build with the `dns.providers.cloudflare` module (DNS / on-demand TLS) | Pages was removed; the gateway now runs stock `caddy`. The DNS module only served Pages' custom project domains. |
| `jinear-message-service` | WebSocket service for real-time chat (account ids as rooms)                | Chat feature removed.                                                                         |
| `jinear-pages`         | Per-project public/restricted feed ("project's twitter profile" / blogs)     | Project Pages feature removed.                                                                |
| `jinear-webview-mobile` | WebView mobile wrapper using app ↔ web `postMessage`                        | Superseded; the frontend is being replaced by `jinear-app`.                                  |

Live projects remain at the repository root (`jinear-core`, `jinear-web`,
`jinear-app`, `jinear-shared-libs`, `jinear-installation-scripts`, `docs`).