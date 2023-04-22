import Button, { ButtonHeight, ButtonVariants } from "@/components/button";
import useWindowSize from "@/hooks/useWindowSize";
import { TaskRelationDto } from "@/model/be/jinear-core";
import { closeTaskOverviewModal, popTaskOverviewModal } from "@/store/slice/modalSlice";
import { useAppDispatch } from "@/store/store";
import Logger from "@/utils/logger";
import React from "react";
import styles from "./TaskRelationCell.module.css";

interface TaskRelationCellProps {
  relation: TaskRelationDto;
}

const logger = Logger("TaskRelationCell");

const TaskRelationCell: React.FC<TaskRelationCellProps> = ({ relation }) => {
  const dispatch = useAppDispatch();
  const relatedTask = relation.relatedTask;
  const tag = `${relatedTask.team?.tag}-${relatedTask.teamTagNo}`;
  const { isMobile } = useWindowSize();

  const onLinkClick = (event: React.MouseEvent<HTMLAnchorElement> | undefined) => {
    logger.log("onLinkClick");
    dispatch(closeTaskOverviewModal());
    if (!isMobile) {
      event?.preventDefault();
      setTimeout(() => {
        openTaskOverviewModal();
      }, 500);
    }
  };

  const openTaskOverviewModal = () => {
    const task = relatedTask;
    const workspaceName = task?.workspace?.username;
    const taskTag = `${task?.team?.tag}-${task?.teamTagNo}`;
    dispatch(popTaskOverviewModal({ taskTag, workspaceName, visible: true }));
  };

  return (
    <Button
      className={styles.container}
      variant={ButtonVariants.filled}
      heightVariant={ButtonHeight.short}
      onClick={onLinkClick}
      href={`/${relatedTask.workspace?.username}/task/${tag}`}
    >
      <div>{tag}</div>
      <div className="single-line flex-1">{relatedTask.title}</div>
    </Button>
  );
};

export default TaskRelationCell;
