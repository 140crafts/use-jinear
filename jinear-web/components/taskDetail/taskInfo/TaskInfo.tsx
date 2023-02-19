import Button, { ButtonHeight, ButtonVariants } from "@/components/button";
import { copyTextToClipboard } from "@/utils/clipboard";
import { getTextColor } from "@/utils/colorHelper";
import { HOST } from "@/utils/constants";
import useTranslation from "locales/useTranslation";
import React from "react";
import toast from "react-hot-toast";
import styles from "./TaskInfo.module.css";

import ProfilePhoto from "@/components/profilePhoto";
import {
  popChangeTaskAssigneeModal,
  popChangeTaskDateModal,
  popChangeTaskTopicModal,
  popChangeTaskWorkflowStatusModal,
} from "@/store/slice/modalSlice";
import { useAppDispatch } from "@/store/store";
import {
  IoCheckmarkCircle,
  IoCloseCircle,
  IoContrast,
  IoEllipseOutline,
  IoPauseCircleOutline,
} from "react-icons/io5";
import {
  useTask,
  useToggleShowSubTaskListEvenIfNoSubtasks,
} from "../context/TaskDetailContext";
import TaskAssignedDateButton from "./taskAssignedDateButton/TaskAssignedDateButton";
import TaskDueDateButton from "./taskDueDateButton/TaskDueDateButton";

interface TaskInfoProps {
  className?: string;
}

const groupIconMap = {
  BACKLOG: <IoPauseCircleOutline size={20} />,
  NOT_STARTED: <IoEllipseOutline size={20} />,
  STARTED: <IoContrast size={20} />,
  COMPLETED: <IoCheckmarkCircle size={20} />,
  CANCELLED: <IoCloseCircle size={20} />,
};

const TaskInfo: React.FC<TaskInfoProps> = ({ className }) => {
  const task = useTask();
  const { t } = useTranslation();
  const toggleShowSubTaskListEvenIfNoSubtasks =
    useToggleShowSubTaskListEvenIfNoSubtasks();
  const dispatch = useAppDispatch();

  const taskHasNoActiveSubtasks =
    task?.relatedIn?.filter((relation) => relation.relationType == "SUBTASK")
      ?.length == 0;

  const copyToClipboard = () => {
    const teamTag = task.team?.tag;
    const teamTagNo = task.teamTagNo;
    const tag = `${teamTag}-${teamTagNo}`;
    const taskLink = `${task.workspace?.username}/task/${tag}`;
    copyTextToClipboard(`${HOST}/${taskLink}`);
    toast(t("newTaskCreatedToastCopiedToClipboard"));
  };

  const popChangeWorkflowStatusModal = () => {
    dispatch(popChangeTaskWorkflowStatusModal({ visible: true, task }));
  };

  const popChangeTopicModal = () => {
    dispatch(popChangeTaskTopicModal({ visible: true, task }));
  };

  const popChangeAssignedDateModal = () => {
    dispatch(popChangeTaskDateModal({ visible: true, task }));
  };

  const popChangeAssigneeModal = () => {
    dispatch(popChangeTaskAssigneeModal({ visible: true, task }));
  };

  return (
    <div className={styles.container}>
      <Button
        onClick={copyToClipboard}
        variant={ButtonVariants.filled}
        heightVariant={ButtonHeight.mid}
        className={styles.button}
        data-tooltip-right={t("taskDetailCopyLinkTooltip")}
      >
        {task.team?.tag}-{task.teamTagNo}
      </Button>

      <Button
        onClick={popChangeWorkflowStatusModal}
        variant={ButtonVariants.filled}
        heightVariant={ButtonHeight.mid}
        className={styles.button}
        data-tooltip-right={t("taskDetailChangeWorkflowStatusTooltip")}
      >
        {groupIconMap?.[task.workflowStatus.workflowStateGroup]}
        {task.workflowStatus.name}
      </Button>

      <Button
        variant={ButtonVariants.filled}
        heightVariant={ButtonHeight.mid}
        data-tooltip-right={t("taskDetailChangeTopicTooltip")}
        onClick={popChangeTopicModal}
        style={{
          backgroundColor: task.topic ? `#${task.topic?.color}` : undefined,
        }}
      >
        {task.topic ? (
          <b
            style={{
              color: task.topic
                ? getTextColor(`#${task.topic.color}`)
                : undefined,
            }}
          >
            {task.topic.name}
          </b>
        ) : (
          <>{t("taskDetailInfoActionBarAssignTopic")}</>
        )}
      </Button>

      <TaskAssignedDateButton />
      <TaskDueDateButton />

      <Button
        variant={ButtonVariants.filled}
        heightVariant={ButtonHeight.mid}
        className={styles.button}
        onClick={popChangeAssigneeModal}
      >
        {task.assignedToAccount ? (
          <>
            <div>{t("taskDetailAssignedTo")}</div>
            <ProfilePhoto
              boringAvatarKey={task.assignedToAccount.accountId}
              storagePath={task.assignedToAccount.profilePicture?.storagePath}
              wrapperClassName={styles.profilePic}
            />
            <b>{task.assignedToAccount.username}</b>
          </>
        ) : (
          <>{t("taskDetailAssignToAccount")}</>
        )}
      </Button>

      {taskHasNoActiveSubtasks && (
        <Button
          onClick={toggleShowSubTaskListEvenIfNoSubtasks}
          variant={ButtonVariants.filled}
          heightVariant={ButtonHeight.mid}
          className={styles.button}
          data-tooltip-multiline={t("taskDetailCreateChecklistTooltip")}
        >
          {t("taskDetailCreateChecklist")}
        </Button>
      )}
    </div>
  );
};

export default TaskInfo;
