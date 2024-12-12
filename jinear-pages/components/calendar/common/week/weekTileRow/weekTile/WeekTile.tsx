import {
  useCalendarNewTaskFromTeam,
  useCalendarWorkspace,
  useDraggingEvent,
  useGhostEvent,
  useSetCalenderLoading,
  useSetGhostEvent,
} from "@/components/calendar/context/CalendarContext";
import { useUpdateCalendarEventDatesMutation } from "@/store/api/calendarEventApi";
import { useUpdateTaskDatesMutation } from "@/store/api/taskUpdateApi";
import { popNewTaskModal } from "@/store/slice/modalSlice";
import { useAppDispatch } from "@/store/store";
import Logger from "@/utils/logger";
import cn from "classnames";
import { addMilliseconds, differenceInMilliseconds, isToday } from "date-fns";
import React, { useEffect, useState } from "react";
import styles from "./WeekTile.module.css";

interface WeekTileProps {
  day: Date;
}

const logger = Logger("WeekTile");
const WeekTile: React.FC<WeekTileProps> = ({ day }) => {
  const dispatch = useAppDispatch();
  const workspace = useCalendarWorkspace();
  const team = useCalendarNewTaskFromTeam();
  const draggingEvent = useDraggingEvent();
  const ghostEvent = useGhostEvent();
  const setGhostEvent = useSetGhostEvent();
  const setCalendarLoading = useSetCalenderLoading();
  const [dragHovering, setDragHovering] = useState<boolean>(false);

  const [updateTaskDates, { isLoading: isUpdateTaskDatesLoading }] = useUpdateTaskDatesMutation();
  const [updateCalendarEventDates, { isLoading: isUpdateCalendarEventDatesLoading }] = useUpdateCalendarEventDatesMutation();
  const isLoading = isUpdateTaskDatesLoading || isUpdateCalendarEventDatesLoading;

  const popNewTaskModalWithAssignedDatePreSelected = (initialAssignedDate: Date) => {
    dispatch(popNewTaskModal({ visible: true, workspace, team, initialAssignedDate }));
  };

  useEffect(() => {
    if (dragHovering && draggingEvent && day) {
      logger.log({ dragHovering, draggingEvent, day });
      const draggingOverDay = day;
      const ghostEvent = { ...draggingEvent };
      const draggingTaskAssignedDay = ghostEvent.assignedDate;
      const draggingTaskDueDate = ghostEvent.dueDate;
      if (draggingTaskAssignedDay && draggingTaskDueDate) {
        const duration = Math.abs(differenceInMilliseconds(new Date(draggingTaskAssignedDay), new Date(draggingTaskDueDate)));
        const newAssignedDate = draggingOverDay;
        const newDueDate = addMilliseconds(newAssignedDate, duration);
        ghostEvent.assignedDate = newAssignedDate;
        ghostEvent.dueDate = newDueDate;
        ghostEvent.calendarEventId += "-dragging";
        setGhostEvent?.(ghostEvent);
      } else if (draggingTaskAssignedDay) {
        const newAssignedDate = draggingOverDay;
        ghostEvent.assignedDate = newAssignedDate;
        ghostEvent.calendarEventId += "-dragging";
        setGhostEvent?.(ghostEvent);
      } else if (draggingTaskDueDate) {
        const newDueDate = draggingOverDay;
        ghostEvent.dueDate = newDueDate;
        ghostEvent.calendarEventId += "-dragging";
        setGhostEvent?.(ghostEvent);
      }
    }
  }, [JSON.stringify(ghostEvent), dragHovering, JSON.stringify(day)]);

  useEffect(() => {
    setCalendarLoading?.(isLoading);
  }, [isLoading, setCalendarLoading]);

  const _onDragEnter = (event: React.DragEvent) => {
    setDragHovering(true);
  };

  const _onDragLeave = (event: React.DragEvent) => {
    setDragHovering(false);
  };

  const _onDrop = (event: React.DragEvent<HTMLDivElement>, date: Date) => {
    logger.log({ _onDrop: event, draggingEvent, ghostEvent, date });
    if (draggingEvent && ghostEvent) {
      const req = {
        assignedDate: ghostEvent.assignedDate,
        dueDate: ghostEvent.dueDate,
        hasPreciseAssignedDate: ghostEvent.hasPreciseAssignedDate,
        hasPreciseDueDate: ghostEvent.hasPreciseDueDate,
      };
      const currentVals = {
        assignedDate: draggingEvent.assignedDate ? new Date(draggingEvent.assignedDate) : undefined,
        dueDate: draggingEvent.dueDate ? new Date(draggingEvent.dueDate) : undefined,
        hasPreciseAssignedDate: draggingEvent.hasPreciseAssignedDate,
        hasPreciseDueDate: draggingEvent.hasPreciseDueDate,
      };
      if (JSON.stringify(currentVals) != JSON.stringify(req)) {
        logger.log({ currentVals, req });
        if (draggingEvent.calendarEventSourceType == "TASK" && draggingEvent.relatedTask) {
          updateTaskDates({
            taskId: draggingEvent.relatedTask.taskId,
            body: req,
          });
        } else {
          draggingEvent.externalCalendarSourceDto &&
            updateCalendarEventDates({
              calendarId: draggingEvent.calendarId,
              calendarSourceId: draggingEvent.externalCalendarSourceDto.externalCalendarSourceId,
              calendarEventId: draggingEvent.calendarEventId,
              assignedDate: ghostEvent.assignedDate,
              dueDate: ghostEvent.dueDate,
              hasPreciseAssignedDate: ghostEvent.hasPreciseAssignedDate,
              hasPreciseDueDate: ghostEvent.hasPreciseDueDate,
            });
        }
      }
    }
    setDragHovering(false);
  };

  return (
    <div
      className={cn(styles.calendarLine, isToday(day) && styles.todayCalendarLine, dragHovering && styles.dragOver)}
      onClick={() => popNewTaskModalWithAssignedDatePreSelected(day)}
      onDragOver={(event) => event.preventDefault()}
      onDragEnter={_onDragEnter}
      onDragLeave={_onDragLeave}
      onDrop={(event) => _onDrop(event, day)}
    />
  );
};

export default WeekTile;
