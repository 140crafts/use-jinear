import Button, { ButtonHeight, ButtonVariants } from "@/components/button";
import TaskDetail from "@/components/taskDetail/TaskDetail";
import TaskDetailHeader from "@/components/taskDetail/taskDetailHeader/TaskDetailHeader";
import useWindowSize from "@/hooks/useWindowSize";
import { useRetrieveWithWorkspaceNameAndTeamTagNoQuery } from "@/store/api/taskApi";
import {
  closeTaskOverviewModal,
  selectTaskOverviewModalTaskTag,
  selectTaskOverviewModalVisible,
  selectTaskOverviewModalWorkspaceName,
} from "@/store/slice/modalSlice";
import { useAppDispatch, useTypedSelector } from "@/store/store";
import { CircularProgress } from "@mui/material";
import useTranslation from "locales/useTranslation";
import React from "react";
import Modal from "../modal/Modal";
import styles from "./TaskOverviewModal.module.scss";

interface TaskOverviewModalProps {}

const TaskOverviewModal: React.FC<TaskOverviewModalProps> = ({}) => {
  const { t } = useTranslation();
  const dispatch = useAppDispatch();
  const visible = useTypedSelector(selectTaskOverviewModalVisible);
  const { isMobile } = useWindowSize();
  const taskTag = useTypedSelector(selectTaskOverviewModalTaskTag);
  const workspaceName = useTypedSelector(selectTaskOverviewModalWorkspaceName);

  const {
    data: taskResponse,
    isLoading: isTaskResponseLoading,
    isSuccess: isTaskResponseSuccess,
  } = useRetrieveWithWorkspaceNameAndTeamTagNoQuery(
    { workspaceName: workspaceName || "", taskTag: taskTag || "" },
    { skip: workspaceName == null || taskTag == null }
  );

  const close = () => {
    dispatch(closeTaskOverviewModal());
  };

  const onGoToTaskClick = () => {
    close();
  };

  return (
    <Modal
      visible={visible}
      width={isMobile ? "fullscreen" : "xxlarge"}
      title={taskTag ? `[${taskTag}] ${taskResponse?.data.title || ""}` : ""}
      hasTitleCloseButton={true}
      requestClose={close}
      bodyClass={styles.modalBody}
      contentContainerClass={styles.modal}
    >
      {isTaskResponseLoading && (
        <div className={styles.loadingContainer}>
          <CircularProgress size={21} />
        </div>
      )}
      {isTaskResponseSuccess && (
        <div className={styles.taskContentWrapper}>
          <TaskDetailHeader task={taskResponse.data} />
          <TaskDetail task={taskResponse.data} />
        </div>
      )}

      {isTaskResponseSuccess && (
        <div className={styles.footer}>
          <Button
            heightVariant={ButtonHeight.short}
            variant={ButtonVariants.contrast}
            href={`/${taskResponse.data?.workspace?.username}/task/${taskResponse.data?.team?.tag}-${taskResponse.data?.teamTagNo}`}
            onClick={onGoToTaskClick}
          >
            <b>{t("taskOverviewModalGoToTask")}</b>
          </Button>
        </div>
      )}
    </Modal>
  );
};

export default TaskOverviewModal;
