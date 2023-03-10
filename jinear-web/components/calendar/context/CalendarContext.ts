import { TaskDto } from "@/model/be/jinear-core";
import { isSameDay, startOfDay } from "date-fns";
import { createContext, useContext } from "react";

interface ICalendarContext {
  viewingDate: Date;
  setViewingDate?: React.Dispatch<React.SetStateAction<Date>>;

  periodStart: Date;
  periodEnd: Date;
  tasks: TaskDto[] | undefined;
  highlightedTaskId: string;
  setHighlightedTaskId?: React.Dispatch<React.SetStateAction<string>>;
}

const initialDate = startOfDay(new Date());

const CalendarContext = createContext<ICalendarContext>({
  viewingDate: initialDate,
  periodStart: initialDate,
  periodEnd: initialDate,
  tasks: [],
  highlightedTaskId: "",
});

export default CalendarContext;

export function useViewingDate() {
  const ctx = useContext(CalendarContext);
  return ctx.viewingDate;
}

export function useSetViewingDate() {
  const ctx = useContext(CalendarContext);
  return ctx.setViewingDate;
}

export function useHighligtedTaskId() {
  const ctx = useContext(CalendarContext);
  return ctx.highlightedTaskId;
}

export function useSetHighlightedTaskId() {
  const ctx = useContext(CalendarContext);
  return ctx.setHighlightedTaskId;
}

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
