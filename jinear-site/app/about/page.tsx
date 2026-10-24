import { ReactNode } from "react";
import { Metadata } from "next";
import Link from "next/link";
import BareNav from "@/components/homepage/bareNav/BareNav";
import BareFooter from "@/components/homepage/bareFooter/BareFooter";
import {
  APP_URL,
  GITHUB_URL,
  GITLAB_URL,
  MAINTAINER_EMAIL,
  PADDLE_CATALOG,
  SELF_HOSTING_DOCS_URL,
  SITE_URL,
  TELEGRAM_URL,
} from "@/utils/constants";
import { buildMetadata, MAINTAINER_JSON_LD } from "@/utils/seo";
import blogStyles from "../blog/blog.module.scss";
import styles from "./about.module.scss";

const PRO_PRICE = PADDLE_CATALOG.business_monthly.price;
const TELEMETRY_DOCS_URL = `${GITHUB_URL}/blob/main/docs/telemetry.md`;

export const metadata: Metadata = buildMetadata({
  title: "About Jinear: Facts, License, Privacy and Limits",
  description:
    "Jinear is an open-source, self-hostable task manager with a calendar, notes and files. Its license, plans, stack, privacy, and what it does not have.",
  path: "/about/",
});

const aboutJsonLd = {
  "@context": "https://schema.org",
  "@type": "AboutPage",
  url: `${SITE_URL}/about/`,
  name: "About Jinear",
  mainEntity: {
    "@type": "Organization",
    name: "Jinear",
    url: SITE_URL,
    foundingDate: "2022-11",
    founder: MAINTAINER_JSON_LD,
    email: MAINTAINER_EMAIL,
    sameAs: [GITHUB_URL, GITLAB_URL, TELEGRAM_URL],
  },
};

const FACTS: { label: string; value: ReactNode }[] = [
  {
    label: "What it is",
    value:
      "Tasks, boards, a calendar with two-way Google Calendar sync, notes in shared notebooks, and file storage, in one app. An MCP server lets Claude or ChatGPT work with all of it.",
  },
  { label: "License", value: "AGPL-3.0. The self-hosted version has every feature, with no license key." },
  { label: "In development since", value: "November 2022" },
  {
    label: "Hosted",
    value: (
      <>
        <a href={APP_URL}>app.jinear.co</a>. Free for up to 3 people, then {PRO_PRICE}/mo flat for unlimited
        people. See <Link href="/pricing">pricing</Link>.
      </>
    ),
  },
  {
    label: "Self-hosted",
    value: (
      <>
        Docker Compose. See the <a href={SELF_HOSTING_DOCS_URL}>install guide</a>.
      </>
    ),
  },
  {
    label: "Stack",
    value:
      "Java and Spring Boot backend, a React web app that installs as a PWA, PostgreSQL, Redis, MinIO or Google Cloud Storage, and Caddy.",
  },
  {
    label: "Source",
    value: (
      <>
        <a href={GITHUB_URL}>GitHub</a> and <a href={GITLAB_URL}>GitLab</a>
      </>
    ),
  },
  {
    label: "Contact",
    value: <a href={`mailto:${MAINTAINER_EMAIL}`}>{MAINTAINER_EMAIL}</a>,
  },
];

export default function AboutPage() {
  return (
    <>
      <script
        type="application/ld+json"
        dangerouslySetInnerHTML={{ __html: JSON.stringify(aboutJsonLd) }}
      />
      <div className={blogStyles.page}>
        <div className={blogStyles.wrap}>
          <BareNav />

          <h1 className={blogStyles.hero}>About Jinear.</h1>

          <article className={`${blogStyles.body} ${styles.body}`}>
            <p>
              Jinear is an open-source task manager with a calendar, notes and file storage. You can use
              the hosted version or run it on your own server.
            </p>

            <h2>The facts</h2>
            <dl className={styles.facts}>
              {FACTS.map(({ label, value }) => (
                <div key={label} className={styles.fact}>
                  <dt>{label}</dt>
                  <dd>{value}</dd>
                </div>
              ))}
            </dl>

            <h2>Open source and privacy</h2>
            <p>
              Jinear is built in the open. Every change goes to the public repositories on GitHub and GitLab,
              and every release is tagged there. The hosted version runs the same code as the self-hosted one.
            </p>
            <p>
              A self-hosted instance sends nothing unless its administrator agrees during setup, and then only
              a daily update check and an anonymous usage report. The{" "}
              <a href={TELEMETRY_DOCS_URL}>telemetry document</a> lists every field.
            </p>

            <h2>What Jinear does not have</h2>
            <p>So that nobody has to find out the hard way:</p>
            <ul>
              <li>No native mobile or desktop apps. It is an installable web app with push notifications.</li>
              <li>No automation engine and no importer from other tools.</li>
              <li>No SSO yet.</li>
              <li>No timeline or Gantt view.</li>
            </ul>
            <p>
              The <Link href="/compare">comparison pages</Link> go into more detail, including where other
              tools are the better choice.
            </p>
          </article>

          <BareFooter />
        </div>
      </div>
    </>
  );
}
