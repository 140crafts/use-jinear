import type {CalendarEventDto} from "@/model/be/jinear-core";
import React, {useEffect, useMemo, useRef} from "react";
import styles from "./DayTimelyView.module.css";

interface DayTimelyViewProps {
    day: Date;
    events: CalendarEventDto[];
    minuteInPx: number;
}

import {
    calculateDraggedCellDates,
    calculateTaskDayPositions,
    convertTaskToCell,
    filterTasksByDay
} from "@/components/calendar/calendarUtils";
import {
    useCalendarNewTaskFromTeam,
    useCalendarWorkspace,
    useDayTimelyViewDragGrabOffsetMinutes,
    useDayTimelyViewDraggingEvent,
    useDraggingOnHourTile,
    useSetCalenderLoading,
    useSetDayTimelyViewDragGrabOffsetMinutes,
    useSetDayTimelyViewDraggingEvent,
    useSetDraggingOnHourTile
} from "@/components/calendar/context/CalendarContext";
import {useUpdateCalendarEventDatesMutation} from "@/store/api/calendarEventApi";
import {useUpdateTaskDatesMutation} from "@/store/api/taskUpdateApi";
import {popNewTaskModal} from "@/store/slice/modalSlice";
import {useAppDispatch} from "@/store";
import Logger from "@/util/logger";
import cn from "classnames";
import {addHours, addMinutes, isToday, startOfDay} from "date-fns";
import CurrentTimeLine from "../currentTimeLine/CurrentTimeLine";
import HourTile, {HOUR_TILE_SLOT_IN_MINUTES} from "./hourTile/HourTile";
import TaskPositionBasedCell from "./taskPositionBasedCell/TaskPositionBasedCell";

const logger = Logger("DayTimelyView");

const MINUTES_IN_A_DAY = 24 * 60;

