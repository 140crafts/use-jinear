import Button, { ButtonHeight, ButtonVariants } from "@/components/button";
import NewTaskForm from "@/components/form/newTaskForm/NewTaskForm";
import Line from "@/components/line/Line";
import { useToggle } from "@/hooks/useToggle";
import { TaskDto } from "@/model/be/jinear-core";
import { useInitializeTaskRelationMutation } from "@/store/api/taskRelationApi";
import {
  changeLoadingModalVisibility,
  closeSearchTaskModal,
  popSearchTaskModal,
} from "@/store/slice/modalSlice";
import { useAppDispatch } from "@/store/store";
import useTranslation from "locales/useTranslation";
import React, { useEffect } from "react";
import { IoPauseCircleOutline } from "react-icons/io5";
import {
  useShowSubTaskListEvenIfNoSubtasks,
  useTask,
} from "../context/TaskDetailContext";
import TaskRelationRow from "./taskRelationRow/TaskRelationRow";
import styles from "./TaskSubtaskList.module.css";

interface TaskSubtaskListProps {}

const TaskSubtaskList: React.FC<TaskSubtaskListProps> = ({}) => {
  const dispatch = useAppDispatch();
  const { t } = useTranslation();
  const showSubTaskListEvenIfNoSubtasks = useShowSubTaskListEvenIfNoSubtasks();

  const { current: newTaskInputVisible, toggle: toggleNewTaskInputVisible } =
    useToggle(showSubTaskListEvenIfNoSubtasks);

  const [
    initializeTaskRelation,
    {
      isSuccess: isInitializeRelationSuccess,
      isError: isInitializeRelationError,
    },
  ] = useInitializeTaskRelationMutation();

  const task = useTask();
  const hasSubTasks =
    task.relatedIn?.filter((relation) => relation.relationType == "SUBTASK")
      ?.length != 0;

  useEffect(() => {
    dispatch(changeLoadingModalVisibility({ visible: false }));
    if (isInitializeRelationSuccess) {
      dispatch(closeSearchTaskModal());
    }
  }, [isInitializeRelationSuccess, isInitializeRelationError]);

  const onExistingTaskSelect = (selectedTask: TaskDto) => {
    dispatch(changeLoadingModalVisibility({ visible: true }));
    initializeTaskRelation({
      taskId: selectedTask.taskId,
      relatedTaskId: task.taskId,
      relation: "SUBTASK",
    });
  };

  const openSearchTaskModal = () => {
    dispatch(
      popSearchTaskModal({
        workspaceId: task.workspaceId,
        teamId: task.teamId,
        onSelect: onExistingTaskSelect,
      })
    );
  };

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
                  onClick={openSearchTaskModal}
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
