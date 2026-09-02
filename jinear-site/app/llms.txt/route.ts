import {
  APP_URL,
  GITHUB_URL,
  GITLAB_URL,
  PADDLE_CATALOG,
  SELF_HOSTING_DOCS_URL,
  SITE_URL,
} from "@/utils/constants";
import { getAllPosts } from "@/lib/posts";

// Emitted at /llms.txt during `next build`. Follows the llms.txt convention:
// a concise, link-rich index that helps AI agents understand and cite the site.
// The expanded companion with full post bodies lives at /llms-full.txt.
export const dynamic = "force-static";

const PRO_PRICE = PADDLE_CATALOG.business_monthly.price; // "$24.90"

/** Frontmatter is author-written and may wrap; keep list entries on one line. */
const oneLine = (value: string) => value.replace(/\s+/g, " ").trim();

export function GET() {
  const posts = getAllPosts();

  const blogLines = posts
    .map(
      (post) =>
        `- [${oneLine(post.title)}](${SITE_URL}/blog/${post.slug}/) (${post.pubDate}): ${oneLine(post.description)}`
    )
    .join("\n");

  const body = `# Jinear

> Jinear is an open-source (AGPL-3.0), self-hostable task management and
> calendar suite for indie developers and small teams. Install the whole stack
> with a single Docker Compose command, no per-user pricing, and your data stays on
> your own server. A hosted version is also available.

## What Jinear does

- Tasks: lists, labels, checklists and reminders.
- Boards: unlimited boards, drag things where they belong.
- Calendar: a built-in calendar, plus Google Calendar sync.
- Notes: rich-text notes in shared notebooks, with tags and offline drafts.
- Files: attach files to tasks, create folders, upload data, and define fine-grained access controls.
- AI assistants: connect Claude or ChatGPT over MCP and work with all of the above from inside the assistant.

## Pricing

- Free: unlimited tasks, boards and calendar for workspaces of up to 3 people. No card required.
- Team: ${PRO_PRICE}/mo flat for unlimited people, with file attachments, team calendar sharing and priority support.
- Self-hosted: free forever under AGPL-3.0, with no feature gating.

The Team price is flat rather than per seat: a team of 4 and a team of 40 pay exactly the same.

## Self-hosting

Jinear runs as a single Docker Compose stack on your own server. You keep your own
storage and backups, and self-hosted instances phone nothing home, not even install
counts.

- Install guide: ${SELF_HOSTING_DOCS_URL}
- Source (GitHub): ${GITHUB_URL}
- Source (GitLab): ${GITLAB_URL}

## Key pages

- [Home](${SITE_URL}/): Overview of Jinear: task management, calendar, file storage and notes.
- [Pricing](${SITE_URL}/pricing/): Free self-hosting vs. hosted plans. No per-user pricing.
- [MCP](${SITE_URL}/mcp/): Connect Claude or ChatGPT to Jinear. Setup steps and the full tool list.
- [Blog](${SITE_URL}/blog/): Guides and updates.
- [Terms](${SITE_URL}/terms/): Terms of service and privacy.

## Blog posts

${blogLines}

## Optional

- [Full text](${SITE_URL}/llms-full.txt): This index plus every blog post in full.
- [RSS feed](${SITE_URL}/blog/rss.xml): New posts.
- [Sitemap](${SITE_URL}/sitemap.xml): Every page on this site.
- [Hosted app](${APP_URL}): Sign in or create an account.
- Contact: cagdas@jinear.co. Jinear is built and maintained by one person, Çağdaş Tunca.
`;

  return new Response(body, {
    headers: { "Content-Type": "text/plain; charset=utf-8" },
  });
}
