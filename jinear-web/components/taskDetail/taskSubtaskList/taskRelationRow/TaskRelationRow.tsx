import AssigneeCell from "@/components/assigneeCell/AssigneeCell";
import Button, { ButtonHeight, ButtonVariants } from "@/components/button";
import useWindowSize from "@/hooks/useWindowSize";
import { TaskRelationDto } from "@/model/be/jinear-core";
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

import { IoCheckmarkCircle, IoClose, IoCloseCircle, IoContrast, IoEllipseOutline, IoPauseCircleOutline } from "react-icons/io5";
import styles from "./TaskRelationRow.module.css";

interface TaskRelationRowProps {
  relation: TaskRelationDto;
}

const ICON_SIZE = 17;
const groupIconMap = {
  BACKLOG: <IoPauseCircleOutline size={ICON_SIZE} />,
  NOT_STARTED: <IoEllipseOutline size={ICON_SIZE} />,
  STARTED: <IoContrast size={ICON_SIZE} />,
  COMPLETED: <IoCheckmarkCircle size={ICON_SIZE} />,
  CANCELLED: <IoCloseCircle size={ICON_SIZE} />,
};

const TaskRelationRow: React.FC<TaskRelationRowProps> = ({ relation }) => {
  const dispatch = useAppDispatch();
  const { t } = useTranslation();
  const tag = `${relation.task.team?.tag}-${relation.task.teamTagNo}`;
  const [deleteTaskRelation, { isSuccess: isDeleteSuccess, isError: isDeleteError }] = useDeleteTaskRelationMutation();
  const { isMobile } = useWindowSize();

  const popChangeWorkflowStatusModal = () => {
    dispatch(popChangeTaskWorkflowStatusModal({ visible: true, task: relation.task }));
  };

  useEffect(() => {
    dispatch(changeLoadingModalVisibility({ visible: false }));
  }, [isDeleteSuccess, isDeleteError]);

  const removeRelation = () => {
    dispatch(closeDialogModal());
    dispatch(changeLoadingModalVisibility({ visible: true }));
    deleteTaskRelation({ relationId: relation.taskRelationId });
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
    const task = relation.task;
    const workspaceName = task?.workspace?.username;
    const taskTag = `${task?.team?.tag}-${task?.teamTagNo}`;
    dispatch(popTaskOverviewModal({ taskTag, workspaceName, visible: true }));
  };

  return (
    <div className={styles.container}>
      <Button
        onClick={popChangeWorkflowStatusModal}
        heightVariant={ButtonHeight.short}
        variant={ButtonVariants.filled}
        className={styles.workflowStatusButton}
      >
        {groupIconMap?.[relation.task.workflowStatus.workflowStateGroup]}
      </Button>
      <div className="spacer-w-1" />
      <Link className={cn(styles.linkButton)} href={`/${relation.task.workspace?.username}/task/${tag}`} onClick={onLinkClick}>
        <div className="line-clamp">
          {tag} {relation.task.title}
        </div>
      </Link>

      <Button
        className={cn(styles.workflowStatusButton, styles.deleteButton)}
        variant={ButtonVariants.hoverFilled2}
        onClick={popAreYouSureModal}
        data-tooltip-right={t("taskRelationUnlink_dialogModalTitle")}
      >
        <IoClose size={ICON_SIZE * 0.8} />
      </Button>

      <div className="spacer-w-1" />
      <AssigneeCell task={relation.task} tooltipPosition={"right"} className={styles.workflowStatusButton} />
    </div>
  );
};

export default TaskRelationRow;
