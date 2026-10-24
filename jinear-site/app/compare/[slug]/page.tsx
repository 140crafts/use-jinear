import { Metadata } from "next";
import Link from "next/link";
import { notFound } from "next/navigation";
import BareNav from "@/components/homepage/bareNav/BareNav";
import BareFooter from "@/components/homepage/bareFooter/BareFooter";
import { Mdx, mdxComponents } from "@/components/mdx/mdxComponents";
import { MAINTAINER_NAME, SITE_URL } from "@/utils/constants";
import { buildMetadata, MAINTAINER_JSON_LD } from "@/utils/seo";
import { getAllComparisons, getComparison, getPublishedComparisonSlugs } from "@/lib/comparisons";
import blogStyles from "../../blog/blog.module.scss";
import styles from "../compare.module.scss";

const AVATAR = "https://storage.googleapis.com/jinear-b0/web-assets/jinear-homescreen-images/v3/ben.jpg";

interface Params {
  params: { slug: string };
}

// Fully enumerate comparisons at build time; reject anything not generated.
export const dynamicParams = false;

export function generateStaticParams() {
  return getPublishedComparisonSlugs().map((slug) => ({ slug }));
}

export function generateMetadata({ params }: Params): Metadata {
  const comparison = getComparison(params.slug);
  if (!comparison) return { title: "Comparison not found", robots: { index: false, follow: false } };
  return buildMetadata({
    title: comparison.title,
    description: comparison.description,
    path: `/compare/${comparison.slug}/`,
    image: {
      url: `/compare/${comparison.slug}/og.png`,
      width: 1200,
      height: 630,
      alt: comparison.title,
    },
    openGraph: {
      type: "article",
      publishedTime: comparison.pubDate,
      modifiedTime: comparison.updatedDate || comparison.pubDate,
      authors: comparison.author ? [comparison.author] : undefined,
      tags: comparison.tags,
    },
  });
}

function formatDate(iso: string): string {
  return new Date(iso).toLocaleDateString("en-US", {
    year: "numeric",
    month: "short",
    day: "numeric",
  });
}

export default function ComparisonPage({ params }: Params) {
  const comparison = getComparison(params.slug);
  if (!comparison || comparison.draft) notFound();

  const url = `${SITE_URL}/compare/${comparison.slug}/`;
  const author = comparison.author || "Jin";
  const others = getAllComparisons().filter((other) => other.slug !== comparison.slug);

  const articleJsonLd = {
    "@context": "https://schema.org",
    "@type": "Article",
    headline: comparison.title,
    description: comparison.description,
    datePublished: comparison.pubDate,
    dateModified: comparison.updatedDate || comparison.pubDate,
    author: comparison.author === MAINTAINER_NAME ? MAINTAINER_JSON_LD : { "@type": "Person", name: comparison.author || "Jinear" },
    publisher: {
      "@type": "Organization",
      name: "Jinear",
      logo: { "@type": "ImageObject", url: `${SITE_URL}/images/icon/icon-512x512.png` },
    },
    about: [
      { "@type": "SoftwareApplication", name: "Jinear", url: SITE_URL },
      ...(comparison.competitorUrl
        ? [{ "@type": "SoftwareApplication", name: comparison.competitor, url: comparison.competitorUrl }]
        : []),
    ],
    mainEntityOfPage: { "@type": "WebPage", "@id": url },
    url,
    keywords: comparison.tags?.join(", "),
  };

  const breadcrumbJsonLd = {
    "@context": "https://schema.org",
    "@type": "BreadcrumbList",
    itemListElement: [
      { "@type": "ListItem", position: 1, name: "Compare", item: `${SITE_URL}/compare/` },
      { "@type": "ListItem", position: 2, name: comparison.title, item: url },
    ],
  };

  return (
    <>
      <script
        type="application/ld+json"
        dangerouslySetInnerHTML={{ __html: JSON.stringify(articleJsonLd) }}
      />
      <script
        type="application/ld+json"
        dangerouslySetInnerHTML={{ __html: JSON.stringify(breadcrumbJsonLd) }}
      />
      <div className={blogStyles.page}>
        <div className={blogStyles.wrap}>
          <BareNav />

          <Link className={blogStyles.back} href="/compare">
            ← All comparisons
          </Link>

          <header className={blogStyles.ahead}>
            <div className={blogStyles.postMeta}>
              <span className={blogStyles.k}>comparison</span>
              <span className={blogStyles.dotS}></span>
              <time dateTime={comparison.updatedDate || comparison.pubDate}>
                {formatDate(comparison.updatedDate || comparison.pubDate)}
              </time>
            </div>
            <h1 className={blogStyles.title}>{comparison.title}</h1>
            <div className={blogStyles.by}>
              <img className={blogStyles.av} src={AVATAR} alt={author} />
              <span className={blogStyles.who}>
                <b>{author === MAINTAINER_NAME ? <Link href="/about">{author}</Link> : author}</b> <span>builds Jinear</span>
              </span>
            </div>
            {comparison.competitorUrl && comparison.checkedDate ? (
              <p className={styles.checked}>
                {comparison.competitor}&apos;s pricing and features were checked on{" "}
                <time dateTime={comparison.checkedDate}>{formatDate(comparison.checkedDate)}</time>.
                Prices change, so confirm on{" "}
                <a href={comparison.competitorUrl} target="_blank" rel="noopener noreferrer">
                  their site
                </a>{" "}
                before you decide. If something here is wrong, email cagdas@jinear.co and I will fix it.
              </p>
            ) : null}
          </header>

          <article className={blogStyles.body}>
            <Mdx source={comparison.content} components={mdxComponents} />
          </article>

          {others.length ? (
            <section className={`${blogStyles.blk} ${styles.others}`}>
              <h2>Other comparisons</h2>
              <ul>
                {others.map((other) => (
                  <li key={other.slug}>
                    <Link href={`/compare/${other.slug}`}>Jinear vs {other.competitor}</Link>
                  </li>
                ))}
              </ul>
            </section>
          ) : null}

          <BareFooter />
        </div>
      </div>
    </>
  );
}
