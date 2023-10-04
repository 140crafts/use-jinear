import { ICalendarDayRowCell } from "@/components/calendar/calendarUtils";
import { useHighligtedTaskId, useSetHighlightedTaskId } from "@/components/calendar/context/CalendarContext";
import { useDebouncedEffect } from "@/hooks/useDebouncedEffect";
import useWindowSize from "@/hooks/useWindowSize";
import { popTaskOverviewModal } from "@/store/slice/modalSlice";
import { useAppDispatch } from "@/store/store";
import { retrieveTaskStatusIcon } from "@/utils/taskIconFactory";
import cn from "classnames";
import { differenceInMinutes } from "date-fns";
import Link from "next/link";
import React, { useEffect, useState } from "react";
import styles from "./TaskPositionBasedCell.module.css";

interface TaskPositionBasedCellProps {
  cell: ICalendarDayRowCell;
}

const TaskPositionBasedCell: React.FC<TaskPositionBasedCellProps> = ({ cell }) => {
  const task = cell.task;

  const dispatch = useAppDispatch();
  const { isMobile } = useWindowSize();
  const highlightedTaskId = useHighligtedTaskId();
  const setHighlightedTaskId = useSetHighlightedTaskId();
  const highlighted = task && highlightedTaskId == task.taskId;
  const [highlightedZIndex, setHighlightedZIndex] = useState<boolean>(false);

  const _assignedDate = task?.assignedDate && new Date(task.assignedDate);
  const _dueDate = task?.dueDate && new Date(task.dueDate);
  const diffInMinutes = differenceInMinutes(cell.endTime, cell.startTime);

  const Icon = retrieveTaskStatusIcon(task?.workflowStatus.workflowStateGroup);
  const isCompleted =
    task &&
    task.workflowStatus.workflowStateGroup &&
    (task.workflowStatus.workflowStateGroup == "COMPLETED" || task.workflowStatus.workflowStateGroup == "CANCELLED");

  const cellStyle = {
    top: cell.top,
    left: `${cell.left}%`,
    height: cell.height,
    width: `${cell.width}%`,
  };
  const topicColor = task?.topic?.color;
  const topicCellStyle = topicColor
    ? {
        borderLeftColor: `#${topicColor}`,
      }
    : {};
  const zIndexStyle = highlightedZIndex ? { zIndex: 5 } : { zIndex: undefined };

  useDebouncedEffect(
    () => {
      if (highlighted) {
        setHighlightedZIndex(true);
      }
    },
    [highlighted],
    1250
  );
  useEffect(() => {
    if (!highlighted) {
      setHighlightedZIndex(false);
    }
  }, [highlighted]);

  const _hoverStart = () => {
    if (task) {
      setHighlightedTaskId?.(task.taskId);
    }
  };

  const _hoverEnd = () => {
    setHighlightedTaskId?.("");
  };

  const onLinkClick = (event: React.MouseEvent<HTMLAnchorElement> | undefined) => {
    if (!isMobile) {
      event?.preventDefault();
      openTaskOverviewModal();
    }
  };

  const openTaskOverviewModal = () => {
    const workspaceName = task?.workspace?.username;
    const taskTag = `${task?.team?.tag}-${task?.teamTagNo}`;
    dispatch(popTaskOverviewModal({ taskTag, workspaceName, visible: true }));
  };

  return (
    <Link
      tabIndex={task ? undefined : -1}
      href={`/${task?.workspace?.username}/task/${task?.team?.tag}-${task?.teamTagNo}`}
      onClick={onLinkClick}
      className={cn(styles.container, highlighted && styles.highlight, isCompleted && styles["completed-fill"])}
      // @ts-ignore
      style={{ ...cellStyle, ...topicCellStyle, ...zIndexStyle }}
      onMouseEnter={_hoverStart}
      onMouseOut={_hoverEnd}
    >
      <div
        className={cn(styles.title, isCompleted && styles["title-line-through"])}
        onMouseEnter={_hoverStart}
        onMouseOut={_hoverEnd}
      >
        {task?.title}
      </div>
    </Link>
  );
};

export default TaskPositionBasedCell;
