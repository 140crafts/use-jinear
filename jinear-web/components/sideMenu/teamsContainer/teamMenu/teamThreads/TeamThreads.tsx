import MenuGroupTitle from "@/components/sideMenu/menuGroupTitle/MenuGroupTitle";
import React from "react";
import styles from "./TeamThreads.module.css";

interface TeamThreadsProps {}

const TeamThreads: React.FC<TeamThreadsProps> = ({}) => {
  return (
    <div className={styles.container}>
      <MenuGroupTitle label="Threads" hasAddButton={true} />
    </div>
  );
};

export default TeamThreads;
