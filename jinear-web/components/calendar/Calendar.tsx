import { TaskDto } from "@/model/be/jinear-core";
import { useRetrieveAllIntersectingTasksQuery } from "@/store/api/taskListingApi";
import Logger from "@/utils/logger";
import { CircularProgress } from "@mui/material";
import { eachDayOfInterval, endOfDay, endOfMonth, endOfWeek, format, isSameDay, parse, startOfDay, startOfWeek } from "date-fns";
import useTranslation from "locales/useTranslation";
import React, { useMemo, useState } from "react";
import styles from "./Calendar.module.css";
import CalendarContext from "./context/CalendarContext";
import DayCell from "./dayCell/DayCell";

interface CalendarProps {
  initialDate?: Date;
  workspaceId: string;
}

function splitChunks<T>(sourceArray: T[], chunkSize: number) {
  if (chunkSize <= 0) throw "chunkSize must be greater than 0";
  let result = [];
  for (var i = 0; i < sourceArray.length; i += chunkSize) {
    result[i / chunkSize] = sourceArray.slice(i, i + chunkSize);
  }
  return result;
}

const logger = Logger("Calendar");

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

const Calendar: React.FC<CalendarProps> = ({ workspaceId, initialDate = startOfDay(new Date()) }) => {
  const { t } = useTranslation();
  const [highlightedTaskId, setHighlightedTaskId] = useState<string>("");
  const [viewingDate, setViewingDate] = useState(initialDate);
  const [selectedDate, setSelectedDate] = useState(initialDate);

  const currentMonth = format(viewingDate, "MMM-yyyy");
  const firstDayCurrentMonth = parse(currentMonth, "MMM-yyyy", new Date());
  const periodStart = startOfWeek(firstDayCurrentMonth, { weekStartsOn: 1 });
  const periodEnd = endOfWeek(endOfMonth(firstDayCurrentMonth), { weekStartsOn: 1 });

  const days = eachDayOfInterval({ start: periodStart, end: periodEnd });
  const {
    data: taskListingResponse,
    isFetching,
    isSuccess,
  } = useRetrieveAllIntersectingTasksQuery(
    {
      workspaceId,
      timespanStart: periodStart,
      timespanEnd: periodEnd,
    },
    { skip: workspaceId == null }
  );

  const weeks = splitChunks(days, 7);
  const tasksByWeek = useMemo(() => {
    if (!taskListingResponse || !taskListingResponse.data) {
      return [];
    }
    const taskChunks: TaskDto[][] = [];
    weeks.map((week) => {
      const weekStart = week[0].getTime();
      const weekEnd = week[week.length - 1].getTime();
      const weekTasks = taskListingResponse.data
        .filter((task) => task.assignedDate || task.dueDate)
        .filter((task) => {
          const startDate = (task.assignedDate ? new Date(task.assignedDate) : new Date(task.dueDate)).getTime();
          const endDate = (task.dueDate ? new Date(task.dueDate) : new Date(task.assignedDate)).getTime();
          const isStartInBetween = weekStart <= startDate && startDate <= weekEnd;
          const isEndInBetween = weekStart <= endDate && endDate <= weekEnd;
          const startedBeforeAndEndedAfter = startDate <= weekStart && weekEnd <= endDate;
          return isStartInBetween || isEndInBetween || startedBeforeAndEndedAfter;
        });
      taskChunks.push(weekTasks);
    });
    return taskChunks;
  }, [taskListingResponse, weeks]);

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

  const allTasksAllHitMissTable: (TaskDto | null)[][] | undefined = useMemo(() => {
    if (!taskListingResponse || !taskListingResponse.data) {
      return;
    }
    const tasks = taskListingResponse.data;
    const allTasksAllMonthHitMissTable: Array<TaskDto | null>[][] = [];
    tasks.map((task) => {
      const taskHitMissTable: Array<TaskDto | null> = [];
      days.map((day) => {
        if (taskForDateFilter(task, day)) {
          taskHitMissTable.push(task);
        } else {
          taskHitMissTable.push(null);
        }
      });
      const taskWeeklyHitMissTable: (TaskDto | null)[][] = splitChunks(taskHitMissTable, 7);
      taskWeeklyHitMissTable.map((data, index) => {
        allTasksAllMonthHitMissTable[index] = allTasksAllMonthHitMissTable[index] ? allTasksAllMonthHitMissTable[index] : [];
        allTasksAllMonthHitMissTable[index].push(data);
      });
    });

    const result = allTasksAllMonthHitMissTable.map(mergeWeek).map(rotateWeek).flat(1);
    logger.log({ allTasksAllHitMissTable: result });
    return result;
  }, [JSON.stringify(days), JSON.stringify(taskListingResponse)]);

  return (
    <CalendarContext.Provider
      value={{
        highlightedTaskId,
        setHighlightedTaskId,
        viewingDate,
        periodStart,
        periodEnd,
        selectedDate,
        tasks: taskListingResponse?.data,
      }}
    >
      <div className={styles.container}>
        <div className={styles.days}>
          {allTasksAllHitMissTable &&
            days?.map((day, dayIndex) => (
              <DayCell key={`calendar-day-cell-${day.toISOString()}`} day={day} tasksOnDay={allTasksAllHitMissTable[dayIndex]} />
            ))}
        </div>

        {isFetching && (
          <div className={styles.loadingContainer}>
            <CircularProgress />
          </div>
        )}
      </div>
    </CalendarContext.Provider>
  );
};

export default Calendar;
