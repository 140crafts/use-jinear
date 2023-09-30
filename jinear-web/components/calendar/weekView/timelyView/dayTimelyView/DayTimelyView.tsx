import { TaskDto } from "@/model/be/jinear-core";
import React, { useMemo } from "react";
import styles from "./DayTimelyView.module.css";

interface DayTimelyViewProps {
  day: Date;
  tasks: TaskDto[];
  minuteInPx: number;
}

import { calculateTaskDayPositions, filterTasksByDay } from "@/components/calendar/calendarUtils";
import { useCalendarNewTaskFromTeam, useCalendarWorkspace } from "@/components/calendar/context/CalendarContext";
import { popNewTaskModal } from "@/store/slice/modalSlice";
import { useAppDispatch } from "@/store/store";
import Logger from "@/utils/logger";
import { addHours, startOfDay } from "date-fns";
import TaskPositionBasedCell from "./taskPositionBasedCell/TaskPositionBasedCell";
import Tile from "./tile/Tile";

const logger = Logger("DayTimelyView");

const DayTimelyView: React.FC<DayTimelyViewProps> = ({ day, tasks, minuteInPx }) => {
  const dispatch = useAppDispatch();
  const workspace = useCalendarWorkspace();
  const team = useCalendarNewTaskFromTeam();

  const dayPositionCells = useMemo(
    () => calculateTaskDayPositions({ tasks: filterTasksByDay(tasks, day), day, minuteInPx }),
    [JSON.stringify(day), JSON.stringify(tasks), minuteInPx]
  );

  logger.log({ day, dayPositionCells });

  const popNewTaskModalWithAssignedDatePreSelected = (initialAssignedDate: Date) => {
    dispatch(popNewTaskModal({ visible: true, workspace, team, initialAssignedDate }));
  };

  return (
    <div className={styles.container}>
      {dayPositionCells.map((cell) => (
        <TaskPositionBasedCell key={`week-view-cell-${cell?.task?.taskId}`} cell={cell} />
      ))}
      {[...new Array(24)].map((_, i) => (
        <Tile
          key={`${day}-tile-${i}`}
          onClick={() => {
            const date = addHours(startOfDay(new Date()), i);
            popNewTaskModalWithAssignedDatePreSelected(date);
          }}
        />
      ))}
    </div>
  );
};

export default DayTimelyView;
