import cn from "classnames";
import useTranslation from "locales/useTranslation";
import React from "react";
import PrefilteredPaginatedTaskList from "../taskLists/prefilteredPaginatedTaskList/PrefilteredPaginatedTaskList";
import styles from "./TeamLongLastingTasks.module.css";

interface TeamLongLastingTasksProps {
  workspaceId: string;
  teamId: string;
  className?: string;
}

const TeamLongLastingTasks: React.FC<TeamLongLastingTasksProps> = ({ workspaceId, teamId, className }) => {
  const { t } = useTranslation();
  return (
    <div className={cn(styles.container, className)}>
      <span>{t("teamLongLastingTasksTitle")}</span>
      <PrefilteredPaginatedTaskList
        id={`TeamLongLastingTasks`}
        filter={{
          workspaceId: workspaceId,
          teamIdList: [teamId],
          workflowStateGroups: ["BACKLOG", "NOT_STARTED", "STARTED"],
          sort: "IDATE_ASC",
          size: 12,
        }}
        containerClassName={styles.taskList}
        contentContainerClassName={styles.taskListContent}
      />
    </div>
  );
};

export default TeamLongLastingTasks;
