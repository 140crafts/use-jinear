import Button, { ButtonHeight, ButtonVariants } from "@/components/button";
import { clearHasUnreadNotification, selectTaskAdditionalData } from "@/store/slice/taskAdditionalDataSlice";
import { useAppDispatch, useTypedSelector } from "@/store/store";
import useTranslation from "locales/useTranslation";
import { useRouter } from "next/navigation";
import React from "react";
import { IoWarning } from "react-icons/io5";
import { useTask } from "../context/TaskDetailContext";
import styles from "./TaskHasUpdatesInfo.module.css";
import { api } from "@/api/api";

interface TaskHasUpdatesInfoProps {
}

const TaskHasUpdatesInfo: React.FC<TaskHasUpdatesInfoProps> = ({}) => {
  const { t } = useTranslation();
  const router = useRouter();
  const dispatch = useAppDispatch();

  const task = useTask();
  const additionalData = useTypedSelector(selectTaskAdditionalData(task.taskId));

  const refreshPage = () => {
    dispatch(api.util.invalidateTags(["v1/task/from-workspace/{workspaceName}/{taskTag}"]));
    dispatch(clearHasUnreadNotification({ taskId: task.taskId }));
  };

  const clearHasUpdatesOnTask = () => {
    dispatch(clearHasUnreadNotification({ taskId: task.taskId }));
  };

  return additionalData?.hasUpdates ? (
    <div className={styles.container}>
      <div className={styles.infoContainer}>
        <IoWarning size={18} />
        {t("taskHasNewUpdates")}
      </div>
      <div className={styles.actionContainer}>
        <Button className={styles.dismissButton} heightVariant={ButtonHeight.short} onClick={clearHasUpdatesOnTask}>
          {t("taskHasNewUpdatesDismiss")}
        </Button>
        <div className={"spacer-w-2"} />
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
