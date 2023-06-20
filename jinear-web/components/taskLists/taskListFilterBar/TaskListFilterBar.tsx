import { TaskFilterRequest, TeamMemberDto, TeamWorkflowStatusDto, TopicDto } from "@/model/be/jinear-core";
import { useRetrieveTeamMembersQuery } from "@/store/api/teamMemberApi";
import { useRetrieveAllFromTeamQuery } from "@/store/api/teamWorkflowStatusApi";
import { useRetrieveExactTeamTopicsQuery } from "@/store/api/topicListingApi";
import Logger from "@/utils/logger";
import { setHours, setMinutes } from "date-fns";
import React, { useEffect, useState } from "react";
import styles from "./TaskListFilterBar.module.css";
import AssigneeFilterButton from "./assigneeFilterButton/AssigneeFilterButton";
import TaskListFilterBarContext from "./context/TaskListFilterBarContext";
import FromDatePickerButton from "./fromDatePickerButton/FromDatePickerButton";
import OwnerFilterButton from "./ownerFilterButton/OwnerFilterButton";
import QuickFilterBar from "./quickFilterBar/QuickFilterBar";
import ToDatePickerButton from "./toDatePickerButton/ToDatePickerButton";
import TopicFilterButton from "./topicFilterButton/TopicFilterButton";
import WorkflowStatusFilterButton from "./workflowStatusFilterButton/WorkflowStatusFilterButton";
interface TaskListFilterBarProps {
  workspaceId: string;
  teamId: string;
  topicIds?: string[];
  ownerIds?: string[];
  assigneeIds?: string[];
  workflowStatusIdList?: string[];
  timespanStart?: Date;
  timespanEnd?: Date;
  hasPreciseFromDate?: boolean;
  hasPreciseToDate?: boolean;
  onFilterChange?: (filter: TaskFilterRequest) => void;
}

const logger = Logger("TaskListFilterBar");

