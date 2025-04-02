import Button, { ButtonHeight, ButtonVariants } from "@/components/button";
import ProfilePhoto from "@/components/profilePhoto";
import { popChangeTaskAssigneeModal } from "@/store/slice/modalSlice";
import { useAppDispatch } from "@/store/store";
import useTranslation from "locales/useTranslation";
import React from "react";
import { useTask } from "../../context/TaskDetailContext";
import styles from "./ChangeAssigneeButton.module.css";

interface ChangeAssigneeButtonProps {
  className?: string;
}

const ChangeAssigneeButton: React.FC<ChangeAssigneeButtonProps> = ({ className }) => {
  const task = useTask();
  const { t } = useTranslation();
  const dispatch = useAppDispatch();

  const popChangeAssigneeModal = () => {
    dispatch(popChangeTaskAssigneeModal({ visible: true, task }));
  };

  return (
    <Button
      variant={ButtonVariants.filled}
      heightVariant={ButtonHeight.short}
      className={className}
      onClick={popChangeAssigneeModal}
    >
      {task.assignedToAccount ? (
        <>
          <div>{t("taskDetailAssignedTo")}</div>
          <ProfilePhoto
            boringAvatarKey={task.assignedToAccount.accountId}
            url={task.assignedToAccount.profilePicture?.url}
            wrapperClassName={styles.profilePic}
          />
          <b>{task.assignedToAccount.username}</b>
        </>
      ) : (
        <>{t("taskDetailAssignToAccount")}</>
      )}
    </Button>
  );
};

export default ChangeAssigneeButton;
