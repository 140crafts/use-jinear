import { TaskDto } from "@/model/be/jinear-core";
import cn from "classnames";
import Link from "next/link";
import React from "react";
import AssigneeCell from "./assigneeCell/AssigneeCell";
import DueDateAfterIcon from "./dueDateAfterIcon/DueDateAfterIcon";
import StartDateBeforeIcon from "./startDateBeforeIcon/StartDateBeforeIcon";
import styles from "./TaskWeekViewCard.module.css";
import TeamTagCell from "./teamTagCell/TeamTagCell";
import TitleCell from "./titleCell/TitleCell";

interface TaskWeekViewCardProps {
  task: TaskDto;
  isStartDateBefore: boolean;
  isDueDateAfter: boolean;
  daysInThisWeek: number;
  style: React.CSSProperties;
  className: string;
}

const TaskWeekViewCard: React.FC<TaskWeekViewCardProps> = ({
  task,
  isStartDateBefore,
  isDueDateAfter,
  daysInThisWeek,
  style,
  className,
}) => {
  return (
    <Link
      href={`/${task.workspace?.username}/task/${task.team?.tag}-${task.teamTagNo}`}
      className={cn(styles.container, className)}
      style={style}
    >
      <TitleCell task={task} daysInThisWeek={daysInThisWeek} />
      <div className={styles.infoContainer}>
        <StartDateBeforeIcon
          assignedDate={task.assignedDate}
          isStartDateBefore={isStartDateBefore}
        />
        <TeamTagCell task={task} />

        <AssigneeCell task={task} />
        <div className="flex-1" />
        <DueDateAfterIcon
          dueDate={task.dueDate}
          isDueDateAfter={isDueDateAfter}
        />
      </div>
    </Link>
  );
};

export default TaskWeekViewCard;
