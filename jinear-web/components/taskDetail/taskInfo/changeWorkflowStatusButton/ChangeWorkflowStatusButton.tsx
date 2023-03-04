import Button, { ButtonHeight, ButtonVariants } from "@/components/button";
import { popChangeTaskWorkflowStatusModal } from "@/store/slice/modalSlice";
import { useAppDispatch } from "@/store/store";
import useTranslation from "locales/useTranslation";
import React from "react";
import { IoCheckmarkCircle, IoCloseCircle, IoContrast, IoEllipseOutline, IoPauseCircleOutline } from "react-icons/io5";
import { useTask } from "../../context/TaskDetailContext";

interface ChangeWorkflowStatusButtonProps {
  className: string;
}

const groupIconMap = {
  BACKLOG: <IoPauseCircleOutline size={20} />,
  NOT_STARTED: <IoEllipseOutline size={20} />,
  STARTED: <IoContrast size={20} />,
  COMPLETED: <IoCheckmarkCircle size={20} />,
  CANCELLED: <IoCloseCircle size={20} />,
};

const ChangeWorkflowStatusButton: React.FC<ChangeWorkflowStatusButtonProps> = ({ className }) => {
  const task = useTask();
  const { t } = useTranslation();
  const dispatch = useAppDispatch();

  const popChangeWorkflowStatusModal = () => {
    dispatch(popChangeTaskWorkflowStatusModal({ visible: true, task }));
  };

  return (
    <Button
      onClick={popChangeWorkflowStatusModal}
      variant={ButtonVariants.filled}
      heightVariant={ButtonHeight.mid}
      className={className}
      data-tooltip-right={t("taskDetailChangeWorkflowStatusTooltip")}
    >
      {groupIconMap?.[task.workflowStatus.workflowStateGroup]}
      {task.workflowStatus.name}
    </Button>
  );
};

export default ChangeWorkflowStatusButton;
