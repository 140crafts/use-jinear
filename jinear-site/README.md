# jinear-site

The public-facing **marketing site, pricing page, and blog** for Jinear.

It is a [Next.js](https://nextjs.org) App Router project built as a **fully
static site** (`output: "export"`) so every route ships pre-rendered HTML (great
for SEO and AI crawlers) and is served by Caddy as plain static files.

This project powers the hosted **jinear.co**. It is **not** part of the default
self-host stack (a self-hosted Jinear just serves the app on your domain); see
[Optional: self-hosting the site](#optional-self-hosting-the-site) below.

## Develop

```bash
npm install
npm run dev      # http://localhost:3000
npm run build    # static export to ./out
```

## Add or edit a blog post (no CMS)

Blog posts are plain Markdown/MDX files; there is no database or admin UI.

1. Create a file in [`content/blog/`](./content/blog), e.g. `my-post.mdx`.
2. Add frontmatter, then write the body in Markdown/MDX:

   ```mdx
   ---
   title: "My Post Title"
   description: "One-sentence summary used for SEO and the blog index."
   pubDate: "2026-06-07"
   tags: ["self-hosting"]
   author: "Jinear Team"
   draft: false
   ---

   Your **Markdown** content here.
   ```

3. Commit. CI rebuilds and redeploys the static site.

`draft: true` hides a post from production builds, the sitemap, and the RSS feed.
The file name (without extension) becomes the URL slug: `my-post.mdx` →
`/blog/my-post`.

## Add or edit a comparison page

Comparison pages ("Jinear vs X") work like blog posts but live in
[`content/compare/`](./content/compare) and are served at `/compare/<slug>`.
They are not listed on `/blog` or in the RSS feed; they appear on `/compare`, in
the sitemap, in `/llms.txt`, and in full in `/llms-full.txt`.

Frontmatter adds three fields to the blog ones. `competitorUrl` and `checkedDate` are
optional: leave both out when the alternative is not a product (for example "DIY with
AI"), and the page drops the "pricing checked on" note and the competitor's JSON-LD entry.

```mdx
---
title: "Jinear vs Trello: An Honest Comparison"
description: "One-sentence summary used for SEO and the /compare index."
pubDate: "2026-10-20"
checkedDate: "2026-10-20"   # when the competitor's pricing and features were last checked
competitor: "Trello"
competitorUrl: "https://trello.com/pricing"
tags: ["comparison", "trello"]
author: "Çağdaş Tunca"
---
```

MDX here has no GFM, so Markdown pipe tables do not render. Use the table component,
with rows written as JSON (double quotes), so `/llms-full.txt` can turn it into a
Markdown table:

```mdx
<CompareTable
  competitor="Trello"
  rows={[
    ["Hosting", "Hosted or self-hosted", "Cloud only"],
  ]}
/>
```

Keep these pages honest: say where the other tool is the better choice, and update
`checkedDate` when you re-check prices.

## SEO & AI-bot features

- Per-route metadata (title, description, canonical, Open Graph, Twitter).
- JSON-LD: `Organization` (site-wide), `SoftwareApplication` (`/`),
  `Product`/`Offer` + `FAQPage` (`/pricing`), `AboutPage` (`/about`),
  `BlogPosting` + `BreadcrumbList` (each post), `Article` + `BreadcrumbList`
  (each comparison), `ItemList` (`/compare`). Posts and comparisons by the
  maintainer use `MAINTAINER_JSON_LD` from `utils/seo.ts` as the author, which
  links to `/about`.
- `/about`: the facts about Jinear in one place (license, plans, stack, privacy),
  plus what it does not have. Keep it current, because AI assistants quote it.
- The `/pricing` FAQ lives in `app/pricing/faq.ts`. The page and the `FAQPage`
  JSON-LD both read it, so edit the questions there.
- `app/sitemap.ts` → `/sitemap.xml`, `app/robots.ts` → `/robots.txt`
  (explicitly allows GPTBot, ClaudeBot, PerplexityBot, etc.).
- `/llms.txt`: a link-rich index for AI agents.
- `/blog/rss.xml`: RSS feed.
- Each comparison has its own social card at `/compare/<slug>/og.png`, drawn at
  build time by `app/compare/[slug]/og.png/route.tsx` from the page title. It uses
  the Hanken Grotesk TTFs in `assets/fonts/` (SIL Open Font License, see
  `assets/fonts/OFL.txt`), so the build needs no network.
- `public/og.png`: the site-wide social card, used by every other route.
  Regenerate it from its source after editing the copy:

  ```bash
  "/Applications/Google Chrome.app/Contents/MacOS/Google Chrome" \
    --headless --disable-gpu --hide-scrollbars --force-device-scale-factor=1 \
    --virtual-time-budget=8000 --window-size=1200,630 \
    --screenshot=public/og.png "file://$PWD/scripts/og-image.html"
  ```

### Writing titles and descriptions

Search consoles flag both ends of the range, so when adding a route or a post:

- **Title** 50-60 characters *as rendered*. `app/layout.tsx` appends `", Jinear"`
  (8 chars) via `title.template` to every route **except** `app/page.tsx`, because
  Next skips the segment that declares the template, so the homepage title has to
  carry the brand itself. `buildMetadata()` strips a trailing period from the
  title first, so a title ending in `.` does not render as `Title., Jinear`.
- **Description** 120-160 characters. Under ~70 gets flagged as too short; over
  ~160 is truncated in results.
- Every route needs its **own** title and description. Omitting one makes the page
  inherit the root layout's, which then shows up as a duplicate.

### IndexNow

[IndexNow](https://www.indexnow.org) pushes changed URLs to Bing, Yandex, Seznam
and Naver instead of waiting for a crawl.

- The key lives at [`public/a8ee1f955b4c4073b45836d8b598e619.txt`](./public) and is
  served at `https://jinear.co/a8ee1f955b4c4073b45836d8b598e619.txt`. It is public
  by design (that file *is* the ownership proof), so it is committed.
- [`scripts/indexnow.mjs`](./scripts/indexnow.mjs) verifies the key file, reads the
  **live** `/sitemap.xml`, and submits every URL.

```bash
npm run indexnow -- --dry-run   # show what would be submitted
npm run indexnow                # submit
```

**Run it only after the server has pulled the new image and the new HTML is
actually being served.** IndexNow means "recrawl these now"; firing it early just
gets the old content recrawled. In CI this is the manual `site:indexnow` job,
which runs after `site:deploy`; click it once the deploy has landed.

## Configuration

Public values are baked into the static build at `next build` time. Set them
before building (see [`.env.example`](./.env.example)):

| Variable               | Default                 | Purpose                                   |
|------------------------|-------------------------|-------------------------------------------|
| `NEXT_PUBLIC_SITE_URL` | `https://jinear.co`     | Canonical URL (metadata, sitemap, JSON-LD)|
| `NEXT_PUBLIC_APP_URL`  | `https://app.jinear.co` | Where "log in / sign up / open app" CTAs go |

## Build & run with Docker

```bash
docker build \
  --build-arg NEXT_PUBLIC_SITE_URL=https://jinear.co \
  --build-arg NEXT_PUBLIC_APP_URL=https://app.jinear.co \
  -t jinear-site .
docker run -p 8080:80 jinear-site   # http://localhost:8080
```

The image builds the static export and serves `out/` via Caddy (see
[`Dockerfile`](./Dockerfile) and [`Caddyfile`](./Caddyfile)). TLS is expected to
be terminated by an upstream reverse proxy.

### Analytics proxy (`/relay`)

The Caddyfile proxies `/relay/*` to PostHog US, so ad blockers that drop
`*.posthog.com` requests do not hide visits. The site sends its events to `/relay`
(see `components/postHogProvider/CSPostHogProvider.js`). The hosted app uses the
same proxy: set `VITE_POSTHOG_HOST=https://jinear.co/relay` on the hosted
`jinear-app` only. Self-hosted app images do not include this route, so they never
send data to Jinear's PostHog. `next dev` has no Caddy, so local events are
dropped.

For correct GeoIP, Caddy trusts `X-Forwarded-For` from private-range addresses
(`trusted_proxies static private_ranges`). If the upstream TLS terminator uses a
public address (for example Cloudflare), change that list.

## Optional: self-hosting the site

The default self-host setup routes your apex domain to `jinear-app`. If you also
want the marketing site, run this container and route the apex to it while moving
the app to a subdomain, for example in your Caddyfile:

```
https://your-domain.com {
    reverse_proxy http://jinear-site:80
}
https://app.your-domain.com {
    reverse_proxy http://jinear-app:80
}
```

Then set `NEXT_PUBLIC_APP_URL=https://app.your-domain.com` when building the
image. Most self-hosters won't need this.
