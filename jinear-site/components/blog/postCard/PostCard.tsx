import React from "react";
import Link from "next/link";
import type { PostMeta } from "@/lib/posts";
import styles from "./PostCard.module.scss";

function formatDate(iso: string): string {
  return new Date(iso).toLocaleDateString("en-US", {
    year: "numeric",
    month: "short",
    day: "numeric",
  });
}

interface PostCardProps {
  post: PostMeta;
}

/** One post in a list: meta row, title link and description. Shared by the blog listing and the homepage. */
const PostCard: React.FC<PostCardProps> = ({ post }) => {
  return (
    <li className={styles.item}>
      <div className={styles.meta}>
        {post.tags?.length ? <span className={styles.tag}>{post.tags[0]}</span> : null}
        <span className={styles.dot}></span>
        <time dateTime={post.pubDate}>{formatDate(post.pubDate)}</time>
      </div>
      <h3 className={styles.title}>
        <Link href={`/blog/${post.slug}`}>{post.title}</Link>
      </h3>
      <p className={styles.excerpt}>{post.description}</p>
    </li>
  );
};

export default PostCard;
