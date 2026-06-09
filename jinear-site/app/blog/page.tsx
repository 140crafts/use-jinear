import { Metadata } from "next";
import Link from "next/link";
import HomePageNavbar from "@/components/homepage/navbar/HomePageNavbar";
import Footer from "@/components/homepage/footer/Footer";
import { SITE_URL } from "@/utils/constants";
import { getAllPosts } from "@/lib/posts";
import styles from "./blog.module.scss";

export const metadata: Metadata = {
  title: "Blog",
  description:
    "Updates, guides, and thoughts on self-hosted project management, open source, and building Jinear.",
  alternates: { canonical: "/blog" },
  openGraph: {
    type: "website",
    title: "Jinear Blog",
    description:
      "Updates, guides, and thoughts on self-hosted project management, open source, and building Jinear.",
    url: `${SITE_URL}/blog`,
  },
};

function formatDate(iso: string): string {
  return new Date(iso).toLocaleDateString("en-US", {
    year: "numeric",
    month: "long",
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
        <HomePageNavbar />
        <main className={styles.container}>
          <header className={styles.header}>
            <p className={styles.eyebrow}>Blog</p>
            <h1 className={styles.pageTitle}>Notes from the Jinear team</h1>
            <p className={styles.pageSubtitle}>
              Guides and updates on self-hosted project management, open source, and how we build Jinear.
            </p>
            <div className="spacer-h-6" />
          </header>

          <ul className={styles.postList}>
            {posts.map((post) => (
              <li key={post.slug} className={styles.postItem}>
                <Link href={`/blog/${post.slug}`} className={styles.postLink}>
                  <p className={styles.postMeta}>
                    <time dateTime={post.pubDate}>{formatDate(post.pubDate)}</time>
                    {post.tags?.length ? <span className={styles.tag}>{post.tags.join(", ")}</span> : null}
                  </p>
                  <h2 className={styles.postTitle}>{post.title}</h2>
                  <p className={styles.postDescription}>{post.description}</p>
                </Link>
              </li>
            ))}
          </ul>

          <div className="spacer-h-12" />
        </main>
        <Footer />
      </div>
    </>
  );
}
