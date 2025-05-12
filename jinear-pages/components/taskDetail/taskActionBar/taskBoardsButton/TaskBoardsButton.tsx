import Button, { ButtonHeight, ButtonVariants } from "@/components/button";
import { popTaskTaskBoardAssignModal } from "@/store/slice/modalSlice";
import { useAppDispatch } from "@/store/store";
import cn from "classnames";
import useTranslation from "locales/useTranslation";
import React from "react";
import { useTask } from "../../context/TaskDetailContext";
import styles from "./TaskBoardsButton.module.css";

interface TaskBoardsButtonProps {
  className: string;
}

const TaskBoardsButton: React.FC<TaskBoardsButtonProps> = ({ className }) => {
  const { t } = useTranslation();
  const dispatch = useAppDispatch();
  const task = useTask();

  const popBoardsModal = () => {
    dispatch(popTaskTaskBoardAssignModal({ taskId: task.taskId, visible: true }));
  };

  return (
    <Button
      className={cn(styles.subscribeButton, className)}
      variant={ButtonVariants.filled}
      heightVariant={ButtonHeight.short}
      onClick={popBoardsModal}
      data-tooltip-right={t("taskBoardsActionButtonTooltip")}
    >
      {t("taskBoardsActionButtonLabel")}
    </Button>
  );
};

export default TaskBoardsButton;
