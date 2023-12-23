import Button, { ButtonHeight, ButtonVariants } from "@/components/button";
import CircularLoading from "@/components/circularLoading/CircularLoading";
import { useWorkspaceFirstTeam } from "@/hooks/useWorkspaceFirstTeam";
import { useFilterTasksQuery } from "@/store/api/taskListingApi";
import { popNewTaskModal } from "@/store/slice/modalSlice";
import { useAppDispatch } from "@/store/store";
import { endOfDay, startOfDay } from "date-fns";
import useTranslation from "locales/useTranslation";
import React, { useMemo } from "react";
import { isTaskDatesIntersect } from "../calendarUtils";
import {
  useCalendarWorkspace,
  useFilterBy,
  useSqueezedView,
  useViewingDate,
  useWeekDays,
  useWeekViewPeriodEnd,
  useWeekViewPeriodStart,
} from "../context/CalendarContext";
import styles from "./DayView.module.css";
import TaskList from "./taskList/TaskList";
import WeekDays from "./weekDays/WeekDays";

interface DayViewProps {}

const DayView: React.FC<DayViewProps> = ({}) => {
  const { t } = useTranslation();
  const dispatch = useAppDispatch();
  const workspace = useCalendarWorkspace();
  const filterBy = useFilterBy();
  const periodStart = useWeekViewPeriodStart();
  const periodEnd = useWeekViewPeriodEnd();
  const days = useWeekDays();
  const squeezedView = useSqueezedView();
  const viewingDate = useViewingDate();
  const workspacesFirstTeam = useWorkspaceFirstTeam(workspace?.workspaceId || "");

  const {
    data: filterResponse,
    isFetching,
    isLoading,
  } = useFilterTasksQuery(
    {
      workspaceId: workspace?.workspaceId || "",
      teamIdList: filterBy ? [filterBy.teamId] : undefined,
      timespanStart: periodStart,
      timespanEnd: periodEnd,
    },
    { skip: workspace == null }
  );

  const viewingDayTasks = useMemo(() => {
    const dateSpan = [startOfDay(viewingDate), endOfDay(viewingDate)];
    return filterResponse?.data.content.filter((task) => isTaskDatesIntersect(task, dateSpan));
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
        {viewingDayTasks && <TaskList viewingDayTasks={viewingDayTasks} className={styles.taskList} />}
      </div>
    </div>
  );
};

export default DayView;
