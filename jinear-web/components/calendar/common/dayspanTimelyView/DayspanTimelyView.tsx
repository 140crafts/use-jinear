import { useFilterTasksQuery } from "@/store/api/taskListingApi";
import { getOffset, getSize } from "@/utils/htmlUtis";
import Logger from "@/utils/logger";
import cn from "classnames";
import React, { useEffect, useMemo, useRef } from "react";
import { ICalendarWeekRowCell, calculateHitMissTable, isDateBetween } from "../../calendarUtils";
import { useCalendarWorkspace, useFilterBy } from "../../context/CalendarContext";
import AllDayTasks from "../../weekView/allDayTasks/AllDayTasks";
import TimelyView from "../../weekView/timelyView/TimelyView";
import WeekDays from "../../weekView/weekDays/WeekDays";
import OverlayLoading from "../overlayLoading/OverlayLoading";
import styles from "./DayspanTimelyView.module.scss";

interface DayspanTimelyViewProps {
  viewingDate: Date;
  periodStart: Date;
  periodEnd: Date;
  days: Date[];
}

const logger = Logger("DayspanTimelyView");

const DayspanTimelyView: React.FC<DayspanTimelyViewProps> = ({ viewingDate, periodStart, periodEnd, days }) => {
  const workspace = useCalendarWorkspace();
  const filterBy = useFilterBy();

  const weekViewContainerRef = useRef<HTMLDivElement>(null);

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

  const weekTableWithoutPreciseDates: ICalendarWeekRowCell[][][] | undefined = useMemo(() => {
    if (!filterResponse || !filterResponse.data.content) {
      return;
    }
    const tasks = filterResponse.data.content;

    return calculateHitMissTable({
      tasks,
      days,
      excludePreciseAssignedDates: true,
      excludePreciseDueDates: true,
      rowCount: 1,
    });
  }, [JSON.stringify(days), JSON.stringify(filterResponse)]);

  const weekTasksWithPreciseDates = useMemo(
    () => filterResponse?.data.content.filter((task) => task.hasPreciseAssignedDate || task.hasPreciseDueDate) || [],
    [JSON.stringify(filterResponse)]
  );

  logger.log({ weekTableWithoutPreciseDates });

  useEffect(() => {
    setTimeout(() => {
      const todayTitle = document.getElementById("calendar-weekday-title-today");
      const currentTimeLine = document.getElementById("calendar-week-view-current-time-line");

      if (viewingDate && isDateBetween(periodStart, viewingDate, periodEnd)) {
        if (todayTitle && currentTimeLine) {
          const todayTitleOffset = getOffset(todayTitle);
          const todayTitleSize = getSize(todayTitle);
          const currentTimeLineOffset = getOffset(currentTimeLine);
          const currentTimeLineSize = getSize(currentTimeLine);
          const pageContent = document.getElementById("workspace-layout-page-content");

          pageContent?.scrollTo?.({
            left: todayTitleOffset.left,
            behavior: "smooth",
          });
          pageContent?.scroll?.({
            top: currentTimeLineOffset.top - currentTimeLineSize.height - 200,
            behavior: "smooth",
          });
        } else {
        }
      }
    }, 500);
  }, [periodStart?.getTime(), viewingDate?.getTime(), periodEnd?.getTime()]);

  return (
    <div ref={weekViewContainerRef} className={styles.weekViewContainer}>
      <div className={cn(styles.weekViewContentContainer)}>
        <WeekDays days={days} />
        <AllDayTasks days={days} weekTable={weekTableWithoutPreciseDates} />
        <TimelyView days={days} tasks={weekTasksWithPreciseDates} />
        <OverlayLoading isFetching={isFetching} />
      </div>
    </div>
  );
};

export default DayspanTimelyView;
