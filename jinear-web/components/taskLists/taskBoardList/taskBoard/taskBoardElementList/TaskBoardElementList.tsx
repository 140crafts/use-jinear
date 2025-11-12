import PaginatedList from "@/components/paginatedList/PaginatedList";
import { TaskBoardEntryDto } from "@/be/jinear-core";
import {
  useChangeBoardEntryOrderMutation, useChangeFilteredBoardEntryOrderMutation,
  useFilterFromTaskBoardQuery,
  useRetrieveFromTaskBoardQuery
} from "@/api/taskBoardEntryApi";

import { changeLoadingModalVisibility } from "@/slice/modalSlice";
import { useAppDispatch } from "@/store/store";
import Logger from "@/utils/logger";
import useTranslation from "@/locals/useTranslation";
import React, { useEffect, useState } from "react";
import {
  DragDropContext,
  Draggable,
  DraggableProvided,
  DraggableStateSnapshot,
  Droppable,
  DroppableProvided,
  DroppableStateSnapshot,
  DropResult
} from "react-beautiful-dnd";
import styles from "./TaskBoardElementList.module.scss";
import TaskBoardElement from "./taskBoardElement/TaskBoardElement";
import cn from "classnames";
import { queryStateJsonObjectParser, useQueryState } from "@/hooks/useQueryState";
import {
  ITaskBoardUrlStateMap
} from "@/components/taskLists/taskBoardList/taskBoard/taskBoardQuickFilterBar/TaskBoardQuickFilterBar";

interface TaskBoardElementListProps {
  taskBoardId: string;
  className?: string;
}

const logger = Logger("TaskBoardElementList");

const TaskBoardElementList: React.FC<TaskBoardElementListProps> = ({
                                                                     taskBoardId,
                                                                     className
                                                                   }) => {
  const { t } = useTranslation();
  const dispatch = useAppDispatch();
  const [page, setPage] = useState<number>(0);

  const taskBoardFilterMap = useQueryState<ITaskBoardUrlStateMap>("board-filter-map", queryStateJsonObjectParser) ?? {};
  const thisBoardsFilter = taskBoardFilterMap[taskBoardId] ?? {};

  const { data: taskBoardElementsResponse, isFetching, isLoading } = useFilterFromTaskBoardQuery({
    taskBoardId,
    page,
    body: thisBoardsFilter
  });
  const [changeFilteredBoardEntryOrder, { isLoading: isChangeBoardFilteredEntryOrderLoading }] = useChangeFilteredBoardEntryOrderMutation();

  useEffect(() => {
    dispatch(changeLoadingModalVisibility({ visible: isChangeBoardFilteredEntryOrderLoading }));
  }, [isChangeBoardFilteredEntryOrderLoading]);

  const renderItem = (item: TaskBoardEntryDto, index: number) => {
    logger.log({ renderItem: item, index });
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
    const oldOrder = source?.index;
    const newOrder = destination?.index;
    const taskBoardEntryId = draggableId;
    if (taskBoardElementsResponse?.data.content) {
      const orderedArray = [...taskBoardElementsResponse?.data.content];
      const [removedElement] = orderedArray.splice(oldOrder, 1);
      orderedArray.splice(newOrder, 0, removedElement);
      const taskBoardEntryIdBefore = newOrder > 0 ? orderedArray[newOrder - 1].taskBoardEntryId : undefined;
      const taskBoardEntryIdAfter = newOrder + 1 < orderedArray.length ? orderedArray[newOrder + 1].taskBoardEntryId : undefined;
      logger.log({ orderedArray, taskBoardEntryIdBefore, taskBoardEntryIdAfter, newOrder, taskBoardEntryId, result });
      changeFilteredBoardEntryOrder({ taskBoardEntryId, taskBoardEntryIdBefore, taskBoardEntryIdAfter });
    }
  };

  return (
    <div className={cn(styles.container, className)}>
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
                hidePaginationOnSinglePages={true}
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
