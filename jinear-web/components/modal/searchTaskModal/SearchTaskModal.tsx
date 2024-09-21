import Button from "@/components/button";
import { useDebouncedEffect } from "@/hooks/useDebouncedEffect";
import useWindowSize from "@/hooks/useWindowSize";
import { useSearchTaskQuery } from "@/store/api/taskSearchApi";
import {
  closeSearchTaskModal,
  selectSearchTaskModalOnSelect,
  selectSearchTaskModalTeamIds,
  selectSearchTaskModalVisible,
  selectSearchTaskModalWorkspaceId
} from "@/store/slice/modalSlice";
import { useAppDispatch, useTypedSelector } from "@/store/store";
import { CircularProgress } from "@mui/material";
import useTranslation from "locales/useTranslation";
import React, { ChangeEvent, useEffect, useRef, useState } from "react";
import { IoCheckmarkCircle, IoCloseCircle, IoContrast, IoEllipseOutline, IoPauseCircleOutline } from "react-icons/io5";
import Modal from "../modal/Modal";
import styles from "./SearchTaskModal.module.css";

interface SearchTaskModalProps {
}

const ICON_SIZE = 15;
const groupIconMap = {
  BACKLOG: <IoPauseCircleOutline size={ICON_SIZE} />,
  NOT_STARTED: <IoEllipseOutline size={ICON_SIZE} />,
  STARTED: <IoContrast size={ICON_SIZE} />,
  COMPLETED: <IoCheckmarkCircle size={ICON_SIZE} />,
  CANCELLED: <IoCloseCircle size={ICON_SIZE} />
};

const SearchTaskModal: React.FC<SearchTaskModalProps> = ({}) => {
  const { t } = useTranslation();
  const dispatch = useAppDispatch();
  const { isMobile } = useWindowSize();
  const visible = useTypedSelector(selectSearchTaskModalVisible);
  const workspaceId = useTypedSelector(selectSearchTaskModalWorkspaceId);
  const teamIds = useTypedSelector(selectSearchTaskModalTeamIds);
  const onSelect = useTypedSelector(selectSearchTaskModalOnSelect);

  const inputRef = useRef<HTMLInputElement>(null);
  const [input, setInput] = useState<string>("");
  const [searchValue, setSearchValue] = useState<string>("");

  const {
    data: searchResponse,
    isSuccess,
    isError,
    isFetching
  } = useSearchTaskQuery(
    {
      body: {
        workspaceId: workspaceId || "",
        teamIdList: teamIds ?? [],
        query: searchValue
      },
      page: 0
    },
    { skip: workspaceId == null || searchValue == "" }
  );

  useEffect(() => {
    if (inputRef.current) {
      inputRef.current.focus();
    }
    setInput("");
    setSearchValue("");
  }, [visible]);

  useDebouncedEffect(() => setSearchValue(input), [input], 500);

  const close = () => {
    dispatch(closeSearchTaskModal());
  };

  const onTextChange = (e: ChangeEvent<HTMLInputElement>) => {
    setInput(e.target.value);
  };

  return (
    <Modal
      visible={visible}
      title={t("searchTaskModalTitle")}
      bodyClass={styles.container}
      requestClose={close}
      width={isMobile ? "fullscreen" : "large"}
      hasTitleCloseButton={true}
    >
      <div className={styles.content}>
        <input
          ref={inputRef}
          type={"text"}
          className={styles.searchInput}
          placeholder={t("searchTaskModalPlaceholder")}
          value={input}
          onChange={onTextChange}
        />
      </div>
      <div className={styles.searchList}>
        {!isFetching && searchValue != "" && searchResponse?.data.content?.map((taskDto) => (
          <Button
            key={`search-result-${taskDto.taskId}`}
            className={styles.searchResultItem}
            onClick={() => {
              onSelect?.(taskDto);
            }}
          >
            <div className={styles.iconContainer}>{groupIconMap?.[taskDto.workflowStatus?.workflowStateGroup]}</div>
            <div className={styles.titleContainer}>
              {taskDto.team?.tag ? taskDto.team?.tag + "-" + taskDto.teamTagNo + " " : ""}
              {taskDto.title}
            </div>
          </Button>
        ))}
        <div className={styles.messageContainer}>
          {!searchResponse?.data.hasContent && searchValue?.length != 0 && !isFetching && (
            <div>{t("searchTaskModalEmptyState")}</div>
          )}
          {searchValue?.length == 0 && !isFetching && <div>{t("searchTaskModalInitialState")}</div>}

          {isFetching && <CircularProgress size={17} />}
        </div>
      </div>
    </Modal>
  );
};

export default SearchTaskModal;
