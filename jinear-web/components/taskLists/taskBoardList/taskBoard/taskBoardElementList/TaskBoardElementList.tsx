import PaginatedList from "@/components/paginatedList/PaginatedList";
import { TaskBoardEntryDto, TaskBoardStateType } from "@/be/jinear-core";
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
import { toast } from "react-hot-toast";

interface TaskBoardElementListProps {
  taskBoardId: string;
  className?: string;
  boardState: TaskBoardStateType;
  page: number;
  setPage: (nextPage?: number) => void;
}

const logger = Logger("TaskBoardElementList");

const TaskBoardElementList: React.FC<TaskBoardElementListProps> = ({
                                                                     taskBoardId,
                                                                     boardState,
                                                                     className,
                                                                     page = 0,
                                                                     setPage
                                                                   }) => {
  const { t } = useTranslation();
  const dispatch = useAppDispatch();

  const taskBoardFilterMap = useQueryState<ITaskBoardUrlStateMap>("board-filter-map", queryStateJsonObjectParser) ?? {};
  const thisBoardsFilter = taskBoardFilterMap[taskBoardId] ?? {};

  const { data: taskBoardElementsResponse, isFetching, isLoading } = useFilterFromTaskBoardQuery({
    taskBoardId,
    page,
    body: thisBoardsFilter
  });
  const [changeFilteredBoardEntryOrder, { isLoading: isChangeBoardFilteredEntryOrderLoading }] = useChangeFilteredBoardEntryOrderMutation();

  const [draggedIndex, setDraggedIndex] = useState<number | null>(null);
  const [dragOverIndex, setDragOverIndex] = useState<number | null>(null);

  useEffect(() => {
    dispatch(changeLoadingModalVisibility({ visible: isChangeBoardFilteredEntryOrderLoading }));
  }, [isChangeBoardFilteredEntryOrderLoading]);

  const handleDragStart = (e: React.DragEvent<HTMLDivElement>, index: number, itemId: string) => {
    setDraggedIndex(index);
    e.dataTransfer.effectAllowed = "move";
    e.dataTransfer.setData("text/plain", itemId);
  };

  const handleDragOver = (e: React.DragEvent<HTMLDivElement>, index: number) => {
    e.preventDefault();
    e.dataTransfer.dropEffect = "move";
    setDragOverIndex(index);
  };

  const handleDragLeave = () => {
    setDragOverIndex(null);
  };

  const handleDragEnd = () => {
    setDraggedIndex(null);
    setDragOverIndex(null);
  };

  const handleDrop = (e: React.DragEvent<HTMLDivElement>, dropIndex: number) => {
    e.preventDefault();
    const taskBoardEntryId = e.dataTransfer.getData("text/plain");

    if (draggedIndex === null || draggedIndex === dropIndex) {
      handleDragEnd();
      return;
    }

    if (boardState === "CLOSED") {
      toast(t("taskBoardIsClosed"));
      handleDragEnd();
      return;
    }

    if (taskBoardElementsResponse?.data?.content) {
      const orderedArray = [...taskBoardElementsResponse.data.content];
      const [removedElement] = orderedArray.splice(draggedIndex, 1);
      orderedArray.splice(dropIndex, 0, removedElement);

      const taskBoardEntryIdBefore = dropIndex > 0 ? orderedArray[dropIndex - 1].taskBoardEntryId : undefined;
      const taskBoardEntryIdAfter = dropIndex + 1 < orderedArray.length ? orderedArray[dropIndex + 1].taskBoardEntryId : undefined;

      logger.log({ orderedArray, taskBoardEntryIdBefore, taskBoardEntryIdAfter, dropIndex, taskBoardEntryId });
      changeFilteredBoardEntryOrder({ taskBoardEntryId, taskBoardEntryIdBefore, taskBoardEntryIdAfter });
    }

    handleDragEnd();
  };

  const renderItem = (item: TaskBoardEntryDto, index: number) => {
    logger.log({ renderItem: item, index });
    const isDragging = draggedIndex === index;
    const isDragOver = dragOverIndex === index;

    return (
      <div
        key={`${item.taskBoardEntryId}-${index}`}
        className={cn(styles.draggableBoardElementContainer, {
          [styles.dragging]: isDragging,
          [styles.dragOver]: isDragOver
        })}
        draggable
        onDragStart={(e) => handleDragStart(e, index, item.taskBoardEntryId)}
        onDragOver={(e) => handleDragOver(e, index)}
        onDragLeave={handleDragLeave}
        onDragEnd={handleDragEnd}
        onDrop={(e) => handleDrop(e, index)}
      >
            <TaskBoardElement item={item} />
      </div>
    );
  };

  return (
    <div className={cn(styles.container, className)}>
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
    </div>
  );
};

export default TaskBoardElementList;
