import MenuGroupTitle from "@/components/sideMenu/menuGroupTitle/MenuGroupTitle";
import useTranslation from "locales/useTranslation";
import React from "react";
import styles from "./TeamThreads.module.css";

interface TeamThreadsProps {}

const TeamThreads: React.FC<TeamThreadsProps> = ({}) => {
  const { t } = useTranslation();

  return (
    <div className={styles.container}>
      <MenuGroupTitle label={t("sideMenuTeamThreads")} hasAddButton={true} />
    </div>
  );
};

export default TeamThreads;
