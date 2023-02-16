import AssigneeCell from "@/components/assigneeCell/AssigneeCell";
import Button, { ButtonVariants } from "@/components/button";
import Line from "@/components/line/Line";
import TeamTagCell from "@/components/teamTagCell/TeamTagCell";
import { TaskDto } from "@/model/be/jinear-core";
import { popChangeTaskDateModal } from "@/store/slice/modalSlice";
import { useAppDispatch } from "@/store/store";
import React from "react";
import { IoTime } from "react-icons/io5";
import styles from "./TaskRow.module.scss";
import TopicInfo from "./topicInfo/TopicInfo";
import WorkflowStatus from "./workflowStatus/WorkflowStatus";

interface TaskRowProps {
  task: TaskDto;
}

const TaskRow: React.FC<TaskRowProps> = ({ task }) => {
  const dispatch = useAppDispatch();
  const tag = `${task.team?.tag}-${task.teamTagNo}`;

  const popChangeDatesModal = (
    event: React.MouseEvent<HTMLButtonElement, MouseEvent>
  ) => {
    event?.preventDefault?.();
    dispatch(popChangeTaskDateModal({ visible: true, task }));
  };

  return task.workspace && task.team ? (
    <>
      <div className={styles.container}>
        <Button
          href={`/${task.workspace?.username}/task/${tag}`}
          className={styles.button}
        >
          <div className={styles.leftInfoContainer}>
            <TeamTagCell task={task} />
          </div>
          <div className={styles.title}>{task.title}</div>
        </Button>

        <div className={styles.rightInfoContainer}>
          {task.topic && <TopicInfo topic={task.topic} />}
          <WorkflowStatus task={task} />
          <AssigneeCell task={task} />
          <Button
            variant={ButtonVariants.filled}
            className={styles.datesButton}
            onClick={popChangeDatesModal}
          >
            <div className={styles.iconContainer}>
              <IoTime size={14} />
            </div>
          </Button>
        </div>
      </div>
      <Line className={styles.line} />
    </>
  ) : null;
};

export default TaskRow;
