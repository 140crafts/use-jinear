import { TaskDto } from "@/model/be/jinear-core";
import cn from "classnames";
import useTranslation from "locales/useTranslation";
import React from "react";
import {
  IoCheckmarkCircle,
  IoCloseCircle,
  IoContrast,
  IoEllipseOutline,
  IoPauseCircleOutline,
} from "react-icons/io5";
import styles from "./TitleCell.module.css";
interface TitleCellProps {
  task: TaskDto;
}

const groupIconMap = {
  BACKLOG: <IoPauseCircleOutline size={20} />,
  NOT_STARTED: <IoEllipseOutline size={20} />,
  STARTED: <IoContrast size={20} />,
  COMPLETED: <IoCheckmarkCircle size={20} />,
  CANCELLED: <IoCloseCircle size={20} />,
};

const TitleCell: React.FC<TitleCellProps> = ({ task }) => {
  const { t } = useTranslation();
  return (
    <div className={styles.titleContainer}>
      <div
        className={styles.iconContainer}
        data-tooltip={t(
          `workflowGroupTitle_${task.workflowStatus.workflowStateGroup}`
        )}
      >
        {groupIconMap?.[task.workflowStatus.workflowStateGroup]}
      </div>
      <div className={cn(styles.title, "single-line")}>{task.title}</div>
    </div>
  );
};

export default TitleCell;
