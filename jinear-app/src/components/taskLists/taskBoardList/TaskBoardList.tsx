import PaginatedList from "@/components/paginatedList/PaginatedList";
import type {TaskBoardDto, TeamDto, WorkspaceDto} from "@/model/be/jinear-core";
import {useRetrieveAllTaskBoardsQuery} from "@/store/api/taskBoardListingApi";
import useTranslation from "@/locales/useTranslation";
import React, {useState} from "react";
import styles from "./TaskBoardList.module.css";
import TaskBoard from "@/components/taskLists/taskBoardList/taskBoard/TaskBoard";
import Button, {ButtonHeight, ButtonVariants} from "@/components/button";
import {useAppDispatch} from "@/store";
import {popNewTaskBoardModal} from "@/slice/modalSlice";
import GradientLine from "@/components/gradientLine/GradientLine.tsx";

interface TaskBoardListProps {
    team: TeamDto;
    workspace: WorkspaceDto;
}

const TaskBoardList: React.FC<TaskBoardListProps> = ({team, workspace}) => {
    const {t} = useTranslation();
    const [page, setPage] = useState<number>(0);
    const dispatch = useAppDispatch();

    const {
        data: taskBoardListingResponse,
        isFetching,
        isLoading
    } = useRetrieveAllTaskBoardsQuery({teamId: team.teamId, workspaceId: workspace.workspaceId, page});

    const renderItem = (item: TaskBoardDto, i: number) => {
        return (
            <div key={item.taskBoardId}>
                <TaskBoard taskBoard={item} team={team} workspace={workspace}/>
                <GradientLine/>
                <div className={'spacer-h-2'}/>
            </div>
        );
    };

    const popNewTaskBoard = () => {
        dispatch(popNewTaskBoardModal({visible: true, team, workspace}));
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
            <div className={styles.panelNavContainer}>
                <Button variant={ButtonVariants.filled} heightVariant={ButtonHeight.short} onClick={popNewTaskBoard}>
                    {t("taskBoardsListEmptyButton")}
                </Button>
            </div>
            <PaginatedList
                id={"task-board-list-paginated"}
                data={taskBoardListingResponse?.data}
                isFetching={isFetching}
                isLoading={isLoading}
                page={page}
                setPage={setPage}
                renderItem={renderItem}
                emptyComponent={emptyComponent}
                hidePaginationOnSinglePages={true}
                contentContainerClassName={styles.list}
                containerClassName={styles.listContainer}
            />
        </div>
    );
};

export default TaskBoardList;
