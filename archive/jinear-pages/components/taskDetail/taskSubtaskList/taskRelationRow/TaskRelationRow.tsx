import AssigneeCell from "@/components/assigneeCell/AssigneeCell";
import Button, { ButtonHeight, ButtonVariants } from "@/components/button";
import useWindowSize from "@/hooks/useWindowSize";
import { RelatedTaskDto, TaskRelationType } from "@/model/be/jinear-core";
import { useDeleteTaskRelationMutation } from "@/store/api/taskRelationApi";
import {
  changeLoadingModalVisibility,
  closeDialogModal,
  popChangeTaskWorkflowStatusModal,
  popDialogModal,
  popTaskOverviewModal,
} from "@/store/slice/modalSlice";
import { useAppDispatch } from "@/store/store";
import cn from "classnames";
import useTranslation from "locales/useTranslation";
import Link from "next/link";
import React, { useEffect } from "react";

import {
  IoCheckmarkCircle,
  IoClose,
  IoCloseCircle,
  IoContrast,
  IoEllipseOutline,
  IoPauseCircleOutline,
  IoRemove,
} from "react-icons/io5";
import styles from "./TaskRelationRow.module.css";

interface TaskRelationRowProps {
  taskRelationId: string;
  taskId?: string | null;
  relatedTaskId?: string | null;
  relationType: TaskRelationType;
  task?: RelatedTaskDto | null;
  relatedTask?: RelatedTaskDto | null;
  noAccessLabel: string;
}

const ICON_SIZE = 17;
const groupIconMap = {
  BACKLOG: <IoPauseCircleOutline size={ICON_SIZE} />,
  NOT_STARTED: <IoEllipseOutline size={ICON_SIZE} />,
  STARTED: <IoContrast size={ICON_SIZE} />,
  COMPLETED: <IoCheckmarkCircle size={ICON_SIZE} />,
  CANCELLED: <IoCloseCircle size={ICON_SIZE} />,
};

const TaskRelationRow: React.FC<TaskRelationRowProps> = ({
  taskRelationId,
  taskId,
  relatedTaskId,
  relationType,
  task,
  relatedTask,
  noAccessLabel,
}) => {
  const dispatch = useAppDispatch();
  const { t } = useTranslation();
  const tag = `${task?.team?.tag}-${task?.teamTagNo}`;
  const [deleteTaskRelation, { isSuccess: isDeleteSuccess, isError: isDeleteError }] = useDeleteTaskRelationMutation();
  const { isMobile } = useWindowSize();

  const popChangeWorkflowStatusModal = () => {
    if (task) {
      dispatch(popChangeTaskWorkflowStatusModal({ visible: true, task }));
    }
  };

  useEffect(() => {
    dispatch(changeLoadingModalVisibility({ visible: false }));
  }, [isDeleteSuccess, isDeleteError]);

  const removeRelation = () => {
    dispatch(closeDialogModal());
    dispatch(changeLoadingModalVisibility({ visible: true }));
    deleteTaskRelation({ relationId: taskRelationId });
  };

  const popAreYouSureModal = () => {
    dispatch(
      popDialogModal({
        visible: true,
        title: t("taskRelationUnlink_dialogModalTitle"),
        content: t("taskRelationUnlink_dialogModalContent"),
        confirmButtonLabel: t("taskRelationUnlink_dialogModalConfirmLabel"),
        onConfirm: removeRelation,
      })
    );
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
    <div className={cn(styles.container, task ? undefined : styles.disabled)}>
      <Button
        disabled={!task}
        onClick={popChangeWorkflowStatusModal}
        heightVariant={ButtonHeight.short}
        variant={ButtonVariants.filled}
        className={styles.workflowStatusButton}
      >
        {task ? groupIconMap?.[task?.workflowStatus.workflowStateGroup] : <IoRemove size={ICON_SIZE} />}
      </Button>
      <div className="spacer-w-1" />
      {task && (
        <Link className={styles.linkButton} href={`/${task?.workspace?.username}/task/${tag}`} onClick={onLinkClick}>
          <div className="line-clamp">{`${tag} ${task.title}`}</div>
        </Link>
      )}

      {!task && <div className={styles.noAccessLabel}>{noAccessLabel}</div>}

      {task && (
        <Button
          className={cn(styles.workflowStatusButton, styles.deleteButton)}
          variant={ButtonVariants.hoverFilled2}
          onClick={popAreYouSureModal}
          data-tooltip-right={t("taskRelationUnlink_dialogModalTitle")}
        >
          <IoClose size={ICON_SIZE * 0.8} />
        </Button>
      )}

      <div className="spacer-w-1" />
      {task && <AssigneeCell task={task} tooltipPosition={"right"} className={styles.workflowStatusButton} />}
    </div>
  );
};

export default TaskRelationRow;
