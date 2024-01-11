import { TeamWorkflowStateGroup } from "@/model/be/jinear-core";
import { retrieveTaskStatusIcon } from "@/utils/taskIconFactory";
import useTranslation from "locales/useTranslation";
import React from "react";
import styles from "./WorkflowStateGroupStats.module.scss";

interface WorkflowStateGroupStatsProps {
  totalTaskCount: number;
  statusCounts: { [P in TeamWorkflowStateGroup]?: number };
}
const STATE_GROUPS: TeamWorkflowStateGroup[] = ["BACKLOG", "NOT_STARTED", "STARTED", "COMPLETED", "CANCELLED"];

const WorkflowStateGroupStats: React.FC<WorkflowStateGroupStatsProps> = ({ totalTaskCount = 0, statusCounts }) => {
  const { t } = useTranslation();
  return (
    <div className={styles.container}>
      <span>{t("workflowStateGroupStatsTitle")}</span>
      <div className={styles.graphContainer}>
        {STATE_GROUPS.map((stateGroup) => {
          const number = statusCounts[stateGroup] || 0;
          const Icon = retrieveTaskStatusIcon(stateGroup);
          return (
            <div key={stateGroup} className={styles.column} data-tooltip-right={t(`workflowStateGroupStats_${stateGroup}`)}>
              <div className={styles.barContainer}>
                <div style={{ flex: totalTaskCount - number }}></div>
                <div className={styles.number}>{number}</div>
                <div className={styles.bar} style={{ flex: number }}></div>
              </div>
              <div className="spacer-h-1" />
              {/* <div className={styles.label}>{t(`workflowStateGroupStats_${stateGroup}`)}</div> */}
              <div className={styles.label}>{<Icon size={18} />}</div>
            </div>
          );
        })}
      </div>
    </div>
  );
};

export default WorkflowStateGroupStats;
