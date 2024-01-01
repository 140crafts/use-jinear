import Button, { ButtonHeight, ButtonVariants } from "@/components/button";
import Tiptap from "@/components/tiptap/Tiptap";
import { useToggle } from "@/hooks/useToggle";
import { RichTextDto } from "@/model/be/jinear-core";
import { useUpdateTaskDescriptionMutation } from "@/store/api/taskUpdateApi";
import Logger from "@/utils/logger";
import { CircularProgress } from "@mui/material";
import cn from "classnames";
import useTranslation from "locales/useTranslation";
import React, { useEffect, useState } from "react";
import { LuPencil } from "react-icons/lu";
import styles from "./TaskDescription.module.css";

interface TaskDescriptionProps {
  taskId: string;
  description?: RichTextDto | null;
}

const logger = Logger("TaskDescription");

const TaskDescription: React.FC<TaskDescriptionProps> = ({ taskId, description }) => {
  const { t } = useTranslation();
  const { current: readOnly, toggle: toggleReadOnly } = useToggle(true);
  const [initialValue, setInitialValue] = useState(description?.value);
  const [updateTaskDescription, { isSuccess: isUpdateSuccess, isLoading: isUpdateLoading }] = useUpdateTaskDescriptionMutation();

  useEffect(() => {
    if (description?.value) {
      setInitialValue(description?.value);
    }
  }, [description?.value]);

  const save = () => {
    const input: HTMLInputElement | null = document.getElementById(`${description?.richTextId}`) as HTMLInputElement;
    if (input) {
      const value = input?.value || "";
      const req = {
        taskId,
        body: {
          description: value,
        },
      };
      updateTaskDescription(req);
    }
  };

  const toggle = () => {
    toggleReadOnly();
    if (!readOnly) {
      save();
    }
  };

  const cancel = () => {
    toggleReadOnly();
    setInitialValue("");
    setTimeout(() => {
      setInitialValue(description?.value);
    }, 100);
  };

  return (
    <div className={cn(styles.container)}>
      <Tiptap
        content={initialValue}
        editable={!readOnly}
        placeholder={t("taskDetalPageTaskDescription")}
        htmlInputId={`${description?.richTextId}`}
      />
      {readOnly && (
        <Button
          onClick={toggle}
          className={styles.editButton}
          variant={ButtonVariants.filled2}
          data-tooltip-right={t("taskDescriptionEdit")}
        >
          <LuPencil />
        </Button>
      )}

      <div className={styles.actionContainer}>
        {isUpdateLoading && (
          <div className={styles.loadingContainer}>
            <CircularProgress size={14} />
            <div>{t("taskDescriptionSaving")}</div>
          </div>
        )}

        {!readOnly && (
          <Button
            disabled={isUpdateLoading}
            loading={isUpdateLoading}
            heightVariant={ButtonHeight.mid}
            variant={readOnly ? ButtonVariants.filled2 : ButtonVariants.contrast}
            onClick={toggle}
          >
            {t("taskDescriptionSave")}
          </Button>
        )}
        {!readOnly && (
          <Button disabled={isUpdateLoading} heightVariant={ButtonHeight.mid} variant={ButtonVariants.filled2} onClick={cancel}>
            {t("taskDescriptionCancel")}
          </Button>
        )}
      </div>
    </div>
  );
};

export default TaskDescription;
