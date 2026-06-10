import Button, {ButtonHeight, ButtonVariants} from "@/components/button";
import CircularLoading from "@/components/circularLoading/CircularLoading";
import {useDebouncedEffect} from "@/hooks/useDebouncedEffect";
import useWindowSize from "@/hooks/useWindowSize";
import {TaskBoardDto} from "@/model/be/jinear-core";
import {useFilterTaskBoardsQuery, useFilterWorkspaceTaskBoardsQuery} from "@/store/api/taskBoardListingApi";
import {
    closeBoardPickerModal,
    selectTaskBoardPickerModalInitialSelectionOnMultiple,
    selectTaskBoardPickerModalMultiple,
    selectTaskBoardPickerModalOnPick,
    selectTaskBoardPickerModalTeamId,
    selectTaskBoardPickerModalVisible,
    selectTaskBoardPickerModalWorkspaceId,
} from "@/store/slice/modalSlice";
import {useAppDispatch, useTypedSelector} from "@/store/store";
import useTranslation from "locales/useTranslation";
import React, {ChangeEvent, useEffect, useRef, useState} from "react";
import {IoClose} from "react-icons/io5";
import Modal from "../modal/Modal";
import styles from "./BoardPickerModal.module.css";

interface BoardPickerModalProps {
}

const BoardPickerModal: React.FC<BoardPickerModalProps> = ({}) => {
    const {t} = useTranslation();
    const dispatch = useAppDispatch();
    const {isMobile} = useWindowSize();
    const visible = useTypedSelector(selectTaskBoardPickerModalVisible);
    const workspaceId = useTypedSelector(selectTaskBoardPickerModalWorkspaceId);
    const teamId = useTypedSelector(selectTaskBoardPickerModalTeamId);
    const multiple = useTypedSelector(selectTaskBoardPickerModalMultiple);
    const initialSelectionOnMultiple = useTypedSelector(selectTaskBoardPickerModalInitialSelectionOnMultiple);
    const onPick = useTypedSelector(selectTaskBoardPickerModalOnPick);

    const inputRef = useRef<HTMLInputElement>(null);
    const [input, setInput] = useState<string>("");
    const [searchValue, setSearchValue] = useState<string>("");
    const [selectedBoards, setSelectedBoards] = useState<TaskBoardDto[]>([]);

    const searchMethod = teamId ? useFilterTaskBoardsQuery : useFilterWorkspaceTaskBoardsQuery;

    const {data: taskBoardListingResponse, isFetching} = searchMethod(
        {workspaceId: workspaceId ?? "", teamId: teamId ?? "", name: searchValue},
        {skip: workspaceId == null}
    );

    useEffect(() => {
        if (inputRef.current) {
            inputRef.current.focus();
        }
        setInput("");
        setSearchValue("");
    }, [visible]);

    useDebouncedEffect(() => setSearchValue(input), [input], 500);

    useEffect(() => {
        if (initialSelectionOnMultiple != null && initialSelectionOnMultiple?.length != 0) {
            setSelectedBoards(initialSelectionOnMultiple);
        }
    }, [initialSelectionOnMultiple]);

    const onTextChange = (e: ChangeEvent<HTMLInputElement>) => {
        setInput(e.target.value);
    };

    const close = () => {
        setSearchValue("");
        setInput("");
        setSelectedBoards([]);
        dispatch(closeBoardPickerModal());
    };

    const addToSelected = (taskBoardDto: TaskBoardDto) => {
        if (selectedBoards.filter((t) => t.taskBoardId == taskBoardDto.taskBoardId).length == 0) {
            setSelectedBoards([...selectedBoards, taskBoardDto]);
        }
    };

    const removeFromSelected = (taskBoardDto: TaskBoardDto) => {
        const filtered = selectedBoards.filter((t) => t.taskBoardId != taskBoardDto.taskBoardId);
        setSelectedBoards(filtered);
    };

    const pickAndClose = (taskBoardDto: TaskBoardDto) => {
        onPick?.([taskBoardDto]);
        close?.();
    };

    const submitPickedAndClose = () => {
        onPick?.(selectedBoards);
        close?.();
    };

    return (
        <Modal
            visible={visible}
            title={t("boardPickerModalTitle")}
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
                    placeholder={t("boardPickerModalFilterPlaceholder")}
                    value={input}
                    onChange={onTextChange}
                />
            </div>
            {!isFetching && (
                <div className={styles.boardList}>
                    {taskBoardListingResponse?.data.content.map((board) => (
                        <Button
                            key={`board-picker-button-${board.taskBoardId}`}
                            className={styles.listItemButton}
                            onClick={() => {
                                multiple ? addToSelected(board) : pickAndClose(board);
                            }}
                        >
                            {board.title}
                        </Button>
                    ))}
                </div>
            )}
            <div className={styles.messageContainer}>
                {!taskBoardListingResponse?.data?.hasContent && searchValue?.length != 0 && !isFetching && (
                    <div>{t("boardPickerModalEmptyState")}</div>
                )}
                {isFetching && <CircularLoading/>}
            </div>

            {multiple && selectedBoards.length != 0 && (
                <div className={styles.selectedListContainer}>
                    {selectedBoards.map((taskBoard) => (
                        <div key={`selected-task-board-${taskBoard.taskBoardId}`} className={styles.selectedContainer}>
                            <div className={styles.selectedName}>{taskBoard.title}</div>
                            <Button
                                variant={ButtonVariants.filled2}
                                className={styles.selectedUnselectButton}
                                onClick={() => removeFromSelected(taskBoard)}
                            >
                                <IoClose/>
                            </Button>
                        </div>
                    ))}
                </div>
            )}
            {multiple && (
                <div className={styles.actionBar}>
                    <Button heightVariant={ButtonHeight.short} onClick={close}>
                        {t("teamMemberPickerModalCancelButton")}
                    </Button>
                    <Button
                        heightVariant={ButtonHeight.short}
                        variant={ButtonVariants.contrast}
                        className={styles.contButton}
                        onClick={submitPickedAndClose}
                        disabled={selectedBoards?.length == 0 && !multiple}
                    >
                        {t("teamMemberPickerModalSelectButton")}
                    </Button>
                </div>
            )}
        </Modal>
    );
};

export default BoardPickerModal;
