import Button, { ButtonHeight, ButtonVariants } from "@/components/button";
import CircularLoading from "@/components/circularLoading/CircularLoading";
import { useRetrieveTaskMediaListQuery, useUploadTaskMediaMutation } from "@/store/api/taskMediaApi";
import Logger from "@/utils/logger";
import useTranslation from "locales/useTranslation";
import React, { ChangeEvent, useRef } from "react";
import { useTask } from "../context/TaskDetailContext";
import styles from "./TaskMediaList.module.css";
import TaskMediaItem from "./taskMediaItem/TaskMediaItem";

interface TaskMediaListProps {}

const logger = Logger("TaskMediaList");
const TaskMediaList: React.FC<TaskMediaListProps> = ({}) => {
  const { t } = useTranslation();
  const attachmentPickerRef = useRef<HTMLInputElement>(null);
  const task = useTask();
  const { data: retrieveTaskMediaListResponse, isFetching: isMediaListFetching } = useRetrieveTaskMediaListQuery({
    taskId: task.taskId,
  });
  const [uploadTaskMedia, { isLoading: isUploadTaskMediaLoading }] = useUploadTaskMediaMutation();

  const pickAttachment = () => {
    if (attachmentPickerRef.current) {
      attachmentPickerRef.current.value = "";
      attachmentPickerRef.current.click();
    }
  };

  const onSelectFile = (event: ChangeEvent<HTMLInputElement>) => {
    const target = event.target as HTMLInputElement;
    if (target.files && target.files.length) {
      const file = event.target?.files?.[0];
      if (file) {
        let formData = new FormData();
        formData.append("file", file);
        uploadTaskMedia({ taskId: task.taskId, formData });
      }
      return;
    }
  };

  return (
    <div className={styles.container}>
      <div className={styles.actionBar}>
        <h3>{t("taskDetailMediaTitle")}</h3>
        <Button
          disabled={isUploadTaskMediaLoading}
          variant={ButtonVariants.filled}
          heightVariant={ButtonHeight.short}
          onClick={pickAttachment}
        >
          {t("taskDetailMediaAddMedia")}
        </Button>
        <input
          ref={attachmentPickerRef}
          id={"attachment-picker"}
          type="file"
          accept="*/*"
          className={styles.attachmentInput}
          onChange={onSelectFile}
        />
      </div>
      <div className={styles.mediaListContainer}>
        {retrieveTaskMediaListResponse?.data?.length == 0 && !isMediaListFetching && <div>{t("taskDetailMediaListEmpty")}</div>}
        {retrieveTaskMediaListResponse?.data?.map?.((taskMedia) => (
          <TaskMediaItem key={`task-media-${task.taskId}-${taskMedia.mediaId}`} media={taskMedia} />
        ))}
        {isUploadTaskMediaLoading && <TaskMediaItem key={`task-media-new-upload`} mock={true} />}
        {isMediaListFetching && <CircularLoading />}
      </div>
    </div>
  );
};

export default TaskMediaList;
