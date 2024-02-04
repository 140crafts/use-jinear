import Button from "@/components/button";
import { CalendarEventDto } from "@/model/be/jinear-core";
import cn from "classnames";
import React from "react";
import styles from "./NonTaskCalendarEventItem.module.css";

interface NonTaskCalendarEventItemProps {
  calendarEvent: CalendarEventDto;
  className?: string;
  withBottomBorderLine?: boolean;
}

const NonTaskCalendarEventItem: React.FC<NonTaskCalendarEventItemProps> = ({
  calendarEvent,
  className,
  withBottomBorderLine = true,
}) => {
  return (
    <div className={cn(styles.container, withBottomBorderLine ? styles.bottomBorderLine : null, className)}>
      <Button
        // href={`/${task.workspace?.username}/task/${tag}`}
        className={cn(styles.button)}
        // onClick={onLinkClick}
      >
        <div className={styles.leftInfoContainer}>
          <TeamTagCell task={task} />
        </div>
        <div className={cn(styles.title, isArchived ? styles.archivedTitle : null)}>{task.title}</div>
      </Button>

      <div className={styles.rightInfoContainer}>
        {task.topic && <TopicInfo topic={task.topic} />}
        <WorkflowStatus task={task} />
        <AssigneeCell task={task} />
        <Button
          variant={ButtonVariants.filled}
          className={styles.iconButton}
          onClick={popChangeDatesModal}
          data-tooltip-right={t("taskRowChangeTaskDates")}
        >
          <div className={styles.iconContainer}>
            <IoTimeOutline size={14} />
          </div>
        </Button>
        <Button
          variant={ButtonVariants.filled}
          className={styles.iconButton}
          onClick={popBoardsModal}
          data-tooltip-right={t("taskRowBoardsTooltip")}
        >
          <div className={styles.iconContainer}>
            <IoReaderOutline size={14} />
          </div>
        </Button>
      </div>
    </div>
  );
};

export default NonTaskCalendarEventItem;
