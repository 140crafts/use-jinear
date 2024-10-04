import React from "react";
import styles from "./ProjectFeedLayoutHeader.module.scss";
import FormLogo from "@/components/formLogo/FormLogo";

interface ProjectFeedLayoutHeaderProps {

}

const ProjectFeedLayoutHeader: React.FC<ProjectFeedLayoutHeaderProps> = ({}) => {

  return (
    <div className={styles.container}>
      <div className={styles.headerLeftContent}>
        <FormLogo contentClassName={styles.logo} withLeftLine={false} />
      </div>
      <div className={styles.headerLeftContent}>

      </div>
    </div>
  );
};

export default ProjectFeedLayoutHeader;