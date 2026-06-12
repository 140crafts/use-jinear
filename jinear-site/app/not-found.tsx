import React from "react";
import Link from "next/link";
import type { Metadata } from "next";
import BareNav from "@/components/homepage/bareNav/BareNav";
import BareFooter from "@/components/homepage/bareFooter/BareFooter";
import styles from "./not-found.module.scss";

export const metadata: Metadata = {
  title: "Page not found",
  robots: { index: false, follow: true },
};

export default function NotFound() {
  return (
    <div className={styles.page}>
      <div className={styles.wrap}>
        <BareNav />

        <div className={styles.body}>
          <span className={styles.code}>404 — Not found</span>
          <h1 className={styles.title}>We couldn&apos;t find that page.</h1>
          <p className={styles.lede}>
            The link may be broken, or the page may have moved. Let&apos;s get you back on track.
          </p>

          <div className={styles.actions}>
            <Link className={`${styles.btn} ${styles.btnAccent}`} href="/">
              Back to home
            </Link>
            <Link className={`${styles.btn} ${styles.btnGhost}`} href="/blog">
              Read the blog
            </Link>
          </div>
        </div>

        <BareFooter />
      </div>
    </div>
  );
}