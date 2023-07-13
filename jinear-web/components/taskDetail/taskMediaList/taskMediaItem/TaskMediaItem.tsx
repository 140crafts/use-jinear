import CircularLoading from "@/components/circularLoading/CircularLoading";
import { MediaDto } from "@/model/be/jinear-core";

import Button, { ButtonHeight } from "@/components/button";
import { root } from "@/store/api/api";
import { useDeleteTaskMediaMutation } from "@/store/api/taskMediaApi";
import { closeDialogModal, popDialogModal } from "@/store/slice/modalSlice";
import { useAppDispatch } from "@/store/store";
import { humanReadibleFileSize } from "@/utils/FileSizeFormatter";
import cn from "classnames";
import { format } from "date-fns";
import useTranslation from "locales/useTranslation";
import React from "react";
import { IoClose, IoDocumentOutline, IoImage } from "react-icons/io5";
import styles from "./TaskMediaItem.module.scss";

interface TaskMediaItemProps {
  media?: MediaDto;
  mock?: boolean;
}

const ICON_SIZE = 16;

const ICON_MAP = {
  png: IoImage,
  jpg: IoImage,
  jpeg: IoImage,
  gif: IoImage,
  ico: IoImage,
};

const TaskMediaItem: React.FC<TaskMediaItemProps> = ({ media, mock = false }) => {
  const { t } = useTranslation();
  const dispatch = useAppDispatch();

  const [deleteTaskMedia, { isLoading: isDeleteLoading }] = useDeleteTaskMediaMutation();

  const renderFileIcon = () => {
    const splitted = media?.originalName?.split(".");
    const fileType = splitted?.[splitted?.length - 1]?.toLowerCase();
    // @ts-ignore
    let Icon = ICON_MAP[fileType] || IoDocumentOutline;
    return <Icon size={ICON_SIZE} className={styles.icon} />;
  };

  const popAreYouSureModalForDelete = () => {
    dispatch(
      popDialogModal({
        visible: true,
        title: t("taskDetailMediaDeleteMediaAreYouSureTitle"),
        content: t("taskDetailMediaDeleteMediaAreYouSureText"),
        confirmButtonLabel: t("taskDetailMediaDeleteMediaAreYouSureConfirmLabel"),
        onConfirm: deleteMedia,
      })
    );
  };

  const deleteMedia = () => {
    if (media) {
      deleteTaskMedia({ taskId: media.relatedObjectId, mediaId: media.mediaId });
      dispatch(closeDialogModal());
    }
  };

  return (
    <div className={styles.container}>
      <Button
        disabled={isDeleteLoading}
        className={styles.mainButton}
        heightVariant={ButtonHeight.short}
        href={media && !isDeleteLoading ? root + `/v1/task/media/${media.relatedObjectId}/download/${media.mediaId}` : undefined}
        target={media && !isDeleteLoading ? "_blank" : undefined}
        download={media && !isDeleteLoading ? media.originalName : undefined}
      >
        <div className={styles.iconAndNameContainer}>
          <div className={styles.iconContainer}>{mock ? <CircularLoading /> : renderFileIcon()}</div>
          <div className={cn(styles.mediaNameLabel, "single-line")}>
            {mock ? t("taskDetailMediaUploading") : media?.originalName}
          </div>
        </div>

        <div className={styles.mediaInfoContainer}>
          <div className={styles.mediaSizeLabel}>{humanReadibleFileSize(media?.size)}</div>
          <div className={styles.dateLabel}>
            {media?.createdDate && format(new Date(media?.createdDate), t("dateTimeFormat"))}
          </div>
        </div>
      </Button>
      {!mock && (
        <Button
          disabled={isDeleteLoading}
          loading={isDeleteLoading}
          heightVariant={ButtonHeight.short}
          data-tooltip-right={t("taskDetailMediaDeleteMedia")}
          onClick={popAreYouSureModalForDelete}
          className={styles.deleteButton}
        >
          <IoClose />
        </Button>
      )}
    </div>
  );
};

export default TaskMediaItem;
