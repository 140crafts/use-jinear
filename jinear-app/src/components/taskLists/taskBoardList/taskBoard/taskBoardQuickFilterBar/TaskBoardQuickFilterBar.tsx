import React from "react";
import styles from "./TaskBoardQuickFilterBar.module.css";
import type { TaskBoardEntryFilterRequest, TeamDto } from "@/be/jinear-core";
import {
  queryStateJsonObjectParser,
  queryStateObjectToJsonStringConverter,
  useQueryState,
  useSetQueryState
} from "@/hooks/useQueryState";
import { useRetrieveAllFromTeamQuery } from "@/api/teamWorkflowStatusApi";
import Button, { ButtonHeight, ButtonVariants } from "@/components/button";
import cn from "classnames";
import useTranslation from "@/locals/useTranslation";
import Logger from "@/util/logger";
import { endOfMonth, endOfWeek, isSameDay, startOfMonth, startOfToday, startOfWeek } from "date-fns";

interface TaskBoardQuickFilterBarProps {
  team: TeamDto;
  taskBoardId: string;
}

export interface ITaskBoardEntryFilterRequest extends TaskBoardEntryFilterRequest {
  page?: number;
}

export interface ITaskBoardUrlStateMap {
  [key: string]: ITaskBoardEntryFilterRequest | undefined;
}

const logger = Logger("TaskBoardQuickFilterBar");

const TaskBoardQuickFilterBar: React.FC<TaskBoardQuickFilterBarProps> = ({ team, taskBoardId }) => {
  const { t } = useTranslation();
  const setQueryState = useSetQueryState();

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

  const taskBoardFilterMap = useQueryState<ITaskBoardUrlStateMap>("board-filter-map", queryStateJsonObjectParser) ?? {};
  const thisBoardsFilter = taskBoardFilterMap[taskBoardId] ?? {};
  const today = startOfToday();
  const isThisMonthSelected = thisBoardsFilter?.timespanStart != null && thisBoardsFilter?.timespanEnd != null && isSameDay(new Date(thisBoardsFilter?.timespanStart), startOfMonth(today)) && isSameDay(new Date(thisBoardsFilter?.timespanEnd), endOfMonth(today));
  const isThisWeekSelected = thisBoardsFilter?.timespanStart != null && thisBoardsFilter?.timespanEnd != null && isSameDay(new Date(thisBoardsFilter?.timespanStart), startOfWeek(today, { weekStartsOn: 1 })) && isSameDay(new Date(thisBoardsFilter?.timespanEnd), endOfWeek(today, { weekStartsOn: 1 }));
  const isActiveSelected = activeStatuses && thisBoardsFilter?.workflowStatusIdList && JSON.stringify(activeStatuses.sort()) == JSON.stringify(thisBoardsFilter?.workflowStatusIdList.sort());
  const isUndoneSelected = undoneStatuses && thisBoardsFilter?.workflowStatusIdList && JSON.stringify(undoneStatuses.sort()) == JSON.stringify(thisBoardsFilter?.workflowStatusIdList.sort());
  const isBacklogSelected = backlogStatuses && thisBoardsFilter?.workflowStatusIdList && JSON.stringify(backlogStatuses.sort()) == JSON.stringify(thisBoardsFilter?.workflowStatusIdList.sort());
  const isArchivedSelected = archivedStatuses && thisBoardsFilter?.workflowStatusIdList && JSON.stringify(archivedStatuses.sort()) == JSON.stringify(thisBoardsFilter?.workflowStatusIdList.sort());

  logger.log({ taskBoardFilterMap });

  const resetState = () => {
    const finalTaskBoardFilterMap = taskBoardFilterMap ?? {};
    finalTaskBoardFilterMap[taskBoardId] = undefined;
    setQueryState("board-filter-map", queryStateObjectToJsonStringConverter(finalTaskBoardFilterMap));
  };

  const resetDateStates = () => {
    const finalTaskBoardFilterMap = taskBoardFilterMap ?? {};
    finalTaskBoardFilterMap[taskBoardId] = finalTaskBoardFilterMap[taskBoardId] ?? {};
    finalTaskBoardFilterMap[taskBoardId].timespanStart = undefined;
    finalTaskBoardFilterMap[taskBoardId].timespanEnd = undefined;
    setQueryState("board-filter-map", queryStateObjectToJsonStringConverter(finalTaskBoardFilterMap));
  };

  const resetWorkflowStatusIdList = () => {
    const finalTaskBoardFilterMap = taskBoardFilterMap ?? {};
    finalTaskBoardFilterMap[taskBoardId] = finalTaskBoardFilterMap[taskBoardId] ?? {};
    finalTaskBoardFilterMap[taskBoardId].workflowStatusIdList = undefined;
    setQueryState("board-filter-map", queryStateObjectToJsonStringConverter(finalTaskBoardFilterMap));
  };

  const setFilterThisMonth = () => {
    if (isThisMonthSelected) {
      resetDateStates();
      return;
    }
    const from = startOfMonth(startOfToday());
    const to = endOfMonth(startOfToday());

    const finalTaskBoardFilterMap = taskBoardFilterMap ?? {};
    finalTaskBoardFilterMap[taskBoardId] = finalTaskBoardFilterMap[taskBoardId] ?? {};
    finalTaskBoardFilterMap[taskBoardId].timespanStart = from;
    finalTaskBoardFilterMap[taskBoardId].timespanEnd = to;

    setQueryState("board-filter-map", queryStateObjectToJsonStringConverter(finalTaskBoardFilterMap));
  };

  const setFilterThisWeek = () => {
    if (isThisWeekSelected) {
      resetDateStates();
      return;
    }
    const from = startOfWeek(startOfToday(), { weekStartsOn: 1 });
    const to = endOfWeek(startOfToday(), { weekStartsOn: 1 });
    const finalTaskBoardFilterMap = taskBoardFilterMap ?? {};
    finalTaskBoardFilterMap[taskBoardId] = finalTaskBoardFilterMap[taskBoardId] ?? {};
    finalTaskBoardFilterMap[taskBoardId].timespanStart = from;
    finalTaskBoardFilterMap[taskBoardId].timespanEnd = to;

    setQueryState("board-filter-map", queryStateObjectToJsonStringConverter(finalTaskBoardFilterMap));
  };

  const setSelectedWorkflowStatuses = (teamWorkflowStatusIds: string[]) => {
    const finalTaskBoardFilterMap = taskBoardFilterMap ?? {};
    finalTaskBoardFilterMap[taskBoardId] = finalTaskBoardFilterMap[taskBoardId] ?? {};
    finalTaskBoardFilterMap[taskBoardId].workflowStatusIdList = teamWorkflowStatusIds;
    setQueryState("board-filter-map", queryStateObjectToJsonStringConverter(finalTaskBoardFilterMap));
  };

  const setActiveStatusesAsFiltered = () => {
    isActiveSelected ? resetWorkflowStatusIdList() :
      setSelectedWorkflowStatuses?.(activeStatuses);
  };

  const setUndoneStatusesAsFiltered = () => {
    isUndoneSelected ? resetWorkflowStatusIdList() :
      setSelectedWorkflowStatuses?.(undoneStatuses);
  };

  const setBacklogStatusesAsFiltered = () => {
    isBacklogSelected ? resetWorkflowStatusIdList() :
      setSelectedWorkflowStatuses?.(backlogStatuses);
  };

  const setArchivedStatusesAsFiltered = () => {
    isArchivedSelected ? resetWorkflowStatusIdList() :
      setSelectedWorkflowStatuses?.(archivedStatuses);
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

export default TaskBoardQuickFilterBar;