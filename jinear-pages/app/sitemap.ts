import { MetadataRoute } from "next";
import { headers } from "next/headers";
import {
  fetchProjectFeedPage,
  fetchPublicProjectInfoByDomain,
} from "@/utils/serverFetch";

const MAX_PAGES = 50;

export default async function sitemap(): Promise<MetadataRoute.Sitemap> {
  const host = headers().get("host") || "";
  const siteUrl = host ? `https://${host}` : "";
  const now = new Date();

  const entries: MetadataRoute.Sitemap = [
    {
      url: `${siteUrl}/`,
      lastModified: now,
      changeFrequency: "daily",
      priority: 1.0,
    },
  ];

  if (!host) return entries;

  const project = await fetchPublicProjectInfoByDomain(host);
  const projectId = project?.data?.projectId;
  if (!projectId) return entries;

  for (let page = 0; page < MAX_PAGES; page++) {
    const feed = await fetchProjectFeedPage(projectId, page);
    const content: any[] = feed?.data?.content ?? [];
    if (!content.length) break;

    for (const post of content) {
      const slug = post.slug ? `${post.slug}-` : "";
      const lastModified = post.updatedDate
        ? new Date(post.updatedDate)
        : post.createdDate
          ? new Date(post.createdDate)
          : now;
      entries.push({
        url: `${siteUrl}/post/${slug}${post.projectPostId}`,
        lastModified,
        changeFrequency: "weekly",
        priority: 0.7,
      });
    }

    const totalPages: number | undefined = feed?.data?.totalPages;
    if (typeof totalPages === "number" && page + 1 >= totalPages) break;
  }

  return entries;
}
