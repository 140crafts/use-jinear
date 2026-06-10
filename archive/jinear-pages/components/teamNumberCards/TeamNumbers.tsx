import { TeamDto, WorkspaceDto } from "@/model/be/jinear-core";
import { useRetrieveTaskNumbersQuery } from "@/store/api/taskAnalyticsApi";
import { useRetrieveAllFromTeamQuery } from "@/store/api/teamWorkflowStatusApi";
import cn from "classnames";
import { addDays } from "date-fns";
import useTranslation from "locales/useTranslation";
import { useRouter } from "next/navigation";
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

const SIGHT = 3;

const TeamNumbers: React.FC<TeamNumbersProps> = ({ workspace, team, className }) => {
  const { t } = useTranslation();
  const router = useRouter();
  const { data: retrieveTaskNumbersResponse, isFetching } = useRetrieveTaskNumbersQuery({
    workspaceId: workspace.workspaceId,
    teamId: team.teamId,
  });
  const { data: teamWorkflowStatusListResponse } = useRetrieveAllFromTeamQuery(
    { teamId: team?.teamId || "" },
    { skip: team == null }
  );

  const notStartedStatuses = teamWorkflowStatusListResponse?.data.groupedTeamWorkflowStatuses.NOT_STARTED || [];
  const startedStatuses = teamWorkflowStatusListResponse?.data.groupedTeamWorkflowStatuses.STARTED || [];
  const completedStatuses = teamWorkflowStatusListResponse?.data.groupedTeamWorkflowStatuses.COMPLETED || [];
  const cancelledStatuses = teamWorkflowStatusListResponse?.data.groupedTeamWorkflowStatuses.CANCELLED || [];
  const backlogStatuses = teamWorkflowStatusListResponse?.data.groupedTeamWorkflowStatuses.BACKLOG || [];
  const activeStatuses = [...notStartedStatuses, ...startedStatuses];
  const undoneStatuses = [...backlogStatuses, ...notStartedStatuses, ...startedStatuses];
  const archivedStatuses = [...completedStatuses, ...cancelledStatuses];

  const tasksPath = `/${workspace.username}/tasks/${team.username}/tasks`;
  const tasksFilteredWithUndoneStatus = `${tasksPath}?workflowStatusIdList=${undoneStatuses.map(
    (tws) => tws.teamWorkflowStatusId
  )}`;
  const tasksFilteredWithClosedStatus = `${tasksPath}?workflowStatusIdList=${archivedStatuses.map(
    (tws) => tws.teamWorkflowStatusId
  )}`;
  const tasksFilteredWithUndoneStatusAndTimespanEndBeforeToday = `${tasksPath}?workflowStatusIdList=${undoneStatuses.map(
    (tws) => tws.teamWorkflowStatusId
  )}&timespanEnd=${new Date().toISOString()}`;
  const tasksFilteredWithUndoneStatusAndTimespanEndNear = `${tasksPath}?workflowStatusIdList=${undoneStatuses.map(
    (tws) => tws.teamWorkflowStatusId
  )}&timespanEnd=${addDays(new Date(), SIGHT).toISOString()}`;

  const tasksWithBacklogStatus = `${tasksPath}?workflowStatusIdList=${backlogStatuses.map((tws) => tws.teamWorkflowStatusId)}`;
  const tasksWithNotStartedStatus = `${tasksPath}?workflowStatusIdList=${notStartedStatuses.map(
    (tws) => tws.teamWorkflowStatusId
  )}`;
  const tasksWithStartedStatus = `${tasksPath}?workflowStatusIdList=${startedStatuses.map((tws) => tws.teamWorkflowStatusId)}`;
  const tasksWithCompletedStatus = `${tasksPath}?workflowStatusIdList=${completedStatuses.map(
    (tws) => tws.teamWorkflowStatusId
  )}`;
  const tasksWithCancelledStatus = `${tasksPath}?workflowStatusIdList=${cancelledStatuses.map(
    (tws) => tws.teamWorkflowStatusId
  )}`;

  return (
    <div className={cn(styles.container, className)}>
      {isFetching && <CircularLoading />}
      {retrieveTaskNumbersResponse && (
        <div className={styles.numbersContainer}>
          <OverviewTaskNumbers
            className={styles.overviewCard}
            {...retrieveTaskNumbersResponse.data}
            tasksPath={tasksPath}
            tasksFilteredWithUndoneStatus={tasksFilteredWithUndoneStatus}
            tasksFilteredWithClosedStatus={tasksFilteredWithClosedStatus}
          />
          <div className={styles.deadlineCounts}>
            <SingleNumberCard
              className={styles.numberCard}
              title={t("taskNumbersDeadlineComingUpCount")}
              number={retrieveTaskNumbersResponse.data.deadlineComingUpCount}
              href={tasksFilteredWithUndoneStatusAndTimespanEndNear}
            />
            <SingleNumberCard
              className={styles.numberCard}
              title={t("taskNumbersMissedDeadlineCount")}
              number={retrieveTaskNumbersResponse.data.missedDeadlineCount}
              href={tasksFilteredWithUndoneStatusAndTimespanEndBeforeToday}
            />
          </div>
          <WorkflowStateGroupStats
            totalTaskCount={retrieveTaskNumbersResponse.data.totalTaskCount}
            statusCounts={retrieveTaskNumbersResponse.data.statusCounts}
            tasksWithBacklogStatus={tasksWithBacklogStatus}
            tasksWithNotStartedStatus={tasksWithNotStartedStatus}
            tasksWithStartedStatus={tasksWithStartedStatus}
            tasksWithCompletedStatus={tasksWithCompletedStatus}
            tasksWithCancelledStatus={tasksWithCancelledStatus}
          />
        </div>
      )}
    </div>
  );
};

export default TeamNumbers;
