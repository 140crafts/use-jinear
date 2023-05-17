import { TeamDto, WorkspaceDto } from "@/model/be/jinear-core";
import { useRetrieveAllIntersectingTasksFromTeamQuery, useRetrieveAllIntersectingTasksQuery } from "@/store/api/taskListingApi";
import Logger from "@/utils/logger";
import { CircularProgress } from "@mui/material";
import cn from "classnames";
import { eachDayOfInterval, endOfMonth, endOfWeek, format, parse, startOfDay, startOfWeek } from "date-fns";
import React, { useMemo, useState } from "react";
import styles from "./Calendar.module.css";
import { ICalendarWeekRowCell, calculateHitMissTable } from "./calendarUtils";
import CalendarContext from "./context/CalendarContext";
import CalendarHeader from "./header/CalendarHeader";
import Month from "./month/Month";
interface CalendarProps {
  initialDate?: Date;
  workspace: WorkspaceDto;
  className?: string;
}

const logger = Logger("Calendar");

const Calendar: React.FC<CalendarProps> = ({ workspace, initialDate = startOfDay(new Date()), className }) => {
  const [filterBy, setFilterBy] = useState<TeamDto>();
  const [highlightedTaskId, setHighlightedTaskId] = useState<string>("");
  const [viewingDate, setViewingDate] = useState(initialDate);
  const currentMonth = format(viewingDate, "MMM-yyyy");
  const firstDayCurrentMonth = parse(currentMonth, "MMM-yyyy", new Date());
  const periodStart = startOfWeek(firstDayCurrentMonth, { weekStartsOn: 1 });
  const periodEnd = endOfWeek(endOfMonth(firstDayCurrentMonth), { weekStartsOn: 1 });
  const days = eachDayOfInterval({ start: periodStart, end: periodEnd });
  const query = filterBy ? useRetrieveAllIntersectingTasksFromTeamQuery : useRetrieveAllIntersectingTasksQuery;
  const {
    data: taskListingResponse,
    isFetching,
    isSuccess,
  } = query(
    {
      workspaceId: workspace.workspaceId,
      timespanStart: periodStart,
      timespanEnd: periodEnd,
      teamId: filterBy ? filterBy.teamId : "",
    },
    { skip: workspace == null }
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
        setViewingDate,
        periodStart,
        periodEnd,
        tasks: taskListingResponse?.data,
      }}
    >
      <div className={cn(styles.container, className)}>
        <CalendarHeader days={days} workspace={workspace} filterBy={filterBy} setFilterBy={setFilterBy} />
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
