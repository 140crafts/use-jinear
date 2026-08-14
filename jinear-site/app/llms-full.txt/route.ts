import { GITHUB_URL, GITLAB_URL, SELF_HOSTING_DOCS_URL, SITE_URL } from "@/utils/constants";
import { getAllPostsWithContent } from "@/lib/posts";

// Emitted at /llms-full.txt during `next build`. The expanded companion to
// /llms.txt: same summary, then every published blog post inlined in full, so an
// agent can answer from a single fetch instead of crawling each post.
export const dynamic = "force-static";

export function GET() {
  const posts = getAllPostsWithContent();

  const articles = posts
    .map((post) => {
      const published = post.updatedDate
        ? `${post.pubDate} (updated ${post.updatedDate})`
        : post.pubDate;
      const tags = post.tags?.length ? `\nTags: ${post.tags.join(", ")}` : "";

      return `# ${post.title}
URL: ${SITE_URL}/blog/${post.slug}/
Published: ${published}${tags}

${post.content.trim()}`;
    })
    // Posts use `---` as a horizontal rule internally, so a `---` separator here
    // would be ambiguous. Each post already opens with an H1 + URL header, which
    // is the boundary.
    .join("\n\n\n");

  const body = `# Jinear

> Jinear is an open-source (AGPL-3.0), self-hostable task management and
> calendar suite for indie developers and small teams. Install the whole stack
> with a single Docker Compose command, no per-user pricing, and your data stays on
> your own server. A hosted version is also available.

This file is the full-text companion to ${SITE_URL}/llms.txt, which carries the
product summary, pricing and site index. Below is every published blog post in full.

- Install guide: ${SELF_HOSTING_DOCS_URL}
- Source (GitHub): ${GITHUB_URL}
- Source (GitLab): ${GITLAB_URL}


${articles}
`;

  return new Response(body, {
    headers: { "Content-Type": "text/plain; charset=utf-8" },
  });
}
