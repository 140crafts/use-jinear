import TeamWorkflowStatusBoard from "@/components/teamWorkflowStatusBoard/TeamWorkflowStatusBoard";
import React from "react";
import styles from "./WorkflowStatusTab.module.scss";

interface WorkflowStatusTabProps {
  workspaceId?: string;
  teamId?: string;
  startDate: Date;
  endDate: Date;
}

const WorkflowStatusTab: React.FC<WorkflowStatusTabProps> = ({
  workspaceId,
  teamId,
  startDate,
  endDate,
}) => {
  return (
    <div className={styles.container}>
      <div className={styles.actionBarContainer}></div>
      <span className={styles.statusBoardContainer}>
        {workspaceId && teamId && (
          <TeamWorkflowStatusBoard
            teamId={teamId}
            workspaceId={workspaceId}
            startDate={startDate}
            endDate={endDate}
          />
        )}
      </span>
    </div>
  );
};

export default WorkflowStatusTab;
