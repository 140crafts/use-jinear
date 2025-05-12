import { TaskBoardDto, TaskBoardStateType, TaskDto, TeamDto, WorkspaceDto } from "@/be/jinear-core";
import { useInitializeTaskBoardEntryMutation } from "@/api/taskBoardEntryApi";

import Button, { ButtonHeight, ButtonVariants } from "@/components/button";
import { useCurrentAccountsTeamRoleIsAdmin } from "@/hooks/useCurrentAccountsTeamRole";
import { useUpdateDueDateMutation, useUpdateStateMutation, useUpdateTitleMutation } from "@/api/taskBoardApi";
import {
  changeLoadingModalVisibility,
  closeBasicTextInputModal,
  closeDatePickerModal,
  closeSearchTaskModal,
  popBasicTextInputModal,
  popDatePickerModal, popNewTaskModal,
  popSearchTaskModal
} from "@/slice/modalSlice";
import { useAppDispatch } from "@/store/store";
import cn from "classnames";
import { differenceInDays, format, isToday, startOfToday } from "date-fns";
import useTranslation from "@/locals/useTranslation";
import React, { useEffect, useMemo } from "react";
import {
  IoArrowForward,
  IoList,
  IoLockClosedOutline,
  IoLockOpenOutline,
  IoPencil,
  IoStatsChart
} from "react-icons/io5";
import styles from "./TaskBoardTitle.module.scss";
import { useSetQueryState } from "@/hooks/useQueryState";
import { LuPenSquare, LuSearch } from "react-icons/lu";

interface TaskBoardTitleProps {
  taskBoard: TaskBoardDto;
  team: TeamDto;
  workspace: WorkspaceDto;
  displayFormat: "list" | "column";
  noDisplayFormatChange: boolean;
}

const TaskBoardTitle: React.FC<TaskBoardTitleProps> = ({
                                                         taskBoard,
                                                         team,
                                                         workspace,
                                                         displayFormat,
                                                         noDisplayFormatChange
                                                       }) => {
  const { t } = useTranslation();
  const setQueryState = useSetQueryState();
  const dispatch = useAppDispatch();

  const title = taskBoard?.title;
  const taskBoardId = taskBoard?.taskBoardId;
  const boardState = taskBoard?.state;
  const dueDate = taskBoard?.dueDate;
  const boardLink = `/${workspace?.username || ""}/tasks/${team?.username || ""}/task-boards/${taskBoardId}`;

  const isTeamAdmin = useCurrentAccountsTeamRoleIsAdmin({ workspaceId: workspace.workspaceId, teamId: team.teamId });
  const [initializeTaskBoardEntry, {
    isLoading: isInitializeLoading,
    isSuccess: isInitializeSuccess
  }] = useInitializeTaskBoardEntryMutation();
  const [updateState, { isLoading: isUpdateStateLoading }] = useUpdateStateMutation();
  const [updateDueDate, { isLoading: isUpdateDueDateLoading }] = useUpdateDueDateMutation();
  const [updateTitle, { isLoading: isUpdateTitleLoading }] = useUpdateTitleMutation();

  useEffect(() => {
    dispatch(
      changeLoadingModalVisibility({
        visible: isInitializeLoading || isUpdateStateLoading || isUpdateDueDateLoading || isUpdateTitleLoading
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
        teamIds: [team.teamId],
        onSelect: onExistingTaskSelect,
        visible: true
      })
    );
  };

  const openNewTaskModal = () => {
    dispatch(popNewTaskModal({
      visible: true,
      workspace: workspace,
      team: team,
      initialBoard: taskBoard
    }));
  };

  const onExistingTaskSelect = (selectedTask: TaskDto) => {
    dispatch(changeLoadingModalVisibility({ visible: true }));
    initializeTaskBoardEntry({
      taskBoardId,
      taskId: selectedTask.taskId
    });
  };

  const toggleBoardState = () => {
    const state = boardState == "OPEN" ? "CLOSED" : "OPEN";
    updateState({ taskBoardId, state });
  };

  const changeDueDate = (dueDate: Date | null) => {
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
        onDateChange: changeDueDate
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
        onSubmit: changeTitle
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

  const changeDisplayFormatToList = () => {
    setQueryState("displayFormat", "list");
  };

  const changeDisplayFormatToWfsColumn = () => {
    setQueryState("displayFormat", "column");
  };
  return (
    <div className={cn(styles.listTitle, boardState == "CLOSED" && styles.closedListTitle)}>
      <div className={styles.titleLabelContainer}>
        <Button
          className={styles.listTitleButton}
          key={`board-title-${taskBoardId}`}
          href={boardLink}
          heightVariant={ButtonHeight.short}
        >
          <h1>
            <b>{title}</b>
          </h1>
        </Button>
        {boardState == "OPEN" && (
          <Button heightVariant={ButtonHeight.short} className={styles.editTitleButton} onClick={popTitleChangeModal}>
            <IoPencil />
          </Button>
        )}
      </div>

      <div className="flex-1" />

      <div className={styles.titleActionBar}>

        {!noDisplayFormatChange &&
          <div className={styles.actionBarRow}>
            <div className={styles.viewTypeButtonContainer}>
              <Button
                onClick={changeDisplayFormatToList}
                variant={displayFormat == "list" ? ButtonVariants.filled2 : ButtonVariants.filled}
                className={styles.button}
                data-tooltip-right={t("taskListTitleAndViewTypeListTooltip")}
                heightVariant={ButtonHeight.short}
              >
                <IoList />
                {t("taskListTitleAndViewTypeListLabel")}
              </Button>
              <Button
                onClick={changeDisplayFormatToWfsColumn}
                variant={displayFormat == "column" ? ButtonVariants.filled2 : ButtonVariants.filled}
                className={styles.button}
                data-tooltip-right={t("taskListTitleAndViewTypeStatusColumnsTooltip")}
                heightVariant={ButtonHeight.short}
              >
                <IoStatsChart className={styles.wfsColumnIcon} />
                {t("taskListTitleAndViewTypeStatusColumnsLabel")}
              </Button>
            </div>
          </div>
        }

        <div className={styles.actionBarRow}>
          <Button
            disabled={boardState != "OPEN"}
            variant={ButtonVariants.filled}
            heightVariant={ButtonHeight.short}
            onClick={popDatePickerForDueDateUpdate}
            data-tooltip-right={dueDate ? format(new Date(dueDate), t("dateFormat")) : undefined}
          >
            {dateDiff ? dateDiff : t("taskBoardAssignDueDate")}
          </Button>
          <Button variant={ButtonVariants.filled} onClick={toggleBoardState} disabled={!isTeamAdmin}>
            {boardState == "OPEN" ? <IoLockOpenOutline className={"icon"} /> :
              <IoLockClosedOutline className={"icon"} />}
          </Button>
          <Button
            disabled={boardState != "OPEN"}
            variant={ButtonVariants.filled}
            heightVariant={ButtonHeight.short}
            onClick={openSearchTaskModal}
          >
            {/*{t("taskBoardAddTaskButtonLabel")}*/}
            <LuSearch className={"icon"} />
          </Button>
          <Button
            disabled={boardState != "OPEN"}
            variant={ButtonVariants.filled}
            heightVariant={ButtonHeight.short}
            onClick={openNewTaskModal}
          >
            {/*{t("taskBoardAddTaskButtonLabel")}*/}
            <LuPenSquare className={"icon"} />
          </Button>
          <Button variant={ButtonVariants.filled} href={boardLink}>
            <IoArrowForward className={"icon"} />
          </Button>
        </div>


      </div>
    </div>
  );
};

export default TaskBoardTitle;
