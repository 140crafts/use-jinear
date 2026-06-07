"use client";
import React from "react";
import styles from "./ProjectFeedLayoutHeader.module.scss";
import FormLogo from "@/components/formLogo/FormLogo";
import ThemeToggle from "@/components/themeToggle/ThemeToggle";
import SideMenuFooter from "@/components/sideMenu/sideMenuFooter/SideMenuFooter";
import { useRetrievePublicProjectInfoWithDomainQuery } from "@/api/projectFeedApi";
import { LuBox } from "react-icons/lu";

interface ProjectFeedLayoutHeaderProps {

}

const ProjectFeedLayoutHeader: React.FC<ProjectFeedLayoutHeaderProps> = ({}) => {

  return (
    <div className={styles.container}>
      <div className={styles.headerLeftContent}>
        {/*<FormLogo contentClassName={styles.logo} withLeftLine={false} />*/}
      </div>
      <div className={styles.headerLeftContent}>
        <SideMenuFooter />
        <ThemeToggle />
      </div>
    </div>
  );
};

export default ProjectFeedLayoutHeader;