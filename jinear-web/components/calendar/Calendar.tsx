"use client";
import { useLocalStorage, useSetLocalStorage } from "@/hooks/useLocalStorage";
import {
  queryStateDateToShortDateConverter,
  queryStateShortDateParser,
  useQueryState,
  useSetQueryState,
} from "@/hooks/useQueryState";
import { useWorkspaceFirstTeam } from "@/hooks/useWorkspaceFirstTeam";
import { CalendarEventDto, WorkspaceDto } from "@/model/be/jinear-core";
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

interface CalendarProps {
  workspace: WorkspaceDto;
  className?: string;
}

export type CalendarViewType = "m" | "w" | "d" | "2d";

const getStoredCalendarViewType = (): CalendarViewType => {
  return useLocalStorage({ key: "calendarViewType", defaultValue: "m" }) as CalendarViewType;
};

const storeCalendarViewType = (value: CalendarViewType) => {
  useSetLocalStorage({ key: "calendarViewType", value });
};

const Calendar: React.FC<CalendarProps> = ({ workspace, className }) => {
  const setQueryState = useSetQueryState();
  const [highlightedEventId, setHighlightedEventId] = useState<string>("");
  const [squeezedView, setSqueezedView] = useState<boolean>(true);

  const viewType = useQueryState<CalendarViewType>("viewType") || getStoredCalendarViewType();
  const viewingDate = useQueryState<Date>("viewingDate", queryStateShortDateParser);

  const [draggingEvent, setDraggingEvent] = useState<CalendarEventDto>();
  const [ghostEvent, setGhostEvent] = useState<CalendarEventDto>();
  const [calenderLoading, setCalenderLoading] = useState<boolean>(false);

  const workspacesFirstTeam = useWorkspaceFirstTeam(workspace.workspaceId);

  useEffect(() => {
    if (viewType) {
      storeCalendarViewType(viewType);
    }
    if (!viewingDate) {
      const initialDay = startOfDay(new Date());
      setQueryState("viewingDate", queryStateDateToShortDateConverter(initialDay));
    }
  }, [viewType, viewingDate]);

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
        setCalenderLoading,
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
