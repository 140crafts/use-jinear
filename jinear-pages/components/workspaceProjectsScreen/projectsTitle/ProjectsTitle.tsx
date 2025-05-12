import React from "react";
import styles from "./ProjectsTitle.module.css";
import Button, { ButtonHeight, ButtonVariants } from "@/components/button";
import useTranslation from "@/locals/useTranslation";
import { useAppDispatch } from "@/store/store";
import { WorkspaceDto } from "@/be/jinear-core";
import Logger from "@/utils/logger";
import { popNewProjectModal } from "@/slice/modalSlice";

interface ProjectsTitleProps {
  workspace: WorkspaceDto;
}

const logger = Logger("ProjectsTitle");

const ProjectsTitle: React.FC<ProjectsTitleProps> = ({ workspace }) => {
  const { t } = useTranslation();
  const dispatch = useAppDispatch();

  const openNewProjectModal = () => {
    dispatch(popNewProjectModal({ visible: true, workspace }));
  };

  return (
    <div className={styles.container}>
      <h2>{t("projectsAllTitle")}</h2>
      <div className={"flex-1"} />
      <Button variant={ButtonVariants.contrast} heightVariant={ButtonHeight.short} onClick={openNewProjectModal}>
        {t("projectsNewProjectButtonLabel")}
      </Button>
    </div>
  );
};

export default ProjectsTitle;