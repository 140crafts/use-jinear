import Button, { ButtonVariants } from "@/components/button";
import { useDebouncedEffect } from "@/hooks/useDebouncedEffect";
import { useDeleteTaskBoardEntryMutation, useInitializeTaskBoardEntryMutation } from "@/store/api/taskBoardEntryApi";
import { useRetrieveTaskAndTaskBoardsRelationQuery } from "@/store/api/taskBoardListingApi";
import {
  changeLoadingModalVisibility,
  closeTaskTaskBoardAssignModal,
  selectTaskTaskBoardAssignModalTaskId,
  selectTaskTaskBoardAssignModalVisible,
} from "@/store/slice/modalSlice";
import { useAppDispatch, useTypedSelector } from "@/store/store";
import { CircularProgress } from "@mui/material";
import useTranslation from "locales/useTranslation";
import React, { ChangeEvent, useEffect, useRef, useState } from "react";
import { IoCheckmarkCircle, IoRadioButtonOffOutline } from "react-icons/io5";
import Modal from "../modal/Modal";
import styles from "./TaskTaskBoardModal.module.css";

interface TaskTaskBoardModalProps {}

const TaskTaskBoardModal: React.FC<TaskTaskBoardModalProps> = ({}) => {
  const { t } = useTranslation();
  const dispatch = useAppDispatch();
  const visible = useTypedSelector(selectTaskTaskBoardAssignModalVisible);
  const taskId = useTypedSelector(selectTaskTaskBoardAssignModalTaskId);
  const inputRef = useRef<HTMLInputElement>(null);
  const [input, setInput] = useState<string>("");
  const [searchValue, setSearchValue] = useState<string>("");

  const { data: taskAndTaskBoardRelationResponse, isFetching } = useRetrieveTaskAndTaskBoardsRelationQuery(
    { taskId: taskId || "", filterRecentsByName: searchValue },
    { skip: taskId == null }
  );

  const [initializeTaskBoardEntry, { isLoading: isInitializeLoading }] = useInitializeTaskBoardEntryMutation();
  const [deleteTaskBoardEntry, { isLoading: isDeleteLoading }] = useDeleteTaskBoardEntryMutation();

  const alreadyAddedBoards = taskAndTaskBoardRelationResponse?.data.alreadyAddedBoards || [];
  const recentBoards = taskAndTaskBoardRelationResponse?.data.recentBoards;

  useEffect(() => {
    dispatch(changeLoadingModalVisibility({ visible: isInitializeLoading || isDeleteLoading }));
  }, [isInitializeLoading, isDeleteLoading]);

  useEffect(() => {
    if (inputRef.current) {
      inputRef.current.focus();
    }
    setInput("");
    setSearchValue("");
  }, [visible]);

  useDebouncedEffect(() => setSearchValue(input), [input], 500);

  const close = () => {
    dispatch(closeTaskTaskBoardAssignModal());
  };

  const onTextChange = (e: ChangeEvent<HTMLInputElement>) => {
    setInput(e.target.value);
  };

  const addToBoard = (taskBoardId: string) => {
    if (taskId) {
      initializeTaskBoardEntry({ taskId, taskBoardId });
    }
  };

  const removeFromBoard = (vo: { taskBoardEntryId: string; taskBoardId?: string }) => {
    if (taskId) {
      deleteTaskBoardEntry(vo);
    }
  };

  return (
    <Modal
      visible={visible}
      title={t("taskTaskBoardModalTitle")}
      bodyClass={styles.container}
      hasTitleCloseButton={true}
      requestClose={close}
      height={"height-medium-or-full"}
    >
      <div className={styles.content}>
        <input
          ref={inputRef}
          type={"text"}
          className={styles.searchInput}
          placeholder={t("taskTaskBoardModalFilterPlaceholder")}
          value={input}
          onChange={onTextChange}
        />
      </div>
      {!isFetching && (
        <div className={styles.boardList}>
          {alreadyAddedBoards
            .filter(
              (taskBoardEntryDetailedDto) =>
                searchValue == "" ||
                taskBoardEntryDetailedDto.taskBoard?.title?.toLocaleLowerCase?.().indexOf?.(searchValue.toLocaleLowerCase()) != -1
            )
            .map((taskBoardEntryDetailedDto) => (
              <Button
                key={`task-task-board-modal-existing-boards-list-item-${taskBoardEntryDetailedDto.taskBoardEntryId}`}
                variant={ButtonVariants.filled}
                className={styles.listItemButton}
                onClick={() =>
                  removeFromBoard({
                    taskBoardEntryId: taskBoardEntryDetailedDto.taskBoardEntryId,
                    taskBoardId: taskBoardEntryDetailedDto.taskBoardId,
                  })
                }
              >
                <div className={styles.listItemIcon}>
                  <IoCheckmarkCircle size={18} />
                </div>
                {taskBoardEntryDetailedDto?.taskBoard?.title}
              </Button>
            ))}
          {recentBoards?.content.map((taskBoardDto) => (
            <Button
              key={`task-task-board-modal-recent-boards-list-board-id-${taskBoardDto.taskBoardId}`}
              variant={ButtonVariants.default}
              className={styles.listItemButton}
              onClick={() => addToBoard(taskBoardDto.taskBoardId)}
            >
              <div className={styles.listItemIcon}>
                <IoRadioButtonOffOutline size={18} />
              </div>
              {taskBoardDto.title}
            </Button>
          ))}
        </div>
      )}
      <div className={styles.messageContainer}>
        {alreadyAddedBoards.length == 0 &&
          recentBoards &&
          !recentBoards.hasContent &&
          searchValue?.length != 0 &&
          !isFetching && <div>{t("taskTaskBoardModalEmptyState")}</div>}
        {isFetching && <CircularProgress size={17} />}
      </div>
    </Modal>
  );
};

export default TaskTaskBoardModal;
