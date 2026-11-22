import Button, { ButtonVariants } from "@/components/button";
import type { TaskDto } from "@/model/be/jinear-core";
import { popChangeTaskWorkflowStatusModal } from "@/store/slice/modalSlice";
import { useAppDispatch } from "@/store";
import useTranslation from "@/locales/useTranslation";
import React from "react";
import cn from "classnames";
import { IoCheckmarkCircle, IoCloseCircle, IoContrast, IoEllipseOutline, IoPauseCircleOutline } from "react-icons/io5";
import styles from "./WorkflowStatus.module.css";

interface WorkflowStatusProps {
  task: TaskDto;
  buttonVariant?: string;
  readOnly?: boolean;
}

const groupIconMap = {
  BACKLOG: <IoPauseCircleOutline size={17} />,
  NOT_STARTED: <IoEllipseOutline size={17} />,
  STARTED: <IoContrast size={17} />,
  COMPLETED: <IoCheckmarkCircle size={17} />,
  CANCELLED: <IoCloseCircle size={17} />,
};

const WorkflowStatus: React.FC<WorkflowStatusProps> = ({ task, buttonVariant, readOnly = false }) => {
  const { t } = useTranslation();
  const dispatch = useAppDispatch();

  const popChangeWorkflowStatusModal = () => {
    dispatch(popChangeTaskWorkflowStatusModal({ visible: true, task }));
  };

  const icon = <div className={styles.iconContainer}>{groupIconMap?.[task.workflowStatus.workflowStateGroup]}</div>;

  if (readOnly) {
    return <div className={cn(styles.container, styles.readOnly)}>{icon}</div>;
  }

  return (
    <Button
      onClick={popChangeWorkflowStatusModal}
      variant={buttonVariant ? buttonVariant : ButtonVariants.filled}
      className={styles.container}
      data-tooltip={t("taskDetailChangeWorkflowStatusTooltip")}
    >
      {icon}
    </Button>
  );
};

export default WorkflowStatus;
