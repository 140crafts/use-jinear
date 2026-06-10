import React from "react";
import styles from "./OpenSourceSection.module.scss";
import useTranslation from "locales/useTranslation";
import { FaGithub, FaGitlab, FaStar } from "react-icons/fa6";

const OpenSourceSection: React.FC = () => {
  const { t } = useTranslation();

  const repos = [
    {
      id: "github",
      Icon: FaGithub,
      platform: "GitHub",
      href: "https://github.com/140crafts/use-jinear",
    },
    {
      id: "gitlab",
      Icon: FaGitlab,
      platform: "GitLab",
      href: "https://gitlab.com/140crafts/use-jinear",
    },
  ];

  return (
    <div className={styles.container}>
      <p className={styles.eyebrow}>{t("landingPageOpenSourceEyebrow")}</p>
      <h2 className={styles.title}>{t("landingPageOpenSourceTitle")}</h2>
      <p className={styles.text}>{t("landingPageOpenSourceText")}</p>

      <div className={styles.repoGrid}>
        {repos.map(({ id, Icon, platform, href }) => (
          <a
            key={id}
            href={href}
            target="_blank"
            rel="noopener noreferrer"
            className={styles.repoCard}
          >
            <div className={styles.repoHeader}>
              <Icon className={styles.platformIcon} />
              <span className={styles.platformName}>{platform}</span>
            </div>
            <p className={styles.repoPath}>
              <span className={styles.repoOrg}>140crafts</span>
              <span className={styles.repoSep}>/</span>
              <span className={styles.repoName}>use-jinear</span>
            </p>
            <div className={styles.repoFooter}>
              <span className={styles.license}>AGPL-3.0</span>
              <span className={styles.starCta}>
                <FaStar className={styles.starIcon} />
              </span>
            </div>
          </a>
        ))}
      </div>
    </div>
  );
};

export default OpenSourceSection;
