import { TeamDto, TeamMemberDto, TeamWorkflowStatusDto, TopicDto, WorkspaceDto } from "@/model/be/jinear-core";
import { createContext, useContext } from "react";

interface ITaskListFilterBarContext {
  workspace?: WorkspaceDto;
  team?: TeamDto;
  selectedTopics: TopicDto[];
  setSelectedTopics?: React.Dispatch<React.SetStateAction<TopicDto[]>>;
  selectedOwners: TeamMemberDto[];
  setSelectedOwners?: React.Dispatch<React.SetStateAction<TeamMemberDto[]>>;
  selectedAssignees: TeamMemberDto[];
  setSelectedAssignees?: React.Dispatch<React.SetStateAction<TeamMemberDto[]>>;
  selectedWorkflowStatuses: TeamWorkflowStatusDto[];
  setSelectedWorkflowStatuses?: React.Dispatch<React.SetStateAction<TeamWorkflowStatusDto[]>>;
  fromDate?: Date;
  setFromDate?: React.Dispatch<React.SetStateAction<Date | undefined>>;
  toDate?: Date;
  setToDate?: React.Dispatch<React.SetStateAction<Date | undefined>>;
  hasPreciseFromDate: Boolean;
  setHasPreciseFromDate?: React.Dispatch<React.SetStateAction<Boolean>>;
  hasPreciseToDate: Boolean;
  setHasPreciseToDate?: React.Dispatch<React.SetStateAction<Boolean>>;
  resetState?: () => void;
}

const TaskListFilterBarContext = createContext<ITaskListFilterBarContext>({
  workspace: undefined,
  team: undefined,
  selectedTopics: [],
  selectedOwners: [],
  selectedAssignees: [],
  selectedWorkflowStatuses: [],
  hasPreciseFromDate: false,
  hasPreciseToDate: false,
});

export default TaskListFilterBarContext;

export function useWorkspace() {
  const ctx = useContext(TaskListFilterBarContext);
  return ctx.workspace;
}

export function useTeam() {
  const ctx = useContext(TaskListFilterBarContext);
  return ctx.team;
}

export function useSelectedTopics() {
  const ctx = useContext(TaskListFilterBarContext);
  return ctx.selectedTopics;
}

export function useSetSelectedTopics() {
  const ctx = useContext(TaskListFilterBarContext);
  return ctx.setSelectedTopics;
}

export function useSelectedOwners() {
  const ctx = useContext(TaskListFilterBarContext);
  return ctx.selectedOwners;
}

export function useSetSelectedOwners() {
  const ctx = useContext(TaskListFilterBarContext);
  return ctx.setSelectedOwners;
}

export function useSelectedAssignees() {
  const ctx = useContext(TaskListFilterBarContext);
  return ctx.selectedAssignees;
}

export function useSetSelectedAssignees() {
  const ctx = useContext(TaskListFilterBarContext);
  return ctx.setSelectedAssignees;
}

export function useSelectedWorkflowStatuses() {
  const ctx = useContext(TaskListFilterBarContext);
  return ctx.selectedWorkflowStatuses;
}

export function useSetSelectedWorkflowStatuses() {
  const ctx = useContext(TaskListFilterBarContext);
  return ctx.setSelectedWorkflowStatuses;
}

export function useFromDate() {
  const ctx = useContext(TaskListFilterBarContext);
  return ctx.fromDate;
}

export function useSetFromDate() {
  const ctx = useContext(TaskListFilterBarContext);
  return ctx.setFromDate;
}

export function useToDate() {
  const ctx = useContext(TaskListFilterBarContext);
  return ctx.toDate;
}

export function useSetToDate() {
  const ctx = useContext(TaskListFilterBarContext);
  return ctx.setToDate;
}

export function useHasPreciseFromDate() {
  const ctx = useContext(TaskListFilterBarContext);
  return ctx.hasPreciseFromDate;
}

export function useSetHasPreciseFromDate() {
  const ctx = useContext(TaskListFilterBarContext);
  return ctx.setHasPreciseFromDate;
}

export function useHasPreciseToDate() {
  const ctx = useContext(TaskListFilterBarContext);
  return ctx.hasPreciseToDate;
}

export function useSetHasPreciseToDate() {
  const ctx = useContext(TaskListFilterBarContext);
  return ctx.setHasPreciseToDate;
}

export function useResetState() {
  const ctx = useContext(TaskListFilterBarContext);
  return ctx.resetState;
}
