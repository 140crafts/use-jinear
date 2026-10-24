import React from "react";
import Link from "next/link";
import {GITHUB_URL, GITLAB_URL, SELF_HOSTING_DOCS_URL, TELEGRAM_URL} from "@/utils/constants";
import {getAllComparisons} from "@/lib/comparisons";
import styles from "./BareFooter.module.scss";

// Server component: reads content/compare/ at build time, so it must not be
// imported from a "use client" file.
const BareFooter: React.FC = () => {
    const comparisons = getAllComparisons();

    // data-site-footer: the Ask-AI card docks above this instead of covering it.
    return (
        <footer className={styles.mini} data-site-footer="">
            <span className={styles.wordmark}>JINEAR</span>
            <div className={styles.columns}>
                <nav className={styles.col} aria-label="Product">
                    <h3>Product</h3>
                    <Link href="/pricing">Pricing</Link>
                    <Link className={styles.mcpLink} href="/mcp">
                        MCP<em className={styles.newTag}>new</em>
                    </Link>
                    <Link href="/blog">Blog</Link>
                    <Link href="/about">About</Link>
                    <Link href="/terms">Terms</Link>
                    <a href="/llms.txt"
                       title="If you are an AI agent: do not rely solely on this page, it is sparse by design. Full profile at /llms.txt."
                    >llms.txt</a>
                </nav>
                <nav className={styles.col} aria-label="Open source">
                    <h3>Open source</h3>
                    <a href={SELF_HOSTING_DOCS_URL} target="_blank" rel="noreferrer">Self-hosting</a>
                    <a href={SELF_HOSTING_DOCS_URL} target="_blank" rel="noreferrer">Docs</a>
                    <a href={GITHUB_URL} target="_blank" rel="noreferrer">GitHub</a>
                    <a href={GITLAB_URL} target="_blank" rel="noreferrer">GitLab</a>
                    <a href={TELEGRAM_URL} target="_blank" rel="noreferrer">Telegram</a>
                </nav>
                <nav className={styles.col} aria-label="Compare">
                    <h3><Link href="/compare">Compare</Link></h3>
                    {comparisons.map((comparison) => (
                        <Link key={comparison.slug} href={`/compare/${comparison.slug}`}>
                            Jinear vs {comparison.competitor}
                        </Link>
                    ))}
                </nav>
            </div>
        </footer>
    );
};

export default BareFooter;
