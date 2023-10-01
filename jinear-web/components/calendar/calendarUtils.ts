import { TaskDto } from "@/model/be/jinear-core";
import Logger from "@/utils/logger";
import { tryCatch } from "@/utils/tryCatch";
import { addDays, addMinutes, differenceInMinutes, endOfDay, isAfter, isBefore, isSameDay, startOfDay } from "date-fns";

const logger = Logger("calendar-utils");

export const DEFAULT_WEEKDAY_VIEW_RESOLUTION = 5;

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
  logger.log({ splitChunks: sourceArray, result });
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

/**
 *
 * @param tasks Viewing period tasks
 * @param days Viewing period days
 * @returns returns 3 dimensional array. First dimension is weeks, second is that weeks rows and third is tasks in that row with day size starts from monday.
 *
 * This method used for generating task-date data to rendering tasks as horizontal sticks without overlapping empty spaces.
 *
 * Example:
 *  If we render each task as a row we might end up with a week look like this.
 *  [_,_,_,X,X,_,_]
 *  [_,_,_,_,_,Z,_]
 *  [_,Y,Y,_,_,_,_]
 *  [_,_,_,T,T,T,T]
 *
 *  Lets call them week lines. So we firstly turn our data to look like above than we try to merge as many lines as possible.
 *
 *  [_,Y,Y,X,X,Z,_]
 *  [_,_,_,T,T,T,T]
 *
 *  then we turn this data to this
 *  WeekRows =>
 *              [{weight:1,task:null},{weight:2,task:Y},{weight:2,task:X},{weight:1,task:Z},{weight:1,task:null}] =>rowTasks
 *              [{weight:3,task:null},{weight:4,task:T}]
 *
 *  and our return data looks like this => [weeks][weekRows][rowTasks]
 *
 */
export const calculateHitMissTable = (vo: {
  tasks: TaskDto[];
  days: Date[];
  excludePreciseAssignedDates?: boolean;
  excludePreciseDueDates?: boolean;
  onlyPreciseAssignedDates?: boolean;
  onlyPreciseDueDates?: boolean;
  rowCount?: number;
}) => {
  const tasks = vo.tasks;
  const days = vo.days;
  const excludePreciseAssignedDates = vo.excludePreciseAssignedDates == null ? false : vo.excludePreciseAssignedDates;
  const excludePreciseDueDates = vo.excludePreciseDueDates == null ? false : vo.excludePreciseDueDates;

  const onlyPreciseAssignedDates = vo.onlyPreciseAssignedDates == null ? false : vo.onlyPreciseAssignedDates;
  const onlyPreciseDueDates = vo.onlyPreciseDueDates == null ? false : vo.onlyPreciseDueDates;

  const rowCount = vo.rowCount ? vo.rowCount : 5; // there can be max 5 rows for month view calendar. //todo fix 2027 February is 4 row

  const allTasksAllMonthHitMissTable: Array<TaskDto | null>[][] = [];
  tasks
    .filter((task) => !(excludePreciseAssignedDates && task.hasPreciseAssignedDate))
    .filter((task) => !(excludePreciseDueDates && task.hasPreciseDueDate))
    .filter((task) => (onlyPreciseAssignedDates ? task.hasPreciseAssignedDate : true))
    .filter((task) => (onlyPreciseDueDates ? task.hasPreciseDueDate : true))
    .forEach((task) => {
      const taskHitMissTable: Array<TaskDto | null> = [];
      days.forEach((day) => (taskForDateFilter(task, day) ? taskHitMissTable.push(task) : taskHitMissTable.push(null)));
      const taskWeeklyHitMissTable: (TaskDto | null)[][] = splitChunks(taskHitMissTable, 7);
      taskWeeklyHitMissTable.forEach((data, index) => {
        allTasksAllMonthHitMissTable[index] = allTasksAllMonthHitMissTable[index] ? allTasksAllMonthHitMissTable[index] : [];
        allTasksAllMonthHitMissTable[index].push(data);
      });
    });

  let result = allTasksAllMonthHitMissTable.map(mergeWeek).map(flattenAllWeeks);
  if (result.length == 0) {
    // const emptyArray = [[...new Array(7)], [...new Array(7)], [...new Array(7)], [...new Array(7)], [...new Array(7)]];
    const emptyArray = new Array(rowCount).map((_) => [...new Array(7)]);
    result = [...emptyArray];
  }
  logger.log({ allTasksAllHitMissTable: result });
  return result;
};

