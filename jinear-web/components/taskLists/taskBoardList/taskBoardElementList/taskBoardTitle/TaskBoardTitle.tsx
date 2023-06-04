import { TaskBoardStateType, TaskSearchResultDto, TeamDto, WorkspaceDto } from "@/model/be/jinear-core";
import { useInitializeTaskBoardEntryMutation } from "@/store/api/taskBoardEntryApi";

import Button, { ButtonHeight, ButtonVariants } from "@/components/button";
import { useUpdateDueDateMutation, useUpdateStateMutation, useUpdateTitleMutation } from "@/store/api/taskBoardApi";
import { selectCurrentAccountsPreferredTeamRoleIsAdmin } from "@/store/slice/accountSlice";
import {
  changeLoadingModalVisibility,
  closeBasicTextInputModal,
  closeDatePickerModal,
  closeSearchTaskModal,
  popBasicTextInputModal,
  popDatePickerModal,
  popSearchTaskModal,
} from "@/store/slice/modalSlice";
import { useAppDispatch, useTypedSelector } from "@/store/store";
import cn from "classnames";
import { differenceInDays, format, isToday, startOfToday } from "date-fns";
import useTranslation from "locales/useTranslation";
import React, { useEffect, useMemo } from "react";
import { IoLockClosedOutline, IoLockOpenOutline, IoPencil } from "react-icons/io5";
import styles from "./TaskBoardTitle.module.scss";

interface TaskBoardTitleProps {
  title: string;
  taskBoardId: string;
  boardState: TaskBoardStateType;
  team: TeamDto;
  workspace: WorkspaceDto;
  dueDate: Date;
}

const TaskBoardTitle: React.FC<TaskBoardTitleProps> = ({ title, taskBoardId, boardState, team, workspace, dueDate }) => {
  const { t } = useTranslation();
  const dispatch = useAppDispatch();
  const isTeamAdmin = useTypedSelector(selectCurrentAccountsPreferredTeamRoleIsAdmin);

  const [initializeTaskBoardEntry, { isLoading: isInitializeLoading, isSuccess: isInitializeSuccess }] =
    useInitializeTaskBoardEntryMutation();
  const [updateState, { isLoading: isUpdateStateLoading }] = useUpdateStateMutation();
  const [updateDueDate, { isLoading: isUpdateDueDateLoading }] = useUpdateDueDateMutation();
  const [updateTitle, { isLoading: isUpdateTitleLoading }] = useUpdateTitleMutation();

  useEffect(() => {
    dispatch(
      changeLoadingModalVisibility({
        visible: isInitializeLoading || isUpdateStateLoading || isUpdateDueDateLoading || isUpdateTitleLoading,
      })
    );
    if (!isInitializeLoading) {
      dispatch(closeSearchTaskModal());
    }
  }, [isInitializeLoading, isUpdateStateLoading, isUpdateDueDateLoading, isUpdateTitleLoading]);

  const openSearchTaskModal = () => {
    dispatch(
      popSearchTaskModal({
        workspaceId: workspace.workspaceId,
        teamId: team.teamId,
        onSelect: onExistingTaskSelect,
      })
    );
  };

  const onExistingTaskSelect = (selectedTask: TaskSearchResultDto) => {
    dispatch(changeLoadingModalVisibility({ visible: true }));
    initializeTaskBoardEntry({
      taskBoardId,
      taskId: selectedTask.taskId,
    });
  };

  const toggleBoardState = () => {
    const state = boardState == "OPEN" ? "CLOSED" : "OPEN";
    updateState({ taskBoardId, state });
  };

  const changeDueDate = (dueDate: Date) => {
    dispatch(closeDatePickerModal());
    updateDueDate({ taskBoardId, dueDate });
  };

  const changeTitle = (title: string) => {
    dispatch(closeBasicTextInputModal());
    updateTitle({ taskBoardId, title });
  };

  const popDatePickerForDueDateUpdate = () => {
    dispatch(
      popDatePickerModal({
        visible: true,
        onDateChange: changeDueDate,
      })
    );
  };

  const popTitleChangeModal = () => {
    dispatch(
      popBasicTextInputModal({
        visible: true,
        title: t("taskBoardChangeTitleModalTitle"),
        infoText: t("taskBoardChangeTitleModalInfoText"),
        initialText: title,
        onSubmit: changeTitle,
      })
    );
  };

  const dateDiff = useMemo(() => {
    try {
      if (!dueDate) {
        return;
      }
      const _dueDate = new Date(dueDate);
      if (isToday(_dueDate)) {
        return t("taskBoardDueDateToday");
      }
      const diffInDays = differenceInDays(_dueDate, startOfToday());
      if (diffInDays == 1) {
        return t("taskBoardDeadlineTomorrow")?.replace("${num}", `${diffInDays}`);
      } else if (diffInDays > 0) {
        return t("taskBoardRemainingDaysLabelDateInDays")?.replace("${num}", `${diffInDays}`);
      }
      return t("taskBoardDueDatePast")?.replace("${num}", `${Math.abs(diffInDays)}`);
    } catch (error) {
      console.error(error);
    }
  }, [dueDate]);

  return (
    <div className={cn(styles.listTitle, boardState == "CLOSED" && styles.closedListTitle)}>
      <div className={styles.titleLabelContainer}>
        <Button
          className={styles.listTitleButton}
          key={`board-title-${taskBoardId}`}
          href={`/${workspace?.username || ""}/${team?.username || ""}/task-boards/${taskBoardId}`}
          heightVariant={ButtonHeight.short}
        >
          <b>{title}</b>
        </Button>
        {boardState == "OPEN" && (
          <Button heightVariant={ButtonHeight.short} className={styles.editTitleButton} onClick={popTitleChangeModal}>
            <IoPencil />
          </Button>
        )}
      </div>

      <div className="flex-1" />

      <div className={styles.titleActionBar}>
        <Button variant={ButtonVariants.filled} onClick={toggleBoardState} disabled={!isTeamAdmin}>
          {boardState == "OPEN" ? <IoLockOpenOutline /> : <IoLockClosedOutline />}
        </Button>
        <Button
          disabled={boardState != "OPEN"}
          variant={ButtonVariants.filled}
          heightVariant={ButtonHeight.short}
          onClick={popDatePickerForDueDateUpdate}
          data-tooltip-right={dueDate ? format(new Date(dueDate), t("dateFormat")) : undefined}
        >
          {dateDiff ? dateDiff : t("taskBoardAssignDueDate")}
        </Button>
        <Button
          disabled={boardState != "OPEN"}
          variant={ButtonVariants.filled}
          heightVariant={ButtonHeight.short}
          onClick={openSearchTaskModal}
        >
          {t("taskBoardAddTaskButtonLabel")}
        </Button>
      </div>
    </div>
  );
};

export default TaskBoardTitle;
