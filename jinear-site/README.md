# jinear-site

The public-facing **marketing site, pricing page, and blog** for Jinear.

It is a [Next.js](https://nextjs.org) App Router project built as a **fully
static site** (`output: "export"`) so every route ships pre-rendered HTML —
great for SEO and AI crawlers — and is served by Caddy as plain static files.

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

Blog posts are plain Markdown/MDX files — there is no database or admin UI.

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

## SEO & AI-bot features

- Per-route metadata (title, description, canonical, Open Graph, Twitter).
- JSON-LD: `Organization` (site-wide), `Product`/`Offer` (`/pricing`),
  `BlogPosting` + `BreadcrumbList` (each post).
- `app/sitemap.ts` → `/sitemap.xml`, `app/robots.ts` → `/robots.txt`
  (explicitly allows GPTBot, ClaudeBot, PerplexityBot, etc.).
- `/llms.txt` — a link-rich index for AI agents.
- `/blog/rss.xml` — RSS feed.
- `public/og.png` — the site-wide social card, inherited by every route.
  Regenerate it from its source after editing the copy:

  ```bash
  "/Applications/Google Chrome.app/Contents/MacOS/Google Chrome" \
    --headless --disable-gpu --hide-scrollbars --force-device-scale-factor=1 \
    --virtual-time-budget=8000 --window-size=1200,630 \
    --screenshot=public/og.png "file://$PWD/scripts/og-image.html"
  ```

### Writing titles and descriptions

Search consoles flag both ends of the range, so when adding a route or a post:

- **Title** 50–60 characters *as rendered*. `app/layout.tsx` appends `" — Jinear"`
  (9 chars) via `title.template` to every route **except** `app/page.tsx` — Next
  skips the segment that declares the template, so the homepage title has to
  carry the brand itself.
- **Description** 120–160 characters. Under ~70 gets flagged as too short; over
  ~160 is truncated in results.
- Every route needs its **own** title and description. Omitting one makes the page
  inherit the root layout's, which then shows up as a duplicate.

### IndexNow

[IndexNow](https://www.indexnow.org) pushes changed URLs to Bing, Yandex, Seznam
and Naver instead of waiting for a crawl.

- The key lives at [`public/a8ee1f955b4c4073b45836d8b598e619.txt`](./public) and is
  served at `https://jinear.co/a8ee1f955b4c4073b45836d8b598e619.txt`. It is public
  by design — that file *is* the ownership proof — so it is committed.
- [`scripts/indexnow.mjs`](./scripts/indexnow.mjs) verifies the key file, reads the
  **live** `/sitemap.xml`, and submits every URL.

```bash
npm run indexnow -- --dry-run   # show what would be submitted
npm run indexnow                # submit
```

**Run it only after the server has pulled the new image and the new HTML is
actually being served.** IndexNow means "recrawl these now"; firing it early just
gets the old content recrawled. In CI this is the manual `site:indexnow` job,
which runs after `site:deploy` — click it once the deploy has landed.

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

## Optional: self-hosting the site

The default self-host setup routes your apex domain to `jinear-app`. If you also
want the marketing site, run this container and route the apex to it while moving
the app to a subdomain — for example in your Caddyfile:

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