export const isDateBetween = (periodStart: Date, dayToLook: Date, periodEnd: Date) => {
  if (dayToLook) {
    const _periodStart = periodStart.getTime();
    const _periodEnd = periodEnd.getTime();
    const milis = dayToLook.getTime();
    return _periodStart <= milis && milis <= _periodEnd;
  }
};

export const filterTasksByDay = (tasks: TaskDto[], day: Date) => {
  const result = tasks.filter(
    (task) =>
      (task.assignedDate && isDateBetween(startOfDay(day), tryCatch(() => new Date(task.assignedDate)).result, endOfDay(day))) ||
      (task.dueDate && isDateBetween(startOfDay(day), tryCatch(() => new Date(task.dueDate)).result, endOfDay(day))) ||
      (task.assignedDate && task.dueDate && isBefore(new Date(task.assignedDate), day) && isAfter(new Date(task.dueDate), day))
  );
  logger.log({ day, filterTasksByDay: tasks, result });
  return result;
};

/**
 *
 * @param tasks Viewing period tasks
 * @param day
 *
 * Works similliar to calculateHitMissTable.
 *
 *          00:00 00:05                         14:30            15:00   23:55
 *            |     |                             |               |        |
 *  Task1     [_____,__X__,__X__,__X__,_____,.....|...............|..,_____]
 *  Task2     [_____,_____,_____,__Y__,__Y__,.....|...............|..,_____]
 *  Task3     [_____,_____,_____,_____,_____,.....,__Z__,...,__Z__,..,_____]
 *
 *
 *      ○_________________________●
 * chunkStartTime           chunkEndTime
 * taskStartDate            taskEndDate
 *  ●_________________________●
 *
 *  datesIntersects
 *
 * */
export const calculateDailyHitMissTable = ({ tasks, day, dayResolutionInMinutes = 15 }: ICalculateDailyHitMissTable) => {
  const timeChunks = splitDayDateToResolution(day, dayResolutionInMinutes);
  const allTasksAllDayHitMissTable: (TaskDto | null)[][] = [];
  tasks.map((task) => {
    const taskHitMissTable: Array<TaskDto | null> = [];
    timeChunks
      .filter((chunk) => chunk)
      .forEach((dateSpan: Date[]) =>
        isTaskDatesIntersect(task, dateSpan) ? taskHitMissTable.push(task) : taskHitMissTable.push(null)
      );
    allTasksAllDayHitMissTable.push(taskHitMissTable);
  });
  const flatColumns = flattenAllWeeks(mergeWeek(allTasksAllDayHitMissTable, 0));
  const mergedWeekDateSpans = mergeWeekDateSpans(allTasksAllDayHitMissTable);
  // const result = mergeWeek(allTasksAllDayHitMissTable, 0);
  logger.log({ tasks, flatColumns });
  return flatColumns;
};
export const splitDayDateToResolution = (day: Date, dayResolutionInMinutes: number) => {
  const start = startOfDay(day);
  const times = [start];
  let next = start;
  while (isSameDay(addMinutes(next, dayResolutionInMinutes), next)) {
    next = addMinutes(next, dayResolutionInMinutes);
    times.push(next);
  }
  const startOfNextDay = startOfDay(addDays(day, 1));
  times.push(startOfNextDay);
  return times.map((time, index) => times?.[index + 1] && [time, times?.[index + 1]]);
};

