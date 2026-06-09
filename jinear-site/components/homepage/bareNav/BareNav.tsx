import React from "react";
import Link from "next/link";
import { GITHUB_URL, SELF_HOSTING_DOCS_URL } from "@/utils/constants";
import styles from "./BareNav.module.scss";

interface BareNavProps {
  /** Bolds the current section's link. */
  active?: "pricing" | "blog";
}

const BareNav: React.FC<BareNavProps> = ({ active }) => {
  return (
    <div className={styles.topbar}>
      <Link href="/">
        <span className={styles.wordmark}>
          JINEAR<span className={styles.dot}>.</span>
        </span>
      </Link>
      <nav>
        <Link className={active === "pricing" ? styles.here : undefined} href="/pricing">
          Pricing
        </Link>
        <a href={SELF_HOSTING_DOCS_URL} target="_blank" rel="noreferrer">
          Self-hosting
        </a>
        <Link className={active === "blog" ? styles.here : undefined} href="/blog">
          Blog
        </Link>
        <a href={GITHUB_URL} target="_blank" rel="noreferrer">
          GitHub
        </a>
      </nav>
    </div>
  );
};

export default BareNav;
