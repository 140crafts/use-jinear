import { useRetrieveAllIntersectingTasksQuery } from "@/store/api/taskListingApi";
import Logger from "@/utils/logger";
import { CircularProgress } from "@mui/material";
import { eachDayOfInterval, endOfMonth, endOfWeek, format, parse, startOfDay, startOfWeek } from "date-fns";
import React, { useMemo, useState } from "react";
import styles from "./Calendar.module.css";
import { calculateHitMissTable, ICalendarWeekRowCell } from "./calendarUtils";
import CalendarContext from "./context/CalendarContext";
import CalendarHeader from "./header/CalendarHeader";
import Month from "./month/Month";

interface CalendarProps {
  initialDate?: Date;
  workspaceId: string;
}

const logger = Logger("Calendar");

const Calendar: React.FC<CalendarProps> = ({ workspaceId, initialDate = startOfDay(new Date()) }) => {
  const [highlightedTaskId, setHighlightedTaskId] = useState<string>("");
  const [viewingDate, setViewingDate] = useState(initialDate);
  const [selectedDate, setSelectedDate] = useState(initialDate);
  const [fullSizeDays, setFullSizeDays] = useState<boolean>(false);
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

  const monthTable: ICalendarWeekRowCell[][][] | undefined = useMemo(() => {
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
        fullSizeDays,
        setFullSizeDays,
        tasks: taskListingResponse?.data,
      }}
    >
      <div className={styles.container}>
        <CalendarHeader days={days} />

        {monthTable && <Month monthTable={monthTable} days={days} />}

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
