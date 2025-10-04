import { queryStateArrayParser, queryStateShortDateParser, useQueryState } from "@/hooks/useQueryState";
import { WorkspaceDto } from "@/model/be/jinear-core";
import { useFilterCalendarEventsQuery } from "@/store/api/calendarEventApi";
import Logger from "@/utils/logger";
import { eachDayOfInterval, endOfMonth, endOfWeek, format, parse, startOfDay, startOfWeek } from "date-fns";
import React, { useMemo } from "react";
import { ICalendarWeekRowCell, calculateHitMissTable } from "../calendarUtils";
import OverlayLoading from "../common/overlayLoading/OverlayLoading";
import { useCalenderLoading, useGhostEvent, useSqueezedView } from "../context/CalendarContext";
import Month from "./month/Month";

interface MonthViewProps {
  workspace: WorkspaceDto;
}

const logger = Logger("MonthView");

const MonthView: React.FC<MonthViewProps> = ({ workspace }) => {
  const squeezedView = useSqueezedView();
  const hiddenCalendars = useQueryState<string[]>("hiddenCalendars", queryStateArrayParser) || [];
  const hiddenTeams = useQueryState<string[]>("hiddenTeams", queryStateArrayParser) || [];
  const taskBoards = useQueryState<string[]>("taskBoards", queryStateArrayParser) || [];

  const viewingDate = useQueryState<Date>("viewingDate", queryStateShortDateParser) || startOfDay(new Date());

  const currentMonth = format(viewingDate, "MMM-yyyy");
  const firstDayCurrentMonth = parse(currentMonth, "MMM-yyyy", new Date());
  const periodStart = startOfWeek(firstDayCurrentMonth, { weekStartsOn: 1 });
  const periodEnd = endOfWeek(endOfMonth(firstDayCurrentMonth), { weekStartsOn: 1 });
  const days = eachDayOfInterval({ start: periodStart, end: periodEnd });

  const ghostEvent = useGhostEvent();
  const calendarLoading = useCalenderLoading();

  const { data: filterResponse, isFetching } = useFilterCalendarEventsQuery(
    {
      workspaceId: workspace?.workspaceId || "",
      taskboardIds: taskBoards,
      timespanStart: periodStart,
      timespanEnd: periodEnd
    },
    { skip: workspace == null }
  );

  const monthTable: ICalendarWeekRowCell[][][] | undefined = useMemo(() => {
    if (!filterResponse || !filterResponse.data) {
      return;
    }
    const responseEvents = filterResponse.data.filter((val) => {
      const lookUpSource = val.calendarEventSourceType == "TASK" ? hiddenTeams : hiddenCalendars;
      const lookUpValue =
        val.calendarEventSourceType == "TASK" ? val.relatedTask?.teamId : val.externalCalendarSourceDto?.externalCalendarSourceId;
      return lookUpSource.findIndex((value) => value == lookUpValue) == -1;
    });

    const events = [...responseEvents];
    if (ghostEvent) {
      events.unshift(ghostEvent);
    }

    return calculateHitMissTable({ events, days }) ?? [[], [], [], [], []];
  }, [
    JSON.stringify(days),
    JSON.stringify(filterResponse),
    JSON.stringify(ghostEvent),
    JSON.stringify(hiddenTeams),
    JSON.stringify(hiddenCalendars)
  ]);

  return (
    <>
      <Month monthTable={monthTable ?? [[], [], [], [], []]} days={days} squeezedView={squeezedView} />
      <OverlayLoading isFetching={isFetching || calendarLoading} />
    </>
  );
};

export default MonthView;
