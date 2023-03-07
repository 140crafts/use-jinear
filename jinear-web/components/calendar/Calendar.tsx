import { TaskDto } from "@/model/be/jinear-core";
import { useRetrieveAllIntersectingTasksQuery } from "@/store/api/taskListingApi";
import Logger from "@/utils/logger";
import { CircularProgress } from "@mui/material";
import { eachDayOfInterval, endOfMonth, endOfWeek, format, parse, startOfDay, startOfWeek } from "date-fns";
import useTranslation from "locales/useTranslation";
import React, { useMemo, useState } from "react";
import styles from "./Calendar.module.css";
import { calculateHitMissTable } from "./calendarUtils";
import CalendarContext from "./context/CalendarContext";
import DayCell from "./dayCell/DayCell";

interface CalendarProps {
  initialDate?: Date;
  workspaceId: string;
}

const logger = Logger("Calendar");

const Calendar: React.FC<CalendarProps> = ({ workspaceId, initialDate = startOfDay(new Date()) }) => {
  const { t } = useTranslation();
  const [highlightedTaskId, setHighlightedTaskId] = useState<string>("");
  const [viewingDate, setViewingDate] = useState(initialDate);
  const [selectedDate, setSelectedDate] = useState(initialDate);

  const currentMonth = format(viewingDate, "MMM-yyyy");
  const firstDayCurrentMonth = parse(currentMonth, "MMM-yyyy", new Date());
  const periodStart = startOfWeek(firstDayCurrentMonth, { weekStartsOn: 1 });
  const periodEnd = endOfWeek(endOfMonth(firstDayCurrentMonth), { weekStartsOn: 1 });

  const days = eachDayOfInterval({ start: periodStart, end: periodEnd });
  const {
    data: taskListingResponse,
    isFetching,
    isSuccess,
  } = useRetrieveAllIntersectingTasksQuery(
    {
      workspaceId,
      timespanStart: periodStart,
      timespanEnd: periodEnd,
    },
    { skip: workspaceId == null }
  );

  const allTasksAllHitMissTable: (TaskDto | null)[][] | undefined = useMemo(() => {
    if (!taskListingResponse || !taskListingResponse.data) {
      return;
    }
    const tasks = taskListingResponse.data;
    return calculateHitMissTable(tasks, days);
  }, [JSON.stringify(days), JSON.stringify(taskListingResponse)]);

  return (
    <CalendarContext.Provider
      value={{
        highlightedTaskId,
        setHighlightedTaskId,
        viewingDate,
        periodStart,
        periodEnd,
        selectedDate,
        tasks: taskListingResponse?.data,
      }}
    >
      <div className={styles.container}>
        <div className={styles.weekdayHeaderContainer}>
          {days.slice(0, 7).map((day) => (
            <div key={`header-${day.getTime()}`} className={styles.weekdayHeader}>
              {format(day, "E", { weekStartsOn: 1 })}
            </div>
          ))}
        </div>
        <div className={styles.days}>
          {allTasksAllHitMissTable &&
            days?.map((day, dayIndex) => (
              <DayCell key={`calendar-day-cell-${day.toISOString()}`} day={day} tasksOnDay={allTasksAllHitMissTable[dayIndex]} />
            ))}
        </div>

        {isFetching && (
          <div className={styles.loadingContainer}>
            <CircularProgress />
          </div>
        )}
      </div>
    </CalendarContext.Provider>
  );
};

export default Calendar;
