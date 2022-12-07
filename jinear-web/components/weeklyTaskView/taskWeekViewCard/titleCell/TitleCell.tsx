import { TaskDto } from "@/model/be/jinear-core";
import getCssVariable from "@/utils/cssHelper";
import cn from "classnames";
import React from "react";
import styles from "./TitleCell.module.css";

interface TitleCellProps {
  task: TaskDto;
  daysInThisWeek: number;
}

const TitleCell: React.FC<TitleCellProps> = ({ task, daysInThisWeek }) => {
  const cardWidth =
    getCssVariable("--task-card-width")?.replace("px", "") || "0";
  const maxWidth = parseInt(cardWidth) * daysInThisWeek;

  return (
    <div className={styles.titleContainer}>
      <div className={cn(styles.title, "single-line")}>{task.title}</div>
    </div>
  );
};

export default TitleCell;
