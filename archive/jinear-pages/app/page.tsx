import React from "react";
import { headers } from "next/headers";
import { Metadata } from "next";
import styles from "./page.module.css";
import ProjectFeedScreen from "@/components/projectFeedScreen/ProjectFeedScreen";
import {
  fetchPublicProjectInfoByDomain,
  fetchProjectFeedPage,
  extractRichTextPlain,
} from "@/utils/serverFetch";

export const revalidate = 300;

const getHost = () => headers().get("host") || "";

export async function generateMetadata(): Promise<Metadata> {
  const project = await fetchPublicProjectInfoByDomain(getHost());
  const data = project?.data;
  const description = extractRichTextPlain(data?.info?.value).slice(0, 200);
  return {
    title: data?.title ? data.title : "Jinear",
    description: description || "Project feed on Jinear.",
  };
}

async function ProjectFeedPage() {
  const host = getHost();
  const project = await fetchPublicProjectInfoByDomain(host);
  const projectData = project?.data;
  const projectId = projectData?.projectId;
  const feed = projectId ? await fetchProjectFeedPage(projectId, 0) : null;
  const posts = feed?.data?.content ?? [];

  return (
    <div className={styles.container}>
      {projectData && (
        <noscript>
          <article>
            <h1>{projectData.title}</h1>
            {projectData.info?.value && (
              <p>{extractRichTextPlain(projectData.info.value)}</p>
            )}
            {posts.map((post: any) => {
              const slug = post.slug ? `${post.slug}-` : "";
              const href = `/post/${slug}${post.projectPostId}`;
              const body = extractRichTextPlain(post.postBody?.value);
              return (
                <article key={post.projectPostId}>
                  <h2>
                    <a href={href}>
                      {body.slice(0, 80) || "Post"}
                    </a>
                  </h2>
                  <p>{body}</p>
                  {post.account?.username && (
                    <p>By {post.account.username}</p>
                  )}
                </article>
              );
            })}
          </article>
        </noscript>
      )}
      <ProjectFeedScreen />
    </div>
  );
}

export default ProjectFeedPage;
