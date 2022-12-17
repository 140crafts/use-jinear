import { useDebouncedEffect } from "@/hooks/useDebouncedEffect";
import { useRetrieveAllIntersectingTasksQuery } from "@/store/api/taskListingApi";
import { CircularProgress } from "@mui/material";
import { eachDayOfInterval } from "date-fns";
import React from "react";
import styles from "./PeriodSpanTaskView.module.css";
import TaskPeriodViewRow from "./taskWeekViewRow/TaskPeriodViewRow";
import { PERIOD_SPAN_TASK_VIEW_TODAY_MARK } from "./weekday/title/WeekdayTitle";
import Weekday from "./weekday/Weekday";

interface PeriodSpanTaskViewProps {
  workspaceId: string;
  teamId: string;
  viewingPeriodStart: Date;
  viewingPeriodEnd: Date;
  showDayOfWeek?: boolean;
  containerRef?: React.RefObject<HTMLDivElement>;
}

const PeriodSpanTaskView: React.FC<PeriodSpanTaskViewProps> = ({
  teamId,
  workspaceId,
  viewingPeriodStart,
  viewingPeriodEnd,
  showDayOfWeek,
  containerRef,
}) => {
  const {
    data: taskListingResponse,
    isFetching,
    isSuccess,
  } = useRetrieveAllIntersectingTasksQuery(
    {
      teamId,
      workspaceId,
      timespanStart: viewingPeriodStart,
      timespanEnd: viewingPeriodEnd,
    },
    { skip: teamId == null || workspaceId == null }
  );

  let days = eachDayOfInterval({
    start: viewingPeriodStart,
    end: viewingPeriodEnd,
  });

  useDebouncedEffect(
    () => {
      const todayTitle = document.getElementById(
        PERIOD_SPAN_TASK_VIEW_TODAY_MARK
      );
      if (!isFetching && containerRef && containerRef.current && todayTitle) {
        containerRef.current.scrollTo?.({
          behavior: "smooth",
          left: todayTitle.offsetLeft - todayTitle.offsetWidth * 2,
        });
      }
    },
    [isFetching],
    250
  );
  return (
    <div className={styles.container}>
      {isFetching && (
        <div className={styles.loading}>
          <CircularProgress size={24} />
        </div>
      )}
      <div className={styles.titleContainer}>
        {days.map((day) => (
          <Weekday
            key={day.toISOString()}
            day={day}
            showDayOfWeek={showDayOfWeek}
          />
        ))}
      </div>
      <div className={styles.contentContainer}>
        {!isFetching &&
          taskListingResponse?.data.map((taskDto) => (
            <TaskPeriodViewRow
              key={`${taskDto.taskId}-${viewingPeriodStart.toISOString()}`}
              task={taskDto}
              viewingPeriodStart={viewingPeriodStart}
              viewingPeriodEnd={viewingPeriodEnd}
            />
          ))}
      </div>
    </div>
  );
};

export default PeriodSpanTaskView;
