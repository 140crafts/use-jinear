import { TaskDto } from "@/model/be/jinear-core";
import { isSameDay, startOfDay } from "date-fns";
import { createContext, useContext } from "react";

interface ICalendarContext {
  viewingDate: Date;
  selectedDate: Date;
  periodStart: Date;
  periodEnd: Date;
  tasks: TaskDto[] | undefined;
  highlightedTaskId: string;
  setHighlightedTaskId?: React.Dispatch<React.SetStateAction<string>>;
  fullSizeDays?: boolean;
  setFullSizeDays?: React.Dispatch<React.SetStateAction<boolean>>;
}

const initialDate = startOfDay(new Date());

const CalendarContext = createContext<ICalendarContext>({
  viewingDate: initialDate,
  selectedDate: initialDate,
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

export function useHighligtedTaskId() {
  const ctx = useContext(CalendarContext);
  return ctx.highlightedTaskId;
}

export function useSetHighlightedTaskId() {
  const ctx = useContext(CalendarContext);
  return ctx.setHighlightedTaskId;
}

export function useFullSizeDays() {
  const ctx = useContext(CalendarContext);
  return ctx.fullSizeDays;
}

export function useSetFullSizeDays() {
  const ctx = useContext(CalendarContext);
  return ctx.setFullSizeDays;
}

export function isDateFirstDayOfViewingPeriod(day: Date) {
  const ctx = useContext(CalendarContext);
  const periodStart = ctx.periodStart;
  return isSameDay(day, periodStart);
}

export function isDateLastDayOfViewingPeriod(day: Date) {
  const ctx = useContext(CalendarContext);
  const periodEnd = ctx.periodEnd;
  return isSameDay(day, periodEnd);
}

export function isDateBetweenViewingPeriod(day: Date) {
  const ctx = useContext(CalendarContext);
  const periodStart = ctx.periodStart.getTime();
  const periodEnd = ctx.periodEnd.getTime();
  const milis = day.getTime();
  return periodStart <= milis && milis <= periodEnd;
}
