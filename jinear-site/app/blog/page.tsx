import { Metadata } from "next";
import Link from "next/link";
import BareNav from "@/components/homepage/bareNav/BareNav";
import BareFooter from "@/components/homepage/bareFooter/BareFooter";
import { SITE_URL } from "@/utils/constants";
import { getAllPosts } from "@/lib/posts";
import styles from "./blog.module.scss";

export const metadata: Metadata = {
  title: "Blog",
  description:
    "Changelogs, build notes and the occasional opinion on self-hosted project management, open source, and building Jinear.",
  alternates: { canonical: "/blog" },
  openGraph: {
    type: "website",
    title: "Jinear Blog",
    description:
      "Changelogs, build notes and the occasional opinion on self-hosted project management, open source, and building Jinear.",
    url: `${SITE_URL}/blog`,
  },
};

function formatDate(iso: string): string {
  return new Date(iso).toLocaleDateString("en-US", {
    year: "numeric",
    month: "short",
    day: "numeric",
  });
}

export default function BlogIndexPage() {
  const posts = getAllPosts();

  const blogJsonLd = {
    "@context": "https://schema.org",
    "@type": "Blog",
    name: "Jinear Blog",
    url: `${SITE_URL}/blog`,
    blogPost: posts.map((post) => ({
      "@type": "BlogPosting",
      headline: post.title,
      description: post.description,
      datePublished: post.pubDate,
      url: `${SITE_URL}/blog/${post.slug}`,
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
            Changelogs, build notes and the occasional opinion — written in the open by the person who
            makes Jinear.
          </p>

          <section className={styles.blk}>
            <h2>Writing</h2>
            <ul className={styles.posts}>
              {posts.map((post) => (
                <li key={post.slug}>
                  <div className={styles.postMeta}>
                    {post.tags?.length ? <span className={styles.k}>{post.tags[0]}</span> : null}
                    <span className={styles.dotS}></span>
                    <time dateTime={post.pubDate}>{formatDate(post.pubDate)}</time>
                    <span className={styles.dotS}></span>
                    <span className={styles.rt}>{post.readingMinutes} min</span>
                  </div>
                  <h3 className={styles.postTitle}>
                    <Link href={`/blog/${post.slug}`}>{post.title}</Link>
                  </h3>
                  <p className={styles.excerpt}>{post.description}</p>
                </li>
              ))}
            </ul>
          </section>

          <BareFooter />
        </div>
      </div>
    </>
  );
}
