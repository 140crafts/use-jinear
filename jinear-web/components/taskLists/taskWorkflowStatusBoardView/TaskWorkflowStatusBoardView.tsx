import TeamWorkflowStatusBoard from "@/components/teamWorkflowStatusBoard/TeamWorkflowStatusBoard";
import { TaskDto } from "@/model/be/jinear-core";
import React from "react";
import styles from "./TaskWorkflowStatusBoardView.module.scss";

interface TaskWorkflowStatusBoardViewProps {
  teamId: string;
  taskList: TaskDto[];
  isTaskListingLoading: boolean;
}

const TaskWorkflowStatusBoardView: React.FC<TaskWorkflowStatusBoardViewProps> = ({ teamId, taskList, isTaskListingLoading }) => {
  return (
    <div className={styles.container}>
      <div className={styles.actionBarContainer}></div>
      <span className={styles.statusBoardContainer}>
        {teamId && <TeamWorkflowStatusBoard teamId={teamId} taskList={taskList} isTaskListingLoading={isTaskListingLoading} />}
      </span>
    </div>
  );
};

export default TaskWorkflowStatusBoardView;
