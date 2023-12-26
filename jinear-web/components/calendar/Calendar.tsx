"use client";
import { TeamDto, WorkspaceDto } from "@/model/be/jinear-core";
import { useRetrieveWorkspaceTeamsQuery } from "@/store/api/teamApi";
import Logger from "@/utils/logger";
import { createUrl } from "@/utils/urlUtils";
import cn from "classnames";
import { eachDayOfInterval, endOfMonth, endOfWeek, format, parse, startOfDay, startOfWeek } from "date-fns";
import { useRouter, useSearchParams } from "next/navigation";
import React, { useEffect, useState } from "react";
import styles from "./Calendar.module.scss";
import CalendarContext from "./context/CalendarContext";
import DayView from "./dayView/DayView";
import CalendarHeader from "./header/CalendarHeader";
import MonthView from "./monthView/MonthView";
import TwoDayView from "./twoDayView/TwoDayView";
import WeekView from "./weekView/WeekView";

interface CalendarProps {
  initialDate?: Date;
  workspace: WorkspaceDto;
  className?: string;
}

export type CalendarViewType = "m" | "w" | "d" | "2d";

const logger = Logger("Calendar");

const URL_DATE_FORMAT = "yyyy-dd-MM";

const getStoredCalendarViewType = (): CalendarViewType => {
  if (typeof window === "object") {
    const value = localStorage.getItem("calendarViewType") || "m";
    localStorage.setItem("calendarViewType", value);
    return value as CalendarViewType;
  }
  return "m";
};

const storeCalendarViewType = (value: CalendarViewType) => {
  if (typeof window === "object") {
    localStorage.setItem("calendarViewType", value);
  }
};

const Calendar: React.FC<CalendarProps> = ({ workspace, initialDate = startOfDay(new Date()), className }) => {
  const router = useRouter();
  const searchParams = useSearchParams();
  logger.log({ searchParams: searchParams?.toString() });

  const viewingDateSearchParam = searchParams?.get("viewingDate");

  const viewTypeSearchParam = searchParams?.get("viewType") as CalendarViewType;
  const viewTypeStored = getStoredCalendarViewType();

  const [viewType, setViewType] = useState<CalendarViewType>(viewTypeSearchParam ? viewTypeSearchParam : viewTypeStored);
  const [filterBy, setFilterBy] = useState<TeamDto>();

  const [highlightedTaskId, setHighlightedTaskId] = useState<string>("");
  const [viewingDate, setViewingDate] = useState(
    viewingDateSearchParam ? parse(viewingDateSearchParam, URL_DATE_FORMAT, new Date()) : initialDate
  );
  const [squeezedView, setSqueezedView] = useState<boolean>(true);

  const currentMonth = format(viewingDate, "MMM-yyyy");
  const firstDayCurrentMonth = parse(currentMonth, "MMM-yyyy", new Date());
  const periodStart = startOfWeek(firstDayCurrentMonth, { weekStartsOn: 1 });
  const periodEnd = endOfWeek(endOfMonth(firstDayCurrentMonth), { weekStartsOn: 1 });
  const days = eachDayOfInterval({ start: periodStart, end: periodEnd });

  const weekViewPeriodStart = startOfWeek(viewingDate, { weekStartsOn: 1 });
  const weekViewPeriodEnd = endOfWeek(viewingDate, { weekStartsOn: 1 });
  const weekDays = eachDayOfInterval({ start: weekViewPeriodStart, end: weekViewPeriodEnd });

  const { data: teamsResponse } = useRetrieveWorkspaceTeamsQuery(workspace.workspaceId, {
    skip: workspace == null,
  });
  const workspacesFirstTeam = teamsResponse?.data?.find((team) => team);

  useEffect(() => {
    if (workspace) {
      const newParams = new URLSearchParams(searchParams?.toString());
      newParams.delete("workspaceName");
      newParams.delete("teamUsername");
      if (viewingDate) {
        newParams.set("viewingDate", format(viewingDate, URL_DATE_FORMAT));
      }
      if (viewType) {
        newParams.set("viewType", viewType);
        storeCalendarViewType(viewType);
      }
      router.push(createUrl(`/${workspace.username}/calendar`, newParams));
    }
  }, [workspace, viewingDate, viewType]);

  return (
    <CalendarContext.Provider
      value={{
        viewType,
        setViewType,
        workspace,
        newTasksFromTeam: filterBy ? filterBy : workspacesFirstTeam,
        filterBy,
        setFilterBy,
        highlightedTaskId,
        setHighlightedTaskId,
        viewingDate,
        setViewingDate,
        periodStart,
        periodEnd,
        days,
        weekViewPeriodStart,
        weekViewPeriodEnd,
        weekDays,
        squeezedView,
        setSqueezedView,
      }}
    >
      <div className={cn(styles.container, className)}>
        <CalendarHeader workspace={workspace} />
        {viewType == "m" && <MonthView />}
        {viewType == "w" && <WeekView />}
        {viewType == "d" && <DayView />}
        {viewType == "2d" && <TwoDayView />}
      </div>
    </CalendarContext.Provider>
  );
};

export default Calendar;
