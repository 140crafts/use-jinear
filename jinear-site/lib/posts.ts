import { createCollection, type Entry, type EntryFrontmatter, type EntryMeta } from "./content";

// File-based blog: one Markdown/MDX file per post under content/blog/.

export type PostFrontmatter = EntryFrontmatter;
export type PostMeta = EntryMeta<PostFrontmatter>;
export type Post = Entry<PostFrontmatter>;

const posts = createCollection<PostFrontmatter>("blog");

export const getPostSlugs = posts.getSlugs;
export const getPost = posts.getEntry;
export const getAllPostsWithContent = posts.getAllWithContent;
export const getAllPosts = posts.getAll;
export const getPublishedSlugs = posts.getPublishedSlugs;
