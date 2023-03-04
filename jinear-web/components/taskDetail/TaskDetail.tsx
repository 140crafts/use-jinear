import { useToggle } from "@/hooks/useToggle";
import { TaskDto } from "@/model/be/jinear-core";
import React from "react";
import Line from "../line/Line";
import TaskDetailContext from "./context/TaskDetailContext";
import TaskActivityList from "./taskActivityList/TaskActivityList";
import TaskBody from "./taskBody/TaskBody";
import styles from "./TaskDetail.module.css";
import TaskInfo from "./taskInfo/TaskInfo";
import TaskSubtaskList from "./taskSubtaskList/TaskSubtaskList";

interface TaskDetailProps {
  task: TaskDto;
}

const TaskDetail: React.FC<TaskDetailProps> = ({ task }) => {
  const { current: showSubTaskListEvenIfNoSubtasks, toggle: toggleShowSubTaskListEvenIfNoSubtasks } = useToggle(false);
  return (
    <TaskDetailContext.Provider
      value={{
        task,
        showSubTaskListEvenIfNoSubtasks,
        toggleShowSubTaskListEvenIfNoSubtasks,
      }}
    >
      <div className={styles.taskLayout}>
        <TaskBody className={styles.taskBody} />
        <Line />
        <TaskInfo className={styles.taskInfo} />
        <TaskSubtaskList />
        <Line />
        <TaskActivityList taskId={task.taskId} />
      </div>
    </TaskDetailContext.Provider>
  );
};

export default TaskDetail;
