import PaginatedList from "@/components/paginatedList/PaginatedList";
import { TaskBoardEntryDto, TaskSearchResultDto, TeamDto, WorkspaceDto } from "@/model/be/jinear-core";
import { useInitializeTaskBoardEntryMutation, useRetrieveFromTaskBoardQuery } from "@/store/api/taskBoardEntryApi";

import Button, { ButtonHeight, ButtonVariants } from "@/components/button";
import { changeLoadingModalVisibility, closeSearchTaskModal, popSearchTaskModal } from "@/store/slice/modalSlice";
import { useAppDispatch } from "@/store/store";
import useTranslation from "locales/useTranslation";
import React, { useEffect, useState } from "react";
import { IoMenuOutline } from "react-icons/io5";
import TaskRow from "../../taskRow/TaskRow";
import styles from "./TaskBoardElementList.module.scss";

interface TaskBoardElementListProps {
  title: string;
  taskBoardId: string;
  team: TeamDto;
  workspace: WorkspaceDto;
}

const TaskBoardElementList: React.FC<TaskBoardElementListProps> = ({ title, taskBoardId, team, workspace }) => {
  const { t } = useTranslation();
  const dispatch = useAppDispatch();
  const [page, setPage] = useState<number>(0);

  const { data: taskBoardElementsResponse, isFetching, isLoading } = useRetrieveFromTaskBoardQuery({ taskBoardId });
  const [initializeTaskBoardEntry, { isLoading: isInitializeLoading, isSuccess: isInitializeSuccess }] =
    useInitializeTaskBoardEntryMutation();

  useEffect(() => {
    dispatch(changeLoadingModalVisibility({ visible: isInitializeLoading }));
    if (!isInitializeLoading) {
      dispatch(closeSearchTaskModal());
    }
  }, [isInitializeLoading]);

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

  const renderItem = (item: TaskBoardEntryDto) => {
    return (
      <div className={styles.itemContainer}>
        <div className={styles.itemDragIconContainer}>
          <IoMenuOutline />
        </div>
        <TaskRow className={styles.taskRow} task={item.task} withBottomBorderLine={false} />
      </div>
    );
  };

  const listTitle = (
    <div className={styles.listTitle}>
      <Button
        className={styles.listTitleButton}
        key={`board-title-${taskBoardId}`}
        href={`/${workspace?.username || ""}/${team?.username || ""}/task-boards/${taskBoardId}`}
        heightVariant={ButtonHeight.short}
      >
        <h2>{title}</h2>
      </Button>
      <Button variant={ButtonVariants.filled} heightVariant={ButtonHeight.short} onClick={openSearchTaskModal}>
        {t("taskBoardAddTaskButtonLabel")}
      </Button>
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
