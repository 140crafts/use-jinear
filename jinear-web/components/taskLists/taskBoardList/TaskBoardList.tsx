import PaginatedList from "@/components/paginatedList/PaginatedList";
import { TaskBoardDto, TeamDto, WorkspaceDto } from "@/model/be/jinear-core";
import { useRetrieveAllTaskBoardsQuery } from "@/store/api/taskBoardListingApi";
import useTranslation from "locales/useTranslation";
import React, { useState } from "react";
import styles from "./TaskBoardList.module.css";
import TaskBoard from "@/components/taskLists/taskBoardList/taskBoard/TaskBoard";
import Button, { ButtonHeight, ButtonVariants } from "@/components/button";
import { useAppDispatch } from "@/store/store";
import { popNewTaskBoardModal } from "@/slice/modalSlice";

interface TaskBoardListProps {
  team: TeamDto;
  workspace: WorkspaceDto;
}

const TaskBoardList: React.FC<TaskBoardListProps> = ({ team, workspace }) => {
  const { t } = useTranslation();
  const [page, setPage] = useState<number>(0);
  const dispatch = useAppDispatch();

  const {
    data: taskBoardListingResponse,
    isFetching,
    isLoading
  } = useRetrieveAllTaskBoardsQuery({ teamId: team.teamId, workspaceId: workspace.workspaceId, page });

  const renderItem = (item: TaskBoardDto, i: number) => {
    return (
      <div key={item.taskBoardId}>
        <TaskBoard
          taskBoard={item}
          team={team}
          workspace={workspace}
          staticViewType={"list"}
        />
        {((taskBoardListingResponse?.data?.content?.length ?? 0) - 1) != i && <div className={styles.divider} />}
      </div>
    );
  };

  const popNewTaskBoard = () => {
    dispatch(popNewTaskBoardModal({ visible: true, team, workspace }));
  };

  const emptyComponent = (
    <div className={styles.emptyStateContainer}>
      <span>
        {t("taskBoardsListEmptyLabel")}
      </span>
      <Button variant={ButtonVariants.filled} heightVariant={ButtonHeight.short} onClick={popNewTaskBoard}>
        {t("taskBoardsListEmptyButton")}
      </Button>
    </div>
  );

  return (
    <div className={styles.container}>
      <PaginatedList
        id={"task-board-list-paginated"}
        data={taskBoardListingResponse?.data}
        isFetching={isFetching}
        isLoading={isLoading}
        page={page}
        setPage={setPage}
        renderItem={renderItem}
        emptyComponent={emptyComponent}
        // emptyLabel={t("taskBoardsListEmptyLabel")}
        listTitle={t("taskBoardsListTitle")}
        listTitleClassName={styles.listTitle}
        hidePaginationOnSinglePages={true}
        contentContainerClassName={styles.list}
      />
    </div>
  );
};

export default TaskBoardList;
