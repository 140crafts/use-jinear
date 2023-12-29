import { useFilterTasksQuery } from "@/store/api/taskListingApi";
import React, { useMemo } from "react";
import { ICalendarWeekRowCell, calculateHitMissTable } from "../calendarUtils";
import OverlayLoading from "../common/overlayLoading/OverlayLoading";
import {
  useCalendarDays,
  useCalendarWorkspace,
  useCalenderLoading,
  useFilterBy,
  useGhostTask,
  usePeriodEnd,
  usePeriodStart,
  useSqueezedView,
} from "../context/CalendarContext";
import Month from "./month/Month";

interface MonthViewProps {}

import Logger from "@/utils/logger";

const logger = Logger("MonthView");

const MonthView: React.FC<MonthViewProps> = ({}) => {
  const workspace = useCalendarWorkspace();
  const filterBy = useFilterBy();
  const periodStart = usePeriodStart();
  const periodEnd = usePeriodEnd();
  const days = useCalendarDays();
  const squeezedView = useSqueezedView();

  const ghostTask = useGhostTask();
  const calendarLoading = useCalenderLoading();

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

  const monthTable: ICalendarWeekRowCell[][][] | undefined = useMemo(() => {
    if (!filterResponse || !filterResponse.data.content) {
      return;
    }
    const responseTasks = filterResponse.data.content;
    const tasks = [...responseTasks];
    if (ghostTask) {
      tasks.unshift(ghostTask);
    }

    return calculateHitMissTable({ tasks, days });
  }, [JSON.stringify(days), JSON.stringify(filterResponse), JSON.stringify(ghostTask)]);

  return (
    <>
      {monthTable && <Month monthTable={monthTable} days={days} squeezedView={squeezedView} />}
      <OverlayLoading isFetching={isFetching || calendarLoading} />
    </>
  );
};

export default MonthView;
