import Line from "@/components/line/Line";
import React from "react";
import { useTask } from "../context/TaskDetailContext";
import TaskChecklist from "./taskChecklist/TaskChecklist";
import styles from "./TaskChecklistContainer.module.css";

interface TaskChecklistContainerProps {}

const TaskChecklistContainer: React.FC<TaskChecklistContainerProps> = ({}) => {
  const task = useTask();
  const hasChecklist = task.checklists != null && task.checklists.length != 0;
  return hasChecklist ? (
    <>
      <Line />
      <div className={styles.container}>
        {task.checklists?.map((checklist) => (
          <TaskChecklist key={checklist.checklistId} initialChecklist={checklist} />
        ))}
      </div>
    </>
  ) : null;
};

export default TaskChecklistContainer;
