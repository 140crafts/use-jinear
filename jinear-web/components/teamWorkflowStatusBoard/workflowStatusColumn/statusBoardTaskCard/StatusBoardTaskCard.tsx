import AssigneeCell from "@/components/assigneeCell/AssigneeCell";
import TeamTagCell from "@/components/teamTagCell/TeamTagCell";
import { TaskDto } from "@/model/be/jinear-core";
import cn from "classnames";
import Link from "next/link";
import React from "react";
import {
  Draggable,
  DraggableProvided,
  DraggableStateSnapshot,
} from "react-beautiful-dnd";
import styles from "./StatusBoardTaskCard.module.css";

interface StatusBoardTaskCardProps {
  task: TaskDto;
  index?: number;
}

const StatusBoardTaskCard: React.FC<StatusBoardTaskCardProps> = ({
  task,
  index = 0,
}) => {
  return (
    <Draggable key={task.taskId} draggableId={task.taskId} index={index}>
      {(
        providedDraggable: DraggableProvided,
        snapshotDraggable: DraggableStateSnapshot
      ) => (
        <Link
          href={`/${task.workspace?.username}/task/${task.team?.tag}-${task.teamTagNo}`}
          className={styles.container}
          ref={providedDraggable.innerRef}
          {...providedDraggable.draggableProps}
          {...providedDraggable.dragHandleProps}
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
              className={styles.taskTagCell}
            />
            <TeamTagCell task={task} className={styles.taskTagCell} />
          </div>
        </Link>
      )}
    </Draggable>
  );
};

export default StatusBoardTaskCard;
