import Button, { ButtonVariants } from "@/components/button";
import FileInfo from "@/components/fileInfo/FileInfo";
import useWindowSize from "@/hooks/useWindowSize";
import { TaskMediaDto } from "@/model/be/jinear-core";
import { popTaskOverviewModal } from "@/store/slice/modalSlice";
import { useAppDispatch } from "@/store/store";
import React from "react";
import { IoArrowForward } from "react-icons/io5";
import styles from "./FileRow.module.css";

interface FileRowProps {
  index: number;
  relatedTaskDifferentFromOneBefore: boolean;
  data: TaskMediaDto;
}

const FileRow: React.FC<FileRowProps> = ({ index, relatedTaskDifferentFromOneBefore, data }) => {
  const dispatch = useAppDispatch();
  const { isMobile } = useWindowSize();

  const onLinkClick = (event: React.MouseEvent<HTMLAnchorElement> | undefined, data: TaskMediaDto) => {
    if (!isMobile && data.task) {
      event?.preventDefault();
      openTaskOverviewModal(data);
    }
  };

  const openTaskOverviewModal = (data: TaskMediaDto) => {
    const workspaceName = data.task?.workspace?.username;
    const taskTag = `${data.task?.team?.tag}-${data.task?.teamTagNo}`;
    dispatch(popTaskOverviewModal({ taskTag, workspaceName, visible: true }));
  };
  return (
    <div className={styles.fileRow}>
      {relatedTaskDifferentFromOneBefore && (
        <>
          {index != 0 && <div className="spacer-h-2" />}
          <Button className={styles.fileTaskButton} variant={ButtonVariants.filled} onClick={(e) => onLinkClick(e, data)}>
            <b>
              {`[${data.task.team?.tag}-${data.task.teamTagNo}] `}
              {data.task.title}
            </b>
            <div className="flex-1" />
            <div className={styles.iconContainer}>
              <IoArrowForward />
            </div>
          </Button>
        </>
      )}
      <div className={styles.fileInfoContainer}>
        <FileInfo media={data.media} />
      </div>
    </div>
  );
};

export default FileRow;
