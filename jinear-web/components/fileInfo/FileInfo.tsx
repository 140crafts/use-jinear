import { MediaDto } from "@/model/be/jinear-core";
import React from "react";
import { IoDocumentOutline, IoImage, IoVideocam } from "react-icons/io5";
import styles from "./FileInfo.module.scss";
import { API_ROOT } from "@/utils/constants";
import { humanReadibleFileSize } from "@/utils/FileSizeFormatter";
import cn from "classnames";
import { format } from "date-fns";
import useTranslation from "locales/useTranslation";
import Button, { ButtonHeight } from "../button";

interface FileInfoProps {
  media: MediaDto;
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
  mkv: IoVideocam,
};

const FileInfo: React.FC<FileInfoProps> = ({ media }) => {
  const { t } = useTranslation();

  const renderFileIcon = () => {
    const splitted = media?.originalName?.split(".");
    const fileType = splitted?.[splitted?.length - 1]?.toLowerCase();
    // @ts-ignore
    let Icon = ICON_MAP[fileType] || IoDocumentOutline;
    return <Icon size={ICON_SIZE} className={styles.icon} />;
  };

  return (
    <div className={styles.container}>
      <Button
        disabled={!media}
        className={styles.mainButton}
        heightVariant={ButtonHeight.short}
        href={media ? API_ROOT + `v1/task/media/${media.relatedObjectId}/download/${media.mediaId}` : undefined}
        target={media && "_blank"}
        download={media ? media.originalName : undefined}
      >
        <div className={styles.iconAndNameContainer}>
          <div className={styles.iconContainer}>{renderFileIcon()}</div>
          <div className={cn(styles.mediaNameLabel, "single-line")}>{media?.originalName}</div>
        </div>

        <div className={styles.mediaInfoContainer}>
          <div className={styles.mediaSizeLabel}>{humanReadibleFileSize(media?.size)}</div>
          <div className={styles.dateLabel}>
            {media?.createdDate && format(new Date(media?.createdDate), t("dateTimeFormat"))}
          </div>
        </div>
      </Button>
    </div>
  );
};

export default FileInfo;
