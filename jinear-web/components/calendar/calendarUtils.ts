import { TaskDto } from "@/model/be/jinear-core";
import Logger from "@/utils/logger";
import { endOfDay, isSameDay, startOfDay } from "date-fns";

const logger = Logger("calendar-utils");

export interface ICalendarWeekRowCell {
  weight: number;
  task: TaskDto | null;
}

function splitChunks<T>(sourceArray: T[], chunkSize: number) {
  if (chunkSize <= 0) throw "chunkSize must be greater than 0";
  let result = [];
  for (var i = 0; i < sourceArray.length; i += chunkSize) {
    result[i / chunkSize] = sourceArray.slice(i, i + chunkSize);
  }
  return result;
}

const canBeMerged = (
  taskWeekLayoutUpper: (TaskDto | null)[] = [null, null, null, null, null, null, null],
  taskWeekLayoutLower: (TaskDto | null)[] = [null, null, null, null, null, null, null]
) => {
  if (taskWeekLayoutUpper != null && taskWeekLayoutUpper?.length != taskWeekLayoutLower?.length) {
    console.error({ message: `Can not merge task week layouts`, taskWeekLayoutUpper, taskWeekLayoutLower });
    return false;
  }
  logger.log({ method: "canBeMerged", taskWeekLayoutUpper, taskWeekLayoutLower });
  const result = taskWeekLayoutUpper
    .map((taskOnDayUpper, index) => {
      const taskOnDayLower = taskWeekLayoutLower[index];
      return (
        (taskOnDayLower == null && taskOnDayUpper != null) ||
        (taskOnDayUpper == null && taskOnDayLower != null) ||
        (taskOnDayUpper == null && taskOnDayLower == null)
      );
    })
    .reduce((prev, current) => prev && current);
  logger.log({ method: "canBeMerged", result });
  return result;
};

const mergeWeekLines = (
  taskWeekLayoutUpper: (TaskDto | null)[] = [null, null, null, null, null, null, null],
  taskWeekLayoutLower: (TaskDto | null)[] = [null, null, null, null, null, null, null]
) => {
  if (taskWeekLayoutUpper.length != taskWeekLayoutLower.length) {
    console.error({ message: `Can not merge task week layouts`, taskWeekLayoutUpper, taskWeekLayoutLower });
    return [null, null, null, null, null, null, null];
  }
  const mergedWeekLine: (TaskDto | null)[] = [];
  taskWeekLayoutUpper.map((taskOnDayUpper, index) => {
    const taskOnDayLower = taskWeekLayoutLower[index];
    const task = taskOnDayUpper != null ? taskOnDayUpper : taskOnDayLower;
    mergedWeekLine.push(task);
  });
  return mergedWeekLine;
};

const mergeWeek = (week: (TaskDto | null)[][], weekIndex: number) => {
  const mergedWeek: (TaskDto | null)[][] = [];
  week.forEach((taskWeekLayout, taskIndex) => {
    let merged = false;
    for (let i = 0; i < mergedWeek.length; i++) {
      mergedWeek[i] = mergedWeek[i] ? mergedWeek[i] : [];
      const currentMergedWeekLine = mergedWeek[i];
      if (canBeMerged(taskWeekLayout, currentMergedWeekLine)) {
        const mergedLine = mergeWeekLines(taskWeekLayout, currentMergedWeekLine);
        mergedWeek[i] = mergedLine;
        merged = true;
        break;
      }
    }
    if (!merged) {
      mergedWeek.push(taskWeekLayout);
    }
  });
  logger.log({ mergeWeek: week, mergedWeek });
  return mergedWeek;
};

const rotateWeek = (week: (TaskDto | null)[][], weekIndex: number) => {
  const rotatedWeek: (TaskDto | null)[][] = [];
  const taskCount = week.length;
  for (let i = 0; i < 7; i++) {
    for (let j = 0; j < taskCount; j++) {
      rotatedWeek[i] = rotatedWeek[i] ? rotatedWeek[i] : [];
      rotatedWeek[i][j] = week[j][i];
    }
  }
  logger.log({ rotateWeek: week, rotatedWeek });
  return rotatedWeek;
};

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

const flattenWeekRow = (week: (TaskDto | null)[]) => {
  const compressed: ICalendarWeekRowCell[] = [];
  for (let i = 0; i < week.length; i++) {
    const task = week[i];
    if (task?.taskId == "01gtyh9jy71rw0ktwccn3ryv8d") {
      console.log(1);
    }
    let weight = 0;
    for (let j = i; j < week.length; j++) {
      const nextTask = week[j];
      if (task?.taskId == nextTask?.taskId) {
        weight++;
        if (j + 1 == week.length) {
          i = j + 1;
        }
      } else {
        i = j - 1;
        break;
      }
    }
    compressed.push({ weight, task });
  }
  logger.log({ flattenWeekRow: week, compressed });
  return compressed;
};

const flattenAllWeeks = (weeks: (TaskDto | null)[][]) => weeks.map(flattenWeekRow);

export const calculateHitMissTable = (tasks: TaskDto[], days: Date[]) => {
  const allTasksAllMonthHitMissTable: Array<TaskDto | null>[][] = [];
  tasks.map((task) => {
    const taskHitMissTable: Array<TaskDto | null> = [];
    days.forEach((day) => (taskForDateFilter(task, day) ? taskHitMissTable.push(task) : taskHitMissTable.push(null)));
    const taskWeeklyHitMissTable: (TaskDto | null)[][] = splitChunks(taskHitMissTable, 7);
    taskWeeklyHitMissTable.forEach((data, index) => {
      allTasksAllMonthHitMissTable[index] = allTasksAllMonthHitMissTable[index] ? allTasksAllMonthHitMissTable[index] : [];
      allTasksAllMonthHitMissTable[index].push(data);
    });
  });

  const result = allTasksAllMonthHitMissTable.map(mergeWeek).map(flattenAllWeeks);
  //   .map(rotateWeek).flat(1);
  logger.log({ allTasksAllHitMissTable: result });
  return result;
};

export const isDateBetween = (periodStart: Date, dayToLook: Date, periodEnd: Date) => {
  const _periodStart = periodStart.getTime();
  const _periodEnd = periodEnd.getTime();
  const milis = dayToLook.getTime();
  return _periodStart <= milis && milis <= _periodEnd;
};
