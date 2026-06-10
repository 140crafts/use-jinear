import React from "react";
import styles from "./ProjectDomainList.module.css";
import { ProjectDto } from "@/be/jinear-core";
import useTranslation from "@/locals/useTranslation";
import FeedUrlInfo
  from "@/components/projectDetailScreen/projectFeedSettingsTab/projectDomainList/feedUrlInfo/FeedUrlInfo";
import Button, { ButtonHeight, ButtonVariants } from "@/components/button";
import { useAppDispatch } from "@/store/store";
import { popNewCustomProjectDomainModal } from "@/slice/modalSlice";

interface ProjectDomainListProps {
  project: ProjectDto;
  hasExplicitAdminAccess?: boolean;
}

const ProjectDomainList: React.FC<ProjectDomainListProps> = ({ project, hasExplicitAdminAccess }) => {
  const { t } = useTranslation();
  const dispatch = useAppDispatch();

  const openNewCustomDomainModal = () => {
    dispatch(popNewCustomProjectDomainModal({ projectId: project.projectId, visible: true }));
  };

  return (
    <div className={styles.container}>
      <div className={styles.titleContainer}>
        <h3>{t("projectFeedUrlTitle")}</h3>
        <div className={styles.actionAndInfoContainer}>
          <span>{t("projectFeedUrlText")}</span>
          <Button
            variant={ButtonVariants.filled}
            heightVariant={ButtonHeight.short}
            onClick={openNewCustomDomainModal}
          >
            {t("projectAddNewCustomDomainButton")}
          </Button>
        </div>
      </div>

      <div className={styles.domainListContainer}>
        {project.domains.map(projectDomain =>
          <FeedUrlInfo
            key={projectDomain.projectDomainId}
            projectDomainDto={projectDomain}
            hasExplicitAdminAccess={hasExplicitAdminAccess}
          />)
        }
      </div>

    </div>
  );
};

export default ProjectDomainList;