const DayTimelyView: React.FC<DayTimelyViewProps> = ({day, events, minuteInPx}) => {
    const dispatch = useAppDispatch();
    const containerRef = useRef<HTMLDivElement>(null);
    const workspace = useCalendarWorkspace();
    const team = useCalendarNewTaskFromTeam();

    const dayTimelyViewDraggingEvent = useDayTimelyViewDraggingEvent();
    const setDayTimelyViewDraggingEvent = useSetDayTimelyViewDraggingEvent();
    const draggingOnHourTile = useDraggingOnHourTile();
    const setDraggingOnHourTile = useSetDraggingOnHourTile();
    const dayTimelyViewDragGrabOffsetMinutes = useDayTimelyViewDragGrabOffsetMinutes();
    const setDayTimelyViewDragGrabOffsetMinutes = useSetDayTimelyViewDragGrabOffsetMinutes();
    const setCalendarLoading = useSetCalenderLoading();

    const [updateTaskDates, {isLoading: isUpdateTaskDatesLoading}] = useUpdateTaskDatesMutation();
    const [updateCalendarEventDates, {isLoading: isUpdateCalendarEventDatesLoading}] = useUpdateCalendarEventDatesMutation();
    const isLoading = isUpdateTaskDatesLoading || isUpdateCalendarEventDatesLoading;

    const _isToday = isToday(day);
    const dayTime = day.getTime();

    const dayPositionCells = useMemo(
        () => calculateTaskDayPositions({events: filterTasksByDay(events, day), day, minuteInPx}),
        [JSON.stringify(day), JSON.stringify(events), minuteInPx]
    );

    const ghostCell = useMemo(() => {
        const draggingEventDto = dayTimelyViewDraggingEvent?.event;
        if (!dayTimelyViewDraggingEvent || !draggingEventDto || !draggingOnHourTile) {
            return undefined;
        }
        const draggedDates = calculateDraggedCellDates(
            dayTimelyViewDraggingEvent,
            draggingOnHourTile,
            dayTimelyViewDragGrabOffsetMinutes ?? 0,
            HOUR_TILE_SLOT_IN_MINUTES
        );
        const ghostEvent = {
            ...draggingEventDto,
            calendarEventId: `${draggingEventDto.calendarEventId}-dragging`,
            assignedDate: draggedDates.assignedDate,
            dueDate: draggedDates.dueDate
        } as CalendarEventDto;
        if (filterTasksByDay([ghostEvent], day).length == 0) {
            return undefined;
        }
        return convertTaskToCell(ghostEvent, day, minuteInPx);
    }, [dayTimelyViewDraggingEvent, draggingOnHourTile, dayTimelyViewDragGrabOffsetMinutes, dayTime, minuteInPx]);

    useEffect(() => {
        setCalendarLoading?.(isLoading);
    }, [isLoading, setCalendarLoading]);

    const popNewTaskModalWithAssignedDatePreSelected = (initialAssignedDate: Date) => {
        dispatch(
            popNewTaskModal({
                visible: true,
                workspace,
                team,
                initialAssignedDate,
                initialAssignedDateIsPrecise: true,
                initialDueDate: addHours(initialAssignedDate, 1),
                initialDueDateIsPrecise: true,
            })
        );
    };

    // The column reads the slot from the cursor instead of letting each hour tile report it.
    // Task cells sit over the tiles, and taking their pointer events away inside dragstart
    // makes chrome cancel the drag.
    const retrieveSlotFromPointer = (clientY: number) => {
        const rect = containerRef.current?.getBoundingClientRect();
        if (!rect || !minuteInPx) {
            return undefined;
        }
        const minutes = (clientY - rect.top) / minuteInPx;
        const snapped = Math.floor(minutes / HOUR_TILE_SLOT_IN_MINUTES) * HOUR_TILE_SLOT_IN_MINUTES;
        const limited = Math.min(Math.max(snapped, 0), MINUTES_IN_A_DAY - HOUR_TILE_SLOT_IN_MINUTES);
        return addMinutes(startOfDay(day), limited);
    };

    const _onDragOver = (event: React.DragEvent<HTMLDivElement>) => {
        if (!dayTimelyViewDraggingEvent) {
            return;
        }
        event.preventDefault();
        const slotDate = retrieveSlotFromPointer(event.clientY);
        if (slotDate && slotDate.getTime() != draggingOnHourTile?.getTime()) {
            setDraggingOnHourTile?.(slotDate);
        }
    };

    const _onDrop = (event: React.DragEvent<HTMLDivElement>) => {
        if (!dayTimelyViewDraggingEvent) {
            return;
        }
        event.preventDefault();
        const slotDate = retrieveSlotFromPointer(event.clientY);
        if (slotDate) {
            _onSlotDrop(slotDate);
        }
    };

    const _onSlotDrop = (slotDate: Date) => {
        const draggingCell = dayTimelyViewDraggingEvent;
        const draggingEventDto = draggingCell?.event;
        if (draggingCell && draggingEventDto) {
            const draggedDates = calculateDraggedCellDates(
                draggingCell,
                slotDate,
                dayTimelyViewDragGrabOffsetMinutes ?? 0,
                HOUR_TILE_SLOT_IN_MINUTES
            );
            // The backend replaces both dates, so an omitted date would wipe the stored one.
            const req = {
                assignedDate: draggedDates.assignedDate,
                dueDate: draggedDates.dueDate,
                hasPreciseAssignedDate: draggingEventDto.hasPreciseAssignedDate,
                hasPreciseDueDate: draggingEventDto.hasPreciseDueDate,
            };
            const currentVals = {
                assignedDate: draggingEventDto.assignedDate ? new Date(draggingEventDto.assignedDate) : undefined,
                dueDate: draggingEventDto.dueDate ? new Date(draggingEventDto.dueDate) : undefined,
                hasPreciseAssignedDate: draggingEventDto.hasPreciseAssignedDate,
                hasPreciseDueDate: draggingEventDto.hasPreciseDueDate,
            };
            if (JSON.stringify(currentVals) != JSON.stringify(req)) {
                logger.log({_onSlotDrop: slotDate, currentVals, req});
                if (draggingEventDto.calendarEventSourceType == "TASK" && draggingEventDto.relatedTask) {
                    updateTaskDates({
                        taskId: draggingEventDto.relatedTask.taskId,
                        body: req,
                    });
                } else if (draggingEventDto.externalCalendarSourceDto && draggedDates.assignedDate && draggedDates.dueDate) {
                    updateCalendarEventDates({
                        calendarId: draggingEventDto.calendarId,
                        calendarSourceId: draggingEventDto.externalCalendarSourceDto.externalCalendarSourceId,
                        calendarEventId: draggingEventDto.calendarEventId,
                        assignedDate: draggedDates.assignedDate,
                        dueDate: draggedDates.dueDate,
                        hasPreciseAssignedDate: draggingEventDto.hasPreciseAssignedDate,
                        hasPreciseDueDate: draggingEventDto.hasPreciseDueDate,
                    });
                }
            }
        }
        // The optimistic cache patch re-lays out the column, which can unmount the drag source
        // before its dragend reaches react. Clear here instead of relying on it.
        setDayTimelyViewDraggingEvent?.(undefined);
        setDraggingOnHourTile?.(undefined);
        setDayTimelyViewDragGrabOffsetMinutes?.(undefined);
    };

    return (
        <div
            ref={containerRef}
            className={cn(styles.container, _isToday && styles.today)}
            onDragOver={_onDragOver}
            onDrop={_onDrop}
        >
            {dayPositionCells.map((cell) => (
                <TaskPositionBasedCell key={`week-view-cell-${cell?.event?.calendarEventId}`} cell={cell}/>
            ))}

            {ghostCell &&
                <TaskPositionBasedCell key={`week-view-cell-${ghostCell?.event?.calendarEventId}`} cell={ghostCell}
                                       isGhost/>}

            {_isToday &&
                <CurrentTimeLine variant="solid" withLabel={false} calendarWeekViewDayMinutePixelRatio={minuteInPx}/>}

            {[...new Array(24)].map((_, i) => (
                <HourTile
                    key={`${day}-tile-${i}`}
                    day={day}
                    hour={i}
                    onClick={() => {
                        const date = addHours(startOfDay(day), i);
                        popNewTaskModalWithAssignedDatePreSelected(date);
                    }}
                />
            ))}
        </div>
    );
};

export default DayTimelyView;
