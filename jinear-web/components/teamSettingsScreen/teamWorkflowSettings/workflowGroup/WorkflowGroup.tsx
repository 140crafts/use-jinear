import {
  TeamWorkflowStateGroup,
  TeamWorkflowStatusDto,
} from "@/model/be/jinear-core";
import useTranslation from "locales/useTranslation";
import React from "react";
import styles from "./WorkflowGroup.module.css";
import WorkflowStatus from "./workflowStatus/WorkflowStatus";

interface WorkflowGroupProps {
  groupType: TeamWorkflowStateGroup;
  statuses: TeamWorkflowStatusDto[] | undefined;
}

const WorkflowGroup: React.FC<WorkflowGroupProps> = ({
  groupType,
  statuses,
}) => {
  const { t } = useTranslation();
  return (
    <div className={styles.container}>
      <div className={styles.groupTitle}>
        {t(`workflowGroupTitle_${groupType}`)}
      </div>
      {statuses?.map((workflowDto) => (
        <WorkflowStatus
          key={workflowDto.teamWorkflowStatusId}
          workflowDto={workflowDto}
          deletable={statuses?.length == 0}
          orderChangable={statuses?.length == 0}
        />
      ))}
    </div>
  );
};

export default WorkflowGroup;
