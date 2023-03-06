import { TaskDto } from "@/model/be/jinear-core";
import { endOfDay, isBefore, isSameDay, startOfDay } from "date-fns";
import { createContext, useContext } from "react";

interface ICalendarContext {
  viewingDate: Date;
  selectedDate: Date;
  periodStart: Date;
  periodEnd: Date;
  tasks: TaskDto[] | undefined;
}

const initialDate = startOfDay(new Date());

const CalendarContext = createContext<ICalendarContext>({
  viewingDate: initialDate,
  selectedDate: initialDate,
  periodStart: initialDate,
  periodEnd: initialDate,
  tasks: [],
});

export default CalendarContext;

export function useViewingDate() {
  const ctx = useContext(CalendarContext);
  return ctx.viewingDate;
}

export function useTaskListForDate(day: Date) {
  const ctx = useContext(CalendarContext);
  const taskList = ctx.tasks;
  return taskList?.filter((task) => taskForDateFilter(task, day));
}

export function isDateBetweenViewingPeriod(day: Date) {
  const ctx = useContext(CalendarContext);
  const periodStart = ctx.periodStart.getTime();
  const periodEnd = ctx.periodEnd.getTime();
  const milis = day.getTime();
  return periodStart <= milis && milis <= periodEnd;
}

export function isDateFirstDayOfViewingPeriod(day: Date) {
  const ctx = useContext(CalendarContext);
  const periodStart = ctx.periodStart;
  return isSameDay(day, periodStart);
}

export function countAllTasksAtDateBeforeTaskDate(dayToLook: Date, taskDate: Date) {
  const tasksAtDate = useTaskListForDate(dayToLook);
  return tasksAtDate?.filter((task) => {
    const date = task.assignedDate ? new Date(task.assignedDate) : new Date(task.dueDate);
    return isBefore(date, taskDate);
  }).length;
}

const taskForDateFilter = (task: TaskDto, day: Date) => {
  const assignedDate = task.assignedDate && new Date(task.assignedDate);
  const dueDate = task.dueDate && new Date(task.dueDate);
  const onAssignedDate = assignedDate && isSameDay(assignedDate, day);
  const onDueDate = dueDate && isSameDay(dueDate, day);
  let isBetween = false;
  if (assignedDate && dueDate) {
    const _assigned = task.hasPreciseAssignedDate ? assignedDate : startOfDay(assignedDate);
    const _due = task.hasPreciseDueDate ? dueDate : endOfDay(dueDate);
    const milis = day.getTime();
    isBetween = _assigned.getTime() <= milis && milis <= _due.getTime();
  }
  return onAssignedDate || onDueDate || isBetween;
};
