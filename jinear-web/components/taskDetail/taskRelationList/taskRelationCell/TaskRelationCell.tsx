import Button, { ButtonHeight, ButtonVariants } from "@/components/button";
import useWindowSize from "@/hooks/useWindowSize";
import { RelatedTaskDto, TaskRelationType } from "@/model/be/jinear-core";
import { closeTaskOverviewModal, popTaskOverviewModal } from "@/store/slice/modalSlice";
import { useAppDispatch } from "@/store/store";
import Logger from "@/utils/logger";
import useTranslation from "locales/useTranslation";
import React from "react";
import styles from "./TaskRelationCell.module.css";

interface TaskRelationCellProps {
  taskRelationId: string;
  taskId?: string | null;
  relatedTaskId?: string | null;
  relationType: TaskRelationType;
  task?: RelatedTaskDto | null;
  relatedTask?: RelatedTaskDto | null;
}

const logger = Logger("TaskRelationCell");

const TaskRelationCell: React.FC<TaskRelationCellProps> = ({
  taskRelationId,
  taskId,
  relatedTaskId,
  relationType,
  task,
  relatedTask,
}) => {
  const { t } = useTranslation();
  const dispatch = useAppDispatch();
  const tag = `${relatedTask?.team?.tag}-${relatedTask?.teamTagNo}`;
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

  return relatedTask ? (
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
  ) : (
    <Button
      className={styles.noAccessContainer}
      variant={ButtonVariants.filled}
      heightVariant={ButtonHeight.short}
      disabled={true}
    >
      {t("taskRelatedTaskNoAccess")}
    </Button>
  );
};

export default TaskRelationCell;
