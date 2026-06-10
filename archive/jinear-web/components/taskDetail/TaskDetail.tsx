import { useToggle } from "@/hooks/useToggle";
import { TaskDto } from "@/model/be/jinear-core";
import { hasWorkspaceFilePermissions } from "@/utils/permissionHelper";
import useTranslation from "locales/useTranslation";
import React, { useMemo } from "react";
import LastTaskActivitiesList from "../lastActivitiesScreen/lastTaskActivitiesList/LastTaskActivitiesList";
import Line from "../line/Line";
import styles from "./TaskDetail.module.css";
import TaskDetailContext from "./context/TaskDetailContext";
import TaskActionBar from "./taskActionBar/TaskActionBar";
import TaskBody from "./taskBody/TaskBody";
import TaskChecklistContainer from "./taskChecklistContainer/TaskChecklistContainer";
import TaskComments from "./taskComments/TaskComments";
import TaskHasUpdatesInfo from "./taskHasUpdatesInfo/TaskHasUpdatesInfo";
import TaskMediaList from "./taskMediaList/TaskMediaList";
import TaskSubtaskList from "./taskSubtaskList/TaskSubtaskList";
import DropZone from "@/components/dropZone/DropZone";
import { pushDataToUploadStatusModalQueue } from "@/slice/modalSlice";
import { useAppDispatch } from "@/store/store";
import {
  retrieveTaskMediaUploadUrl,
  useNotifyUploadCompletedMutation,
  useRetrieveTaskMediaUploadUrlMutation
} from "@/api/taskMediaApi";

interface TaskDetailProps {
  task: TaskDto;
}

const TaskDetail: React.FC<TaskDetailProps> = ({ task }) => {
  const { t } = useTranslation();
  const dispatch = useAppDispatch();
  const [showSubTaskListEvenIfNoSubtasks, toggleShowSubTaskListEvenIfNoSubtasks] = useToggle(false);
  const context = useMemo(() => ({
    task,
    showSubTaskListEvenIfNoSubtasks,
    toggleShowSubTaskListEvenIfNoSubtasks
  }), [task, showSubTaskListEvenIfNoSubtasks, toggleShowSubTaskListEvenIfNoSubtasks]);
  const [retrieveTaskMediaUploadUrl, { isLoading: isRetrieveTaskMediaUploadUrlLoading }] = useRetrieveTaskMediaUploadUrlMutation();
  const [notifyUploadCompleted, { isLoading: isNotifyUploadCompletedLoading }] = useNotifyUploadCompletedMutation();

  const filePermissionsExists = hasWorkspaceFilePermissions(task.workspace);

  const handleAttachmentUpload = async ({ files }: { files: File[] }) => {
    const presignedUploadData = [];

    for (const file of files) {
      const mediaUploadUrlRequest = {
        originalName: file.name ?? "Task-File",
        fileSize: file.size,
        contentType: file.type ?? "application/octet-stream"
      };
      const { data: presignedUrlResponse } = await retrieveTaskMediaUploadUrl({
        taskId: task.taskId,
        mediaUploadUrlRequest
      }).unwrap();

      const onCompleteNotify = () => {
        notifyUploadCompleted({ taskId: task.taskId, mediaId: presignedUrlResponse.mediaId });
      };

      const uploadData = {
        relatedObjectId: presignedUrlResponse.mediaId,
        presignedUrl: presignedUrlResponse.presignedUrl,
        file,
        onComplete: onCompleteNotify
      };
      presignedUploadData.push(uploadData);
    }

    dispatch(pushDataToUploadStatusModalQueue({
      workspaceId: task.workspaceId,
      presignedUploadData,
      visible: true
    }));
  };

  return (
    <TaskDetailContext.Provider
      value={context}
    >
      <DropZone
        disabled={!filePermissionsExists}
        onDrop={handleAttachmentUpload}
        overlayText={t("materialDropToUploadText")}
      >
        <div className={styles.taskLayout}>
          <TaskHasUpdatesInfo />
          <TaskBody className={styles.taskBody} />
          <Line />
          <TaskActionBar className={styles.taskInfo} />
          <TaskChecklistContainer />
          {filePermissionsExists && (
            <>
              <Line />
              <TaskMediaList handleAttachmentUpload={handleAttachmentUpload} />
            </>
          )}
          <TaskSubtaskList />
          <Line />
          <TaskComments />
          <Line />
          <LastTaskActivitiesList
            workspaceId={task.workspaceId}
            taskId={task.taskId}
            listTitle={t("taskActivityListTitle")}
            listTitleClassName={styles.taskActivitiesTitle}
          />
        </div>
      </DropZone>
    </TaskDetailContext.Provider>
  );
};

export default TaskDetail;
