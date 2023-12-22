import { useFilterTasksQuery } from "@/store/api/taskListingApi";
import { endOfDay, startOfDay } from "date-fns";
import React, { useMemo } from "react";
import { isTaskDatesIntersect } from "../calendarUtils";
import {
  useCalendarWorkspace,
  useFilterBy,
  useSqueezedView,
  useViewingDate,
  useWeekDays,
  useWeekViewPeriodEnd,
  useWeekViewPeriodStart,
} from "../context/CalendarContext";
import styles from "./DayView.module.css";
import TaskList from "./taskList/TaskList";
import WeekDays from "./weekDays/WeekDays";

interface DayViewProps {}

const DayView: React.FC<DayViewProps> = ({}) => {
  const workspace = useCalendarWorkspace();
  const filterBy = useFilterBy();
  const periodStart = useWeekViewPeriodStart();
  const periodEnd = useWeekViewPeriodEnd();
  const days = useWeekDays();
  const squeezedView = useSqueezedView();
  const viewingDate = useViewingDate();

  const {
    data: filterResponse,
    isFetching,
    isLoading,
  } = useFilterTasksQuery(
    {
      workspaceId: workspace?.workspaceId || "",
      teamIdList: filterBy ? [filterBy.teamId] : undefined,
      timespanStart: periodStart,
      timespanEnd: periodEnd,
    },
    { skip: workspace == null }
  );

  const viewingDayTasks = useMemo(() => {
    const dateSpan = [startOfDay(viewingDate), endOfDay(viewingDate)];
    return filterResponse?.data.content.filter((task) => isTaskDatesIntersect(task, dateSpan));
  }, [filterResponse, viewingDate]);

  return (
    <div className={styles.container}>
      <WeekDays days={days} />
      <div className={styles.listContainer}>
        {viewingDayTasks && <TaskList viewingDayTasks={viewingDayTasks} className={styles.taskList} />}
      </div>
    </div>
  );
};

export default DayView;
