import PaginatedList from "@/components/paginatedList/PaginatedList";
import { TaskBoardEntryDto, TaskBoardStateType, TeamDto, WorkspaceDto } from "@/model/be/jinear-core";
import { useChangeBoardEntryOrderMutation, useRetrieveFromTaskBoardQuery } from "@/store/api/taskBoardEntryApi";

import { changeLoadingModalVisibility } from "@/store/slice/modalSlice";
import { useAppDispatch } from "@/store/store";
import Logger from "@/utils/logger";
import useTranslation from "locales/useTranslation";
import React, { useEffect, useState } from "react";
import {
  DragDropContext,
  Draggable,
  DraggableProvided,
  DraggableStateSnapshot,
  DropResult,
  Droppable,
  DroppableProvided,
  DroppableStateSnapshot,
} from "react-beautiful-dnd";
import styles from "./TaskBoardElementList.module.scss";
import TaskBoardElement from "./taskBoardElement/TaskBoardElement";
import TaskBoardTitle from "./taskBoardTitle/TaskBoardTitle";

interface TaskBoardElementListProps {
  title: string;
  taskBoardId: string;
  boardState: TaskBoardStateType;
  team: TeamDto;
  workspace: WorkspaceDto;
  dueDate: Date;
}

const logger = Logger("TaskBoardElementList");

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
  const [changeBoardEntryOrder, { isLoading: isChangeBoardEntryOrderLoading }] = useChangeBoardEntryOrderMutation();

  useEffect(() => {
    dispatch(changeLoadingModalVisibility({ visible: isChangeBoardEntryOrderLoading }));
  }, [isChangeBoardEntryOrderLoading]);

  const renderItem = (item: TaskBoardEntryDto, index: number) => {
    return (
      <Draggable key={item.taskBoardEntryId} draggableId={item.taskBoardEntryId} index={index}>
        {(providedDraggable: DraggableProvided, snapshotDraggable: DraggableStateSnapshot) => (
          <div
            className={styles.draggableBoardElementContainer}
            ref={providedDraggable.innerRef}
            {...providedDraggable.draggableProps}
            {...providedDraggable.dragHandleProps}
          >
            <TaskBoardElement item={item} />
          </div>
        )}
      </Draggable>
    );
  };

  const onDragEnd = (result: DropResult) => {
    const { source, destination, draggableId } = result;
    if (!destination) {
      return;
    }
    const newOrder = destination?.index;
    const taskBoardEntryId = draggableId;
    logger.log({ newOrder, taskBoardEntryId, result });
    changeItemOrder({ newOrder, taskBoardEntryId });
  };

  const changeItemOrder = (vo: { newOrder: number; taskBoardEntryId: string }) => {
    changeBoardEntryOrder({ taskBoardId, ...vo });
  };

  return (
    <div className={styles.container}>
      <DragDropContext onDragEnd={onDragEnd}>
        <Droppable key={`task-board-droppable-${taskBoardId}`} droppableId={`task-board-droppable-${taskBoardId}`}>
          {(provided: DroppableProvided, snapshot: DroppableStateSnapshot): JSX.Element => (
            <div ref={provided.innerRef} {...provided.droppableProps}>
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
                listTitleComponent={
                  <TaskBoardTitle
                    title={title}
                    taskBoardId={taskBoardId}
                    boardState={boardState}
                    team={team}
                    workspace={workspace}
                    dueDate={dueDate}
                  />
                }
                contentContainerClassName={styles.taskRowContainer}
              />
              {provided.placeholder}
            </div>
          )}
        </Droppable>
      </DragDropContext>
    </div>
  );
};

export default TaskBoardElementList;
