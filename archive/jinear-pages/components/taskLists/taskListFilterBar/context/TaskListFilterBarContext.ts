import { TeamDto, WorkspaceDto } from "@/model/be/jinear-core";
import { createContext, useContext } from "react";

interface ITaskListFilterBarContext {
  workspace?: WorkspaceDto;
  team?: TeamDto;
}

const TaskListFilterBarContext = createContext<ITaskListFilterBarContext>({
  workspace: undefined,
  team: undefined,
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
