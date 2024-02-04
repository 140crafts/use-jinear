import { CalendarEventDto } from "@/model/be/jinear-core";
import React, { useMemo } from "react";
import styles from "./DayTimelyView.module.css";

interface DayTimelyViewProps {
  day: Date;
  events: CalendarEventDto[];
  minuteInPx: number;
}

import { calculateTaskDayPositions, filterTasksByDay } from "@/components/calendar/calendarUtils";
import { useCalendarNewTaskFromTeam, useCalendarWorkspace } from "@/components/calendar/context/CalendarContext";
import { popNewTaskModal } from "@/store/slice/modalSlice";
import { useAppDispatch } from "@/store/store";
import Logger from "@/utils/logger";
import cn from "classnames";
import { addHours, isToday, startOfDay } from "date-fns";
import CurrentTimeLine from "../currentTimeLine/CurrentTimeLine";
import HourTile from "./hourTile/HourTile";
import TaskPositionBasedCell from "./taskPositionBasedCell/TaskPositionBasedCell";

const logger = Logger("DayTimelyView");

const DayTimelyView: React.FC<DayTimelyViewProps> = ({ day, events, minuteInPx }) => {
  const dispatch = useAppDispatch();
  const workspace = useCalendarWorkspace();
  const team = useCalendarNewTaskFromTeam();

  const _isToday = isToday(day);

  const dayPositionCells = useMemo(
    () => calculateTaskDayPositions({ events: filterTasksByDay(events, day), day, minuteInPx }),
    [JSON.stringify(day), JSON.stringify(events), minuteInPx]
  );

  logger.log({ day, dayPositionCells });

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

  return (
    <div className={cn(styles.container, _isToday && styles.today)}>
      {dayPositionCells.map((cell) => (
        <TaskPositionBasedCell key={`week-view-cell-${cell?.event?.calendarEventId}`} cell={cell} />
      ))}

      {_isToday && <CurrentTimeLine variant="solid" withLabel={false} calendarWeekViewDayMinutePixelRatio={minuteInPx} />}

      {[...new Array(24)].map((_, i) => (
        <HourTile
          key={`${day}-tile-${i}`}
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
