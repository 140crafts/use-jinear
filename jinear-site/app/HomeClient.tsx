import React from "react";
import Link from "next/link";
import BareNav from "@/components/homepage/bareNav/BareNav";
import BareFooter from "@/components/homepage/bareFooter/BareFooter";
import { APP_URL, GITHUB_URL, GITLAB_URL, SELF_HOSTING_DOCS_URL } from "@/utils/constants";
import styles from "./index.module.scss";

const IMG_BASE = "https://storage.googleapis.com/jinear-b0/web-assets/jinear-homescreen-images/v3";

/** A screenshot inside Design A's browser-chrome frame, with a caption. */
function Shot({ src, alt, url, caption }: { src: string; alt: string; url: string; caption: string }) {
  return (
    <figure className={styles.look}>
      <div className={styles.appwin}>
        <div className={styles.appwinBar}>
          <i></i>
          <i></i>
          <i></i>
          <span className={styles.url}>{url}</span>
        </div>
        <img className={styles.lookMedia} src={src} alt={alt} />
      </div>
      <figcaption>{caption}</figcaption>
    </figure>
  );
}

export default function HomeClient() {
  return (
    <div className={styles.page}>
      <div className={styles.wrap}>
        <BareNav />

        <h1 className={styles.hero}>An open-source project manager you can actually own.</h1>
        <p className={styles.lede}>
          Jinear keeps tasks, boards, a calendar and files in one place. Use the hosted version, or{" "}
          <a className={styles.linkU} href={SELF_HOSTING_DOCS_URL} target="_blank" rel="noreferrer">
            run it yourself
          </a>{" "}
          with Docker.
        </p>

        <div className={styles.actions}>
          <a className={`${styles.btn} ${styles.btnAccent}`} href={`${APP_URL}/register`} target="_blank" rel="noreferrer">
            Start for free
          </a>
          <a
            className={`${styles.btn} ${styles.btnGhost} ${styles.btnSm}`}
            href={SELF_HOSTING_DOCS_URL}
            target="_blank"
            rel="noreferrer"
          >
            Read the docs
          </a>
          <span className={styles.hint}>No card. Free for up to 3 people.</span>
        </div>

        <section className={styles.blk}>
          <h2>What it does</h2>
          <ul className={styles.does}>
            <li>
              <span className={styles.k}>tasks</span>
              <span>Lists, labels, checklists and reminders that don&apos;t get in your way.</span>
            </li>
            <li>
              <span className={styles.k}>boards</span>
              <span>Unlimited boards and projects. Drag things where they belong.</span>
            </li>
            <li>
              <span className={styles.k}>calendar</span>
              <span>A built-in calendar, plus Google Calendar sync.</span>
            </li>
            <li>
              <span className={styles.k}>files</span>
              <span>Attach files to tasks, create folders, upload data, and define fine-grained access controls.</span>
            </li>
          </ul>
        </section>

        <section className={styles.blk}>
          <h2>A quick look</h2>
          <Shot
            src={`${IMG_BASE}/tasks.png`}
            alt="Tasks and boards in Jinear"
            url="jinear.co / cagdas / tasks / jinear"
            caption="Tasks, lists and boards, all in one place."
          />
        </section>

        <section className={styles.blk}>
          <h2>Your month at a glance</h2>
          <Shot
            src={`${IMG_BASE}/calendar.png`}
            alt="The calendar in Jinear"
            url="jinear.co / cagdas / calendar"
            caption="A built-in calendar with Google Calendar sync."
          />
        </section>

        <section className={styles.blk}>
          <h2>Your files, your storage</h2>
          <Shot
            src={`${IMG_BASE}/files.png`}
            alt="File storage in Jinear"
            url="jinear.co / cagdas /files"
            caption="Attach files to tasks, kept on your own storage."
          />
          <div className={styles.note}>
            A self hosted Jinear instance runs as a single Docker Compose stack on your own server.{" "}
            <b>No per-user tax, no lock-in, no vendor reading your tasks.</b> The source is on{" "}
            <a className={styles.linkU} href={GITHUB_URL} target="_blank" rel="noreferrer">
              GitHub
            </a>{" "}
            and{" "}
            <a className={styles.linkU} href={GITLAB_URL} target="_blank" rel="noreferrer">
              GitLab
            </a>{" "}
            (AGPL-3.0) Fork it, audit it, change it.
          </div>
        </section>

        <section className={styles.blk}>
          <h2>Pricing, briefly</h2>
          <div className={styles.priceline}>
            <span>
              <strong>Free</strong> for individuals &amp; up to 3 people
            </span>
            <span className={styles.priceSep}>/</span>
            <span>
              <strong>$24.90/mo flat</strong> for unlimited people
            </span>
            <span className={styles.priceSep}>/</span>
            <span>
              <strong>Free</strong> self-hosted, forever
            </span>
          </div>
          <p className={styles.priceMore}>
            <Link className={styles.linkU} href="/pricing">
              See the full pricing →
            </Link>
          </p>
        </section>

        <section className={styles.letter} aria-label="A note from the maker">
          <p className={styles.letterHi}>Hey, I&apos;m Çağdaş.</p>
          <p className={styles.letterP}>
            I build and maintain Jinear on my own, no team, no company behind it. I use
            it every day for my own work, which is why it exists and why it keeps getting worked on.
          </p>
          <p className={styles.letterP}>
            If you run into trouble self-hosting, have a question, or want to suggest something, just email me. I read
            and reply to everything myself. I don&apos;t use your email for marketing or auto reply using AI. I&apos;d love to hear from you.
          </p>
          <p className={styles.letterP}>
            And if you&apos;ve already got Jinear running somewhere, say hello. Self-hosted instances phone nothing home, not
            even install counts, so there&apos;s no dashboard on my end lighting up when you spin one up. Hearing that
            someone found it useful makes my day.
          </p>
          <div className={styles.letterEmailLine}>
            <img className={styles.letterAvatar} src={`${IMG_BASE}/ben.jpg`} alt="Çağdaş, maker of Jinear" />
            <a className={styles.letterEmail} href="mailto:cagdas@jinear.co">
              cagdas@jinear.co
            </a>
          </div>
        </section>

        <BareFooter />
      </div>
    </div>
  );
}
