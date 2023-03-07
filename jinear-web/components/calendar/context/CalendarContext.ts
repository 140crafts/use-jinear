import { TaskDto } from "@/model/be/jinear-core";
import { endOfDay, endOfWeek, isAfter, isBefore, isSameDay, isSameWeek, startOfDay, startOfWeek } from "date-fns";
import { createContext, useContext } from "react";

interface ICalendarContext {
  viewingDate: Date;
  selectedDate: Date;
  periodStart: Date;
  periodEnd: Date;
  tasks: TaskDto[] | undefined;
  highlightedTaskId: string;
  setHighlightedTaskId?: React.Dispatch<React.SetStateAction<string>>;
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

export function countAllTasksAtDateBeforeTaskDate(dayToLook: Date, taskDate: Date, taskId: String) {
  const tasksAtDate = useTaskListForDate(dayToLook);
  return tasksAtDate
    ?.filter((task) => task.assignedDate || task.dueDate)
    ?.filter((task) => {
      const startDate = task.assignedDate ? new Date(task.assignedDate) : new Date(task.dueDate);
      return isBefore(startDate, dayToLook);
    })
    ?.filter((task) => {
      const endDate = task.dueDate ? new Date(task.dueDate) : new Date(task.assignedDate);
      return isSameDay(endDate, dayToLook) || isAfter(endDate, dayToLook);
    })
    ?.filter((task) => task.taskId != taskId).length;
}
export function getTaskMap() {
  const ctx = useContext(CalendarContext);
  const taskList = ctx.tasks;
  if (!taskList) {
    return;
  }
  const matrix: string[][] = [];
  for (let i = 0; i < 7; i++) {
    for (let j = 0; j < taskList.length; j++) {
      const task = taskList[j];
      const column = matrix[i];
    }
  }
}
// M[0->7][0->n]

// [X,X,X,X,X,_,_]
// [_,_,_,Y,Y,Y,_]
// [_,_,_,_,_,_,_]
// [_,_,_,_,_,_,_]
// +
// [_,_,_,_,_,Z,_]

// row a donustur mevcut rowlar ile merge etmeye calis edilmezse mevcut rowlara yeni row ekle

// [_,_,_,_,_,_,_]
//
//
//
//
//

export function buHaftaBendenOnceBitmemisKacTaskVar(day: Date, task: TaskDto) {
  const ctx = useContext(CalendarContext);
  const taskList = ctx.tasks;
  const thisWeeksTasksAndBeforeDay = taskList
    ?.filter((t) => t.taskId != task.taskId)
    ?.filter((t) => {
      const date = t.assignedDate ? new Date(t.assignedDate) : new Date(t.dueDate);
      return isSameWeek(date, day, { weekStartsOn: 1 });
    })
    ?.filter((t) => {
      const dateEnd = t.dueDate ? new Date(t.dueDate) : new Date(t.assignedDate);
      return isBefore(dateEnd, day);
    });

  const weekStart = startOfWeek(day, { weekStartsOn: 1 });
  const weekEnd = endOfWeek(day, { weekStartsOn: 1 });
}

export const taskForDateFilter = (task: TaskDto, day: Date) => {
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
