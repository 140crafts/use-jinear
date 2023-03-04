import Button, { ButtonHeight, ButtonVariants } from "@/components/button";
import { popReminderListModal } from "@/store/slice/modalSlice";
import { useAppDispatch } from "@/store/store";
import useTranslation from "locales/useTranslation";
import React from "react";
import { useTask } from "../../context/TaskDetailContext";

interface RemindersButtonProps {
  className?: string;
}

const RemindersButton: React.FC<RemindersButtonProps> = ({ className }) => {
  const { t } = useTranslation();
  const dispatch = useAppDispatch();
  const task = useTask();
  const reminders = task.taskReminders || [];
  const additionalLabel = (reminders?.length || 0) > 0 ? ` (${reminders?.length})` : "";

  const popRemindersModal = () => {
    dispatch(popReminderListModal({ visible: true, task }));
  };

  return (
    <Button className={className} variant={ButtonVariants.filled} heightVariant={ButtonHeight.mid} onClick={popRemindersModal}>
      {t("taskDetailRemindersButtonLabel") + additionalLabel}
    </Button>
  );
};

export default RemindersButton;
