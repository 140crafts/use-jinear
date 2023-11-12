import { useFilterTasksQuery } from "@/store/api/taskListingApi";
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
    const tasks = filterResponse.data.content;
    return calculateHitMissTable({ tasks, days });
  }, [JSON.stringify(days), JSON.stringify(filterResponse)]);

  return (
    <>
      {monthTable && <Month monthTable={monthTable} days={days} squeezedView={squeezedView} />}
      <OverlayLoading isFetching={isFetching} />
    </>
  );
};

export default MonthView;
