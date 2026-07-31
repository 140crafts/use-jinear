import React from "react";
import styles from "./TaskActionBar.module.css";

import AddSubtaskButton from "./addSubtaskButton/AddSubtaskButton";
import ChangeAssigneeButton from "./changeAssigneeButton/ChangeAssigneeButton";
import ChangeTopicButton from "./changeTopicButton/ChangeTopicButton";
import ChangeWorkflowStatusButton from "./changeWorkflowStatusButton/ChangeWorkflowStatusButton";
import RemindersButton from "./remindersButton/RemindersButton";
import TaskAddChecklistButton from "./taskAddChecklistButton/TaskAddChecklistButton";
import TaskAssignedDateButton from "./taskAssignedDateButton/TaskAssignedDateButton";
import TaskBoardsButton from "./taskBoardsButton/TaskBoardsButton";
import TaskDueDateButton from "./taskDueDateButton/TaskDueDateButton";
import TaskSubscribeButton from "./taskSubscribeButton/TaskSubscribeButton";
import TaskTagNoButton from "./taskTagNoButton/TaskTagNoButton";
import {useTask} from "@/components/taskDetail/context/TaskDetailContext";

interface TaskActionBarProps {
    className?: string;
}

const TaskActionBar: React.FC<TaskActionBarProps> = ({className}) => {
    const task = useTask();

    return (
        <div className={styles.container}>
            <ChangeWorkflowStatusButton className={styles.button}/>
            <ChangeTopicButton/>
            <TaskAssignedDateButton/>
            <TaskDueDateButton/>
            <ChangeAssigneeButton className={styles.button}/>
            <RemindersButton className={styles.button}/>
            <AddSubtaskButton className={styles.button}/>
            <TaskAddChecklistButton className={styles.button}/>
            <TaskBoardsButton className={styles.button}/>
            <TaskSubscribeButton className={styles.button}/>
        </div>
    );
};

export default TaskActionBar;
