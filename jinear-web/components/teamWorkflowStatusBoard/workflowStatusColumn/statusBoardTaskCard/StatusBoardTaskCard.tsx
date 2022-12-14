import AssigneeCell from "@/components/assigneeCell/AssigneeCell";
import TeamTagCell from "@/components/teamTagCell/TeamTagCell";
import { TaskDto } from "@/model/be/jinear-core";
import cn from "classnames";
import Link from "next/link";
import React from "react";
import styles from "./StatusBoardTaskCard.module.css";

interface StatusBoardTaskCardProps {
  task: TaskDto;
}

const StatusBoardTaskCard: React.FC<StatusBoardTaskCardProps> = ({ task }) => {
  return (
    <Link
      href={`/${task.workspace?.username}/task/${task.team?.tag}-${task.teamTagNo}`}
      className={styles.container}
    >
      <div className={cn(styles.title)}>{task.title}</div>
      <div className={styles.infoContainer}>
        <AssigneeCell
          task={task}
          tooltipPosition={
            task.workflowStatus.workflowStateGroup == "BACKLOG"
              ? "left"
              : "right"
          }
        />
        <TeamTagCell task={task} />
      </div>
    </Link>
  );
};

export default StatusBoardTaskCard;
