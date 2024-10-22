"use client";
import {
  queryStateDateToShortDateConverter,
  queryStateShortDateParser,
  useQueryState,
  useSetQueryStateMultiple
} from "@/hooks/useQueryState";
import { useWorkspaceFirstTeam } from "@/hooks/useWorkspaceFirstTeam";
import { CalendarEventDto, WorkspaceDto } from "@/model/be/jinear-core";
import Logger from "@/utils/logger";
import cn from "classnames";
import { startOfDay } from "date-fns";
import React, { useEffect, useState } from "react";
import styles from "./Calendar.module.scss";
import CalendarContext from "./context/CalendarContext";
import DayView from "./dayView/DayView";
import CalendarHeader from "./header/CalendarHeader";
import MonthView from "./monthView/MonthView";
import TwoDayView from "./twoDayView/TwoDayView";
import WeekView from "./weekView/WeekView";
import {
  CalendarViewType,
  storeCalendarViewType,
  useStoredCalendarViewType
} from "@/components/calendar/calendarUtils";

interface CalendarProps {
  workspace: WorkspaceDto;
  className?: string;
}


const logger = Logger("Calendar");

const Calendar: React.FC<CalendarProps> = ({ workspace, className }) => {
  const setQueryStateMultiple = useSetQueryStateMultiple();
  const [highlightedEventId, setHighlightedEventId] = useState<string>("");
  const [squeezedView, setSqueezedView] = useState<boolean>(true);
  const defaultCalendarViewType = useStoredCalendarViewType();
  const viewType = useQueryState<CalendarViewType>("viewType");
  const viewingDate = useQueryState<Date>("viewingDate", queryStateShortDateParser);

  const [draggingEvent, setDraggingEvent] = useState<CalendarEventDto>();
  const [ghostEvent, setGhostEvent] = useState<CalendarEventDto>();
  const [calenderLoading, setCalenderLoading] = useState<boolean>(false);

  const workspacesFirstTeam = useWorkspaceFirstTeam(workspace.workspaceId);

  useEffect(() => {
    const nextQueryState = new Map<string, string>([]);
    if (!viewType) {
      storeCalendarViewType(defaultCalendarViewType);
      nextQueryState.set("viewType", defaultCalendarViewType);
    }
    if (!viewingDate) {
      const initialDay = startOfDay(new Date());
      nextQueryState.set("viewingDate", queryStateDateToShortDateConverter(initialDay) as string);
    }
    logger.log({ nextQueryState, viewType });
    setQueryStateMultiple(nextQueryState);
  }, [JSON.stringify(viewType), JSON.stringify(viewingDate), defaultCalendarViewType]);

  return (
    <CalendarContext.Provider
      value={{
        highlightedEventId,
        setHighlightedEventId,
        squeezedView,
        setSqueezedView,
        workspace,
        newTasksFromTeam: workspacesFirstTeam,
        draggingEvent,
        setDraggingEvent,
        ghostEvent,
        setGhostEvent,
        calenderLoading,
        setCalenderLoading
      }}
    >
      <div className={cn(styles.container, className)}>
        <CalendarHeader />
        {workspace && viewType == "m" && <MonthView workspace={workspace} />}
        {workspace && viewType == "w" && <WeekView workspace={workspace} />}
        {workspace && viewType == "d" && <DayView workspace={workspace} />}
        {workspace && viewType == "2d" && <TwoDayView workspace={workspace} />}
      </div>
    </CalendarContext.Provider>
  );
};

export default Calendar;