const TaskListFilterBar: React.FC<TaskListFilterBarProps> = ({
  workspaceId,
  teamId,
  topicIds,
  ownerIds = [],
  assigneeIds = [],
  workflowStatusIdList = [],
  timespanStart,
  timespanEnd,
  onFilterChange,
}) => {
  const [selectedTopics, setSelectedTopics] = useState<TopicDto[]>([]);
  const [selectedOwners, setSelectedOwners] = useState<TeamMemberDto[]>([]);
  const [selectedAssignees, setSelectedAssignees] = useState<TeamMemberDto[]>([]);
  const [selectedWorkflowStatuses, setSelectedWorkflowStatuses] = useState<TeamWorkflowStatusDto[]>([]);
  const [fromDate, setFromDate] = useState<Date>();
  const [toDate, setToDate] = useState<Date>();
  const [hasPreciseFromDate, setHasPreciseFromDate] = useState<Boolean>(false);
  const [hasPreciseToDate, setHasPreciseToDate] = useState<Boolean>(false);

  const filter: TaskFilterRequest = {
    workspaceId: workspaceId,
    teamIdList: [teamId],
    topicIds: selectedTopics?.map?.((topicDto) => topicDto.topicId),
    ownerIds: selectedOwners?.map?.((owner) => owner.accountId),
    assigneeIds: selectedAssignees?.map?.((assignee) => assignee.accountId),
    workflowStatusIdList: selectedWorkflowStatuses?.map?.((wfs) => wfs.teamWorkflowStatusId),
    timespanStart: fromDate,
    timespanEnd: toDate ? (hasPreciseToDate ? toDate : setMinutes(setHours(toDate || new Date(), 23), 59)) : undefined,
  };

  const { data: findExactTeamTopicsResponse } = useRetrieveExactTeamTopicsQuery(
    {
      teamId: teamId || "",
      body: { topicIds: topicIds || [] },
    },
    { skip: teamId == null || teamId == "" || topicIds == null || topicIds?.length == 0 }
  );

  const { data: teamMembersResponse } = useRetrieveTeamMembersQuery(
    { teamId },
    {
      skip: ownerIds.length == 0 || assigneeIds.length == 0,
    }
  );

  const { data: teamWorkflowStatusListResponse } = useRetrieveAllFromTeamQuery(
    { teamId: teamId || "" },
    {
      skip: teamId == null || workflowStatusIdList.length == 0,
    }
  );

  useEffect(() => {
    if (filter) {
      //   logger.log({ filter });
      onFilterChange?.(filter);
    }
  }, [filter]);

  useEffect(() => {
    if (topicIds != null && topicIds?.length != 0 && findExactTeamTopicsResponse && findExactTeamTopicsResponse.data) {
      setSelectedTopics(findExactTeamTopicsResponse.data);
    } else if (topicIds != null && topicIds?.length == 0) {
      setSelectedTopics([]);
    }
  }, [topicIds, findExactTeamTopicsResponse]);

  useEffect(() => {
    if (ownerIds.length != 0 && teamMembersResponse) {
      const owners = teamMembersResponse.data.content.filter((teamMemberDto) => ownerIds.indexOf(teamMemberDto.accountId) != -1);
      setSelectedOwners(owners);
    }
  }, [ownerIds, teamMembersResponse]);

  useEffect(() => {
    if (assigneeIds.length != 0 && teamMembersResponse) {
      const assignees = teamMembersResponse.data.content.filter(
        (teamMemberDto) => assigneeIds.indexOf(teamMemberDto.accountId) != -1
      );
      setSelectedAssignees(assignees);
    }
  }, [assigneeIds, teamMembersResponse]);

  useEffect(() => {
    if (workflowStatusIdList.length != 0 && teamWorkflowStatusListResponse) {
      const backlogList = teamWorkflowStatusListResponse.data.groupedTeamWorkflowStatuses.BACKLOG || [];
      const notStartedList = teamWorkflowStatusListResponse.data.groupedTeamWorkflowStatuses.NOT_STARTED || [];
      const startedList = teamWorkflowStatusListResponse.data.groupedTeamWorkflowStatuses.STARTED || [];
      const completedList = teamWorkflowStatusListResponse.data.groupedTeamWorkflowStatuses.COMPLETED || [];
      const cancelledList = teamWorkflowStatusListResponse.data.groupedTeamWorkflowStatuses.CANCELLED || [];
      const workflowStatues = [...backlogList, ...notStartedList, ...startedList, ...completedList, ...cancelledList];
      const selected = workflowStatues.filter(
        (teamWorkflowStatusDto) => workflowStatusIdList.indexOf(teamWorkflowStatusDto.teamWorkflowStatusId) != -1
      );
      setSelectedWorkflowStatuses(selected);
    }
  }, [workflowStatusIdList, teamWorkflowStatusListResponse]);

  useEffect(() => {
    if (timespanStart) {
      setFromDate(timespanStart);
    }
    if (timespanEnd) {
      setToDate(timespanEnd);
    }
    if (hasPreciseFromDate != null) {
      setHasPreciseFromDate(hasPreciseFromDate);
    }
    if (hasPreciseToDate != null) {
      setHasPreciseToDate(hasPreciseToDate);
    }
  }, [timespanStart, timespanEnd, hasPreciseFromDate, hasPreciseToDate]);

  const resetState = () => {
    setSelectedTopics([]);
    setSelectedOwners([]);
    setSelectedAssignees([]);
    setSelectedWorkflowStatuses([]);
    setFromDate(undefined);
    setToDate(undefined);
    setHasPreciseFromDate(false);
    setHasPreciseToDate(false);
  };

  return (
    <TaskListFilterBarContext.Provider
      value={{
        teamId,
        selectedTopics,
        setSelectedTopics,
        selectedOwners,
        setSelectedOwners,
        selectedAssignees,
        setSelectedAssignees,
        selectedWorkflowStatuses,
        setSelectedWorkflowStatuses,
        fromDate,
        setFromDate,
        toDate,
        setToDate,
        hasPreciseFromDate,
        setHasPreciseFromDate,
        hasPreciseToDate,
        setHasPreciseToDate,
        resetState,
      }}
    >
      <>
        <div className={styles.container}>
          <FromDatePickerButton />
          <ToDatePickerButton />
          <WorkflowStatusFilterButton />
          <AssigneeFilterButton />
          <OwnerFilterButton />
          <TopicFilterButton />
        </div>
        <QuickFilterBar />
      </>
    </TaskListFilterBarContext.Provider>
  );
};

export default React.memo(TaskListFilterBar, (prevProps, nextProps) => {
  const prev = JSON.stringify(prevProps);
  const next = JSON.stringify(nextProps);
  const areEqual = prev == next;
  logger.log({ prevProps, nextProps, areEqual });
  return areEqual;
});
