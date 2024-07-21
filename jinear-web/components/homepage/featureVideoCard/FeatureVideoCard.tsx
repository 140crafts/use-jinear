import React from "react";
import styles from "./FeatureVideoCard.module.scss";
import { useTheme } from "@/components/themeProvider/ThemeProvider";

interface FeatureVideoCardProps {
  title: string;
  text: string;
  vimeoIdLight: string;
  vimeoIdDark: string;
}

const FeatureVideoCard: React.FC<FeatureVideoCardProps> = ({ title, text, vimeoIdLight, vimeoIdDark }) => {
  const theme = useTheme();

  return (
    <div className={styles.container}>
      <h2 className={styles.heroTitle}>{title}</h2>
      <div className={styles.heroText}>{text}</div>
      <div className="spacer-h-2" />
      <div className={styles.heroVideoContainer}>
        <iframe
          src={`https://player.vimeo.com/video/${theme == "light" ? vimeoIdLight : vimeoIdDark}?badge=0&autoplay=1&loop=1&background=1&amp;autopause=0&muted=1&amp;player_id=0&amp;app_id=58479`}
          frameBorder="0" allow="autoplay; fullscreen; picture-in-picture; clipboard-write"
          className={styles.vimeoIframe} title="task-create-dark"></iframe>
      </div>
    </div>
  );
};

export default FeatureVideoCard;
