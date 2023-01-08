import Button, { ButtonHeight, ButtonVariants } from "@/components/button";
import { useToggle } from "@/hooks/useToggle";
import { useUpdateTaskTitleMutation } from "@/store/api/taskUpdateApi";
import { CircularProgress } from "@mui/material";
import cn from "classnames";
import useTranslation from "locales/useTranslation";
import React, { ChangeEvent, useEffect, useRef, useState } from "react";
import toast from "react-hot-toast";
import styles from "./TaskTitle.module.css";

interface TaskTitleProps {
  taskId: string;
  title: string;
}

const TaskTitle: React.FC<TaskTitleProps> = ({ taskId, title }) => {
  const { t } = useTranslation();
  const [taskTitle, setTaskTitle] = useState(title);
  const textAreaRef = useRef<HTMLTextAreaElement>(null);
  const titleInputRef = useRef<HTMLDivElement>(null);
  const { current: readOnly, toggle: toggleReadOnly } = useToggle(true);
  const [
    updateTaskTitle,
    {
      isLoading: isUpdateTaskTitleLoading,
      isSuccess: isUpdateTaskTitleSuccess,
    },
  ] = useUpdateTaskTitleMutation();

  useEffect(() => {
    if (titleInputRef.current) {
      titleInputRef.current.innerText = title;
    }
    // setTaskTitle(title);
  }, [title]);

  useEffect(() => {
    if (!readOnly) {
      setTimeout(() => {
        textAreaRef.current?.focus();
        titleInputRef.current?.focus();
      }, 250);
    }
  }, [readOnly]);

  const toggle = () => {
    const taskTitle = titleInputRef.current?.innerText || "";
    if (taskTitle.split(" ").join("").length < 3) {
      toast(t("taskTitleTooShort"));
      textAreaRef.current?.focus();
      return;
    }
    toggleReadOnly();
    if (!readOnly) {
      save();
    }
  };

  const cancel = () => {
    if (titleInputRef.current) {
      titleInputRef.current.innerText = title;
    }
    toggleReadOnly();
  };

  const save = () => {
    if (titleInputRef.current) {
      const req = {
        taskId,
        body: {
          title: titleInputRef.current.innerText,
        },
      };
      updateTaskTitle(req);
    }
  };

  const onTitleChange = (e: ChangeEvent<HTMLTextAreaElement>) => {
    const value = e.target.value?.replace?.(/\n/g, "");
    setTaskTitle(value);
  };

  return (
    <div
      className={cn(styles.container, readOnly && styles["contaier-readonly"])}
      onClick={readOnly ? toggle : undefined}
    >
      <div
        className={cn(
          styles.actionContainer,
          !readOnly && styles.marginTop,
          (!readOnly || isUpdateTaskTitleLoading) &&
            styles.actionContainerVisible
        )}
      >
        {isUpdateTaskTitleLoading && (
          <div className={styles.loadingContainer}>
            <CircularProgress size={14} />
            <div>{t("taskDescriptionSaving")}</div>
          </div>
        )}
        {!readOnly && (
          <Button
            disabled={isUpdateTaskTitleLoading}
            loading={isUpdateTaskTitleLoading}
            variant={
              readOnly ? ButtonVariants.filled2 : ButtonVariants.contrast
            }
            heightVariant={ButtonHeight.mid}
            onClick={toggle}
          >
            {t("taskDescriptionSave")}
          </Button>
        )}
        {!readOnly && (
          <Button
            disabled={isUpdateTaskTitleLoading}
            variant={ButtonVariants.filled2}
            heightVariant={ButtonHeight.mid}
            onClick={cancel}
          >
            {t("taskDescriptionCancel")}
          </Button>
        )}
      </div>
      {/* <textarea
        ref={textAreaRef}
        disabled={readOnly}
        className={cn(styles.titleBase, readOnly && styles.title)}
        value={taskTitle}
        onChange={onTitleChange}
      /> */}
      <div
        ref={titleInputRef}
        contentEditable={!readOnly}
        className={cn(styles.titleBase, readOnly && styles.title)}
      >
        {taskTitle}
      </div>
    </div>
  );
};

export default TaskTitle;
