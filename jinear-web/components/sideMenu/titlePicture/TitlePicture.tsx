import React from "react";
import styles from "./TitlePicture.module.css";

interface TitlePictureProps {
  initials: string;
}

const TitlePicture: React.FC<TitlePictureProps> = ({ initials }) => {
  return (
    <div className={styles.container}>
      <div className={styles.initials}>{initials}</div>
    </div>
  );
};

export default TitlePicture;
