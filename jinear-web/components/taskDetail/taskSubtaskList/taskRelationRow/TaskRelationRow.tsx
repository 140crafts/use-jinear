import AssigneeCell from "@/components/assigneeCell/AssigneeCell";
import Button, { ButtonHeight } from "@/components/button";
import { TaskRelationDto } from "@/model/be/jinear-core";
import { popChangeTaskWorkflowStatusModal } from "@/store/slice/modalSlice";
import { useAppDispatch } from "@/store/store";
import cn from "classnames";
import Link from "next/link";
import React from "react";
import {
  IoCheckmarkCircle,
  IoCloseCircle,
  IoContrast,
  IoEllipseOutline,
  IoPauseCircleOutline,
} from "react-icons/io5";
import styles from "./TaskRelationRow.module.css";

interface TaskRelationRowProps {
  relation: TaskRelationDto;
}

const groupIconMap = {
  BACKLOG: <IoPauseCircleOutline size={20} />,
  NOT_STARTED: <IoEllipseOutline size={20} />,
  STARTED: <IoContrast size={20} />,
  COMPLETED: <IoCheckmarkCircle size={20} />,
  CANCELLED: <IoCloseCircle size={20} />,
};

const TaskRelationRow: React.FC<TaskRelationRowProps> = ({ relation }) => {
  const dispatch = useAppDispatch();

  const popChangeWorkflowStatusModal = () => {
    dispatch(
      popChangeTaskWorkflowStatusModal({ visible: true, task: relation.task })
    );
  };

  return (
    <div className={styles.container}>
      <Button
        onClick={popChangeWorkflowStatusModal}
        heightVariant={ButtonHeight.short}
      >
        {groupIconMap?.[relation.task.workflowStatus.workflowStateGroup]}
      </Button>
      <Link
        className={cn(styles.linkButton)}
        href={`/${relation.task.workspace?.username}/task/${relation.task.team?.tag}-${relation.task.teamTagNo}`}
      >
        <div className="line-clamp">{relation.task.title}</div>
      </Link>
      <AssigneeCell task={relation.task} tooltipPosition={"right"} />
    </div>
  );
};

export default TaskRelationRow;
