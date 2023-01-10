import Button from "@/components/button";
import Line from "@/components/line/Line";
import TeamTagCell from "@/components/teamTagCell/TeamTagCell";
import { TaskDto } from "@/model/be/jinear-core";
import React from "react";
import styles from "./TaskRow.module.css";

interface TaskRowProps {
  task: TaskDto;
}

const TaskRow: React.FC<TaskRowProps> = ({ task }) => {
  const tag = `${task.team?.tag}-${task.teamTagNo}`;
  return task.workspace && task.team ? (
    <>
      <Button
        href={`/${task.workspace?.username}/task/${tag}`}
        className={styles.container}
      >
        <div className={styles.leftInfoContainer}>
          <TeamTagCell task={task} />
        </div>
        <div className={"line-clamp"}>
          {task.title +
            " " +
            task.title +
            " " +
            task.title +
            " " +
            task.title +
            " " +
            task.title +
            " " +
            task.title +
            " " +
            task.title}
        </div>
        <div className="flex-1" />
        <div>zort</div>
      </Button>
      <Line className={styles.line} />
    </>
  ) : null;
};

export default TaskRow;
