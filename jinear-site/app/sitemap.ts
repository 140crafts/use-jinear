import { MetadataRoute } from "next";
import { SITE_URL } from "@/utils/constants";
import { getAllPosts } from "@/lib/posts";

// Emitted as /sitemap.xml at `next build` (static export).
export const dynamic = "force-static";

export default function sitemap(): MetadataRoute.Sitemap {
  const staticRoutes: MetadataRoute.Sitemap = [
    { url: `${SITE_URL}/`, changeFrequency: "weekly", priority: 1 },
    { url: `${SITE_URL}/pricing/`, changeFrequency: "monthly", priority: 0.8 },
    { url: `${SITE_URL}/blog/`, changeFrequency: "weekly", priority: 0.7 },
    { url: `${SITE_URL}/mcp/`, changeFrequency: "monthly", priority: 0.7 },
    { url: `${SITE_URL}/terms/`, changeFrequency: "yearly", priority: 0.3 },
  ];

  const postRoutes: MetadataRoute.Sitemap = getAllPosts().map((post) => ({
    url: `${SITE_URL}/blog/${post.slug}/`,
    lastModified: new Date(post.updatedDate || post.pubDate),
    changeFrequency: "yearly",
    priority: 0.6,
  }));

  return [...staticRoutes, ...postRoutes];
}