export const isTaskDatesIntersect = (task: TaskDto, dateSpan: Date[]) => {
  const date0 = dateSpan[0];
  const date1 = dateSpan[1];
  const assignedDate = task.assignedDate && new Date(task.assignedDate);
  const dueDate = task.dueDate && new Date(task.dueDate);

  const isAssignedDateBetweenTimespan =
    (assignedDate && date0.getTime() <= assignedDate.getTime() && assignedDate.getTime() <= date1.getTime()) || false;
  const isAssignedDateBeforeTimespan = (assignedDate && assignedDate.getTime() <= date0.getTime()) || false;
  const isDueDateBetween = (dueDate && date0.getTime() <= dueDate.getTime() && dueDate.getTime() <= date1.getTime()) || false;
  const isDueDateAfter = (dueDate && date1.getTime() <= dueDate.getTime()) || false;

  const isStartDateBetweenTimeSpanAndEndDateNull = isAssignedDateBetweenTimespan && dueDate == null;
  const isEndDateBetweenTimeSpanAndStartDateNull = isDueDateBetween && assignedDate == null;

  const isTaskStartsBeforeTimespanEndsBetweenTimespan = isAssignedDateBeforeTimespan && isDueDateBetween;
  const isTaskStartsBetweenTimespanEndsAfterTimespan = isAssignedDateBetweenTimespan && isDueDateAfter;
  const isTaskstartsBeforeTimespanEndsAfterTimespan = isAssignedDateBeforeTimespan && isDueDateAfter;
  const isTaskStartsAndEndsBetweenTimespan = isAssignedDateBetweenTimespan && isDueDateBetween;

  return (
    isTaskStartsBeforeTimespanEndsBetweenTimespan ||
    isTaskStartsBetweenTimespanEndsAfterTimespan ||
    isTaskstartsBeforeTimespanEndsAfterTimespan ||
    isTaskStartsAndEndsBetweenTimespan ||
    isStartDateBetweenTimeSpanAndEndDateNull ||
    isEndDateBetweenTimeSpanAndStartDateNull
  );
};

export const mergeWeekDateSpans = (dayTable: (TaskDto | null)[][]) => {
  const resultTable: any = [];
  const dayTaskCount = dayTable.length;

  if (dayTaskCount != 0) {
    const cellCountInThatDay = dayTable?.[0]?.length;
    for (let i = 0; i < cellCountInThatDay; i++) {
      const tasksInThatTimespan = [];
      for (let j = 0; j < dayTaskCount; j++) {
        const task = dayTable[j][i];
        if (task) {
          tasksInThatTimespan.push({ task, rowNo: i, columnNo: j });
        }
      }
      resultTable.push(tasksInThatTimespan);
    }
  }

  return resultTable;
};

export const convertWeekviewDayColumnsToRows = (flatColumns: ICalendarWeekRowCell[][]) => {};

// const totalRowCount = flatColumns?.[0]?.reduce()
//totalRowCount kadar don
//result = []
//her columnin ilk elemanini al hepsi nullsa veya biri haric gerisi nullsa result a pushla => x durumu
//birden fazla null olmayan varsa x durumu olana kadar atla
export const flatColumnsToRows = (flatColumns: ICalendarWeekRowCell[][]) => {
  const result = [];
  const totalRowCount = flatColumns?.[0]?.map((cell) => cell.weight).reduce((sum, curr) => sum + curr, 0);
  const columnCount = flatColumns?.length;

  for (let i = 0; i <= totalRowCount; i++) {
    for (let j = 0; j < columnCount; j++) {
      const element = flatColumns[i][j];
    }
  }
};

export interface ICalculateDailyHitMissTable {
  tasks: TaskDto[];
  day: Date;
  dayResolutionInMinutes?: number;
}

export const calculateTaskDayPositions = ({ tasks, day, minuteInPx = 2.5 }: ICalculateTaskDayPositions) => {
  //ICalculatePositionBasedDailyHitMissTable
  const dayCells = tasks.map((task) => convertTaskToCell(task, day, minuteInPx));
  const modified = calculateIntersectionsAndModifyLeft(dayCells);
  logger.log({ dayCells, modified });
  return modified;
};

