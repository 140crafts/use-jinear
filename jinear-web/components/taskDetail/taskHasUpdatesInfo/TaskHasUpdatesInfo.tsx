import Button, { ButtonHeight, ButtonVariants } from "@/components/button";
import { changeLoadingModalVisibility } from "@/store/slice/modalSlice";
import { clearHasUnreadNotification, selectTaskAdditionalData } from "@/store/slice/taskAdditionalDataSlice";
import { useAppDispatch, useTypedSelector } from "@/store/store";
import useTranslation from "locales/useTranslation";
import { useRouter } from "next/router";
import React from "react";
import { IoClose, IoWarning } from "react-icons/io5";
import { useTask } from "../context/TaskDetailContext";
import styles from "./TaskHasUpdatesInfo.module.css";

interface TaskHasUpdatesInfoProps {}

const TaskHasUpdatesInfo: React.FC<TaskHasUpdatesInfoProps> = ({}) => {
  const { t } = useTranslation();
  const router = useRouter();
  const dispatch = useAppDispatch();

  const task = useTask();
  const additionalData = useTypedSelector(selectTaskAdditionalData(task.taskId));

  const refreshPage = () => {
    dispatch(changeLoadingModalVisibility({ visible: true }));
    router.reload();
  };

  const clearHasUpdatesOnTask = () => {
    dispatch(clearHasUnreadNotification({ taskId: task.taskId }));
  };

  return additionalData?.hasUpdates ? (
    <div className={styles.container}>
      <div className={styles.infoContainer}>
        <IoWarning size={18} />
        {t("taskHasNewUpdates")}
        <div className="flex-1" />
        <Button className={styles.dismissButton} heightVariant={ButtonHeight.short} onClick={clearHasUpdatesOnTask}>
          <IoClose size={14} />
        </Button>
      </div>
      <div className={styles.actionContainer}>
        <Button
          className={styles.refreshButton}
          variant={ButtonVariants.filled2}
          heightVariant={ButtonHeight.short}
          onClick={refreshPage}
        >
          {t("taskHasNewUpdatesRefreshPage")}
        </Button>
      </div>
    </div>
  ) : null;
};

export default TaskHasUpdatesInfo;
