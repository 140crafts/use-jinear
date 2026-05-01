import React from "react";
import { headers } from "next/headers";
import { Metadata } from "next";
import styles from "./page.module.css";
import ProjectFeedPostDetailScreen from "@/components/projectFeedPostDetailScreen/ProjectFeedPostDetailScreen";
import {
  fetchPublicProjectInfoByDomain,
  fetchProjectFeedPost,
  extractRichTextPlain,
} from "@/utils/serverFetch";

export const revalidate = 300;

interface ProjectPostPageProps {
  params: { projectPostId: string };
}

const getHost = () => headers().get("host") || "";

const extractPostId = (slugWithId: string): string =>
  slugWithId
    ? slugWithId.substring(slugWithId.length - 26, slugWithId.length)
    : slugWithId;

async function loadPost(host: string, projectPostIdSlug: string) {
  const project = await fetchPublicProjectInfoByDomain(host);
  const projectId = project?.data?.projectId;
  const postId = extractPostId(projectPostIdSlug);
  const post = projectId ? await fetchProjectFeedPost(projectId, postId) : null;
  return { project, post, projectId, postId };
}

export async function generateMetadata({
  params,
}: ProjectPostPageProps): Promise<Metadata> {
  const { project, post } = await loadPost(getHost(), params.projectPostId);
  const projectTitle = project?.data?.title;
  const body = extractRichTextPlain(post?.data?.postBody?.value);
  const description = body.slice(0, 200);
  const headline = body.slice(0, 80);
  const title = headline
    ? `${headline}${projectTitle ? ` — ${projectTitle}` : ""}`
    : projectTitle || "Jinear";
  return {
    title,
    description: description || projectTitle || "",
  };
}

async function ProjectPostPage({ params }: ProjectPostPageProps) {
  const { post, projectId, postId } = await loadPost(
    getHost(),
    params.projectPostId
  );
  const postData = post?.data;
  const body = extractRichTextPlain(postData?.postBody?.value);

  return (
    <div className={styles.container}>
      {postData && (
        <noscript>
          <article>
            <h1>{body.slice(0, 100) || "Post"}</h1>
            <p>{body}</p>
            {postData.account?.username && (
              <p>By {postData.account.username}</p>
            )}
          </article>
        </noscript>
      )}
      {projectId && postId && (
        <ProjectFeedPostDetailScreen postId={postId} projectId={projectId} />
      )}
    </div>
  );
}

export default ProjectPostPage;
