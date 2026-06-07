import { TaskDto } from "@/model/be/jinear-core";
import { createContext, useContext } from "react";

interface ITaskDetailContext {
  task: TaskDto;
  showSubTaskListEvenIfNoSubtasks: boolean;
  toggleShowSubTaskListEvenIfNoSubtasks: () => void;
}

//@ts-ignore
const TaskDetailContext = createContext<ITaskDetailContext>({});

export default TaskDetailContext;

export function useTask() {
  const ctx = useContext(TaskDetailContext);
  return ctx.task;
}

export function useShowSubTaskListEvenIfNoSubtasks() {
  const ctx = useContext(TaskDetailContext);
  return ctx.showSubTaskListEvenIfNoSubtasks;
}

export function useToggleShowSubTaskListEvenIfNoSubtasks() {
  const ctx = useContext(TaskDetailContext);
  return ctx.toggleShowSubTaskListEvenIfNoSubtasks;
}
