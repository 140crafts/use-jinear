import PaginatedList from "@/components/paginatedList/PaginatedList";
import { TaskBoardEntryDto, TaskBoardStateType, TaskSearchResultDto, TeamDto, WorkspaceDto } from "@/model/be/jinear-core";
import { useInitializeTaskBoardEntryMutation, useRetrieveFromTaskBoardQuery } from "@/store/api/taskBoardEntryApi";

import Button, { ButtonHeight, ButtonVariants } from "@/components/button";
import { useUpdateDueDateMutation, useUpdateStateMutation, useUpdateTitleMutation } from "@/store/api/taskBoardApi";
import {
  changeLoadingModalVisibility,
  closeBasicTextInputModal,
  closeDatePickerModal,
  closeSearchTaskModal,
  popBasicTextInputModal,
  popDatePickerModal,
  popSearchTaskModal,
} from "@/store/slice/modalSlice";
import { useAppDispatch } from "@/store/store";
import cn from "classnames";
import { differenceInDays, format, isToday, startOfToday } from "date-fns";
import useTranslation from "locales/useTranslation";
import React, { useEffect, useMemo, useState } from "react";
import { ImLock, ImUnlocked } from "react-icons/im";
import { IoEllipsisVerticalOutline, IoPencil } from "react-icons/io5";
import TaskRow from "../../taskRow/TaskRow";
import styles from "./TaskBoardElementList.module.scss";

interface TaskBoardElementListProps {
  title: string;
  taskBoardId: string;
  boardState: TaskBoardStateType;
  team: TeamDto;
  workspace: WorkspaceDto;
  dueDate: Date;
}

const TaskBoardElementList: React.FC<TaskBoardElementListProps> = ({
  title,
  taskBoardId,
  boardState,
  team,
  workspace,
  dueDate,
}) => {
  const { t } = useTranslation();
  const dispatch = useAppDispatch();
  const [page, setPage] = useState<number>(0);

  const { data: taskBoardElementsResponse, isFetching, isLoading } = useRetrieveFromTaskBoardQuery({ taskBoardId });
  const [initializeTaskBoardEntry, { isLoading: isInitializeLoading, isSuccess: isInitializeSuccess }] =
    useInitializeTaskBoardEntryMutation();
  const [updateState, { isLoading: isUpdateStateLoading, isSuccess: isUpdateStateSuccess }] = useUpdateStateMutation();
  const [updateDueDate, { isLoading: isUpdateDueDateLoading, isSuccess: isUpdateDueDateSuccess }] = useUpdateDueDateMutation();
  const [updateTitle, { isLoading: isUpdateTitleLoading, isSuccess: isUpdateTitleSuccess }] = useUpdateTitleMutation();

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

  const renderItem = (item: TaskBoardEntryDto) => {
    return (
      <div className={styles.itemContainer}>
        <Button heightVariant={ButtonHeight.short}>
          <IoEllipsisVerticalOutline />
        </Button>
        <TaskRow className={styles.taskRow} task={item.task} withBottomBorderLine={false} />
      </div>
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

  const listTitle = (
    <div className={cn(styles.listTitle, boardState == "CLOSED" && "op-05")}>
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

      <Button variant={ButtonVariants.filled} onClick={toggleBoardState}>
        {boardState == "OPEN" ? <ImUnlocked /> : <ImLock />}
      </Button>
      <div className="spacer-w-1" />
      {boardState == "OPEN" && (
        <div className={styles.titleActionBar}>
          <Button
            variant={ButtonVariants.filled}
            heightVariant={ButtonHeight.short}
            onClick={popDatePickerForDueDateUpdate}
            data-tooltip-right={dueDate ? format(new Date(dueDate), t("dateFormat")) : undefined}
          >
            {dateDiff ? dateDiff : t("taskBoardAssignDueDate")}
          </Button>
          <Button variant={ButtonVariants.filled2} heightVariant={ButtonHeight.short} onClick={openSearchTaskModal}>
            {t("taskBoardAddTaskButtonLabel")}
          </Button>
        </div>
      )}
    </div>
  );

  return (
    <div className={styles.container}>
      <PaginatedList
        id={`task-board-element-paginated-${taskBoardId}`}
        data={taskBoardElementsResponse?.data}
        isFetching={isFetching}
        isLoading={isLoading}
        page={page}
        setPage={setPage}
        renderItem={renderItem}
        emptyLabel={t("taskBoardEmptyLabel")}
        listTitle={title}
        hidePaginationOnSinglePages={true}
        listTitleComponent={listTitle}
        contentContainerClassName={styles.taskRowContainer}
      />
    </div>
  );
};

export default TaskBoardElementList;
