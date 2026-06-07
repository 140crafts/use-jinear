import React from "react";
import styles from "./ProjectArchivedInfo.module.css";
import { ProjectDto } from "@/be/jinear-core";
import useTranslation from "@/locals/useTranslation";
import Button, { ButtonHeight, ButtonVariants } from "@/components/button";
import { useUpdateProjectArchivedMutation } from "@/api/projectOperationApi";
import { LuArchiveRestore } from "react-icons/lu";

interface ProjectArchivedInfoProps {
  project: ProjectDto;
}

const ProjectArchivedInfo: React.FC<ProjectArchivedInfoProps> = ({ project }) => {
  const { t } = useTranslation();
  const [updateProjectArchived, { isLoading }] = useUpdateProjectArchivedMutation();

  const updateAsUnarchived = () => {
    updateProjectArchived({ projectId: project.projectId, body: { archived: false } });
  };

  return !project.archived ? null : (
    <div className={styles.container}>
      <h2>{t("projectArchivedIntoTitle")}</h2>
      <span>{t("projectArchivedIntoText")}</span>
      <Button
        loading={isLoading}
        disabled={isLoading}
        className={styles.unarchiveButton}
        variant={ButtonVariants.filled}
        heightVariant={ButtonHeight.short}
        onClick={updateAsUnarchived}
      >
        <LuArchiveRestore className={"icon"} />
        <div className={"spacer-w-1"} />
        <b>{t("projectArchivedInfoUnarchive")}</b>
      </Button>
    </div>
  );
};

export default ProjectArchivedInfo;