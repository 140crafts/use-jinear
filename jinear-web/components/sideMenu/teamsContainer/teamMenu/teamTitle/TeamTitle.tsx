import TitlePicture from "@/components/sideMenu/titlePicture/TitlePicture";
import React from "react";
import styles from "./TeamTitle.module.css";

interface TeamTitleProps {
  name: string;
}

const TeamTitle: React.FC<TeamTitleProps> = ({ name }) => {
  return (
    <div className={styles.teamNameContainer}>
      <TitlePicture initials={name.substring(0, 2)} />
      <div className={styles.teamName}>{name}</div>
    </div>
  );
};

export default TeamTitle;
