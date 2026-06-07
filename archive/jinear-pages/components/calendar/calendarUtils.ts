import { queryStateShortDateParser, useQueryState } from "@/hooks/useQueryState";
import { CalendarEventDto } from "@/model/be/jinear-core";
import Logger from "@/utils/logger";
import { tryCatch } from "@/utils/tryCatch";

import {
  addDays,
  addMinutes,
  differenceInMinutes,
  endOfDay,
  endOfMonth,
  endOfWeek,
  format,
  isAfter,
  isBefore,
  isSameDay,
  parse,
  startOfDay,
  startOfWeek
} from "date-fns";

const logger = Logger("calendar-utils");

export const DEFAULT_WEEKDAY_VIEW_RESOLUTION = 5;

export interface ICalendarWeekRowCell {
  weight: number;
  calendarEvent: CalendarEventDto | null;
}

export function useIsDateFirstDayOfViewingPeriod(day: Date) {
  const viewingDate = useQueryState<Date>("viewingDate", queryStateShortDateParser) || startOfDay(new Date());
  const currentMonth = format(viewingDate, "MMM-yyyy");
  const firstDayCurrentMonth = parse(currentMonth, "MMM-yyyy", new Date());
  const periodStart = startOfWeek(firstDayCurrentMonth, { weekStartsOn: 1 });
  return isSameDay(day, periodStart);
}

export function useIsDateLastDayOfViewingPeriod(day: Date) {
  const viewingDate = useQueryState<Date>("viewingDate", queryStateShortDateParser) || startOfDay(new Date());
  const currentMonth = format(viewingDate, "MMM-yyyy");
  const firstDayCurrentMonth = parse(currentMonth, "MMM-yyyy", new Date());
  const periodEnd = endOfWeek(endOfMonth(firstDayCurrentMonth), { weekStartsOn: 1 });
  return isSameDay(day, periodEnd);
}

