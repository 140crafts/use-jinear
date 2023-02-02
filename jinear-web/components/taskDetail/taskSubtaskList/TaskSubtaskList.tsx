import Button, { ButtonHeight, ButtonVariants } from "@/components/button";
import NewTaskForm from "@/components/form/newTaskForm/NewTaskForm";
import Line from "@/components/line/Line";
import { useToggle } from "@/hooks/useToggle";
import useTranslation from "locales/useTranslation";
import React from "react";
import { IoPauseCircleOutline } from "react-icons/io5";
import {
  useShowSubTaskListEvenIfNoSubtasks,
  useTask,
} from "../context/TaskDetailContext";
import TaskRelationRow from "./taskRelationRow/TaskRelationRow";
import styles from "./TaskSubtaskList.module.css";

interface TaskSubtaskListProps {}

const TaskSubtaskList: React.FC<TaskSubtaskListProps> = ({}) => {
  const { t } = useTranslation();
  const showSubTaskListEvenIfNoSubtasks = useShowSubTaskListEvenIfNoSubtasks();

  const { current: newTaskInputVisible, toggle: toggleNewTaskInputVisible } =
    useToggle(showSubTaskListEvenIfNoSubtasks);

  const task = useTask();
  const hasSubTasks =
    task.relatedIn?.filter((relation) => relation.relationType == "SUBTASK")
      ?.length != 0;

  return showSubTaskListEvenIfNoSubtasks || hasSubTasks ? (
    <>
      <Line />
      <div className={styles.container}>
        <h2>{t("taskSubtaskList")}</h2>
        <div className={styles.content}>
          {task.relatedIn?.map((relation) => (
            <TaskRelationRow
              key={relation.taskRelationId}
              relation={relation}
            />
          ))}

          <div className={styles.newInputContent}>
            {!newTaskInputVisible && (
              <div className={styles.newTaskInputButtonContainer}>
                <Button
                  onClick={toggleNewTaskInputVisible}
                  variant={ButtonVariants.filled2}
                  heightVariant={ButtonHeight.short}
                >
                  {t("taskSubtaskListAddNewTaskButton")}
                </Button>
                <Button
                  onClick={toggleNewTaskInputVisible}
                  variant={ButtonVariants.filled2}
                  heightVariant={ButtonHeight.short}
                >
                  {t("taskSubtaskListAddExistingTaskButton")}
                </Button>
              </div>
            )}
            {newTaskInputVisible && (
              <div className={styles.newTaskFormContainer}>
                <Button className={styles.newTaskInputButton}>
                  <IoPauseCircleOutline size={20} />
                </Button>
                <NewTaskForm
                  workspaceId={task.workspaceId}
                  teamId={task.teamId}
                  subTaskOf={task.taskId}
                  onClose={toggleNewTaskInputVisible}
                  className={styles.newTaskForm}
                />
              </div>
            )}
          </div>
        </div>
      </div>
    </>
  ) : null;
};

export default TaskSubtaskList;
