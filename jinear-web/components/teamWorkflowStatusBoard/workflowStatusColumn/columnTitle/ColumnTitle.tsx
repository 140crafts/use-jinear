import { TeamWorkflowStatusDto } from "@/model/be/jinear-core";
import cn from "classnames";
import React from "react";
import {
  IoCheckmarkCircle,
  IoCloseCircle,
  IoContrast,
  IoEllipseOutline,
  IoPauseCircleOutline,
} from "react-icons/io5";
import styles from "./ColumnTitle.module.css";

interface ColumnTitleProps {
  workflowStatusDto: TeamWorkflowStatusDto;
}

const groupIconMap = {
  BACKLOG: <IoPauseCircleOutline size={20} />,
  NOT_STARTED: <IoEllipseOutline size={20} />,
  STARTED: <IoContrast size={20} />,
  COMPLETED: <IoCheckmarkCircle size={20} />,
  CANCELLED: <IoCloseCircle size={20} />,
};
const ColumnTitle: React.FC<ColumnTitleProps> = ({ workflowStatusDto }) => {
  return (
    <div className={styles.container}>
      <div className={styles.titleContainer}>
        <div className={styles.iconContainer}>
          {groupIconMap?.[workflowStatusDto.workflowStateGroup]}
        </div>
        <h2 className={cn(styles.title, "single-line")}>
          {workflowStatusDto.name}
        </h2>
      </div>
      {/* <Line /> */}
    </div>
  );
};

export default ColumnTitle;