export function useIsDateBetweenViewingPeriod(day?: Date) {
  const viewingDate = useQueryState<Date>("viewingDate", queryStateShortDateParser) || startOfDay(new Date());
  if (!day) {
    return false;
  }
  const currentMonth = format(viewingDate, "MMM-yyyy");
  const firstDayCurrentMonth = parse(currentMonth, "MMM-yyyy", new Date());
  const periodStart = startOfWeek(firstDayCurrentMonth, { weekStartsOn: 1 }).getTime();
  const periodEnd = endOfWeek(endOfMonth(firstDayCurrentMonth), { weekStartsOn: 1 }).getTime();
  const milis = day.getTime();
  return periodStart <= milis && milis <= periodEnd;
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
  taskWeekLayoutUpper: (CalendarEventDto | null)[] = [null, null, null, null, null, null, null],
  taskWeekLayoutLower: (CalendarEventDto | null)[] = [null, null, null, null, null, null, null]
) => {
  if (taskWeekLayoutUpper != null && taskWeekLayoutUpper?.length != taskWeekLayoutLower?.length) {
    console.error({ message: `Can not merge calendar event week layouts`, taskWeekLayoutUpper, taskWeekLayoutLower });
    return false;
  }
  logger.log({ method: "canBeMerged", taskWeekLayoutUpper, taskWeekLayoutLower });
  const result = taskWeekLayoutUpper
    ?.map((taskOnDayUpper, index) => {
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
  taskWeekLayoutUpper: (CalendarEventDto | null)[] = [null, null, null, null, null, null, null],
  taskWeekLayoutLower: (CalendarEventDto | null)[] = [null, null, null, null, null, null, null]
) => {
  if (taskWeekLayoutUpper.length != taskWeekLayoutLower.length) {
    console.error({ message: `Can not merge calendar event week layouts`, taskWeekLayoutUpper, taskWeekLayoutLower });
    return [null, null, null, null, null, null, null];
  }
  const mergedWeekLine: (CalendarEventDto | null)[] = [];
  taskWeekLayoutUpper.map((taskOnDayUpper, index) => {
    const taskOnDayLower = taskWeekLayoutLower[index];
    const task = taskOnDayUpper != null ? taskOnDayUpper : taskOnDayLower;
    mergedWeekLine.push(task);
  });
  return mergedWeekLine;
};

const mergeWeek = (week: (CalendarEventDto | null)[][], weekIndex: number) => {
  const mergedWeek: (CalendarEventDto | null)[][] = [];
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

const rotateWeek = (week: (CalendarEventDto | null)[][], weekIndex: number) => {
  const rotatedWeek: (CalendarEventDto | null)[][] = [];
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

export const taskForDateFilter = (event: CalendarEventDto, day: Date) => {
  const assignedDate = event.assignedDate && new Date(event.assignedDate);
  const dueDate = event.dueDate && new Date(event.dueDate);
  const onAssignedDate = assignedDate && isSameDay(assignedDate, day);
  const onDueDate = dueDate && isSameDay(dueDate, day);
  let isBetween = false;
  if (assignedDate && dueDate) {
    const _assigned = event.hasPreciseAssignedDate ? assignedDate : startOfDay(assignedDate);
    const _due = event.hasPreciseDueDate ? dueDate : endOfDay(dueDate);
    const milis = day.getTime();
    isBetween = _assigned.getTime() <= milis && milis <= _due.getTime();
  }
  return onAssignedDate || onDueDate || isBetween;
};

const flattenWeekRow = (week: (CalendarEventDto | null)[]) => {
  const compressed: ICalendarWeekRowCell[] = [];
  for (let i = 0; i < week.length; i++) {
    const event = week[i];
    let weight = 0;
    for (let j = i; j < week.length; j++) {
      const nextEvent = week[j];
      if (event?.calendarEventId == nextEvent?.calendarEventId) {
        weight++;
        if (j + 1 == week.length) {
          i = j + 1;
        }
      } else {
        i = j - 1;
        break;
      }
    }
    compressed.push({ weight, calendarEvent: event });
  }
  logger.log({ flattenWeekRow: week, compressed });
  return compressed;
};

const flattenAllWeeks = (weeks: (CalendarEventDto | null)[][]) => weeks.map(flattenWeekRow);

/**
 * @param vo contains following
 *  events Viewing period calendar events
 *  days Viewing period days
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
  events: CalendarEventDto[];
  days: Date[];
  excludePreciseAssignedDates?: boolean;
  excludePreciseDueDates?: boolean;
  onlyPreciseAssignedDates?: boolean;
  onlyPreciseDueDates?: boolean;
  rowCount?: number;
}) => {
  const events = vo.events;
  const days = vo.days;
  const excludePreciseAssignedDates = vo.excludePreciseAssignedDates == null ? false : vo.excludePreciseAssignedDates;
  const excludePreciseDueDates = vo.excludePreciseDueDates == null ? false : vo.excludePreciseDueDates;

  const onlyPreciseAssignedDates = vo.onlyPreciseAssignedDates == null ? false : vo.onlyPreciseAssignedDates;
  const onlyPreciseDueDates = vo.onlyPreciseDueDates == null ? false : vo.onlyPreciseDueDates;

  const rowCount = vo.rowCount ? vo.rowCount : 5; // there can be max 5 rows for month view calendar. //todo fix 2027 February is 4 row

  const allTasksAllMonthHitMissTable: Array<CalendarEventDto | null>[][] = [];
  events
    .filter((event) => !(excludePreciseAssignedDates && event.hasPreciseAssignedDate))
    .filter((event) => !(excludePreciseDueDates && event.hasPreciseDueDate))
    .filter((event) => (onlyPreciseAssignedDates ? event.hasPreciseAssignedDate : true))
    .filter((event) => (onlyPreciseDueDates ? event.hasPreciseDueDate : true))
    .forEach((event) => {
      const taskHitMissTable: Array<CalendarEventDto | null> = [];
      days.forEach((day) => (taskForDateFilter(event, day) ? taskHitMissTable.push(event) : taskHitMissTable.push(null)));
      const taskWeeklyHitMissTable: (CalendarEventDto | null)[][] = splitChunks(taskHitMissTable, 7);
      taskWeeklyHitMissTable.forEach((data, index) => {
        allTasksAllMonthHitMissTable[index] = allTasksAllMonthHitMissTable[index] ? allTasksAllMonthHitMissTable[index] : [];
        allTasksAllMonthHitMissTable[index].push(data);
      });
    });
  logger.log({ allTasksAllMonthHitMissTable });
  // sort tasks in each week with duration
  allTasksAllMonthHitMissTable.forEach((data, index) => {
    allTasksAllMonthHitMissTable[index] = data.sort((a, b) => {
      const aNotNullLenght = a.filter((day) => day != null).length || 0;
      const bNotNullLenght = b.filter((day) => day != null).length || 0;
      return bNotNullLenght - aNotNullLenght;
    });
  });
  logger.log({ allTasksAllMonthHitMissTableAfterSort: allTasksAllMonthHitMissTable });

  let result = allTasksAllMonthHitMissTable.map(mergeWeek).map(flattenAllWeeks);
  if (result.length == 0) {
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

export const filterTasksByDay = (events: CalendarEventDto[], day: Date) => {
  const result = events.filter(
    (event) =>
      (event.assignedDate &&
        isDateBetween(startOfDay(day), tryCatch(() => new Date(event.assignedDate)).result, endOfDay(day))) ||
      (event.dueDate && isDateBetween(startOfDay(day), tryCatch(() => new Date(event.dueDate)).result, endOfDay(day))) ||
      (event.assignedDate &&
        event.dueDate &&
        isBefore(new Date(event.assignedDate), day) &&
        isAfter(new Date(event.dueDate), day))
  );
  logger.log({ day, filterEventsByDay: events, result });
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
export const calculateDailyHitMissTable = ({ events, day, dayResolutionInMinutes = 15 }: ICalculateDailyHitMissTable) => {
  const timeChunks = splitDayDateToResolution(day, dayResolutionInMinutes);
  const allTasksAllDayHitMissTable: (CalendarEventDto | null)[][] = [];
  events.map((event) => {
    const taskHitMissTable: Array<CalendarEventDto | null> = [];
    timeChunks
      .filter((chunk) => chunk)
      .forEach((dateSpan: Date[]) =>
        isTaskDatesIntersect(event, dateSpan) ? taskHitMissTable.push(event) : taskHitMissTable.push(null)
      );
    allTasksAllDayHitMissTable.push(taskHitMissTable);
  });
  const flatColumns = flattenAllWeeks(mergeWeek(allTasksAllDayHitMissTable, 0));
  const mergedWeekDateSpans = mergeWeekDateSpans(allTasksAllDayHitMissTable);
  // const result = mergeWeek(allTasksAllDayHitMissTable, 0);
  logger.log({ events, flatColumns });
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

export const isTaskDatesIntersect = (event: CalendarEventDto, dateSpan: Date[]) => {
  const date0 = dateSpan[0];
  const date1 = dateSpan[1];
  const assignedDate = event.assignedDate && new Date(event.assignedDate);
  const dueDate = event.dueDate && new Date(event.dueDate);

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

export const mergeWeekDateSpans = (dayTable: (CalendarEventDto | null)[][]) => {
  const resultTable: any = [];
  const dayTaskCount = dayTable.length;

  if (dayTaskCount != 0) {
    const cellCountInThatDay = dayTable?.[0]?.length;
    for (let i = 0; i < cellCountInThatDay; i++) {
      const tasksInThatTimespan = [];
      for (let j = 0; j < dayTaskCount; j++) {
        const event = dayTable[j][i];
        if (event) {
          tasksInThatTimespan.push({ event, rowNo: i, columnNo: j });
        }
      }
      resultTable.push(tasksInThatTimespan);
    }
  }

  return resultTable;
};

export const convertWeekviewDayColumnsToRows = (flatColumns: ICalendarWeekRowCell[][]) => {
};

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
  events: CalendarEventDto[];
  day: Date;
  dayResolutionInMinutes?: number;
}

export const calculateTaskDayPositions = ({ events, day, minuteInPx = 2.5 }: ICalculateTaskDayPositions) => {
  //ICalculatePositionBasedDailyHitMissTable
  const dayCells = events.map((event) => convertTaskToCell(event, day, minuteInPx));
  const modified = calculateIntersectionsAndModifyLeft(dayCells);
  logger.log({ dayCells, modified });
  return modified;
};

export const convertTaskToCell = (event: CalendarEventDto, day: Date, minuteInPx = 2.5) => {
  const startTime = event.assignedDate
    ? isSameDay(new Date(event.assignedDate), day)
      ? new Date(event.assignedDate)
      : startOfDay(day)
    : addMinutes(new Date(event.dueDate), -15);

  const endTime = event.dueDate
    ? isSameDay(new Date(event.dueDate), day)
      ? new Date(event.dueDate)
      : endOfDay(day)
    : addMinutes(new Date(event.assignedDate), 15);

  const top = differenceInMinutes(startTime, day) * minuteInPx;
  const height = differenceInMinutes(endTime, startTime) * minuteInPx;
  const width = 90;
  const left = 0; //10
  logger.log({ minuteInPx });
  return { event, startTime, endTime, top, height, width, left };
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
  const Ay1 = dayCell_A.top + dayCell_A.height;
  const By0 = dayCell_B.top;
  const By1 = dayCell_B.top + dayCell_A.height;

  const intersectsAtXaxis = (Ax0 < Bx0 && Bx0 < Ax1) || (Ax0 < Bx1 && Bx1 < Ax1);
  const intersectsAtYaxis = (Ay0 < By0 && By0 < Ay1) || (Ay0 < By1 && By1 < Ay1);
  return intersectsAtXaxis && intersectsAtYaxis;
};

export interface ICalendarDayRowCell {
  top: number;
  left: number;
  height: number;
  width: number;
  event: CalendarEventDto | null;
  startTime: Date;
  endTime: Date;
}

export interface ICalculateTaskDayPositions {
  events: CalendarEventDto[];
  day: Date;
  minuteInPx?: number;
}

export type CalendarViewType = "m" | "w" | "d" | "2d";

export const useStoredCalendarViewType = (): CalendarViewType => {
  if (typeof window === "object") {
    const type = localStorage.getItem("calendarViewType") ?? "m";
    return type as CalendarViewType;
  }
  return "m";
};

export const storeCalendarViewType = (value: CalendarViewType) => {
  if (typeof window === "object") {
    localStorage.setItem("calendarViewType", value);
  }
};