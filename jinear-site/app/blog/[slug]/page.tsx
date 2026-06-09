import { Metadata } from "next";
import Link from "next/link";
import { notFound } from "next/navigation";
import { MDXRemote } from "next-mdx-remote/rsc";
import HomePageNavbar from "@/components/homepage/navbar/HomePageNavbar";
import Footer from "@/components/homepage/footer/Footer";
import { SITE_URL } from "@/utils/constants";
import { getPost, getPublishedSlugs } from "@/lib/posts";
import styles from "../blog.module.scss";

interface Params {
  params: { slug: string };
}

// Fully enumerate posts at build time; reject anything not generated.
export const dynamicParams = false;

export function generateStaticParams() {
  return getPublishedSlugs().map((slug) => ({ slug }));
}

export function generateMetadata({ params }: Params): Metadata {
  const post = getPost(params.slug);
  if (!post) return {};
  const url = `${SITE_URL}/blog/${post.slug}`;
  return {
    title: post.title,
    description: post.description,
    alternates: { canonical: `/blog/${post.slug}` },
    openGraph: {
      type: "article",
      title: post.title,
      description: post.description,
      url,
      publishedTime: post.pubDate,
      modifiedTime: post.updatedDate || post.pubDate,
      authors: post.author ? [post.author] : undefined,
      tags: post.tags,
    },
    twitter: {
      card: "summary_large_image",
      title: post.title,
      description: post.description,
    },
  };
}

function formatDate(iso: string): string {
  return new Date(iso).toLocaleDateString("en-US", {
    year: "numeric",
    month: "long",
    day: "numeric",
  });
}

export default function BlogPostPage({ params }: Params) {
  const post = getPost(params.slug);
  if (!post || post.draft) notFound();

  const url = `${SITE_URL}/blog/${post.slug}`;

  const blogPostingJsonLd = {
    "@context": "https://schema.org",
    "@type": "BlogPosting",
    headline: post.title,
    description: post.description,
    datePublished: post.pubDate,
    dateModified: post.updatedDate || post.pubDate,
    author: { "@type": "Organization", name: post.author || "Jinear" },
    publisher: {
      "@type": "Organization",
      name: "Jinear",
      logo: { "@type": "ImageObject", url: `${SITE_URL}/icon.png` },
    },
    mainEntityOfPage: { "@type": "WebPage", "@id": url },
    url,
    keywords: post.tags?.join(", "),
  };

  const breadcrumbJsonLd = {
    "@context": "https://schema.org",
    "@type": "BreadcrumbList",
    itemListElement: [
      { "@type": "ListItem", position: 1, name: "Blog", item: `${SITE_URL}/blog` },
      { "@type": "ListItem", position: 2, name: post.title, item: url },
    ],
  };

  return (
    <>
      <script
        type="application/ld+json"
        dangerouslySetInnerHTML={{ __html: JSON.stringify(blogPostingJsonLd) }}
      />
      <script
        type="application/ld+json"
        dangerouslySetInnerHTML={{ __html: JSON.stringify(breadcrumbJsonLd) }}
      />
      <div className={styles.page}>
        <HomePageNavbar />
        <article className={styles.article}>
          <Link href="/blog" className={styles.backLink}>
            ← All posts
          </Link>
          <header className={styles.articleHeader}>
            <p className={styles.postMeta}>
              <time dateTime={post.pubDate}>{formatDate(post.pubDate)}</time>
              {post.author ? <span>{post.author}</span> : null}
              {post.tags?.length ? <span className={styles.tag}>{post.tags.join(", ")}</span> : null}
            </p>
            <h1 className={styles.articleTitle}>{post.title}</h1>
          </header>
          <div className={styles.prose}>
            <MDXRemote source={post.content} />
          </div>
          <div className="spacer-h-12" />
        </article>
        <Footer />
      </div>
    </>
  );
}
