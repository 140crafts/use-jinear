import { TaskDto } from "@/model/be/jinear-core";
import React from "react";
import styles from "./TeamTagCell.module.css";

interface TeamTagCellProps {
  task: TaskDto;
}

const TeamTagCell: React.FC<TeamTagCellProps> = ({ task }) => {
  return task && task.team ? (
    <div className={styles.container}>
      {`${task.team?.tag}-${task.teamTagNo}`}
    </div>
  ) : null;
};

export default TeamTagCell;
