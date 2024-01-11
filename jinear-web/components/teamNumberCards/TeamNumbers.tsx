import { TeamDto, WorkspaceDto } from "@/model/be/jinear-core";
import { useRetrieveTaskNumbersQuery } from "@/store/api/taskAnalyticsApi";
import cn from "classnames";
import useTranslation from "locales/useTranslation";
import React from "react";
import CircularLoading from "../circularLoading/CircularLoading";
import styles from "./TeamNumbers.module.css";
import OverviewTaskNumbers from "./overviewTaskNumbers/OverviewTaskNumbers";
import SingleNumberCard from "./singleNumberCard/SingleNumberCard";
import WorkflowStateGroupStats from "./workflowStateGroupStats/WorkflowStateGroupStats";

interface TeamNumbersProps {
  workspace: WorkspaceDto;
  team: TeamDto;
  className?: string;
}

const TeamNumbers: React.FC<TeamNumbersProps> = ({ workspace, team, className }) => {
  const { t } = useTranslation();
  const { data: retrieveTaskNumbersResponse, isFetching } = useRetrieveTaskNumbersQuery({
    workspaceId: workspace.workspaceId,
    teamId: team.teamId,
  });

  return (
    <div className={cn(styles.container, className)}>
      {isFetching && <CircularLoading />}
      {retrieveTaskNumbersResponse && (
        <div className={styles.numbersContainer}>
          <OverviewTaskNumbers className={styles.overviewCard} {...retrieveTaskNumbersResponse.data} />
          <div className={styles.deadlineCounts}>
            <SingleNumberCard
              className={styles.numberCard}
              title={t("taskNumbersDeadlineComingUpCount")}
              number={retrieveTaskNumbersResponse.data.deadlineComingUpCount}
            />
            <SingleNumberCard
              className={styles.numberCard}
              title={t("taskNumbersMissedDeadlineCount")}
              number={retrieveTaskNumbersResponse.data.missedDeadlineCount}
            />
          </div>
          <WorkflowStateGroupStats
            totalTaskCount={retrieveTaskNumbersResponse.data.totalTaskCount}
            statusCounts={retrieveTaskNumbersResponse.data.statusCounts}
          />
        </div>
      )}
    </div>
  );
};

export default TeamNumbers;
