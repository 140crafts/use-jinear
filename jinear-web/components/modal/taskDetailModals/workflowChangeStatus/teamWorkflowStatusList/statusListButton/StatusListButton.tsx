import Button, { ButtonVariants } from "@/components/button";
import { TeamWorkflowStatusDto } from "@/model/be/jinear-core";
import { useUpdateTaskWorkflowStatusMutation } from "@/store/api/taskWorkflowStatusApi";

import {
  changeLoadingModalVisibility,
  closeChangeTaskWorkflowStatusModal,
  selectChangeTaskWorkflowStatusModalTask,
} from "@/store/slice/modalSlice";
import { useAppDispatch, useTypedSelector } from "@/store/store";
import useTranslation from "locales/useTranslation";
import React, { useEffect } from "react";
import { IoCheckmarkCircle, IoCloseCircle, IoContrast, IoEllipseOutline, IoPauseCircleOutline } from "react-icons/io5";
import styles from "./StatusListButton.module.css";
interface StatusListButtonProps {
  wfs: TeamWorkflowStatusDto;
}

const groupIconMap = {
  BACKLOG: <IoPauseCircleOutline size={20} />,
  NOT_STARTED: <IoEllipseOutline size={20} />,
  STARTED: <IoContrast size={20} />,
  COMPLETED: <IoCheckmarkCircle size={20} />,
  CANCELLED: <IoCloseCircle size={20} />,
};

const StatusListButton: React.FC<StatusListButtonProps> = ({ wfs }) => {
  const { t } = useTranslation();
  const dispatch = useAppDispatch();
  const task = useTypedSelector(selectChangeTaskWorkflowStatusModalTask);
  const currentWorkflowStatusId = task?.workflowStatus.teamWorkflowStatusId;

  const [updateTaskWorkflowStatus, { isLoading, isSuccess }] = useUpdateTaskWorkflowStatusMutation();

  const close = () => {
    dispatch(closeChangeTaskWorkflowStatusModal());
  };

  const change = () => {
    if (task) {
      dispatch(changeLoadingModalVisibility({ visible: true }));
      updateTaskWorkflowStatus({
        taskId: task?.taskId,
        workflowStatusId: wfs.teamWorkflowStatusId,
      });
    }
  };

  useEffect(() => {
    if (isSuccess) {
      close();
      dispatch(changeLoadingModalVisibility({ visible: false }));
    }
  }, [isSuccess]);

  return (
    <Button
      key={wfs.teamWorkflowStatusId}
      disabled={isLoading}
      className={styles.button}
      onClick={change}
      variant={wfs.teamWorkflowStatusId == currentWorkflowStatusId ? ButtonVariants.filled2 : ButtonVariants.default}
      data-tooltip-right={
        ["COMPLETED", "CANCELLED"].indexOf(wfs.workflowStateGroup) != -1 && wfs.teamWorkflowStatusId != currentWorkflowStatusId
          ? t("taskStatusChangeModalRemovesRemindersTooltip")
          : null
      }
    >
      {groupIconMap?.[wfs.workflowStateGroup]}
      {wfs.name}
    </Button>
  );
};

export default StatusListButton;
