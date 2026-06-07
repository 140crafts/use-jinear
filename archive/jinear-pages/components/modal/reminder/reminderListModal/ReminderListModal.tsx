import Button, { ButtonVariants } from "@/components/button";
import Line from "@/components/line/Line";
import {
  closeReminderListModal,
  popNewReminderModal,
  selectReminderListModalTask,
  selectReminderListModalVisible,
} from "@/store/slice/modalSlice";
import { useAppDispatch, useTypedSelector } from "@/store/store";
import useTranslation from "locales/useTranslation";
import React from "react";
import Modal from "../../modal/Modal";
import ReminderListItem from "./reminderListItem/ReminderListItem";
import styles from "./ReminderListModal.module.css";

interface ReminderListModalProps {}

const ReminderListModal: React.FC<ReminderListModalProps> = ({}) => {
  const { t } = useTranslation();
  const dispatch = useAppDispatch();
  const visible = useTypedSelector(selectReminderListModalVisible);
  const task = useTypedSelector(selectReminderListModalTask);
  const anyRemindersExists = task?.taskReminders?.length != 0;

  const close = () => {
    dispatch(closeReminderListModal());
  };

  const _popNewReminderModal = () => {
    dispatch(popNewReminderModal({ visible: true, task }));
    close();
  };

  return (
    <Modal
      visible={visible}
      title={t("taskReminderListModalTitle")}
      bodyClass={styles.container}
      hasTitleCloseButton={true}
      requestClose={close}
    >
      {!anyRemindersExists && <div>{t("taskReminderListModalNoRemindersExist")}</div>}

      <div className={styles.reminderList}>
        {task?.taskReminders?.map?.((taskReminder) => (
          <ReminderListItem key={taskReminder.taskReminderId} taskReminder={taskReminder} close={close} />
        ))}
      </div>

      <div className="spacer-h-2" />
      <Line />
      <div className="spacer-h-2" />

      <Button variant={ButtonVariants.contrast} onClick={_popNewReminderModal}>
        {t("taskReminderListModalNewReminder")}
      </Button>
    </Modal>
  );
};

export default ReminderListModal;
