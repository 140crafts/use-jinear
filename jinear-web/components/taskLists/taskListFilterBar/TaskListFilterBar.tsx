import { TeamMemberDto, TeamWorkflowStatusDto, TopicDto } from "@/model/be/jinear-core";
import { useRetrieveTeamMembersQuery } from "@/store/api/teamMemberApi";
import { useRetrieveExactTeamTopicsQuery } from "@/store/api/topicListingApi";
import React, { useEffect, useState } from "react";
import styles from "./TaskListFilterBar.module.css";
import AssigneeFilterButton from "./assigneeFilterButton/AssigneeFilterButton";
import TaskListFilterBarContext from "./context/TaskListFilterBarContext";
import FromDatePickerButton from "./fromDatePickerButton/FromDatePickerButton";
import OwnerFilterButton from "./ownerFilterButton/OwnerFilterButton";
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
}

const TaskListFilterBar: React.FC<TaskListFilterBarProps> = ({
  workspaceId,
  teamId,
  topicIds,
  ownerIds = [],
  assigneeIds = [],
}) => {
  const [selectedTopics, setSelectedTopics] = useState<TopicDto[]>([]);
  const [selectedOwners, setSelectedOwners] = useState<TeamMemberDto[]>([]);
  const [selectedAssignees, setSelectedAssignees] = useState<TeamMemberDto[]>([]);
  const [selectedWorkflowStatuses, setSelectedWorkflowStatuses] = useState<TeamWorkflowStatusDto[]>([]);
  const [fromDate, setFromDate] = useState<Date>();
  const [toDate, setToDate] = useState<Date>();
  const [hasPreciseFromDate, setHasPreciseFromDate] = useState<Boolean>(false);
  const [hasPreciseToDate, setHasPreciseToDate] = useState<Boolean>(false);

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

  useEffect(() => {
    if (topicIds != null && topicIds?.length != 0 && findExactTeamTopicsResponse && findExactTeamTopicsResponse.data) {
      setSelectedTopics(findExactTeamTopicsResponse.data);
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
      }}
    >
      <div className={styles.container}>
        <FromDatePickerButton />
        <ToDatePickerButton />
        <WorkflowStatusFilterButton />
        <AssigneeFilterButton />
        <OwnerFilterButton />
        <TopicFilterButton />
      </div>
    </TaskListFilterBarContext.Provider>
  );
};

export default TaskListFilterBar;
