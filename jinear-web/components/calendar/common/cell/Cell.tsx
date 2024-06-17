import useWindowSize from "@/hooks/useWindowSize";
import { CalendarEventDto } from "@/model/be/jinear-core";
import { popCalendarExternalEventViewModal, popTaskOverviewModal } from "@/store/slice/modalSlice";
import { useAppDispatch } from "@/store/store";
import Logger from "@/utils/logger";
import { retrieveTaskStatusIcon } from "@/utils/taskIconFactory";
import cn from "classnames";
import Link from "next/link";
import React from "react";
import { IoCalendarOutline } from "react-icons/io5";
import {
  isDateBetween,
  useIsDateBetweenViewingPeriod,
  useIsDateFirstDayOfViewingPeriod,
  useIsDateLastDayOfViewingPeriod
} from "../../calendarUtils";
import {
  useDraggingEvent,
  useHighlightedEventId,
  useSetDraggingEvent,
  useSetGhostEvent,
  useSetHighlightedEventId
} from "../../context/CalendarContext";
import styles from "./Cell.module.scss";

interface CellProps {
  weight: number;
  calendarEvent?: CalendarEventDto | null;
  id: string;
  weekStart: Date;
  weekEnd: Date;
}

const logger = Logger("Cell");
const Cell: React.FC<CellProps> = ({ id, weight, calendarEvent, weekStart, weekEnd }) => {
  const dispatch = useAppDispatch();
  const { isMobile } = useWindowSize();

  const highlightedEventId = useHighlightedEventId();
  const setHighlightedEventId = useSetHighlightedEventId();

  const highlighted = calendarEvent && highlightedEventId == calendarEvent.calendarEventId;

  const draggingEvent = useDraggingEvent();
  const someEventDragging = draggingEvent != null;
  const setDraggingEvent = useSetDraggingEvent();
  const setGhostEvent = useSetGhostEvent();
  const isGhostEvent = calendarEvent && calendarEvent.calendarEventId.indexOf("dragging") != -1;
  const isDraggingEvent = calendarEvent && draggingEvent && calendarEvent.calendarEventId == draggingEvent.calendarEventId;

  const _assignedDate = calendarEvent?.assignedDate && new Date(calendarEvent.assignedDate);
  const _dueDate = calendarEvent?.dueDate && new Date(calendarEvent.dueDate);
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

  const Icon =
    calendarEvent?.calendarEventSourceType == "TASK"
      ? retrieveTaskStatusIcon(calendarEvent?.relatedTask?.workflowStatus.workflowStateGroup)
      : IoCalendarOutline;
  const iconSize = calendarEvent?.calendarEventSourceType == "TASK" ? 17 : 14;

  const isCompleted =
    calendarEvent &&
    calendarEvent.relatedTask?.workflowStatus.workflowStateGroup &&
    (calendarEvent.relatedTask?.workflowStatus.workflowStateGroup == "COMPLETED" ||
      calendarEvent.relatedTask?.workflowStatus.workflowStateGroup == "CANCELLED");

  const topicColor = calendarEvent?.relatedTask?.topic?.color ? `#${calendarEvent?.relatedTask?.topic?.color}` : "transparent";
  const topicCellStyle = {
    flex: weight,
    borderLeftColor: topicColor,
    borderLeftStyle: "solid",
    borderLeftWidth: 2.1,
    borderLeftLeftRadius: 0,
    borderLeftRightRadius: 0
  };

  const _hoverStart = () => {
    if (calendarEvent) {
      setHighlightedEventId?.(calendarEvent.calendarEventId);
    }
  };

  const _hoverEnd = () => {
    setHighlightedEventId?.("");
  };

  const _onDragStart = (event: React.DragEvent) => {
    logger.log({ _onDragStart: calendarEvent?.calendarEventId, event });
    event.dataTransfer.setData("text", `${calendarEvent?.calendarEventId}`);
    if (calendarEvent) {
      setDraggingEvent?.(calendarEvent);
    }
  };

  const _onDragEnd = (event: React.DragEvent) => {
    logger.log({ _onDragEnd: calendarEvent?.calendarEventId, event });
    setDraggingEvent?.(undefined);
    setGhostEvent?.(undefined);
  };

  const onLinkClick = (event: React.MouseEvent<HTMLAnchorElement> | undefined) => {
    if (calendarEvent?.calendarEventSourceType != "TASK") {
      event?.preventDefault();
      event?.nativeEvent?.stopImmediatePropagation?.();
      openCalendarExternalEventOverviewModal();
    } else if (!isMobile) {
      event?.preventDefault();
      event?.nativeEvent?.stopImmediatePropagation?.();
      openTaskOverviewModal();
    }
  };

  const openTaskOverviewModal = () => {
    const workspaceName = calendarEvent?.relatedTask?.workspace?.username;
    const taskTag = `${calendarEvent?.relatedTask?.team?.tag}-${calendarEvent?.relatedTask?.teamTagNo}`;
    dispatch(popTaskOverviewModal({ taskTag, workspaceName, visible: true }));
  };

  const openCalendarExternalEventOverviewModal = () => {
    if (calendarEvent) {
      dispatch(popCalendarExternalEventViewModal({ calendarEventDto: calendarEvent, visible: true }));
    }
  };

  return (
    <Link
      tabIndex={calendarEvent ? undefined : -1}
      href={`/${calendarEvent?.relatedTask?.workspace?.username}/task/${calendarEvent?.relatedTask?.team?.tag}-${calendarEvent?.relatedTask?.teamTagNo}`}
      onClick={onLinkClick}
      id={id}
      className={cn(
        styles.container,
        calendarEvent && isGhostEvent && styles.ghost,
        calendarEvent && styles.fill,
        calendarEvent && highlighted && styles.highlight,
        calendarEvent && someEventDragging && !isDraggingEvent && styles.noPointerEvents,
        (isDueDateWithinThisWeek || isOneOfDatesNotSet) && styles.startDayEndDayMargin,
        (isAssignedDateWithinThisWeek || isOneOfDatesNotSet) && styles.startDay,
        (isDueDateWithinThisWeek || isOneOfDatesNotSet) && styles.endDay,
        calendarEvent && isCompleted && styles["completed-fill"]
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
        <div className={cn(styles["arrow-right"], isGhostEvent && styles["ghost-arrow-right"])}></div>
      )}

      {calendarEvent && <div className={styles.iconContainer}>{<Icon className={styles.icon} size={iconSize} />}</div>}

      <div
        className={cn(styles.title, "line-clamp", isCompleted && styles["title-line-through"])}
        onMouseEnter={_hoverStart}
        onMouseOut={_hoverEnd}
      >
        {calendarEvent?.title}
      </div>
      {isEndDateNotInViewingPeriodAndTodayIsLastDayOfViewingPeriod && (
        <div className={styles["arrow-right-end-bg"]}>
          <div
            className={cn(
              styles["arrow-right-end"],
              highlighted && styles["arrow-right-end-highlight"],
              isGhostEvent && styles["ghost-arrow-right-end-highlight"]
            )}
          ></div>
        </div>
      )}
    </Link>
  );
};

export default Cell;