export const convertTaskToCell = (task: TaskDto, day: Date, minuteInPx = 2.5) => {
  const startTime = task.assignedDate
    ? isSameDay(new Date(task.assignedDate), day)
      ? new Date(task.assignedDate)
      : startOfDay(day)
    : addMinutes(new Date(task.dueDate), -15);

  const endTime = task.dueDate
    ? isSameDay(new Date(task.dueDate), day)
      ? new Date(task.dueDate)
      : endOfDay(day)
    : addMinutes(new Date(task.assignedDate), 15);

  const top = differenceInMinutes(startTime, day) * minuteInPx;
  const height = differenceInMinutes(endTime, startTime) * minuteInPx;
  const width = 90;
  const left = 10;
  logger.log({ minuteInPx });
  return { task, startTime, endTime, top, height, width, left };
};

export const calculateIntersectionsAndModifyLeft = (dayCells: ICalendarDayRowCell[], STEP_SIZE = 30) => {
  for (let i = 0; i < dayCells.length; i++) {
    const currentElement = dayCells[i];
    let interactionCount = 0;
    for (let j = i + 1; j < dayCells.length; j++) {
      const nextElement = dayCells?.[j];
      if (nextElement && doesCellsIntersect(currentElement, nextElement)) {
        interactionCount++;
        if (doesCellsTitlesIntersect(currentElement, nextElement)) {
          nextElement.width = nextElement.width * 0.8;
        } else {
          nextElement.width = nextElement.width * 0.95;
        }
        nextElement.left = 100 - nextElement.width;
        dayCells[j] = nextElement;
      }
    }
  }
  return dayCells;
};

export const doesCellsIntersect = (dayCell_A: ICalendarDayRowCell, dayCell_B: ICalendarDayRowCell) => {
  const Ax0 = dayCell_A.left;
  const Ax1 = dayCell_A.left + dayCell_A.width;
  const Bx0 = dayCell_B.left;
  const Bx1 = dayCell_B.left + dayCell_B.width;

  const Ay0 = dayCell_A.top;
  const Ay1 = dayCell_A.top + dayCell_A.height;
  const By0 = dayCell_B.top;
  const By1 = dayCell_B.top + dayCell_B.height;

  const intersectsAtXaxis = (Ax0 <= Bx0 && Bx0 <= Ax1) || (Ax0 <= Bx1 && Bx1 <= Ax1);
  const intersectsAtYaxis = (Ay0 <= By0 && By0 <= Ay1) || (Ay0 <= By1 && By1 <= Ay1);
  return intersectsAtXaxis && intersectsAtYaxis;
};

export const doesCellsTitlesIntersect = (dayCell_A: ICalendarDayRowCell, dayCell_B: ICalendarDayRowCell) => {
  const Ax0 = dayCell_A.left;
  const Ax1 = dayCell_A.left + dayCell_A.width;
  const Bx0 = dayCell_B.left;
  const Bx1 = dayCell_B.left + dayCell_B.width;

  const Ay0 = dayCell_A.top;
  const Ay1 = dayCell_A.top + 25;
  const By0 = dayCell_B.top;
  const By1 = dayCell_B.top + 25;

  const intersectsAtXaxis = (Ax0 <= Bx0 && Bx0 <= Ax1) || (Ax0 <= Bx1 && Bx1 <= Ax1);
  const intersectsAtYaxis = (Ay0 <= By0 && By0 <= Ay1) || (Ay0 <= By1 && By1 <= Ay1);
  return intersectsAtXaxis && intersectsAtYaxis;
};

export interface ICalendarDayRowCell {
  top: number;
  left: number;
  height: number;
  width: number;
  task: TaskDto | null;
  startTime: Date;
  endTime: Date;
}

export interface ICalculateTaskDayPositions {
  tasks: TaskDto[];
  day: Date;
  minuteInPx?: number;
}
