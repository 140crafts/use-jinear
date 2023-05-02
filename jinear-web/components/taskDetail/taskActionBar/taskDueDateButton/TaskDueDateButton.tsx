import Button, { ButtonHeight, ButtonVariants } from "@/components/button";
import { popChangeTaskDateModal } from "@/store/slice/modalSlice";
import { useAppDispatch } from "@/store/store";
import { format } from "date-fns";
import useTranslation from "locales/useTranslation";
import React from "react";
import { useTask } from "../../context/TaskDetailContext";
import styles from "./TaskDueDateButton.module.css";

interface TaskDueDateButtonProps {}

const TaskDueDateButton: React.FC<TaskDueDateButtonProps> = ({}) => {
  const { t } = useTranslation();
  const task = useTask();
  const dueDate = new Date(task.dueDate);
  const dispatch = useAppDispatch();

  const popChangeDueDateModal = () => {
    dispatch(popChangeTaskDateModal({ visible: true, task }));
  };

  return (
    <Button
      variant={ButtonVariants.filled}
      heightVariant={ButtonHeight.short}
      className={styles.button}
      onClick={popChangeDueDateModal}
    >
      {task.dueDate ? (
        <>
          {t("taskDetailDueDate")}
          <b>{format(dueDate, task.hasPreciseDueDate ? t("dateTimeFormat") : t("dateFormat"))}</b>
        </>
      ) : (
        <>{t("taskDetailAddDueDate")}</>
      )}
    </Button>
  );
};

export default TaskDueDateButton;
