import { useDebouncedEffect } from "@/hooks/useDebouncedEffect";
import { useRetrieveAllIntersectingTasksQuery } from "@/store/api/taskListingApi";
import { CircularProgress } from "@mui/material";
import { eachDayOfInterval } from "date-fns";
import React from "react";
import PeriodSpanTaskViewContext from "./context/PeriodSpanTaskViewContext";
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
  variant: "month" | "week";
}

const PeriodSpanTaskView: React.FC<PeriodSpanTaskViewProps> = ({
  teamId,
  workspaceId,
  viewingPeriodStart,
  viewingPeriodEnd,
  showDayOfWeek,
  containerRef,
  variant = "week",
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
    <PeriodSpanTaskViewContext.Provider
      value={{
        viewingPeriodStart: viewingPeriodStart,
        viewingPeriodEnd: viewingPeriodEnd,
        variant,
        showDayOfWeek,
      }}
    >
      <div className={styles.container}>
        {isFetching && (
          <div className={styles.loading}>
            <CircularProgress size={24} />
          </div>
        )}
        <div className={styles.titleContainer}>
          {days.map((day) => (
            <Weekday key={day.toISOString()} day={day} />
          ))}
        </div>
        <div className={styles.contentContainer}>
          {/* <div className={styles.leftBar}>123</div> */}
          {!isFetching &&
            taskListingResponse?.data.map((taskDto) => (
              <TaskPeriodViewRow
                key={`${taskDto.taskId}-${viewingPeriodStart.toISOString()}`}
                task={taskDto}
              />
            ))}
        </div>
      </div>
    </PeriodSpanTaskViewContext.Provider>
  );
};

export default PeriodSpanTaskView;
