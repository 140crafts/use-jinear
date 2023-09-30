import { useRetrieveAllIntersectingTasksFromTeamQuery, useRetrieveAllIntersectingTasksQuery } from "@/store/api/taskListingApi";
import React, { useMemo } from "react";
import { ICalendarWeekRowCell, calculateHitMissTable } from "../calendarUtils";
import OverlayLoading from "../common/overlayLoading/OverlayLoading";
import {
  useCalendarDays,
  useCalendarWorkspace,
  useFilterBy,
  usePeriodEnd,
  usePeriodStart,
  useSqueezedView,
} from "../context/CalendarContext";
import Month from "./month/Month";

interface MonthViewProps {}

const MonthView: React.FC<MonthViewProps> = ({}) => {
  const workspace = useCalendarWorkspace();
  const filterBy = useFilterBy();
  const periodStart = usePeriodStart();
  const periodEnd = usePeriodEnd();
  const days = useCalendarDays();
  const squeezedView = useSqueezedView();

  const query = filterBy ? useRetrieveAllIntersectingTasksFromTeamQuery : useRetrieveAllIntersectingTasksQuery;
  const { data: taskListingResponse, isFetching } = query(
    {
      workspaceId: workspace?.workspaceId || "",
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
    return calculateHitMissTable({ tasks, days });
  }, [JSON.stringify(days), JSON.stringify(taskListingResponse)]);

  return (
    <>
      {monthTable && <Month monthTable={monthTable} days={days} squeezedView={squeezedView} />}
      <OverlayLoading isFetching={isFetching} />
    </>
  );
};

export default MonthView;
