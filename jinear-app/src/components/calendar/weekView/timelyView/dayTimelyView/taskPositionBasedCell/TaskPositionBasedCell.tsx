import type {ICalendarDayRowCell} from "@/components/calendar/calendarUtils";
import {
    useDayTimelyViewDraggingEvent,
    useHighlightedEventId,
    useSetDayTimelyViewDragGrabOffsetMinutes,
    useSetDayTimelyViewDraggingEvent,
    useSetDraggingOnHourTile,
    useSetHighlightedEventId
} from "@/components/calendar/context/CalendarContext";
import {useDebouncedEffect} from "@/hooks/useDebouncedEffect";
import useWindowSize from "@/hooks/useWindowSize";
import {popCalendarExternalEventViewModal, popTaskOverviewModal} from "@/store/slice/modalSlice";
import cn from "classnames";
import {differenceInMinutes} from "date-fns";
import React, {useEffect, useState} from "react";
import styles from "./TaskPositionBasedCell.module.css";
import useTranslation from "@/locals/useTranslation";
import {Link} from "react-router-dom";
import {useAppDispatch} from "@/store";

interface TaskPositionBasedCellProps {
    cell: ICalendarDayRowCell;
    isGhost?: boolean;
}

const TaskPositionBasedCell: React.FC<TaskPositionBasedCellProps> = ({cell, isGhost}) => {
    const {t} = useTranslation();
    const calendarEvent = cell.event;
    const dispatch = useAppDispatch();
    const {isMobile} = useWindowSize();

    const highlightedEventId = useHighlightedEventId();
    const setHighlightedEventId = useSetHighlightedEventId();
    const setDayTimelyViewDraggingEvent = useSetDayTimelyViewDraggingEvent();
    const setDayTimelyViewDragGrabOffsetMinutes = useSetDayTimelyViewDragGrabOffsetMinutes();
    const setDraggingOnHourTile = useSetDraggingOnHourTile();
    const dayTimelyViewDraggingEvent = useDayTimelyViewDraggingEvent();

    const isDraggingEvent =
        dayTimelyViewDraggingEvent != null &&
        calendarEvent?.calendarEventId == dayTimelyViewDraggingEvent?.event?.calendarEventId;
    const [dimDragSource, setDimDragSource] = useState<boolean>(false);

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
        width: `${cell.width}%`
    };

    const topicColor = calendarEvent?.relatedTask?.topic?.color ? `#${calendarEvent?.relatedTask?.topic?.color}` : undefined;
    const relatedBoardColors = calendarEvent?.relatedTask?.taskBoardEntries?.map(taskBoardEntry => `#${taskBoardEntry.taskBoard.color}`) ?? [];
    const limitedBoardColors = relatedBoardColors?.length > 3 ? relatedBoardColors.slice(0, 3) : relatedBoardColors;
    const cellColorTags = topicColor ? [...limitedBoardColors, topicColor] : limitedBoardColors;

    const topicCellStyle = {
        // borderLeftColor: `#${topicColor}`
    };

    const zIndexStyle = highlightedZIndex ? {zIndex: 5} : {zIndex: undefined};

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
        dispatch(popTaskOverviewModal({
            taskTag,
            workspaceName,
            task: calendarEvent?.relatedTask ?? undefined,
            visible: true
        }));
    };

    const openCalendarExternalEventOverviewModal = () => {
        if (calendarEvent) {
            dispatch(popCalendarExternalEventViewModal({calendarEventDto: calendarEvent, visible: true}));
        }
    };

    const _onDragStart = (event: React.DragEvent) => {
        if (!cell) {
            return;
        }
        // Firefox does not start a drag unless something is written to the data transfer.
        event.dataTransfer.setData("text", `${calendarEvent?.calendarEventId}`);
        // The ratio comes from the rendered element, so the cell does not need minuteInPx.
        const rect = event.currentTarget.getBoundingClientRect();
        const grabRatio = rect.height > 0 ? (event.clientY - rect.top) / rect.height : 0;
        setDayTimelyViewDragGrabOffsetMinutes?.(Math.max(0, grabRatio * diffInMinutes));
        setDayTimelyViewDraggingEvent?.(cell);
        // Chrome cancels the drag when the source changes inside dragstart, and the drag image
        // is taken from the element as it looks now. Dim it on the next tick instead.
        setTimeout(() => setDimDragSource(true), 0);
    };

    const _onDragEnd = (event: React.DragEvent) => {
        setDimDragSource(false);
        setDayTimelyViewDraggingEvent?.(undefined);
        setDayTimelyViewDragGrabOffsetMinutes?.(undefined);
        setDraggingOnHourTile?.(undefined);
    };

    return (
        <Link
            draggable={!isGhost}
            tabIndex={calendarEvent && !isGhost ? undefined : -1}
            to={`/${calendarEvent?.relatedTask?.workspace?.username}/task/${calendarEvent?.relatedTask?.team?.tag}-${calendarEvent?.relatedTask?.teamTagNo}`}
            onClick={isGhost ? undefined : onLinkClick}
            className={cn(
                styles.container,
                highlighted && styles.highlight,
                isCompleted && styles["completed-fill"],
                isGhost && styles.ghost,
                !isGhost && isDraggingEvent && dimDragSource && styles.dragging
            )}
            // @ts-ignore
            style={{...cellStyle, ...topicCellStyle, ...zIndexStyle}}
            onMouseEnter={isGhost ? undefined : _hoverStart}
            onMouseOut={isGhost ? undefined : _hoverEnd}
            onDragStart={isGhost ? undefined : _onDragStart}
            onDragEnd={isGhost ? undefined : _onDragEnd}
        >
            <div className={styles.colorLineContainer}>
                {cellColorTags.map((color, i) =>
                    <div
                        key={`${calendarEvent?.calendarEventId}-color-tag-${color}-${i}`}
                        className={styles.colorLine}
                        style={{backgroundColor: color}}/>
                )}
            </div>

            <div
                className={cn(styles.title, isCompleted && styles["title-line-through"])}
                onMouseEnter={_hoverStart}
                onMouseOut={_hoverEnd}
            >
                {calendarEvent && (calendarEvent.title ? calendarEvent.title : t("calendarTitleNotProvided"))}
            </div>
        </Link>
    );
};

export default TaskPositionBasedCell;
