import CircularLoading from "@/components/circularLoading/CircularLoading";
import { MediaDto } from "@/model/be/jinear-core";

import Button, { ButtonHeight } from "@/components/button";
import { API_ROOT } from "@/utils/constants";
import { useDeleteTaskMediaMutation, useUpdateTaskMediaVisibilityMutation } from "@/store/api/taskMediaApi";
import { closeDialogModal, popDialogModal } from "@/store/slice/modalSlice";
import { useAppDispatch } from "@/store/store";
import { humanReadibleFileSize } from "@/utils/FileSizeFormatter";
import cn from "classnames";
import { format } from "date-fns";
import useTranslation from "locales/useTranslation";
import React from "react";
import { IoClose, IoDocumentOutline, IoImage, IoVideocam } from "react-icons/io5";
import styles from "./TaskMediaItem.module.scss";
import { FaLock, FaLockOpen } from "react-icons/fa6";
import { LuShare } from "react-icons/lu";
import { copyTextToClipboard } from "@/utils/clipboard";
import toast from "react-hot-toast";

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
  webm: IoImage,
  mp4: IoVideocam,
  mpeg4: IoVideocam,
  mov: IoVideocam,
  wmv: IoVideocam,
  avi: IoVideocam,
  avchd: IoVideocam,
  flv: IoVideocam,
  f4v: IoVideocam,
  swf: IoVideocam,
  mkv: IoVideocam
};

const TaskMediaItem: React.FC<TaskMediaItemProps> = ({ media, mock = false }) => {
  const { t } = useTranslation();
  const dispatch = useAppDispatch();

  const [deleteTaskMedia, { isLoading: isDeleteLoading }] = useDeleteTaskMediaMutation();
  const [updateTaskMediaVisibility, { isLoading: isUpdateTaskMediaVisibilityLoading }] = useUpdateTaskMediaVisibilityMutation();

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
        onConfirm: deleteMedia
      })
    );
  };

  const deleteMedia = () => {
    if (media) {
      deleteTaskMedia({ taskId: media.relatedObjectId, mediaId: media.mediaId });
      dispatch(closeDialogModal());
    }
  };

  const toggleMediaVisibility = () => {
    if (media) {
      const next = media.visibility == "PRIVATE" ? "PUBLIC" : "PRIVATE";
      updateTaskMediaVisibility({ taskId: media.relatedObjectId, mediaId: media.mediaId, mediaVisibilityType: next });
    }
  };

  const shareFileUrl = async () => {
    if (media) {
      navigator.canShare();
      try {
        const shareData = {
          title: t("taskDetailMediaShareTitle"),
          text: (media.originalName ?? "Shared File") + ` (${humanReadibleFileSize(media?.size)})`,
          url: media.url
        };
        await navigator.share(shareData);
      } catch (e) {
        copyTextToClipboard(media?.url);
        toast(t("taskDetailMediaToastUrlCopiedToClipboard"));
      }
    }
  };

  const VisibilityIcon = media?.visibility == "PRIVATE" ? FaLock : FaLockOpen;

  return (
    <div className={styles.container}>
      <Button
        disabled={isDeleteLoading}
        className={styles.mainButton}
        heightVariant={ButtonHeight.short}
        href={media && !isDeleteLoading ? API_ROOT + `v1/task/media/${media.relatedObjectId}/download/${media.mediaId}` : undefined}
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
      {!mock && media &&
        <Button
          disabled={media.visibility != "PUBLIC" || (isDeleteLoading || isUpdateTaskMediaVisibilityLoading)}
          heightVariant={ButtonHeight.short}
          onClick={shareFileUrl}
          data-tooltip-right={media.visibility != "PUBLIC" ? t("taskDetailMediaCannotSharePublicly") : undefined}
          className={styles.actionButton}
        >
          <LuShare className={"icon"} />
        </Button>
      }
      {!mock && media &&
        <Button
          disabled={isDeleteLoading || isUpdateTaskMediaVisibilityLoading}
          heightVariant={ButtonHeight.short}
          data-tooltip-right={t(`taskDetailMediaToggleVisibility_${media.visibility}`)}
          onClick={toggleMediaVisibility}
          className={styles.actionButton}
        >
          <VisibilityIcon className={"icon"} />
        </Button>
      }
      {!mock && (
        <Button
          disabled={isDeleteLoading || isUpdateTaskMediaVisibilityLoading}
          loading={isDeleteLoading}
          heightVariant={ButtonHeight.short}
          data-tooltip-right={t("taskDetailMediaDeleteMedia")}
          onClick={popAreYouSureModalForDelete}
          className={styles.actionButton}
        >
          <IoClose />
        </Button>
      )}
    </div>
  );
};

export default TaskMediaItem;
