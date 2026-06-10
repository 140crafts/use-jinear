import { SITE_URL } from "@/utils/constants";
import { getAllPosts } from "@/lib/posts";

// Emitted at /llms.txt during `next build`. Follows the llms.txt convention:
// a concise, link-rich index that helps AI agents understand and cite the site.
export const dynamic = "force-static";

export function GET() {
  const posts = getAllPosts();

  const blogLines = posts
    .map((post) => `- [${post.title}](${SITE_URL}/blog/${post.slug}/): ${post.description}`)
    .join("\n");

  const body = `# Jinear

> Jinear is an open-source (AGPL-3.0), self-hostable project management and
> calendar suite for indie developers and small teams. Install the whole stack
> with a single Docker Compose command — no per-user pricing, your data stays on
> your own server. A hosted version is also available.

## Key pages

- [Home](${SITE_URL}/): Overview of Jinear — project management, calendar, and file storage.
- [Pricing](${SITE_URL}/pricing/): Free self-hosting vs. hosted plans. No per-user pricing.
- [Blog](${SITE_URL}/blog/): Guides and updates.

## Self-hosting

- Install guide: https://github.com/140crafts/use-jinear/blob/main/jinear-installation-scripts/README.md
- Source (GitHub): https://github.com/140crafts/use-jinear
- Source (GitLab): https://gitlab.com/140crafts/use-jinear

## Blog posts

${blogLines}
`;

  return new Response(body, {
    headers: { "Content-Type": "text/plain; charset=utf-8" },
  });
}
