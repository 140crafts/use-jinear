import Button, { ButtonHeight, ButtonVariants } from "@/components/button";
import Tiptap, { ITiptapRef } from "@/components/tiptap/Tiptap";
import { useToggle } from "@/hooks/useToggle";
import { RichTextDto } from "@/model/be/jinear-core";
import useTranslation from "locales/useTranslation";
import React, { useRef, useState } from "react";
import { LuPencil } from "react-icons/lu";
import styles from "./EventDescription.module.css";

interface EventDescriptionProps {
  description?: RichTextDto | null;
}

const EventDescription: React.FC<EventDescriptionProps> = ({ description }) => {
  const { t } = useTranslation();
  const [readOnly, toggleReadOnly] = useToggle(true);
  const [initialValue, setInitialValue] = useState(description?.value);
  // const [updateTaskDescription, { isSuccess: isUpdateSuccess, isLoading: isUpdateLoading }] = useUpdateTaskDescriptionMutation();
  const tiptapRef = useRef<ITiptapRef>(null);

  const save = () => {
    const input: HTMLInputElement | null = document.getElementById(`${description?.richTextId}`) as HTMLInputElement;
    if (input) {
      const value = input?.value || "";
      const req = {
        // taskId,
        body: {
          description: value,
        },
      };
      // TODO
      //   updateTaskDescription(req);
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
    <div className={styles.container}>
      <Tiptap
        ref={tiptapRef}
        content={initialValue}
        editable={!readOnly}
        placeholder={t("calendarEventDetailDescription")}
        htmlInputId={`${description?.richTextId}`}
        actionBarMode={"simple"}
      />
      {readOnly && (
        <Button
          onClick={toggle}
          className={styles.editButton}
          variant={ButtonVariants.filled2}
          data-tooltip-right={t("calendarEventDetailDescriptionEdit")}
        >
          <LuPencil />
        </Button>
      )}

      <div className={styles.actionContainer}>
        {/* {isUpdateLoading && (
          <div className={styles.loadingContainer}>
            <CircularProgress size={14} />
            <div>{t("taskDescriptionSaving")}</div>
          </div>
        )} */}

        {!readOnly && (
          <Button
            // disabled={isUpdateLoading}
            // loading={isUpdateLoading}
            heightVariant={ButtonHeight.mid}
            variant={readOnly ? ButtonVariants.filled2 : ButtonVariants.contrast}
            onClick={toggle}
          >
            {t("calendarEventDetailDescriptionSave")}
          </Button>
        )}
        {!readOnly && (
          <Button
            //    disabled={isUpdateLoading}
            heightVariant={ButtonHeight.mid}
            variant={ButtonVariants.filled2}
            onClick={cancel}
          >
            {t("calendarEventDetailDescriptionCancel")}
          </Button>
        )}
      </div>
    </div>
  );
};

export default EventDescription;
