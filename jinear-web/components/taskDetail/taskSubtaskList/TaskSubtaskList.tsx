import Button, { ButtonHeight, ButtonVariants } from "@/components/button";
import Line from "@/components/line/Line";
import { TaskSearchResultDto } from "@/model/be/jinear-core";
import { useInitializeTaskRelationMutation } from "@/store/api/taskRelationApi";
import {
  changeLoadingModalVisibility,
  closeSearchTaskModal,
  popNewTaskWithSubtaskRelationModal,
  popSearchTaskModal,
} from "@/store/slice/modalSlice";
import { useAppDispatch } from "@/store/store";
import useTranslation from "locales/useTranslation";
import React, { useEffect } from "react";
import { useShowSubTaskListEvenIfNoSubtasks, useTask } from "../context/TaskDetailContext";
import styles from "./TaskSubtaskList.module.scss";
import TaskRelationRow from "./taskRelationRow/TaskRelationRow";

interface TaskSubtaskListProps {}

const TaskSubtaskList: React.FC<TaskSubtaskListProps> = ({}) => {
  const dispatch = useAppDispatch();
  const { t } = useTranslation();
  const showSubTaskListEvenIfNoSubtasks = useShowSubTaskListEvenIfNoSubtasks();

  const [initializeTaskRelation, { isSuccess: isInitializeRelationSuccess, isError: isInitializeRelationError }] =
    useInitializeTaskRelationMutation();

  const task = useTask();
  const hasSubTasks = task.relatedIn?.filter((relation) => relation.relationType == "SUBTASK")?.length != 0;

  useEffect(() => {
    dispatch(changeLoadingModalVisibility({ visible: false }));
    if (isInitializeRelationSuccess) {
      dispatch(closeSearchTaskModal());
    }
  }, [isInitializeRelationSuccess, isInitializeRelationError]);

  const onExistingTaskSelect = (selectedTask: TaskSearchResultDto) => {
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

  const popNewTaskModalWithRelation = () => {
    const workspace = task.workspace;
    const team = task.team;
    if (workspace && team) {
      dispatch(
        popNewTaskWithSubtaskRelationModal({
          visible: true,
          subTaskOf: task.taskId,
          workspace,
          team,
          subTaskOfLabel: `[${task.team?.tag}-${task.teamTagNo}] ${task.title}`,
        })
      );
    }
  };

  return showSubTaskListEvenIfNoSubtasks || hasSubTasks ? (
    <>
      <Line />
      <div className={styles.container}>
        <div className={styles.headerContainer}>
          <h3>{t("taskSubtaskList")}</h3>

          <div className={styles.newTaskInputButtonContainer}>
            <Button
              onClick={popNewTaskModalWithRelation}
              variant={ButtonVariants.filled}
              heightVariant={ButtonHeight.short}
              data-tooltip-right={t("taskSubtaskListAddNewTaskButtonTooltip")}
            >
              {t("taskSubtaskListAddNewTaskButton")}
            </Button>
            <Button
              onClick={openSearchTaskModal}
              variant={ButtonVariants.filled}
              heightVariant={ButtonHeight.short}
              data-tooltip-right={t("taskSubtaskListAddExistingTaskButtonTooltip")}
            >
              {t("taskSubtaskListAddExistingTaskButton")}
            </Button>
          </div>
        </div>
        <div className={styles.content}>
          {task.relatedIn?.map((relation) => (
            <TaskRelationRow key={relation.taskRelationId} {...relation} noAccessLabel={t("taskRelatedTaskNoAccess")} />
          ))}

          {!hasSubTasks && <div>{t("taskSubtaskListEmpty")}</div>}
        </div>
      </div>
    </>
  ) : null;
};

export default TaskSubtaskList;
