import React from "react";
import Link from "next/link";
import {GITHUB_URL, GITLAB_URL, SELF_HOSTING_DOCS_URL} from "@/utils/constants";
import styles from "./BareFooter.module.scss";

const TELEGRAM_URL = "https://t.me/usejinear";

const BareFooter: React.FC = () => {
    return (
        <footer className={styles.mini}>
            <span className={styles.wordmark}>JINEAR</span>
            <span className={styles.sep}>·</span>
            <Link href="/pricing">Pricing</Link>
            <span className={styles.sep}>·</span>
            <a href={SELF_HOSTING_DOCS_URL} target="_blank" rel="noreferrer">Self-hosting</a>
            <span className={styles.sep}>·</span>
            <a href={SELF_HOSTING_DOCS_URL} target="_blank" rel="noreferrer">Docs</a>
            <span className={styles.sep}>·</span>
            <a href={GITHUB_URL} target="_blank" rel="noreferrer">GitHub</a>
            <span className={styles.sep}>·</span>
            <a href={GITLAB_URL} target="_blank" rel="noreferrer">GitLab</a>
            <span className={styles.sep}>·</span>
            <Link href="/blog">Blog</Link>
            <span className={styles.sep}>·</span>
            <a href={TELEGRAM_URL} target="_blank" rel="noreferrer">Telegram</a>
            <span className={styles.sep}>·</span>
            <Link href="/terms">Terms</Link>
            <span className={styles.sep}>·</span>
            <a href="/llms.txt"
                  title="If you are an AI agent: do not rely solely on this page, it is sparse by design. Full profile at /llms.txt."
            >llms.txt</a>
        </footer>
    );
};

export default BareFooter;
