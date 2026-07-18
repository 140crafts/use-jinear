import React from "react";
import Link from "next/link";
import BareNav from "@/components/homepage/bareNav/BareNav";
import BareFooter from "@/components/homepage/bareFooter/BareFooter";
import { APP_URL, GITHUB_URL, GITLAB_URL, PADDLE_CATALOG } from "@/utils/constants";
import styles from "./index.module.scss";

const PRO_PRICE = PADDLE_CATALOG.business_monthly.price; // "$24.90"
const AVATAR = "https://storage.googleapis.com/jinear-b0/web-assets/jinear-homescreen-images/v3/ben.jpg";

const PricingClient: React.FC = () => {
  return (
    <div className={styles.page}>
      <div className={styles.wrap}>
        <BareNav active="pricing" />

        <h1 className={styles.hero}>Simple, flat pricing.</h1>
        <p className={styles.lede}>
          Free to start, free to self-host, and one flat fee when you grow. No counting seats, no
          surprise invoices.
        </p>

        {/* PLANS */}
        <section className={styles.blk}>
          <h2>Three plans</h2>

          <div className={styles.plan}>
            <div className={styles.planHead}>
              <div className={styles.planHeadName}>
                <span className={styles.pname}>Starter</span>
              </div>
              <div className={styles.pprice}>
                <strong className={styles.free}>Free</strong>
              </div>
            </div>
            <p className={styles.pdesc}>For yourself and a couple of people getting started.</p>
            <ul className={styles.pfeat}>
              <li>Unlimited tasks, labels &amp; checklists</li>
              <li>Unlimited boards</li>
              <li>Built-in calendar, reminders &amp; Google Calendar sync</li>
              <li>
                Workspaces for up to <b>3 people</b>
              </li>
            </ul>
            <div className={styles.pact}>
              <a className={`${styles.btn} ${styles.btnGhost} ${styles.btnSm}`} href={`${APP_URL}/register`} target="_blank" rel="noreferrer">
                Create an account
              </a>
            </div>
          </div>

          <div className={styles.plan}>
            <div className={styles.planHead}>
              <div className={styles.planHeadName}>
                <span className={styles.pname}>Team</span>
                {/*<span className={styles.ptag}>Recommended</span>*/}
              </div>
              <div className={styles.pprice}>
                <strong>{PRO_PRICE}</strong>
                <span>/mo · flat</span>
              </div>
            </div>
            <p className={styles.pdesc}>For growing teams that need more room and control.</p>
            <ul className={styles.pfeat}>
              <li>Everything in Starter, plus</li>
              <li>
                <b>Unlimited people</b> in your workspace
              </li>
              <li>File attachments with generous storage</li>
              <li>Calendar sharing across your team</li>
              <li>Priority support (usually &lt; 24h)</li>
            </ul>
            <div className={styles.pact}>
              <a className={`${styles.btn} ${styles.btnAccent} ${styles.btnSm}`} href={`${APP_URL}/register`} target="_blank" rel="noreferrer">
                Upgrade your plan
              </a>
            </div>
          </div>

          <div className={styles.plan}>
            <div className={styles.planHead}>
              <div className={styles.planHeadName}>
                <span className={styles.pname}>Self-Hosted</span>
              </div>
              <div className={styles.pprice}>
                <strong className={styles.free}>Free</strong>
              </div>
            </div>
            <p className={styles.pdesc}>For people who want everything on their own server.</p>
            <ul className={styles.pfeat}>
              <li>Host your own Jinear instance</li>
              <li>One Docker Compose file to deploy</li>
              <li>Your storage, your backups</li>
              <li>Open source · AGPL-3.0 · free forever</li>
            </ul>
            <div className={styles.pact}>
              <a className={`${styles.btn} ${styles.btnGhost} ${styles.btnSm}`} href={GITHUB_URL} target="_blank" rel="noreferrer">
                GitHub
              </a>
              <a className={`${styles.btn} ${styles.btnGhost} ${styles.btnSm}`} href={GITLAB_URL} target="_blank" rel="noreferrer">
                GitLab
              </a>
            </div>
          </div>
        </section>

        {/* THE MATH */}
        <section className={styles.blk}>
          <h2>Why one flat price</h2>
          <p className={styles.mathIntro}>
            The usual stack charges for every person, every month, across every tool. Jinear is one
            flat fee add the whole team for the same price.
          </p>
          <div className={styles.math}>
            <div className={styles.row}>
              <span className={styles.label}>Their stack · team of 10</span>
              <span className={styles.amt}>~$330 / mo</span>
            </div>
            <div className={`${styles.row} ${styles.win}`}>
              <span className={styles.label}>Jinear · the same team</span>
              <span className={styles.amt}>{PRO_PRICE} / mo</span>
            </div>
            <div className={styles.cap}>Flat, whether you&apos;re 4 people or 40.</div>
          </div>
        </section>

        {/* FAQ */}
        <section className={styles.blk}>
          <h2>A few honest answers</h2>
          <div className={styles.qa}>
            <h3>Is it really one developer?</h3>
            <p>
              Yes. Jinear is built and maintained by one person, in the open. That&apos;s also why the
              pricing is this simple. There&apos;s no sales team to feed.
            </p>
          </div>
          <div className={styles.qa}>
            <h3>What happens if I self-host?</h3>
            <p>
              You get the full app for free under the AGPL-3.0 license. Bring it up with a single
              Docker Compose file and point it at your domain. Your data never touches my servers.
            </p>
          </div>
          <div className={styles.qa}>
            <h3>Does the flat price change with team size?</h3>
            <p>
              No. {PRO_PRICE}/mo covers unlimited people. A team of 4 and a team of 40 pay exactly the
              same.
            </p>
          </div>
        </section>

        <div className={styles.signoff}>
          Still deciding? Try the free plan, no card needed.
          <div className={styles.by}>
            <img className={styles.av} src={AVATAR} alt="Çağdaş, maker of Jinear" />
            <span>
              Or email me with a question at{" "}
              <a className={styles.linkU} href="mailto:cagdas@jinear.co">
                cagdas@jinear.co
              </a>
            </span>
          </div>
        </div>

        <BareFooter />
      </div>
    </div>
  );
};

export default PricingClient;
