import AssigneeCell from "@/components/assigneeCell/AssigneeCell";
import Button from "@/components/button";
import TeamTagCell from "@/components/teamTagCell/TeamTagCell";
import { TaskDto } from "@/model/be/jinear-core";
import { popChangeTaskDateModal } from "@/store/slice/modalSlice";
import { useAppDispatch } from "@/store/store";
import cn from "classnames";
import Link from "next/link";
import React from "react";
import {
  Draggable,
  DraggableProvided,
  DraggableStateSnapshot,
} from "react-beautiful-dnd";
import { IoTime } from "react-icons/io5";
import styles from "./StatusBoardTaskCard.module.css";

interface StatusBoardTaskCardProps {
  task: TaskDto;
  index?: number;
}

const StatusBoardTaskCard: React.FC<StatusBoardTaskCardProps> = ({
  task,
  index = 0,
}) => {
  const dispatch = useAppDispatch();

  const popChangeDatesModal = (
    event: React.MouseEvent<HTMLButtonElement, MouseEvent>
  ) => {
    event?.preventDefault?.();
    dispatch(popChangeTaskDateModal({ visible: true, task }));
  };

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
            <Button
              className={styles.taskTagCell}
              onClick={popChangeDatesModal}
            >
              <IoTime size={12} />
            </Button>
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
