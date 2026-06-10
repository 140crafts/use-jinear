import Button, { ButtonHeight, ButtonVariants } from "@/components/button";
import {
  queryStateAnyToStringConverter,
  queryStateDateToIsoDateConverter,
  useSetQueryState,
  useSetQueryStateMultiple,
} from "@/hooks/useQueryState";
import { TeamWorkflowStatusDto } from "@/model/be/jinear-core";
import { useRetrieveAllFromTeamQuery } from "@/store/api/teamWorkflowStatusApi";
import { endOfMonth, endOfWeek, startOfMonth, startOfToday, startOfWeek } from "date-fns";
import useTranslation from "locales/useTranslation";
import React from "react";
import { useTeam } from "../context/TaskListFilterBarContext";
import styles from "./QuickFilterBar.module.css";

interface QuickFilterBarProps {}

const QuickFilterBar: React.FC<QuickFilterBarProps> = ({}) => {
  const { t } = useTranslation();
  const team = useTeam();
  const setQueryState = useSetQueryState();
  const setQueryStateMultiple = useSetQueryStateMultiple();

  const { data: teamWorkflowStatusListResponse, isFetching } = useRetrieveAllFromTeamQuery(
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

  const resetState = () => {
    setQueryStateMultiple(
      new Map([
        ["page", undefined],
        ["topicIds", undefined],
        ["ownerIds", undefined],
        ["assigneeIds", undefined],
        ["workflowStatusIdList", undefined],
        ["workflowStateGroups", undefined],
        ["timespanStart", undefined],
        ["hasPreciseFromDate", undefined],
        ["timespanEnd", undefined],
        ["hasPreciseToDate", undefined],
      ])
    );
  };

  const setSelectedWorkflowStatuses = (teamWorkflowStatusDtos: TeamWorkflowStatusDto[]) => {
    const ids = teamWorkflowStatusDtos.map((tws) => tws.teamWorkflowStatusId);
    setQueryState("workflowStatusIdList", queryStateAnyToStringConverter(ids));
  };

  const setFilterThisMonth = () => {
    const from = startOfMonth(startOfToday());
    const to = endOfMonth(startOfToday());
    setQueryStateMultiple(
      new Map([
        ["timespanStart", queryStateDateToIsoDateConverter(from)],
        ["timespanEnd", queryStateDateToIsoDateConverter(to)],
      ])
    );
  };

  const setFilterThisWeek = () => {
    const from = startOfWeek(startOfToday(), { weekStartsOn: 1 });
    const to = endOfWeek(startOfToday(), { weekStartsOn: 1 });
    setQueryStateMultiple(
      new Map([
        ["timespanStart", queryStateDateToIsoDateConverter(from)],
        ["timespanEnd", queryStateDateToIsoDateConverter(to)],
      ])
    );
  };

  const setActiveStatusesAsFiltered = () => {
    setSelectedWorkflowStatuses?.(activeStatuses);
  };

  const setUndoneStatusesAsFiltered = () => {
    setSelectedWorkflowStatuses?.(undoneStatuses);
  };

  const setBacklogStatusesAsFiltered = () => {
    setSelectedWorkflowStatuses?.(backlogStatuses);
  };

  const setArchivedStatusesAsFiltered = () => {
    setSelectedWorkflowStatuses?.(archivedStatuses);
  };

  return (
    <div className={styles.container}>
      <Button
        className={styles.button}
        heightVariant={ButtonHeight.short}
        variant={ButtonVariants.default}
        onClick={setFilterThisWeek}
      >
        {t("quickFilterBarThisWeek")}
      </Button>
      <Button
        className={styles.button}
        heightVariant={ButtonHeight.short}
        variant={ButtonVariants.default}
        onClick={setFilterThisMonth}
      >
        {t("quickFilterBarThisMonth")}
      </Button>
      <Button
        className={styles.button}
        heightVariant={ButtonHeight.short}
        variant={ButtonVariants.default}
        onClick={setActiveStatusesAsFiltered}
      >
        {t("quickFilterBarActive")}
      </Button>
      <Button
        className={styles.button}
        heightVariant={ButtonHeight.short}
        variant={ButtonVariants.default}
        onClick={setUndoneStatusesAsFiltered}
      >
        {t("quickFilterBarUndone")}
      </Button>
      <Button
        className={styles.button}
        heightVariant={ButtonHeight.short}
        variant={ButtonVariants.default}
        onClick={setBacklogStatusesAsFiltered}
      >
        {t("quickFilterBarBacklog")}
      </Button>
      <Button
        className={styles.button}
        heightVariant={ButtonHeight.short}
        variant={ButtonVariants.default}
        onClick={setArchivedStatusesAsFiltered}
      >
        {t("quickFilterBarArchived")}
      </Button>
      <Button className={styles.button} heightVariant={ButtonHeight.short} variant={ButtonVariants.default} onClick={resetState}>
        {t("quickFilterBarClearAll")}
      </Button>
    </div>
  );
};

export default QuickFilterBar;
