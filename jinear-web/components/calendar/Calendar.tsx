import { useRetrieveAllIntersectingTasksQuery } from "@/store/api/taskListingApi";
import { CircularProgress } from "@mui/material";
import { eachDayOfInterval, endOfMonth, endOfWeek, format, parse, startOfDay, startOfWeek } from "date-fns";
import useTranslation from "locales/useTranslation";
import React, { useState } from "react";
import styles from "./Calendar.module.css";
import CalendarContext from "./context/CalendarContext";
import DayCell from "./dayCell/DayCell";

interface CalendarProps {
  initialDate?: Date;
  workspaceId: string;
}

const Calendar: React.FC<CalendarProps> = ({ workspaceId, initialDate = startOfDay(new Date()) }) => {
  const { t } = useTranslation();
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

  return (
    <CalendarContext.Provider value={{ viewingDate, periodStart, periodEnd, selectedDate, tasks: taskListingResponse?.data }}>
      <div className={styles.container}>
        <div className={styles.days}>
          {days?.map((day) => (
            <DayCell key={`calendar-day-cell-${day.toISOString()}`} day={day} />
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
