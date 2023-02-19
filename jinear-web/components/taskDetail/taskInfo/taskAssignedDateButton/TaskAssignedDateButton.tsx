import Button, { ButtonHeight, ButtonVariants } from "@/components/button";
import { popChangeTaskDateModal } from "@/store/slice/modalSlice";
import { useAppDispatch } from "@/store/store";
import { format, getHours, getMinutes } from "date-fns";
import useTranslation from "locales/useTranslation";
import React from "react";
import { useTask } from "../../context/TaskDetailContext";
import styles from "./TaskAssignedDateButton.module.css";

interface TaskAssignedDateButtonProps {}

const TaskAssignedDateButton: React.FC<TaskAssignedDateButtonProps> = ({}) => {
  const { t } = useTranslation();
  const task = useTask();
  const assignedDate = new Date(task.assignedDate);
  const hasTime = getHours(assignedDate) != 0 || getMinutes(assignedDate);
  const dispatch = useAppDispatch();

  const popChangeAssignedDateModal = () => {
    dispatch(popChangeTaskDateModal({ visible: true, task }));
  };

  return (
    <Button
      variant={ButtonVariants.filled}
      heightVariant={ButtonHeight.mid}
      className={styles.button}
      onClick={popChangeAssignedDateModal}
    >
      {task.assignedDate ? (
        <>
          {t("taskDetailAssignedDate")}
          <b>
            {format(
              assignedDate,
              hasTime ? t("dateTimeFormat") : t("dateFormat")
            )}
          </b>
        </>
      ) : (
        <>{t("taskDetailAddAssignedDate")}</>
      )}
    </Button>
  );
};

export default TaskAssignedDateButton;
