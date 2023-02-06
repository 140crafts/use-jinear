import { TaskDto } from "@/model/be/jinear-core";
import cn from "classnames";
import React from "react";
import styles from "./TeamTagCell.module.css";
interface TeamTagCellProps {
  task: TaskDto;
  className?: string;
}

const TeamTagCell: React.FC<TeamTagCellProps> = ({ task, className }) => {
  return task && task.team ? (
    <div className={cn(styles.container, className)}>
      {`${task.team?.tag}-${task.teamTagNo}`}
    </div>
  ) : null;
};

export default TeamTagCell;
