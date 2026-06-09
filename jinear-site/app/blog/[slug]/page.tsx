import { Metadata } from "next";
import type { FC } from "react";
import Link from "next/link";
import { notFound } from "next/navigation";
import { MDXRemote, type MDXRemoteProps } from "next-mdx-remote/rsc";
import BareNav from "@/components/homepage/bareNav/BareNav";
import BareFooter from "@/components/homepage/bareFooter/BareFooter";
import { SITE_URL } from "@/utils/constants";
import { getAllPosts, getPost, getPublishedSlugs } from "@/lib/posts";
import styles from "../blog.module.scss";

const AVATAR = "https://storage.googleapis.com/jinear-b0/web-assets/jinear-homescreen-images/v3/ben.jpg";

interface Params {
  params: { slug: string };
}

// `next-mdx-remote/rsc`'s MDXRemote is an async Server Component (returns a
// Promise), which React 18's JSX types reject (TS2786). It renders correctly at
// runtime — RSC awaits it — so alias it to a plain component type for the editor.
const Mdx = MDXRemote as unknown as FC<MDXRemoteProps>;

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
    month: "short",
    day: "numeric",
  });
}

export default function BlogPostPage({ params }: Params) {
  const post = getPost(params.slug);
  if (!post || post.draft) notFound();

  const url = `${SITE_URL}/blog/${post.slug}`;
  const author = post.author || "Jin";

  // Newest-first list → "Previous" is the newer neighbour, "Next" the older one.
  const all = getAllPosts();
  const idx = all.findIndex((p) => p.slug === post.slug);
  const prev = idx > 0 ? all[idx - 1] : null;
  const next = idx >= 0 && idx < all.length - 1 ? all[idx + 1] : null;

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
        <div className={styles.wrap}>
          <BareNav active="blog" />

          <Link className={styles.back} href="/blog">
            ← All posts
          </Link>

          <header className={styles.ahead}>
            <div className={styles.postMeta}>
              {post.tags?.length ? <span className={styles.k}>{post.tags[0]}</span> : null}
              <span className={styles.dotS}></span>
              <time dateTime={post.pubDate}>{formatDate(post.pubDate)}</time>
              <span className={styles.dotS}></span>
              <span className={styles.rt}>{post.readingMinutes} min read</span>
            </div>
            <h1 className={styles.title}>{post.title}</h1>
            <div className={styles.by}>
              <img className={styles.av} src={AVATAR} alt={author} />
              <span className={styles.who}>
                <b>{author}</b> <span></span>
              </span>
            </div>
          </header>

          <article className={styles.body}>
            <Mdx source={post.content} />
          </article>

          {post.tags?.length ? (
            <div className={styles.endmatter}>
              <div className={styles.ptags}>
                {post.tags.map((tag) => (
                  <span key={tag}>{tag}</span>
                ))}
              </div>
            </div>
          ) : null}

          <div className={styles.authcard}>
            <img className={styles.av} src={AVATAR} alt={author} />
            <div>
              <h4>Written by {author}</h4>
              <p>
                I build and maintain Jinear in the open. Mostly I write here when something ships, or
                when I&apos;ve changed my mind about how the product should work.
              </p>
            </div>
          </div>

          {prev || next ? (
            <nav className={styles.pager}>
              {prev ? (
                <Link className={styles.prev} href={`/blog/${prev.slug}`}>
                  <div className={styles.dir}>← Previous</div>
                  <div className={styles.pt}>{prev.title}</div>
                </Link>
              ) : (
                <span />
              )}
              {next ? (
                <Link className={styles.next} href={`/blog/${next.slug}`}>
                  <div className={styles.dir}>Next →</div>
                  <div className={styles.pt}>{next.title}</div>
                </Link>
              ) : (
                <span />
              )}
            </nav>
          ) : null}

          <BareFooter />
        </div>
      </div>
    </>
  );
}
