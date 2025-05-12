import React, { useEffect, useRef, useState } from "react";
import styles from "./ProjectDescription.module.scss";
import cn from "classnames";
import Tiptap, { ITiptapRef } from "@/components/tiptap/Tiptap";
import Button, { ButtonHeight, ButtonVariants } from "@/components/button";
import { LuPencil } from "react-icons/lu";
import { CircularProgress } from "@mui/material";
import { RichTextDto } from "@/be/jinear-core";
import useTranslation from "@/locals/useTranslation";
import { useToggle } from "@/hooks/useToggle";
import { useUpdateProjectDescriptionMutation } from "@/api/projectOperationApi";

interface ProjectDescriptionProps {
  projectId: string;
  description?: RichTextDto | null;
  isFetching?: boolean;
}

const ProjectDescription: React.FC<ProjectDescriptionProps> = ({ projectId, description, isFetching }) => {
  const { t } = useTranslation();
  const [readOnly, toggleReadOnly] = useToggle(true);
  const [initialValue, setInitialValue] = useState(description?.value);
  const [updateProjectDescription, {
    isSuccess: isUpdateSuccess,
    isLoading: isUpdateLoading
  }] = useUpdateProjectDescriptionMutation();
  const tiptapRef = useRef<ITiptapRef>(null);

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
        projectId,
        body: {
          description: value
        }
      };
      updateProjectDescription(req);
    }
  };

  const toggle = () => {
    toggleReadOnly();
    if (!readOnly) {
      save();
    } else {
      tiptapRef?.current?.focus();
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
        ref={tiptapRef}
        content={initialValue}
        editable={!readOnly}
        placeholder={t("projectDetailPageTaskDescription")}
        htmlInputId={`${description?.richTextId}`}
      />
      {readOnly && (
        <Button
          disabled={isFetching}
          onClick={toggle}
          className={styles.editButton}
          variant={ButtonVariants.contrast}
          heightVariant={ButtonHeight.short}
          data-tooltip-right={t("projectDescriptionEdit")}
        >
          <LuPencil />
        </Button>
      )}

      <div className={styles.actionContainer}>
        {isUpdateLoading && (
          <div className={styles.loadingContainer}>
            <CircularProgress size={14} />
            <div>{t("projectDescriptionSaving")}</div>
          </div>
        )}

        {!readOnly && (
          <Button
            disabled={isUpdateLoading || isFetching}
            loading={isUpdateLoading}
            heightVariant={ButtonHeight.mid}
            variant={readOnly ? ButtonVariants.filled2 : ButtonVariants.contrast}
            onClick={toggle}
          >
            {t("projectDescriptionSave")}
          </Button>
        )}
        {!readOnly && (
          <Button disabled={isUpdateLoading || isFetching} heightVariant={ButtonHeight.mid}
                  variant={ButtonVariants.filled2}
                  onClick={cancel}>
            {t("projectDescriptionCancel")}
          </Button>
        )}
      </div>
    </div>
  );
};

export default ProjectDescription;