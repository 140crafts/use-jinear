import useTranslation from "locales/useTranslation";
import React from "react";
import styles from "./TaskDescription.module.css";

interface TaskDescriptionProps {
  description?: string;
}

const TaskDescription: React.FC<TaskDescriptionProps> = ({ description }) => {
  const { t } = useTranslation();
  return (
    <div className={styles.container}>
      {/* <SectionLabel label={t("taskDetailPageDescription")} /> */}
      <div className={styles.description}>{description}</div>
    </div>
  );
};

export default TaskDescription;
