import Button, { ButtonHeight, ButtonVariants } from "@/components/button";
import { useUpdateTaskTitleMutation } from "@/store/api/taskUpdateApi";
import { closeBasicTextInputModal, popBasicTextInputModal } from "@/store/slice/modalSlice";
import { useAppDispatch } from "@/store/store";
import { CircularProgress } from "@mui/material";
import useTranslation from "locales/useTranslation";
import React, { useState } from "react";
import { IoPencil } from "react-icons/io5";
import styles from "./TaskTitle.module.css";

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

  const changeTitle = (title: string) => {
    dispatch(closeBasicTextInputModal());
    const req = {
      taskId,
      body: { title },
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
        onSubmit: changeTitle,
      })
    );
  };

  return (
    <div className={styles.container}>
      <h1>
        <b>{taskTitle}</b>
      </h1>
      {isUpdateTaskTitleLoading && <CircularProgress size={16} />}
      {isUpdateTaskTitleLoading && <span>{t("taskDescriptionSaving")}</span>}
      {!isUpdateTaskTitleLoading && (
        <Button
          disabled={isUpdateTaskTitleLoading}
          heightVariant={ButtonHeight.short}
          variant={ButtonVariants.filled2}
          className={styles.editTitleButton}
          onClick={popTitleChangeModal}
        >
          <IoPencil />
        </Button>
      )}
    </div>
  );
};

export default TaskTitle;
