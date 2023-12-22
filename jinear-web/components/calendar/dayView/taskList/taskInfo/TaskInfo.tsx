import { isDateBetween } from "@/components/calendar/calendarUtils";
import useWindowSize from "@/hooks/useWindowSize";
import { TaskDto } from "@/model/be/jinear-core";
import { popTaskOverviewModal } from "@/store/slice/modalSlice";
import { useAppDispatch } from "@/store/store";
import { retrieveTaskStatusIcon } from "@/utils/taskIconFactory";
import cn from "classnames";
import { endOfDay, startOfDay } from "date-fns";
import Link from "next/link";
import React from "react";
import styles from "./TaskInfo.module.css";

interface TaskInfoProps {
  task: TaskDto;
  viewingDate: Date;
}

const TaskInfo: React.FC<TaskInfoProps> = ({ task, viewingDate }) => {
  const dispatch = useAppDispatch();
  const { isMobile } = useWindowSize();
  const dayStart = startOfDay(viewingDate);
  const dayEnd = endOfDay(viewingDate);

  const _assignedDate = task?.assignedDate && new Date(task.assignedDate);
  const _dueDate = task?.dueDate && new Date(task.dueDate);
  const isAssignedDateWithinViewingDay = _assignedDate && isDateBetween(dayStart, _assignedDate, dayEnd);
  const isDueDateWithinViewingDay = _dueDate && isDateBetween(dayStart, _dueDate, dayEnd);
  const isOneOfDatesNotSet = !_assignedDate || !_dueDate;

  const Icon = retrieveTaskStatusIcon(task?.workflowStatus.workflowStateGroup);
  const topicColor = task?.topic?.color;
  const isCompleted =
    task &&
    task.workflowStatus.workflowStateGroup &&
    (task.workflowStatus.workflowStateGroup == "COMPLETED" || task.workflowStatus.workflowStateGroup == "CANCELLED");

  const openTaskOverviewModal = () => {
    const workspaceName = task?.workspace?.username;
    const taskTag = `${task?.team?.tag}-${task?.teamTagNo}`;
    dispatch(popTaskOverviewModal({ taskTag, workspaceName, visible: true }));
  };

  const onLinkClick = (event: React.MouseEvent<HTMLAnchorElement> | undefined) => {
    if (!isMobile) {
      event?.preventDefault();
      openTaskOverviewModal();
    }
  };

  return (
    <Link
      className={styles.container}
      href={`/${task?.workspace?.username}/task/${task?.team?.tag}-${task?.teamTagNo}`}
      onClick={onLinkClick}
    >
      <div className={styles.taskStateInfo}>
        <div className={styles.workflowStatusInfo}>
          <div className={styles.iconContainer}>{task && <Icon size={21} />}</div>
          <div>{task.workflowStatus.name}</div>
        </div>
      </div>
      <div className={cn(styles.title, "line-clamp", isCompleted && styles["title-line-through"])}>{task.title} </div>
    </Link>
  );
};

export default TaskInfo;
