import Button from "@/components/button";
import TeamTagCell from "@/components/teamTagCell/TeamTagCell";
import { TaskDto } from "@/model/be/jinear-core";
import { popChangeTaskDateModal } from "@/store/slice/modalSlice";
import { useAppDispatch } from "@/store/store";
import cn from "classnames";
import Link from "next/link";
import React from "react";
import { IoTime } from "react-icons/io5";
import AssigneeCell from "../../assigneeCell/AssigneeCell";
import DueDateAfterIcon from "./dueDateAfterIcon/DueDateAfterIcon";
import StartDateBeforeIcon from "./startDateBeforeIcon/StartDateBeforeIcon";
import styles from "./TaskPeriodViewCard.module.css";
import TitleCell from "./titleCell/TitleCell";

interface TaskPeriodViewCardProps {
  task: TaskDto;
  isStartDateBefore: boolean;
  isDueDateAfter: boolean;
  style: React.CSSProperties;
  className?: string;
  showAdditionalInfo?: boolean;
  duration?: number;
}

const TaskPeriodViewCard: React.FC<TaskPeriodViewCardProps> = ({
  task,
  isStartDateBefore,
  isDueDateAfter,
  style,
  className,
  showAdditionalInfo = true,
  duration = 1,
}) => {
  const dispatch = useAppDispatch();

  const popChangeDatesModal = (event: React.MouseEvent<HTMLButtonElement, MouseEvent>) => {
    event?.preventDefault?.();
    dispatch(popChangeTaskDateModal({ visible: true, task }));
  };

  return (
    <Link
      href={`/${task.workspace?.username}/task/${task.team?.tag}-${task.teamTagNo}`}
      className={cn(styles.container, className)}
      style={style}
    >
      <div className={styles.content}>
        <TitleCell task={task} duration={duration} data-tooltip-multiline={duration == 1 ? task.title : undefined} />
        {showAdditionalInfo && (
          <div className={styles.infoContainer}>
            <StartDateBeforeIcon assignedDate={task.assignedDate} isStartDateBefore={isStartDateBefore} />

            <TeamTagCell task={task} className={styles.taskTagCell} />

            <Button className={styles.taskTagCell} onClick={popChangeDatesModal}>
              <IoTime size={12} />
            </Button>

            {!task.workspace?.isPersonal && <AssigneeCell task={task} className={styles.taskTagCell} />}
            <div className="flex-1" />
            <DueDateAfterIcon dueDate={task.dueDate} isDueDateAfter={isDueDateAfter} />
          </div>
        )}
      </div>
    </Link>
  );
};

export default TaskPeriodViewCard;
