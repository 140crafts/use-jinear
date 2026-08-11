import fs from "fs";
import path from "path";
import matter from "gray-matter";

// File-based blog: one Markdown/MDX file per post under content/blog/. No CMS:
// add or edit a file, commit, and CI rebuilds the static site.

const BLOG_DIR = path.join(process.cwd(), "content", "blog");
const isProd = process.env.NODE_ENV === "production";

export interface PostFrontmatter {
  title: string;
  description: string;
  /** ISO date string, e.g. "2026-06-07" */
  pubDate: string;
  updatedDate?: string;
  tags?: string[];
  author?: string;
  /** Drafts are excluded from production builds. */
  draft?: boolean;
}

export interface PostMeta extends PostFrontmatter {
  slug: string;
}

export interface Post extends PostMeta {
  content: string;
}

export function getPostSlugs(): string[] {
  if (!fs.existsSync(BLOG_DIR)) return [];
  return fs
    .readdirSync(BLOG_DIR)
    .filter((file) => file.endsWith(".mdx") || file.endsWith(".md"))
    .map((file) => file.replace(/\.mdx?$/, ""));
}

export function getPost(slug: string): Post | null {
  const mdxPath = path.join(BLOG_DIR, `${slug}.mdx`);
  const mdPath = path.join(BLOG_DIR, `${slug}.md`);
  const filePath = fs.existsSync(mdxPath) ? mdxPath : fs.existsSync(mdPath) ? mdPath : null;
  if (!filePath) return null;

  const raw = fs.readFileSync(filePath, "utf8");
  const { data, content } = matter(raw);
  const fm = data as PostFrontmatter;
  return { ...fm, slug, content };
}

/** All published posts (drafts hidden in production), newest first. */
export function getAllPosts(): PostMeta[] {
  return getPostSlugs()
    .map((slug) => getPost(slug))
    .filter((post): post is Post => post !== null)
    .filter((post) => !(isProd && post.draft))
    .sort((a, b) => new Date(b.pubDate).getTime() - new Date(a.pubDate).getTime())
    .map(({ content, ...meta }) => meta);
}

/** Slugs to statically generate (drafts excluded in production). */
export function getPublishedSlugs(): string[] {
  return getAllPosts().map((post) => post.slug);
}
