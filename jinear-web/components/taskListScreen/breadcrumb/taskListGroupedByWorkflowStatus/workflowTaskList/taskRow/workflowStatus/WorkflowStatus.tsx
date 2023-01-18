import Button, { ButtonHeight, ButtonVariants } from "@/components/button";
import { TaskDto } from "@/model/be/jinear-core";
import { popChangeTaskWorkflowStatusModal } from "@/store/slice/modalSlice";
import { useAppDispatch } from "@/store/store";
import useTranslation from "locales/useTranslation";
import React from "react";
import {
  IoCheckmarkCircle,
  IoCloseCircle,
  IoContrast,
  IoEllipseOutline,
  IoPauseCircleOutline,
} from "react-icons/io5";
import styles from "./WorkflowStatus.module.css";

interface WorkflowStatusProps {
  task: TaskDto;
}

const groupIconMap = {
  BACKLOG: <IoPauseCircleOutline size={20} />,
  NOT_STARTED: <IoEllipseOutline size={20} />,
  STARTED: <IoContrast size={20} />,
  COMPLETED: <IoCheckmarkCircle size={20} />,
  CANCELLED: <IoCloseCircle size={20} />,
};

const WorkflowStatus: React.FC<WorkflowStatusProps> = ({ task }) => {
  const { t } = useTranslation();
  const dispatch = useAppDispatch();

  const popChangeWorkflowStatusModal = () => {
    dispatch(popChangeTaskWorkflowStatusModal({ visible: true, task }));
  };

  return (
    <Button
      onClick={popChangeWorkflowStatusModal}
      variant={ButtonVariants.filled}
      heightVariant={ButtonHeight.short}
      className={styles.container}
      data-tooltip-right={t("taskDetailChangeWorkflowStatusTooltip")}
    >
      {groupIconMap?.[task.workflowStatus.workflowStateGroup]}
    </Button>
  );
};

export default WorkflowStatus;
