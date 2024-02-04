import Button, { ButtonHeight, ButtonVariants } from "@/components/button";
import CircularLoading from "@/components/circularLoading/CircularLoading";
import { queryStateShortDateParser, useQueryState } from "@/hooks/useQueryState";
import { useWorkspaceFirstTeam } from "@/hooks/useWorkspaceFirstTeam";
import { WorkspaceDto } from "@/model/be/jinear-core";
import { useFilterCalendarEventsQuery } from "@/store/api/calendarEventApi";
import { popNewTaskModal } from "@/store/slice/modalSlice";
import { useAppDispatch } from "@/store/store";
import { eachDayOfInterval, endOfDay, endOfWeek, startOfDay, startOfWeek } from "date-fns";
import useTranslation from "locales/useTranslation";
import React, { useMemo } from "react";
import { isTaskDatesIntersect } from "../calendarUtils";
import styles from "./DayView.module.css";
import TaskList from "./taskList/TaskList";
import WeekDays from "./weekDays/WeekDays";

interface DayViewProps {
  workspace: WorkspaceDto;
}

const DayView: React.FC<DayViewProps> = ({ workspace }) => {
  const { t } = useTranslation();
  const dispatch = useAppDispatch();
  const viewingDate = useQueryState<Date>("viewingDate", queryStateShortDateParser) || startOfDay(new Date());

  const periodStart = startOfWeek(viewingDate, { weekStartsOn: 1 });
  const periodEnd = endOfWeek(viewingDate, { weekStartsOn: 1 });
  const days = eachDayOfInterval({ start: periodStart, end: periodEnd });

  const workspacesFirstTeam = useWorkspaceFirstTeam(workspace?.workspaceId || "");

  const { data: filterResponse, isFetching } = useFilterCalendarEventsQuery(
    {
      workspaceId: workspace?.workspaceId || "",
      timespanStart: periodStart,
      timespanEnd: periodEnd,
    },
    { skip: workspace == null }
  );

  const viewingDayEvents = useMemo(() => {
    const dateSpan = [startOfDay(viewingDate), endOfDay(viewingDate)];
    return filterResponse?.data.filter((event) => isTaskDatesIntersect(event, dateSpan));
  }, [filterResponse, viewingDate]);

  const popNewTaskModalWithAssignedDatePreSelected = () => {
    dispatch(popNewTaskModal({ visible: true, workspace, team: workspacesFirstTeam, initialAssignedDate: viewingDate }));
  };

  return (
    <div className={styles.container}>
      <WeekDays days={days} />
      <div className={styles.actionContainer}>
        <Button
          onClick={popNewTaskModalWithAssignedDatePreSelected}
          variant={ButtonVariants.filled}
          heightVariant={ButtonHeight.short}
        >
          {t("calendarDayViewNewTaskButton")}
        </Button>
      </div>
      <div className={styles.listContainer}>
        {isFetching && <CircularLoading />}
        {viewingDayEvents && <TaskList viewingDayEvents={viewingDayEvents} className={styles.taskList} />}
      </div>
    </div>
  );
};

export default DayView;
