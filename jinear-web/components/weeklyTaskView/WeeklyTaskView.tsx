import { useRetrieveAllIntersectingTasksQuery } from "@/store/api/taskListingApi";
import { CircularProgress } from "@mui/material";
import { eachDayOfInterval } from "date-fns";
import React from "react";
import TaskWeekViewRow from "./taskWeekViewRow/TaskWeekViewRow";
import Weekday from "./weekday/Weekday";
import styles from "./WeeklyTaskView.module.css";

interface WeeklyTaskViewProps {
  workspaceId: string;
  teamId: string;
  viewingWeekStart: Date;
  viewingWeekEnd: Date;
}

const WeeklyTaskView: React.FC<WeeklyTaskViewProps> = ({
  teamId,
  workspaceId,
  viewingWeekStart,
  viewingWeekEnd,
}) => {
  const {
    data: taskListingResponse,
    isLoading,
    isSuccess,
  } = useRetrieveAllIntersectingTasksQuery(
    {
      teamId,
      workspaceId,
      timespanStart: viewingWeekStart,
      timespanEnd: viewingWeekEnd,
    },
    { skip: teamId == null || workspaceId == null }
  );

  let days = eachDayOfInterval({
    start: viewingWeekStart,
    end: viewingWeekEnd,
  });

  return (
    <div className={styles.container}>
      <div className={styles.titleContainer}>
        {days.map((day) => (
          <Weekday key={day.toISOString()} day={day} />
        ))}
      </div>
      <div className={styles.contentContainer}>
        {isLoading && (
          <div className={styles.loading}>
            <CircularProgress size={24} />
          </div>
        )}
        {taskListingResponse?.data.map((taskDto) => (
          <TaskWeekViewRow
            key={`${taskDto.taskId}-${viewingWeekStart.toISOString()}`}
            task={taskDto}
            viewingWeekStart={viewingWeekStart}
            viewingWeekEnd={viewingWeekEnd}
          />
        ))}
      </div>
    </div>
  );
};

export default WeeklyTaskView;
