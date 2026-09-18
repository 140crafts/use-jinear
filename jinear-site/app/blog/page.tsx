import { Metadata } from "next";
import BareNav from "@/components/homepage/bareNav/BareNav";
import BareFooter from "@/components/homepage/bareFooter/BareFooter";
import PostCard from "@/components/blog/postCard/PostCard";
import { SITE_URL } from "@/utils/constants";
import { buildMetadata } from "@/utils/seo";
import { getAllPosts } from "@/lib/posts";
import styles from "./blog.module.scss";

export const metadata: Metadata = buildMetadata({
  title: "Blog: Open-Source & Self-Hosting Development Notes",
  description:
    "Changelogs, build notes and the occasional opinion on self-hosted task management, open source, and building Jinear. New posts as features ship.",
  path: "/blog/",
  ogTitle: "Jinear Blog",
  ogDescription:
    "Changelogs, build notes and the occasional opinion on self-hosted task management, open source, and building Jinear.",
});

export default function BlogIndexPage() {
  const posts = getAllPosts();

  const blogJsonLd = {
    "@context": "https://schema.org",
    "@type": "Blog",
    name: "Jinear Blog",
    url: `${SITE_URL}/blog/`,
    blogPost: posts.map((post) => ({
      "@type": "BlogPosting",
      headline: post.title,
      description: post.description,
      datePublished: post.pubDate,
      url: `${SITE_URL}/blog/${post.slug}/`,
    })),
  };

  return (
    <>
      <script
        type="application/ld+json"
        dangerouslySetInnerHTML={{ __html: JSON.stringify(blogJsonLd) }}
      />
      <div className={styles.page}>
        <div className={styles.wrap}>
          <BareNav active="blog" />

          <h1 className={styles.hero}>Notes from the workshop.</h1>
          <p className={styles.lede}>
            Changelogs, build notes and the occasional opinion by me
          </p>

          <section className={styles.blk}>
            <h2>Writing</h2>
            <ul className={styles.posts}>
              {posts.map((post) => (
                <PostCard key={post.slug} post={post} />
              ))}
            </ul>
          </section>

          <BareFooter />
        </div>
      </div>
    </>
  );
}
