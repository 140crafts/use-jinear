import useTranslation from "locales/useTranslation";
import React from "react";
import { useTask } from "../context/TaskDetailContext";
import TaskRelationCell from "./taskRelationCell/TaskRelationCell";
import styles from "./TaskRelationList.module.css";

interface TaskRelationListProps {}

const TaskRelationList: React.FC<TaskRelationListProps> = ({}) => {
  const { t } = useTranslation();
  const task = useTask();
  const hasRelations = task.relations?.length != 0;

  return hasRelations ? (
    <>
      {/* <Line /> */}
      <div className={styles.container}>
        <b>{t("taskRelationListTitle")}</b>
        <div className="spacer-h-1" />
        <div className={styles.relationList}>
          {task.relations?.map((relation) => (
            <TaskRelationCell key={`task-relation-cell-${relation.taskRelationId}`} relation={relation} />
          ))}
        </div>
      </div>
    </>
  ) : null;
};

export default TaskRelationList;
