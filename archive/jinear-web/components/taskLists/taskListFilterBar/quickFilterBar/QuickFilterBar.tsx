import Button, { ButtonHeight, ButtonVariants } from "@/components/button";
import {
  queryStateAnyToStringConverter, queryStateArrayParser,
  queryStateDateToIsoDateConverter, queryStateIsoDateParser, useQueryState,
  useSetQueryState,
  useSetQueryStateMultiple
} from "@/hooks/useQueryState";
import { TeamWorkflowStateGroup, TeamWorkflowStatusDto } from "@/model/be/jinear-core";
import { useRetrieveAllFromTeamQuery } from "@/store/api/teamWorkflowStatusApi";
import { endOfMonth, endOfWeek, isSameDay, startOfMonth, startOfToday, startOfWeek } from "date-fns";
import useTranslation from "locales/useTranslation";
import React from "react";
import { useTeam } from "../context/TaskListFilterBarContext";
import styles from "./QuickFilterBar.module.css";
import Logger from "@/utils/logger";
import cn from "classnames";

interface QuickFilterBarProps {
}

const logger = Logger("QuickFilterBar");

const QuickFilterBar: React.FC<QuickFilterBarProps> = ({}) => {
  const { t } = useTranslation();
  const team = useTeam();
  const setQueryState = useSetQueryState();
  const setQueryStateMultiple = useSetQueryStateMultiple();
  const { data: teamWorkflowStatusListResponse, isFetching } = useRetrieveAllFromTeamQuery(
    { teamId: team?.teamId || "" },
    { skip: team == null }
  );

  const notStartedStatuses = (teamWorkflowStatusListResponse?.data.groupedTeamWorkflowStatuses.NOT_STARTED || []).map((tws) => tws.teamWorkflowStatusId);
  const startedStatuses = (teamWorkflowStatusListResponse?.data.groupedTeamWorkflowStatuses.STARTED || []).map((tws) => tws.teamWorkflowStatusId);
  const completedStatuses = (teamWorkflowStatusListResponse?.data.groupedTeamWorkflowStatuses.COMPLETED || []).map((tws) => tws.teamWorkflowStatusId);
  const cancelledStatuses = (teamWorkflowStatusListResponse?.data.groupedTeamWorkflowStatuses.CANCELLED || []).map((tws) => tws.teamWorkflowStatusId);
  const backlogStatuses = (teamWorkflowStatusListResponse?.data.groupedTeamWorkflowStatuses.BACKLOG || []).map((tws) => tws.teamWorkflowStatusId);
  const activeStatuses = [...notStartedStatuses, ...startedStatuses];
  const undoneStatuses = [...backlogStatuses, ...notStartedStatuses, ...startedStatuses];
  const archivedStatuses = [...completedStatuses, ...cancelledStatuses];

  const workflowStatusIdList = useQueryState<string[]>("workflowStatusIdList", queryStateArrayParser);
  const workflowStateGroups = useQueryState<TeamWorkflowStateGroup[]>("workflowStateGroups", queryStateArrayParser);
  const timespanStart = useQueryState<Date>("timespanStart", queryStateIsoDateParser);
  const timespanEnd = useQueryState<Date>("timespanEnd", queryStateIsoDateParser);

  const today = startOfToday();
  const isThisMonthSelected = timespanStart != null && timespanEnd != null && isSameDay(timespanStart, startOfMonth(today)) && isSameDay(timespanEnd, endOfMonth(today));
  const isThisWeekSelected = timespanStart != null && timespanEnd != null && isSameDay(timespanStart, startOfWeek(today, { weekStartsOn: 1 })) && isSameDay(timespanEnd, endOfWeek(today, { weekStartsOn: 1 }));
  const isActiveSelected = activeStatuses && workflowStatusIdList && JSON.stringify(activeStatuses.sort()) == JSON.stringify(workflowStatusIdList.sort());
  const isUndoneSelected = undoneStatuses && workflowStatusIdList && JSON.stringify(undoneStatuses.sort()) == JSON.stringify(workflowStatusIdList.sort());
  const isBacklogSelected = backlogStatuses && workflowStatusIdList && JSON.stringify(backlogStatuses.sort()) == JSON.stringify(workflowStatusIdList.sort());
  const isArchivedSelected = archivedStatuses && workflowStatusIdList && JSON.stringify(archivedStatuses.sort()) == JSON.stringify(workflowStatusIdList.sort());

  logger.log({
    isThisMonthSelected,
    isThisWeekSelected,
    workflowStatusIdList,
    workflowStateGroups,
    activeStatusesSorted: activeStatuses && JSON.stringify(activeStatuses.sort()),
    workflowStatusIdListSorted: workflowStatusIdList && JSON.stringify(workflowStatusIdList.sort()),
    timespanStart,
    timespanEnd
  });

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
        ["hasPreciseToDate", undefined]
      ])
    );
  };

  const resetDateStates = () => {
    setQueryStateMultiple(
      new Map([
        ["timespanStart", undefined],
        ["hasPreciseFromDate", undefined],
        ["timespanEnd", undefined],
        ["hasPreciseToDate", undefined]
      ])
    );
  };

  const resetWorkflowStatusIdList = () => {
    setQueryState("workflowStatusIdList", undefined);
  };

  const setSelectedWorkflowStatuses = (teamWorkflowStatusIds: string[]) => {
    setQueryState("workflowStatusIdList", queryStateAnyToStringConverter(teamWorkflowStatusIds));
  };

  const setFilterThisMonth = () => {
    if (isThisMonthSelected) {
      resetDateStates();
      return;
    }
    const from = startOfMonth(startOfToday());
    const to = endOfMonth(startOfToday());
    setQueryStateMultiple(
      new Map([
        ["timespanStart", queryStateDateToIsoDateConverter(from)],
        ["timespanEnd", queryStateDateToIsoDateConverter(to)]
      ])
    );
  };

  const setFilterThisWeek = () => {
    if (isThisWeekSelected) {
      resetDateStates();
      return;
    }
    const from = startOfWeek(startOfToday(), { weekStartsOn: 1 });
    const to = endOfWeek(startOfToday(), { weekStartsOn: 1 });
    setQueryStateMultiple(
      new Map([
        ["timespanStart", queryStateDateToIsoDateConverter(from)],
        ["timespanEnd", queryStateDateToIsoDateConverter(to)]
      ])
    );
  };

  const setActiveStatusesAsFiltered = () => {
    isActiveSelected ? resetWorkflowStatusIdList() : setSelectedWorkflowStatuses?.(activeStatuses);
  };

  const setUndoneStatusesAsFiltered = () => {
    isUndoneSelected ? resetWorkflowStatusIdList() : setSelectedWorkflowStatuses?.(undoneStatuses);
  };

  const setBacklogStatusesAsFiltered = () => {
    isBacklogSelected ? resetWorkflowStatusIdList() : setSelectedWorkflowStatuses?.(backlogStatuses);
  };

  const setArchivedStatusesAsFiltered = () => {
    isArchivedSelected ? resetWorkflowStatusIdList() : setSelectedWorkflowStatuses?.(archivedStatuses);
  };

  return (
    <div className={styles.container}>
      <Button
        className={cn(styles.button, isThisWeekSelected && styles.selected)}
        heightVariant={ButtonHeight.short}
        variant={ButtonVariants.default}
        onClick={setFilterThisWeek}
      >
        {t("quickFilterBarThisWeek")}
      </Button>
      <Button
        className={cn(styles.button, isThisMonthSelected && styles.selected)}
        heightVariant={ButtonHeight.short}
        variant={ButtonVariants.default}
        onClick={setFilterThisMonth}
      >
        {t("quickFilterBarThisMonth")}
      </Button>
      <Button
        className={cn(styles.button, isActiveSelected && styles.selected)}
        heightVariant={ButtonHeight.short}
        variant={ButtonVariants.default}
        onClick={setActiveStatusesAsFiltered}
      >
        {t("quickFilterBarActive")}
      </Button>
      <Button
        className={cn(styles.button, isUndoneSelected && styles.selected)}
        heightVariant={ButtonHeight.short}
        variant={ButtonVariants.default}
        onClick={setUndoneStatusesAsFiltered}
      >
        {t("quickFilterBarUndone")}
      </Button>
      <Button
        className={cn(styles.button, isBacklogSelected && styles.selected)}
        heightVariant={ButtonHeight.short}
        variant={ButtonVariants.default}
        onClick={setBacklogStatusesAsFiltered}
      >
        {t("quickFilterBarBacklog")}
      </Button>
      <Button
        className={cn(styles.button, isArchivedSelected && styles.selected)}
        heightVariant={ButtonHeight.short}
        variant={ButtonVariants.default}
        onClick={setArchivedStatusesAsFiltered}
      >
        {t("quickFilterBarArchived")}
      </Button>
      <Button className={styles.button} heightVariant={ButtonHeight.short} variant={ButtonVariants.default}
              onClick={resetState}>
        {t("quickFilterBarClearAll")}
      </Button>
    </div>
  );
};

export default QuickFilterBar;
