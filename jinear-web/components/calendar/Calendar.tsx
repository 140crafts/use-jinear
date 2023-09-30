import { TeamDto, WorkspaceDto } from "@/model/be/jinear-core";
import { useRetrieveWorkspaceTeamsQuery } from "@/store/api/teamApi";
import Logger from "@/utils/logger";
import cn from "classnames";
import { eachDayOfInterval, endOfMonth, endOfWeek, format, parse, startOfDay, startOfWeek } from "date-fns";
import React, { useState } from "react";
import styles from "./Calendar.module.scss";
import CalendarContext from "./context/CalendarContext";
import CalendarHeader from "./header/CalendarHeader";
import MonthView from "./monthView/MonthView";
import WeekView from "./weekView/WeekView";

interface CalendarProps {
  initialDate?: Date;
  workspace: WorkspaceDto;
  className?: string;
}

export type CalendarViewType = "MONTH" | "WEEK";

const logger = Logger("Calendar");

const Calendar: React.FC<CalendarProps> = ({ workspace, initialDate = startOfDay(new Date()), className }) => {
  const [viewType, setViewType] = useState<CalendarViewType>("MONTH");
  const [filterBy, setFilterBy] = useState<TeamDto>();

  const [highlightedTaskId, setHighlightedTaskId] = useState<string>("");
  const [viewingDate, setViewingDate] = useState(initialDate);
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
        {viewType == "MONTH" && <MonthView />}
        {viewType == "WEEK" && <WeekView />}
      </div>
    </CalendarContext.Provider>
  );
};

export default Calendar;
