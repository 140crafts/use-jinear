import React from "react";
import styles from "./ProjectDetailMilestones.module.css";
import { ProjectDto } from "@/be/jinear-core";
import useTranslation from "@/locals/useTranslation";
import MilestoneDetail
  from "@/components/projectDetailScreen/projectDetailMilestones/milestoneDetail/MilestoneDetail";
import Button, { ButtonHeight, ButtonVariants } from "@/components/button";
import { popNewMilestoneModal, popProjectAndMilestonePickerModal } from "@/slice/modalSlice";
import { useAppDispatch } from "@/store/store";

interface ProjectDetailMilestonesProps {
  project: ProjectDto;
}

const ProjectDetailMilestones: React.FC<ProjectDetailMilestonesProps> = ({ project }) => {
  const { t } = useTranslation();
  const dispatch = useAppDispatch();

  const openNewMilestoneModal = () => {
    dispatch(popNewMilestoneModal({ project, visible: true }));
  };

  return (
    <div id={`project-detail-milestones-container`} className={styles.container}>
      <div className={styles.milestonesHeaderContainer}>
        <h3>{t("projectDetailMilestones")}</h3>
        <Button
          variant={ButtonVariants.filled}
          heightVariant={ButtonHeight.short}
          onClick={openNewMilestoneModal}>
          {t("projectDetailMilestonesNewMilestone")}
        </Button>
      </div>

      <div className={"spacer-h-3"} />

      {project.milestones.map((milestone, index) =>
        <MilestoneDetail
          project={project}
          isFirst={index == 0}
          isLast={index == project.milestones.length - 1}
          isOneBeforeLast={index == project.milestones.length - 2}
          key={milestone.milestoneId}
          milestone={milestone}
          workspaceId={project.workspaceId} />)
      }
    </div>
  );
};

export default ProjectDetailMilestones;