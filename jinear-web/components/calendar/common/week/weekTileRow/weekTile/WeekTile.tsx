import {
  useCalendarNewTaskFromTeam,
  useCalendarWorkspace,
  useDraggingTask,
  useGhostTask,
  useSetCalenderLoading,
  useSetGhostTask,
} from "@/components/calendar/context/CalendarContext";
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
  const draggingTask = useDraggingTask();
  const ghostTask = useGhostTask();
  const setGhostTask = useSetGhostTask();
  const setCalendarLoading = useSetCalenderLoading();
  const [dragHovering, setDragHovering] = useState<boolean>(false);
  const [updateTaskDates, { isLoading: isUpdateTaskDatesLoading }] = useUpdateTaskDatesMutation();

  const popNewTaskModalWithAssignedDatePreSelected = (initialAssignedDate: Date) => {
    dispatch(popNewTaskModal({ visible: true, workspace, team, initialAssignedDate }));
  };

  useEffect(() => {
    if (dragHovering && draggingTask && day) {
      logger.log({ dragHovering, draggingTask, day });
      const draggingOverDay = day;
      const ghostTask = { ...draggingTask };
      const draggingTaskAssignedDay = ghostTask.assignedDate;
      const draggingTaskDueDate = ghostTask.dueDate;
      if (draggingTaskAssignedDay && draggingTaskDueDate) {
        const duration = Math.abs(differenceInMilliseconds(new Date(draggingTaskAssignedDay), new Date(draggingTaskDueDate)));
        const newAssignedDate = draggingOverDay;
        const newDueDate = addMilliseconds(newAssignedDate, duration);
        ghostTask.assignedDate = newAssignedDate;
        ghostTask.dueDate = newDueDate;
        ghostTask.taskId += "-dragging";
        setGhostTask?.(ghostTask);
      } else if (draggingTaskAssignedDay) {
        const newAssignedDate = draggingOverDay;
        ghostTask.assignedDate = newAssignedDate;
        ghostTask.taskId += "-dragging";
        setGhostTask?.(ghostTask);
      } else if (draggingTaskDueDate) {
        const newDueDate = draggingOverDay;
        ghostTask.dueDate = newDueDate;
        ghostTask.taskId += "-dragging";
        setGhostTask?.(ghostTask);
      }
    }
  }, [JSON.stringify(draggingTask), dragHovering, JSON.stringify(day)]);

  useEffect(() => {
    setCalendarLoading?.(isUpdateTaskDatesLoading);
  }, [isUpdateTaskDatesLoading, setCalendarLoading]);

  const _onDragEnter = (event: React.DragEvent) => {
    setDragHovering(true);
  };

  const _onDragLeave = (event: React.DragEvent) => {
    setDragHovering(false);
  };

  const _onDrop = (event: React.DragEvent<HTMLDivElement>, date: Date) => {
    logger.log({ _onDrop: event, draggingTask, ghostTask, date });
    if (draggingTask && ghostTask) {
      const req = {
        assignedDate: ghostTask.assignedDate,
        dueDate: ghostTask.dueDate,
        hasPreciseAssignedDate: ghostTask.hasPreciseAssignedDate,
        hasPreciseDueDate: ghostTask.hasPreciseDueDate,
      };
      const currentVals = {
        assignedDate: draggingTask.assignedDate ? new Date(draggingTask.assignedDate) : undefined,
        dueDate: draggingTask.dueDate ? new Date(draggingTask.dueDate) : undefined,
        hasPreciseAssignedDate: draggingTask.hasPreciseAssignedDate,
        hasPreciseDueDate: draggingTask.hasPreciseDueDate,
      };
      if (JSON.stringify(currentVals) != JSON.stringify(req)) {
        logger.log({ currentVals, req });
        updateTaskDates({
          taskId: draggingTask.taskId,
          body: req,
        });
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
