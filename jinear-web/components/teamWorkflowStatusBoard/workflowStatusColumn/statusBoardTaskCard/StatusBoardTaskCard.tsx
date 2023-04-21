import AssigneeCell from "@/components/assigneeCell/AssigneeCell";
import Button from "@/components/button";
import TopicInfo from "@/components/taskListScreen/taskLists/taskRow/topicInfo/TopicInfo";
import TeamTagCell from "@/components/teamTagCell/TeamTagCell";
import useWindowSize from "@/hooks/useWindowSize";
import { TaskDto } from "@/model/be/jinear-core";
import { popChangeTaskDateModal, popTaskOverviewModal } from "@/store/slice/modalSlice";
import { useAppDispatch } from "@/store/store";
import cn from "classnames";
import Link from "next/link";
import React from "react";
import { Draggable, DraggableProvided, DraggableStateSnapshot } from "react-beautiful-dnd";
import { IoTime } from "react-icons/io5";
import styles from "./StatusBoardTaskCard.module.css";

interface StatusBoardTaskCardProps {
  task: TaskDto;
  index?: number;
}

const StatusBoardTaskCard: React.FC<StatusBoardTaskCardProps> = ({ task, index = 0 }) => {
  const dispatch = useAppDispatch();
  const { isMobile } = useWindowSize();

  const popChangeDatesModal = (event: React.MouseEvent<HTMLButtonElement, MouseEvent>) => {
    event?.preventDefault?.();
    dispatch(popChangeTaskDateModal({ visible: true, task }));
  };

  const onLinkClick = (event: React.MouseEvent<HTMLAnchorElement> | undefined) => {
    if (!isMobile) {
      event?.preventDefault();
      openTaskOverviewModal();
    }
  };

  const openTaskOverviewModal = () => {
    const workspaceName = task?.workspace?.username;
    const taskTag = `${task?.team?.tag}-${task?.teamTagNo}`;
    dispatch(popTaskOverviewModal({ taskTag, workspaceName, visible: true }));
  };

  return (
    <Draggable key={task.taskId} draggableId={task.taskId} index={index}>
      {(providedDraggable: DraggableProvided, snapshotDraggable: DraggableStateSnapshot) => (
        <Link
          href={`/${task.workspace?.username}/task/${task.team?.tag}-${task.teamTagNo}`}
          className={styles.container}
          onClick={onLinkClick}
          ref={providedDraggable.innerRef}
          {...providedDraggable.draggableProps}
          {...providedDraggable.dragHandleProps}
        >
          <div className={cn(styles.title)}>{task.title}</div>
          <div className={styles.infoContainer}>
            {task.topic && <TopicInfo topic={task.topic} />}
            {/* <div className="flex-1" /> */}
            <Button className={styles.taskIconButton} onClick={popChangeDatesModal}>
              <IoTime size={12} />
            </Button>

            {!task.workspace?.isPersonal && (
              <AssigneeCell
                task={task}
                tooltipPosition={task.workflowStatus.workflowStateGroup == "BACKLOG" ? "left" : "right"}
                className={styles.taskIconButton}
              />
            )}
            <TeamTagCell task={task} className={styles.taskTagCell} />
          </div>
        </Link>
      )}
    </Draggable>
  );
};

export default StatusBoardTaskCard;
