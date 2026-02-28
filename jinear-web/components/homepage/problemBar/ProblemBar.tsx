import React from "react";
import styles from "./ProblemBar.module.scss";
import useTranslation from "locales/useTranslation";
import { LuTrendingUp, LuEye, LuPlugZap } from "react-icons/lu";

const ProblemBar: React.FC = () => {
  const { t } = useTranslation();

  const cards = [
    {
      id: "price",
      Icon: LuTrendingUp,
      title: t("landingPageProblem1Title"),
      text: t("landingPageProblem1Text"),
    },
    {
      id: "data",
      Icon: LuEye,
      title: t("landingPageProblem2Title"),
      text: t("landingPageProblem2Text"),
    },
    {
      id: "lockin",
      Icon: LuPlugZap,
      title: t("landingPageProblem3Title"),
      text: t("landingPageProblem3Text"),
    },
  ];

  return (
    <div className={styles.container}>
      <p className={styles.eyebrow}>{t("landingPageProblemEyebrow")}</p>
      <h2 className={styles.title}>{t("landingPageProblemTitle")}</h2>
      <div className={styles.cards}>
        {cards.map(({ id, Icon, title, text }) => (
          <div key={id} className={styles.card}>
            <Icon className={styles.icon} />
            <h3 className={styles.cardTitle}>{title}</h3>
            <p className={styles.cardText}>{text}</p>
          </div>
        ))}
      </div>
    </div>
  );
};

export default ProblemBar;
