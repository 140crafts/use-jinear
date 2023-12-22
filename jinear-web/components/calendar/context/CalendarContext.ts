import { TeamDto, WorkspaceDto } from "@/model/be/jinear-core";
import { isSameDay, startOfDay } from "date-fns";
import { createContext, useContext } from "react";
import { CalendarViewType } from "../Calendar";

interface ICalendarContext {
  viewType: CalendarViewType;
  setViewType?: React.Dispatch<React.SetStateAction<CalendarViewType>>;

  workspace?: WorkspaceDto;
  newTasksFromTeam?: TeamDto;

  filterBy?: TeamDto;
  setFilterBy?: React.Dispatch<React.SetStateAction<TeamDto | undefined>>;

  viewingDate: Date;
  setViewingDate?: React.Dispatch<React.SetStateAction<Date>>;

  periodStart: Date;
  periodEnd: Date;
  days: Date[];

  weekViewPeriodStart: Date;
  weekViewPeriodEnd: Date;
  weekDays: Date[];

  highlightedTaskId: string;
  setHighlightedTaskId?: React.Dispatch<React.SetStateAction<string>>;

  squeezedView: boolean;
  setSqueezedView?: React.Dispatch<React.SetStateAction<boolean>>;
}

const initialDate = startOfDay(new Date());

const CalendarContext = createContext<ICalendarContext>({
  viewType: "w",
  viewingDate: initialDate,
  periodStart: initialDate,
  periodEnd: initialDate,
  days: [],
  weekViewPeriodStart: initialDate,
  weekViewPeriodEnd: initialDate,
  weekDays: [],
  highlightedTaskId: "",
  squeezedView: true,
});

export default CalendarContext;

export function useViewType() {
  const ctx = useContext(CalendarContext);
  return ctx.viewType;
}

export function useSetViewType() {
  const ctx = useContext(CalendarContext);
  return ctx.setViewType;
}

export function useViewingDate() {
  const ctx = useContext(CalendarContext);
  return ctx.viewingDate;
}

export function useSetViewingDate() {
  const ctx = useContext(CalendarContext);
  return ctx.setViewingDate;
}

export function usePeriodStart() {
  const ctx = useContext(CalendarContext);
  return ctx.periodStart;
}

export function usePeriodEnd() {
  const ctx = useContext(CalendarContext);
  return ctx.periodEnd;
}

export function useHighligtedTaskId() {
  const ctx = useContext(CalendarContext);
  return ctx.highlightedTaskId;
}

export function useCalendarDays() {
  const ctx = useContext(CalendarContext);
  return ctx.days;
}

export function useSqueezedView() {
  const ctx = useContext(CalendarContext);
  return ctx.squeezedView;
}

export function useSetSqueezedView() {
  const ctx = useContext(CalendarContext);
  return ctx.setSqueezedView;
}

export function useSetHighlightedTaskId() {
  const ctx = useContext(CalendarContext);
  return ctx.setHighlightedTaskId;
}

export function useCalendarWorkspace() {
  const ctx = useContext(CalendarContext);
  return ctx.workspace;
}

export function useCalendarNewTaskFromTeam() {
  const ctx = useContext(CalendarContext);
  return ctx.newTasksFromTeam;
}

export function useFilterBy() {
  const ctx = useContext(CalendarContext);
  return ctx.filterBy;
}

export function useSetFilterBy() {
  const ctx = useContext(CalendarContext);
  return ctx.setFilterBy;
}

export function useWeekViewPeriodStart() {
  const ctx = useContext(CalendarContext);
  return ctx.weekViewPeriodStart;
}

export function useWeekViewPeriodEnd() {
  const ctx = useContext(CalendarContext);
  return ctx.weekViewPeriodEnd;
}

export function useWeekDays() {
  const ctx = useContext(CalendarContext);
  return ctx.weekDays;
}

//helper hooks
export function useIsDateFirstDayOfViewingPeriod(day: Date) {
  const ctx = useContext(CalendarContext);
  const periodStart = ctx.periodStart;
  return isSameDay(day, periodStart);
}

export function useIsDateLastDayOfViewingPeriod(day: Date) {
  const ctx = useContext(CalendarContext);
  const periodEnd = ctx.periodEnd;
  return isSameDay(day, periodEnd);
}

export function useIsDateBetweenViewingPeriod(day?: Date) {
  const ctx = useContext(CalendarContext);
  const periodStart = ctx.periodStart.getTime();
  const periodEnd = ctx.periodEnd.getTime();
  if (!day) {
    return false;
  }
  const milis = day.getTime();
  return periodStart <= milis && milis <= periodEnd;
}
