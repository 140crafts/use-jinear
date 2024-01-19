import useWindowSize from "@/hooks/useWindowSize";
import { TaskDto } from "@/model/be/jinear-core";
import { popTaskOverviewModal } from "@/store/slice/modalSlice";
import { useAppDispatch } from "@/store/store";
import Logger from "@/utils/logger";
import { retrieveTaskStatusIcon } from "@/utils/taskIconFactory";
import cn from "classnames";
import Link from "next/link";
import React from "react";
import { isDateBetween } from "../../calendarUtils";
import {
  useDraggingTask,
  useHighligtedTaskId,
  useIsDateBetweenViewingPeriod,
  useIsDateFirstDayOfViewingPeriod,
  useIsDateLastDayOfViewingPeriod,
  useSetDraggingTask,
  useSetGhostTask,
  useSetHighlightedTaskId,
} from "../../context/CalendarContext";
import styles from "./Cell.module.scss";

interface CellProps {
  weight: number;
  task?: TaskDto | null;
  id: string;
  weekStart: Date;
  weekEnd: Date;
}

const logger = Logger("Cell");
const Cell: React.FC<CellProps> = ({ id, weight, task, weekStart, weekEnd }) => {
  const dispatch = useAppDispatch();
  const { isMobile } = useWindowSize();

  const highlightedTaskId = useHighligtedTaskId();
  const setHighlightedTaskId = useSetHighlightedTaskId();
  const highlighted = task && highlightedTaskId == task.taskId;

  const draggingTask = useDraggingTask();
  const someTaskDragging = draggingTask != null;
  const setDraggingTask = useSetDraggingTask();
  const setGhostTask = useSetGhostTask();
  const isGhostTask = task && task.taskId.indexOf("dragging") != -1;
  const isDraggingTask = task && draggingTask && task.taskId == draggingTask.taskId;

  const _assignedDate = task?.assignedDate && new Date(task.assignedDate);
  const _dueDate = task?.dueDate && new Date(task.dueDate);
  const isAssignedDateWithinThisWeek = _assignedDate && isDateBetween(weekStart, _assignedDate, weekEnd);
  const isDueDateWithinThisWeek = _dueDate && isDateBetween(weekStart, _dueDate, weekEnd);
  const isOneOfDatesNotSet = !_assignedDate || !_dueDate;

  const isDateFirstDayOfViewingPeriod = useIsDateFirstDayOfViewingPeriod(weekStart);
  const isDateLastDayOfViewingPeriod = useIsDateLastDayOfViewingPeriod(weekEnd);
  const isAssignedDateWithinPeriod = useIsDateBetweenViewingPeriod(_assignedDate);
  const isDueDateWithinPeriod = useIsDateBetweenViewingPeriod(_dueDate);

  const isStartDateNotInViewingPeriodAndTodayIsFirstDayOfViewingPeriod =
    _assignedDate && !isAssignedDateWithinPeriod && isDateFirstDayOfViewingPeriod;
  const isEndDateNotInViewingPeriodAndTodayIsLastDayOfViewingPeriod =
    _dueDate && !isDueDateWithinPeriod && isDateLastDayOfViewingPeriod;

  const Icon = retrieveTaskStatusIcon(task?.workflowStatus.workflowStateGroup);
  const isCompleted =
    task &&
    task.workflowStatus.workflowStateGroup &&
    (task.workflowStatus.workflowStateGroup == "COMPLETED" || task.workflowStatus.workflowStateGroup == "CANCELLED");

  const topicColor = task?.topic?.color ? `#${task?.topic?.color}` : "transparent";
  const topicCellStyle = {
    flex: weight,
    borderLeftColor: topicColor,
    borderLeftStyle: "solid",
    borderLeftWidth: 2.1,
    borderLeftLeftRadius: 0,
    borderLeftRightRadius: 0,
  };

  const _hoverStart = () => {
    if (task) {
      setHighlightedTaskId?.(task.taskId);
    }
  };

  const _hoverEnd = () => {
    setHighlightedTaskId?.("");
  };

  const _onDragStart = (event: React.DragEvent) => {
    logger.log({ _onDragStart: task?.taskId, event });
    event.dataTransfer.setData("text", `${task?.taskId}`);
    if (task) {
      setDraggingTask?.(task);
    }
  };

  const _onDragEnd = (event: React.DragEvent) => {
    logger.log({ _onDragEnd: task?.taskId, event });
    setDraggingTask?.(undefined);
    setGhostTask?.(undefined);
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
      id={id}
      className={cn(
        styles.container,
        task && isGhostTask && styles.ghost,
        task && styles.fill,
        task && highlighted && styles.highlight,
        task && someTaskDragging && !isDraggingTask && styles.noPointerEvents,
        (isDueDateWithinThisWeek || isOneOfDatesNotSet) && styles.startDayEndDayMargin,
        (isAssignedDateWithinThisWeek || isOneOfDatesNotSet) && styles.startDay,
        (isDueDateWithinThisWeek || isOneOfDatesNotSet) && styles.endDay,
        task && isCompleted && styles["completed-fill"]
      )}
      // @ts-ignore
      style={topicCellStyle}
      onMouseEnter={_hoverStart}
      onMouseOut={_hoverEnd}
      onDragStart={_onDragStart}
      onDragEnd={_onDragEnd}
      draggable={true}
    >
      {isStartDateNotInViewingPeriodAndTodayIsFirstDayOfViewingPeriod && (
        <div className={cn(styles["arrow-right"], isGhostTask && styles["ghost-arrow-right"])}></div>
      )}
      <div className={styles.iconContainer}>{task && <Icon size={17} />}</div>
      <div
        className={cn(styles.title, "line-clamp", isCompleted && styles["title-line-through"])}
        onMouseEnter={_hoverStart}
        onMouseOut={_hoverEnd}
      >
        {task?.title}
      </div>
      {isEndDateNotInViewingPeriodAndTodayIsLastDayOfViewingPeriod && (
        <div className={styles["arrow-right-end-bg"]}>
          <div
            className={cn(
              styles["arrow-right-end"],
              highlighted && styles["arrow-right-end-highlight"],
              isGhostTask && styles["ghost-arrow-right-end-highlight"]
            )}
          ></div>
        </div>
      )}
    </Link>
  );
};

export default Cell;
