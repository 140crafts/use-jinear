import Button, { ButtonVariants } from "@/components/button";
import { TaskDto } from "@/model/be/jinear-core";
import { popChangeTaskWorkflowStatusModal } from "@/store/slice/modalSlice";
import { useAppDispatch } from "@/store/store";
import useTranslation from "locales/useTranslation";
import React from "react";
import { IoCheckmarkCircle, IoCloseCircle, IoContrast, IoEllipseOutline, IoPauseCircleOutline } from "react-icons/io5";
import styles from "./WorkflowStatus.module.css";

interface WorkflowStatusProps {
  task: TaskDto;
  buttonVariant?: string;
}

const groupIconMap = {
  BACKLOG: <IoPauseCircleOutline size={17} />,
  NOT_STARTED: <IoEllipseOutline size={17} />,
  STARTED: <IoContrast size={17} />,
  COMPLETED: <IoCheckmarkCircle size={17} />,
  CANCELLED: <IoCloseCircle size={17} />,
};

const WorkflowStatus: React.FC<WorkflowStatusProps> = ({ task, buttonVariant }) => {
  const { t } = useTranslation();
  const dispatch = useAppDispatch();

  const popChangeWorkflowStatusModal = () => {
    dispatch(popChangeTaskWorkflowStatusModal({ visible: true, task }));
  };

  return (
    <Button
      onClick={popChangeWorkflowStatusModal}
      variant={buttonVariant ? buttonVariant : ButtonVariants.filled}
      className={styles.container}
      data-tooltip={t("taskDetailChangeWorkflowStatusTooltip")}
    >
      <div className={styles.iconContainer}>{groupIconMap?.[task.workflowStatus.workflowStateGroup]}</div>
    </Button>
  );
};

export default WorkflowStatus;
