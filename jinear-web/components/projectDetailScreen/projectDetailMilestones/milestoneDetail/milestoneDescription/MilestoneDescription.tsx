import React, { useEffect, useRef, useState } from "react";
import styles from "./MilestoneDescription.module.scss";
import cn from "classnames";
import Tiptap, { ITiptapRef } from "@/components/tiptap/Tiptap";
import Button, { ButtonHeight, ButtonVariants } from "@/components/button";
import { LuPencil } from "react-icons/lu";
import { CircularProgress } from "@mui/material";
import { RichTextDto } from "@/be/jinear-core";
import useTranslation from "@/locals/useTranslation";
import { useToggle } from "@/hooks/useToggle";
import { useUpdateMilestoneDescriptionMutation } from "@/api/projectMilestoneApi";

interface MilestoneDescriptionProps {
  milestoneId: string;
  description?: RichTextDto | null;
  isFetching?: boolean;
}

const MilestoneDescription: React.FC<MilestoneDescriptionProps> = ({ milestoneId, description, isFetching }) => {
  const { t } = useTranslation();
  const [readOnly, toggleReadOnly] = useToggle(true);
  const [initialValue, setInitialValue] = useState(description?.value);
  const [updateMilestoneDescription, {
    isSuccess: isUpdateSuccess,
    isLoading: isUpdateLoading
  }] = useUpdateMilestoneDescriptionMutation();
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
        milestoneId,
        body: {
          description: value
        }
      };
      updateMilestoneDescription(req);
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
        placeholder={t("projectDetailPageMilestoneDescription")}
        htmlInputId={`${description?.richTextId}`}
      />
      {readOnly && (
        <Button
          disabled={isFetching}
          onClick={toggle}
          className={styles.editButton}
          heightVariant={ButtonHeight.short}
          variant={ButtonVariants.contrast}
          data-tooltip-right={t("projectDetailPageMilestoneDescriptionEdit")}
        >
          <LuPencil />
        </Button>
      )}

      <div className={styles.actionContainer}>
        {isUpdateLoading && (
          <div className={styles.loadingContainer}>
            <CircularProgress size={14} />
            <div>{t("projectDetailPageDescriptionSaving")}</div>
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
            {t("projectDetailPageMilestoneDescriptionSave")}
          </Button>
        )}
        {!readOnly && (
          <Button disabled={isUpdateLoading || isFetching} heightVariant={ButtonHeight.mid}
                  variant={ButtonVariants.filled2}
                  onClick={cancel}>
            {t("projectDetailPageMilestoneDescriptionCancel")}
          </Button>
        )}
      </div>
    </div>
  );
};

export default MilestoneDescription;