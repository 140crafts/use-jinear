import fs from "fs";
import path from "path";
import matter from "gray-matter";

// File-based content: one Markdown/MDX file per entry under content/<dir>/. No
// CMS: add or edit a file, commit, and CI rebuilds the static site. The blog and
// the comparison pages are both collections built on this.

const isProd = process.env.NODE_ENV === "production";

export interface EntryFrontmatter {
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

export type Entry<T extends EntryFrontmatter> = T & { slug: string; content: string };
export type EntryMeta<T extends EntryFrontmatter> = T & { slug: string };

export function createCollection<T extends EntryFrontmatter>(dirName: string) {
  const dir = path.join(process.cwd(), "content", dirName);

  function getSlugs(): string[] {
    if (!fs.existsSync(dir)) return [];
    return fs
      .readdirSync(dir)
      .filter((file) => file.endsWith(".mdx") || file.endsWith(".md"))
      .map((file) => file.replace(/\.mdx?$/, ""));
  }

  function getEntry(slug: string): Entry<T> | null {
    const mdxPath = path.join(dir, `${slug}.mdx`);
    const mdPath = path.join(dir, `${slug}.md`);
    const filePath = fs.existsSync(mdxPath) ? mdxPath : fs.existsSync(mdPath) ? mdPath : null;
    if (!filePath) return null;

    const raw = fs.readFileSync(filePath, "utf8");
    const { data, content } = matter(raw);
    return { ...(data as T), slug, content };
  }

  /** All published entries including body content (drafts hidden in production), newest first. */
  function getAllWithContent(): Entry<T>[] {
    return getSlugs()
      .map((slug) => getEntry(slug))
      .filter((entry): entry is Entry<T> => entry !== null)
      .filter((entry) => !(isProd && entry.draft))
      .sort((a, b) => new Date(b.pubDate).getTime() - new Date(a.pubDate).getTime());
  }

  /** All published entries (drafts hidden in production), newest first. */
  function getAll(): EntryMeta<T>[] {
    return getAllWithContent().map(({ content, ...meta }) => meta as EntryMeta<T>);
  }

  /** Slugs to statically generate (drafts excluded in production). */
  function getPublishedSlugs(): string[] {
    return getAll().map((entry) => entry.slug);
  }

  return { getSlugs, getEntry, getAllWithContent, getAll, getPublishedSlugs };
}
