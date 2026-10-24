import { Metadata } from "next";
import Link from "next/link";
import BareNav from "@/components/homepage/bareNav/BareNav";
import BareFooter from "@/components/homepage/bareFooter/BareFooter";
import { SITE_URL } from "@/utils/constants";
import { buildMetadata } from "@/utils/seo";
import { getAllComparisons } from "@/lib/comparisons";
import blogStyles from "../blog/blog.module.scss";
import styles from "./compare.module.scss";

export const metadata: Metadata = buildMetadata({
  title: "Jinear vs Trello, Asana, Notion, Jira and More",
  description:
    "Honest comparisons of Jinear with Trello, Asana, Notion, Jira, Basecamp and others: pricing, self-hosting, and where each tool is the better pick.",
  path: "/compare/",
  ogTitle: "Jinear compared",
});

export default function CompareIndexPage() {
  const comparisons = getAllComparisons();

  const itemListJsonLd = {
    "@context": "https://schema.org",
    "@type": "ItemList",
    name: "Jinear comparisons",
    url: `${SITE_URL}/compare/`,
    itemListElement: comparisons.map((comparison, index) => ({
      "@type": "ListItem",
      position: index + 1,
      name: comparison.title,
      url: `${SITE_URL}/compare/${comparison.slug}/`,
    })),
  };

  return (
    <>
      <script
        type="application/ld+json"
        dangerouslySetInnerHTML={{ __html: JSON.stringify(itemListJsonLd) }}
      />
      <div className={blogStyles.page}>
        <div className={blogStyles.wrap}>
          <BareNav />

          <h1 className={blogStyles.hero}>Jinear, compared.</h1>
          <p className={blogStyles.lede}>
            How Jinear stacks up against the tools people ask about. I build Jinear, so read these with
            that in mind. Every page also says where the other tool is the better choice, because for
            a lot of teams it is.
          </p>

          <section className={blogStyles.blk}>
            <h2>Comparisons</h2>
            <ul className={styles.list}>
              {comparisons.map((comparison) => (
                <li key={comparison.slug} className={styles.item}>
                  <h3 className={blogStyles.postTitle}>
                    <Link href={`/compare/${comparison.slug}`}>Jinear vs {comparison.competitor}</Link>
                  </h3>
                  <p className={styles.excerpt}>{comparison.description}</p>
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
