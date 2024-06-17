import { ICalendarDayRowCell } from "@/components/calendar/calendarUtils";
import { useHighlightedEventId, useSetHighlightedEventId } from "@/components/calendar/context/CalendarContext";
import { useDebouncedEffect } from "@/hooks/useDebouncedEffect";
import useWindowSize from "@/hooks/useWindowSize";
import { popCalendarExternalEventViewModal, popTaskOverviewModal } from "@/store/slice/modalSlice";
import { useAppDispatch } from "@/store/store";
import cn from "classnames";
import { differenceInMinutes } from "date-fns";
import Link from "next/link";
import React, { useEffect, useState } from "react";
import styles from "./TaskPositionBasedCell.module.css";

interface TaskPositionBasedCellProps {
  cell: ICalendarDayRowCell;
}

const TaskPositionBasedCell: React.FC<TaskPositionBasedCellProps> = ({ cell }) => {
  const calendarEvent = cell.event;
  const dispatch = useAppDispatch();
  const { isMobile } = useWindowSize();

  const highlightedEventId = useHighlightedEventId();
  const setHighlightedEventId = useSetHighlightedEventId();

  const highlighted = calendarEvent && highlightedEventId == calendarEvent.calendarEventId;
  const [highlightedZIndex, setHighlightedZIndex] = useState<boolean>(false);

  const _assignedDate = calendarEvent?.assignedDate && new Date(calendarEvent.assignedDate);
  const _dueDate = calendarEvent?.dueDate && new Date(calendarEvent.dueDate);
  const diffInMinutes = differenceInMinutes(cell.endTime, cell.startTime);

  const isCompleted =
    calendarEvent &&
    calendarEvent?.relatedTask?.workflowStatus.workflowStateGroup &&
    (calendarEvent?.relatedTask?.workflowStatus.workflowStateGroup == "COMPLETED" ||
      calendarEvent?.relatedTask?.workflowStatus.workflowStateGroup == "CANCELLED");

  const cellStyle = {
    top: cell.top,
    left: `${cell.left}%`,
    height: cell.height,
    width: `${cell.width}%`,
  };
  const topicColor = calendarEvent?.relatedTask?.topic?.color || "transparent";
  const topicCellStyle = {
    borderLeftColor: `#${topicColor}`,
  };
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
    if (calendarEvent) {
      setHighlightedEventId?.(calendarEvent.calendarEventId);
    }
  };

  const _hoverEnd = () => {
    setHighlightedEventId?.("");
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
        {calendarEvent?.title}
      </div>
    </Link>
  );
};

export default TaskPositionBasedCell;
