import TaskRow from "@/components/taskRow/TaskRow";
import { queryStateShortDateParser, useQueryState } from "@/hooks/useQueryState";
import { CalendarEventDto } from "@/model/be/jinear-core";
import Logger from "@/utils/logger";
import cn from "classnames";
import { startOfDay } from "date-fns";
import useTranslation from "locales/useTranslation";
import React from "react";
import styles from "./TaskList.module.css";

interface TaskListProps {
  className: string;
  viewingDayEvents: CalendarEventDto[];
}

const logger = Logger("TaskList");
const TaskList: React.FC<TaskListProps> = ({ className, viewingDayEvents }) => {
  const { t } = useTranslation();
  const viewingDate = useQueryState<Date>("viewingDate", queryStateShortDateParser) || startOfDay(new Date());
  logger.log({ viewingDate, viewingDayEvents });

  return (
    <div className={cn(styles.container, className)}>
      {viewingDayEvents.map((event) =>
        event.calendarEventSourceType == "TASK" && event.relatedTask ? (
          <TaskRow key={event.calendarEventId} task={event.relatedTask} />
        ) : (
          <>todo events</>
        )
      )}
      {viewingDayEvents.length == 0 && <div className={styles.emptyLabel}>{t("calendarDayViewEmptyDayLabel")}</div>}
    </div>
  );
};

export default TaskList;
