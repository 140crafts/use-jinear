import AssigneeCell from "@/components/assigneeCell/AssigneeCell";
import Button from "@/components/button";
import Line from "@/components/line/Line";
import TeamTagCell from "@/components/teamTagCell/TeamTagCell";
import { TaskDto } from "@/model/be/jinear-core";
import React from "react";
import styles from "./TaskRow.module.scss";
import TopicInfo from "./topicInfo/TopicInfo";
import WorkflowStatus from "./workflowStatus/WorkflowStatus";

interface TaskRowProps {
  task: TaskDto;
}

const TaskRow: React.FC<TaskRowProps> = ({ task }) => {
  const tag = `${task.team?.tag}-${task.teamTagNo}`;
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
        </div>
      </div>
      <Line className={styles.line} />
    </>
  ) : null;
};

export default TaskRow;
