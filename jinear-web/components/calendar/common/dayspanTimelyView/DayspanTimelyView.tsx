import { CalendarEventDto, WorkspaceDto } from "@/model/be/jinear-core";
import { useFilterCalendarEventsQuery } from "@/store/api/calendarEventApi";
import { getOffset, getSize } from "@/utils/htmlUtis";
import Logger from "@/utils/logger";
import cn from "classnames";
import React, { useEffect, useMemo, useRef } from "react";
import { ICalendarWeekRowCell, calculateHitMissTable, isDateBetween } from "../../calendarUtils";
import AllDayTasks from "../../weekView/allDayTasks/AllDayTasks";
import TimelyView from "../../weekView/timelyView/TimelyView";
import WeekDays from "../../weekView/weekDays/WeekDays";
import OverlayLoading from "../overlayLoading/OverlayLoading";
import styles from "./DayspanTimelyView.module.scss";

interface DayspanTimelyViewProps {
  workspace: WorkspaceDto;
  viewingDate: Date;
  periodStart: Date;
  periodEnd: Date;
  days: Date[];
  ghostEvent?: CalendarEventDto;
}

const logger = Logger("DayspanTimelyView");

const DayspanTimelyView: React.FC<DayspanTimelyViewProps> = ({
  workspace,
  ghostEvent,
  viewingDate,
  periodStart,
  periodEnd,
  days,
}) => {
  const weekViewContainerRef = useRef<HTMLDivElement>(null);

  const { data: filterResponse, isFetching } = useFilterCalendarEventsQuery(
    {
      workspaceId: workspace?.workspaceId || "",
      timespanStart: periodStart,
      timespanEnd: periodEnd,
    },
    { skip: workspace == null }
  );

  const weekTableWithoutPreciseDates: ICalendarWeekRowCell[][][] | undefined = useMemo(() => {
    if (!filterResponse || !filterResponse.data) {
      return;
    }
    const responseEvents = filterResponse.data;
    const events = [...responseEvents];
    if (ghostEvent) {
      events.unshift(ghostEvent);
    }

    return calculateHitMissTable({
      events,
      days,
      excludePreciseAssignedDates: true,
      excludePreciseDueDates: true,
      rowCount: 1,
    });
  }, [JSON.stringify(days), JSON.stringify(filterResponse), JSON.stringify(ghostEvent)]);

  const weekTasksWithPreciseDates = useMemo(
    () => filterResponse?.data.filter((event) => event.hasPreciseAssignedDate || event.hasPreciseDueDate) || [],
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
  }, [periodStart?.getTime(), viewingDate?.getTime(), periodEnd?.getTime(), JSON.stringify(ghostEvent)]);

  return (
    <div ref={weekViewContainerRef} className={styles.weekViewContainer}>
      <div className={cn(styles.weekViewContentContainer)}>
        <WeekDays days={days} />
        <AllDayTasks days={days} weekTable={weekTableWithoutPreciseDates} />
        <TimelyView days={days} events={weekTasksWithPreciseDates} />
        <OverlayLoading isFetching={isFetching} />
      </div>
    </div>
  );
};

export default DayspanTimelyView;
