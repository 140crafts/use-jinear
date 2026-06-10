"use client";
import React from "react";
import styles from "./Footer.module.css";
import { LuGlobe, LuMail } from "react-icons/lu";
import { SiApple, SiGoogleplay, SiPwa, SiReddit, SiTelegram, SiX } from "react-icons/si";
import cn from "classnames";
import { APP_URL } from "@/utils/constants";
import Button, { ButtonHeight, ButtonVariants } from "@/components/button";
import { IoLogoPwa } from "react-icons/io5";
import useTranslation from "@/locals/useTranslation";
import { FaGithub, FaGitlab } from "react-icons/fa6";

interface FooterProps {

}

const Footer: React.FC<FooterProps> = ({}) => {
  const { t } = useTranslation();

  return (
    <div className={styles.container}>
      <div className={styles.column}>
        <b>Jinear</b>
        <div className={"spacer-h-1"} />
        <a
          href={`/pricing`}
          className={styles.link}
        >
          {t("landingPageFooterPricing")}
        </a>
        <a
          href={`/terms`}
          className={styles.link}
        >
          {t("landingPageFooterTerms")}
        </a>
      </div>

      <div className={styles.column}>
        <b>{t("landingPageFooterSelfHosting")}</b>
        <div className={"spacer-h-1"} />
        <a
          href={"https://github.com/140crafts/use-jinear/blob/main/jinear-installation-scripts/README.md"}
          target={"_blank"}
          rel={"noreferrer"}
          className={styles.link}
        >
          {t("landingPageFooterInstallGuide")}
        </a>
        <a
          href={"https://github.com/140crafts/use-jinear/blob/main/jinear-installation-scripts/README.md"}
          target={"_blank"}
          rel={"noreferrer"}
          className={styles.link}
        >
          {t("landingPageFooterDocs")}
        </a>
        <a
          href={"https://github.com/140crafts/use-jinear"}
          target={"_blank"}
          rel={"noreferrer"}
          className={styles.link}
        >
          <FaGithub className={"icon"} /> GitHub
        </a>
        <a
          href={"https://gitlab.com/140crafts/use-jinear"}
          target={"_blank"}
          rel={"noreferrer"}
          className={styles.link}
        >
          <FaGitlab className={"icon"} /> GitLab
        </a>
      </div>

      <div className={styles.column}>
        <b>{t("landingPageFooterAccess")}</b>
        <div className={"spacer-h-1"} />
        <a
          href={`${APP_URL}/login`}
          target={"_blank"}
          rel={"noreferrer"}
          className={styles.link}
        >
          <LuGlobe className={"icon"} /> {t("landingPageFooterAccessBrowser")}
        </a>
        {/*<a*/}
        {/*  href={APP_STORE_URL}*/}
        {/*  target={"_blank"}*/}
        {/*  rel={"noreferrer"}*/}
        {/*  className={styles.link}*/}
        {/*>*/}
        {/*  <SiApple className={"icon"} />Appstore*/}
        {/*</a>*/}
        {/*<a*/}
        {/*  href={PLAY_STORE_URL}*/}
        {/*  target={"_blank"}*/}
        {/*  rel={"noreferrer"}*/}
        {/*  className={styles.link}*/}
        {/*>*/}
        {/*  <SiGoogleplay className={"icon"} />Playstore*/}
        {/*</a>*/}
        <a
          href={APP_URL}
          target={"_blank"}
          rel={"noreferrer"}
          className={styles.link}
        >
          <SiPwa className={"icon"} />
          PWA
        </a>
      </div>
      <div className={styles.column}>
        <b>Contact</b>
        <div className={"spacer-h-1"} />
        <a
          href={`/blog`}
          className={styles.link}
        >
          <span className={cn("jinear-logo", styles.jinearLogo)}>J</span> Blog
        </a>
        <a
          href={`https://twitter.com/usejinear`}
          target={"_blank"}
          rel={"noreferrer"}
          className={styles.link}
        >
          <SiX className={"icon"} /> x/usejinear
        </a>
        <a
          href={`https://www.reddit.com/r/jinear`}
          target={"_blank"}
          rel={"noreferrer"}
          className={styles.link}
        >
          <SiReddit className={"icon"} /> r/jinear
        </a>
        <a
          href={`https://t.me/usejinear`}
          target={"_blank"}
          rel={"noreferrer"}
          className={styles.link}
        >
          <SiTelegram className={"icon"} /> Telegram
        </a>
        <a
          href={`mailto:info@jinear.co`}
          target={"_blank"}
          rel={"noreferrer"}
          className={styles.link}
        >
          <LuMail className={"icon"} /> Email
        </a>
      </div>
    </div>
  );
};

export default Footer;