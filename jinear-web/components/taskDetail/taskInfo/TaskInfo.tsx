import React from "react";
import styles from "./TaskInfo.module.css";

import AddSubtaskButton from "./addSubtaskButton/AddSubtaskButton";
import ChangeAssigneeButton from "./changeAssigneeButton/ChangeAssigneeButton";
import ChangeTopicButton from "./changeTopicButton/ChangeTopicButton";
import ChangeWorkflowStatusButton from "./changeWorkflowStatusButton/ChangeWorkflowStatusButton";
import RemindersButton from "./remindersButton/RemindersButton";
import TaskAddChecklistButton from "./taskAddChecklistButton/TaskAddChecklistButton";
import TaskAssignedDateButton from "./taskAssignedDateButton/TaskAssignedDateButton";
import TaskDueDateButton from "./taskDueDateButton/TaskDueDateButton";
import TaskTagNoButton from "./taskTagNoButton/TaskTagNoButton";

interface TaskInfoProps {
  className?: string;
}

const TaskInfo: React.FC<TaskInfoProps> = ({ className }) => {
  return (
    <div className={styles.container}>
      <TaskTagNoButton className={styles.button} />
      <ChangeWorkflowStatusButton className={styles.button} />
      <ChangeTopicButton />
      <TaskAssignedDateButton />
      <TaskDueDateButton />
      <ChangeAssigneeButton className={styles.button} />
      <RemindersButton className={styles.button} />
      <AddSubtaskButton className={styles.button} />
      <TaskAddChecklistButton className={styles.button} />
    </div>
  );
};

export default TaskInfo;
