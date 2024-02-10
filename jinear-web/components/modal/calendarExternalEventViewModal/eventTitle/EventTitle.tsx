import Button, { ButtonHeight, ButtonVariants } from "@/components/button";
import { closeBasicTextInputModal, popBasicTextInputModal } from "@/store/slice/modalSlice";
import { useAppDispatch } from "@/store/store";
import useTranslation from "locales/useTranslation";
import React, { useEffect, useState } from "react";
import { LuPencil } from "react-icons/lu";
import styles from "./EventTitle.module.css";

interface EventTitleProps {
  title?: string;
}

const EventTitle: React.FC<EventTitleProps> = ({ title }) => {
  const { t } = useTranslation();
  const dispatch = useAppDispatch();
  const [taskTitle, setTaskTitle] = useState(title);

  useEffect(() => {
    setTaskTitle(title);
  }, [title]);

  const changeTitle = (title: string) => {
    dispatch(closeBasicTextInputModal());
    alert(`TODO: new title ${title}`);
  };

  const popTitleChangeModal = () => {
    dispatch(
      popBasicTextInputModal({
        visible: true,
        title: t("calendarEventTitleChangeModalTitle"),
        infoText: t("calendarEventTitleChangeModalInfoText"),
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
      {/* {isUpdateTaskTitleLoading && <CircularProgress size={16} />} */}
      {/* {isUpdateTaskTitleLoading && <span>{t("taskDescriptionSaving")}</span>} */}
      {/* {!isUpdateTaskTitleLoading && ( */}
      <Button
        //   disabled={isUpdateTaskTitleLoading}
        heightVariant={ButtonHeight.short}
        variant={ButtonVariants.filled}
        className={styles.editTitleButton}
        onClick={popTitleChangeModal}
      >
        <LuPencil />
      </Button>
      {/* )} */}
    </div>
  );
};

export default EventTitle;
