import { TaskDto } from "@/model/be/jinear-core";
import cn from "classnames";
import Link from "next/link";
import React from "react";
import AssigneeCell from "../../assigneeCell/AssigneeCell";
import TeamTagCell from "../../teamTagCell/TeamTagCell";
import DueDateAfterIcon from "./dueDateAfterIcon/DueDateAfterIcon";
import StartDateBeforeIcon from "./startDateBeforeIcon/StartDateBeforeIcon";
import styles from "./TaskPeriodViewCard.module.css";
import TitleCell from "./titleCell/TitleCell";

interface TaskPeriodViewCardProps {
  task: TaskDto;
  isStartDateBefore: boolean;
  isDueDateAfter: boolean;
  style: React.CSSProperties;
  className: string;
}

const TaskPeriodViewCard: React.FC<TaskPeriodViewCardProps> = ({
  task,
  isStartDateBefore,
  isDueDateAfter,
  style,
  className,
}) => {
  return (
    <Link
      href={`/${task.workspace?.username}/task/${task.team?.tag}-${task.teamTagNo}`}
      className={cn(styles.container, className)}
      style={style}
    >
      <div className={styles.content}>
        <TitleCell task={task} />
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
      </div>
    </Link>
  );
};

export default TaskPeriodViewCard;
