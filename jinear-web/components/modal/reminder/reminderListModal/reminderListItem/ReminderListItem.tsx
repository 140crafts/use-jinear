import Button, { ButtonVariants } from "@/components/button";
import { TaskReminderDto } from "@/model/be/jinear-core";
import { usePassivizeTaskReminderMutation, useRetrieveNextJobQuery } from "@/store/api/taskReminderApi";
import { changeLoadingModalVisibility } from "@/store/slice/modalSlice";
import { useAppDispatch } from "@/store/store";
import { CircularProgress } from "@mui/material";
import { format } from "date-fns";
import useTranslation from "locales/useTranslation";
import React, { useEffect } from "react";
import toast from "react-hot-toast";
import { IoClose } from "react-icons/io5";
import styles from "./ReminderListItem.module.css";

interface ReminderListItemProps {
  taskReminder: TaskReminderDto;
  close: () => void;
}

const ReminderListItem: React.FC<ReminderListItemProps> = ({ taskReminder, close }) => {
  const { t } = useTranslation();
  const dispatch = useAppDispatch();

  const {
    data: nextJobData,
    isLoading,
    isError,
  } = useRetrieveNextJobQuery({
    taskReminderId: taskReminder.taskReminderId,
  });

  const [passivizeTaskReminder, { isLoading: isPassivizeLoading, isError: isPassivizeError, isSuccess: isPassivizeSuccess }] =
    usePassivizeTaskReminderMutation();

  const deleteTaskReminder = () => {
    if (isPassivizeLoading) {
      return;
    }
    dispatch(changeLoadingModalVisibility({ visible: true }));
    passivizeTaskReminder({ taskReminderId: taskReminder.taskReminderId });
  };

  useEffect(() => {
    if (!isPassivizeLoading && isPassivizeSuccess) {
      toast(t("genericSuccess"));
      dispatch(changeLoadingModalVisibility({ visible: false }));
      close();
    }
    if (!isPassivizeLoading && isPassivizeError) {
      dispatch(changeLoadingModalVisibility({ visible: false }));
    }
  }, [isPassivizeLoading, isPassivizeSuccess, isPassivizeError]);

  return (
    <div className={styles.container}>
      {t(`taskReminderType_${taskReminder.taskReminderType}`)}
      <div className="flex-1" />
      <div className={styles.repeatInfo}>
        {taskReminder?.reminder?.repeatType != "NONE" &&
          `${t(`taskNewReminderModalReminderRepeatType_${taskReminder.reminder.repeatType}`)} ${t(
            "reminderListItemRepatInfoLabel"
          )}`}
      </div>
      <div className="spacer-w-1" />
      <div className={styles.nextJobDate}>
        {isLoading && <CircularProgress size={7} />}
        {/* TODO cgds-73 */}
        {nextJobData?.data.date && format(new Date(nextJobData?.data.date), t("dateTimeFormat"))}
      </div>
      <div className="spacer-w-1" />
      <Button
        variant={ButtonVariants.hoverFilled}
        // loading={isPassivizeLoading}
        disabled={isPassivizeLoading}
        onClick={deleteTaskReminder}
      >
        <IoClose />
      </Button>
    </div>
  );
};

export default ReminderListItem;
