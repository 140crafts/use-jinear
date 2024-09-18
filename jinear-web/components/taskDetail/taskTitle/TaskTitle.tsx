import Button, { ButtonHeight, ButtonVariants } from "@/components/button";
import { useUpdateTaskTitleMutation } from "@/store/api/taskUpdateApi";
import { closeBasicTextInputModal, popBasicTextInputModal } from "@/store/slice/modalSlice";
import { useAppDispatch } from "@/store/store";
import { CircularProgress } from "@mui/material";
import useTranslation from "locales/useTranslation";
import React, { useEffect, useState } from "react";
import { LuPencil } from "react-icons/lu";
import styles from "./TaskTitle.module.scss";

interface TaskTitleProps {
  taskId: string;
  title: string;
}

const TaskTitle: React.FC<TaskTitleProps> = ({ taskId, title }) => {
  const { t } = useTranslation();
  const dispatch = useAppDispatch();
  const [taskTitle, setTaskTitle] = useState(title);
  const [updateTaskTitle, { isLoading: isUpdateTaskTitleLoading, isSuccess: isUpdateTaskTitleSuccess }] =
    useUpdateTaskTitleMutation();

  useEffect(() => {
    setTaskTitle(title);
  }, [title]);

  const changeTitle = (title: string) => {
    dispatch(closeBasicTextInputModal());
    const req = {
      taskId,
      body: { title }
    };
    updateTaskTitle(req);
    setTaskTitle(title);
  };

  const popTitleChangeModal = () => {
    dispatch(
      popBasicTextInputModal({
        visible: true,
        title: t("taskTitleChangeModalTitle"),
        infoText: t("taskTitleChangeModalInfoText"),
        initialText: title,
        onSubmit: changeTitle
      })
    );
  };

  return (
    <div className={styles.container}>
      <h1>
        <b>{taskTitle}</b>
      </h1>
      {isUpdateTaskTitleLoading &&
        <span className={styles.savingLabel}>
          <CircularProgress size={12} className={styles.loading} />
          {t("taskDescriptionSaving")}
      </span>}
      {!isUpdateTaskTitleLoading && (
        <Button
          disabled={isUpdateTaskTitleLoading}
          heightVariant={ButtonHeight.short}
          variant={ButtonVariants.contrast}
          className={styles.editTitleButton}
          onClick={popTitleChangeModal}
        >
          <LuPencil />
        </Button>
      )}
    </div>
  );
};

export default TaskTitle;
