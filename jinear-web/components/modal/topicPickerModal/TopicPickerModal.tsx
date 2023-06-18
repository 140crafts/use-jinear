import Button, { ButtonHeight, ButtonVariants } from "@/components/button";
import { useDebouncedEffect } from "@/hooks/useDebouncedEffect";
import { TopicDto } from "@/model/be/jinear-core";
import { useSearchTeamTopicsQuery } from "@/store/api/topicListingApi";
import {
  closeTopicPickerModal,
  popNewTopicModal,
  selectTopicPickerModalInitialSelectionOnMultiple,
  selectTopicPickerModalMultiple,
  selectTopicPickerModalOnPick,
  selectTopicPickerModalTeamId,
  selectTopicPickerModalVisible,
} from "@/store/slice/modalSlice";
import { useAppDispatch, useTypedSelector } from "@/store/store";
import { CircularProgress } from "@mui/material";
import useTranslation from "locales/useTranslation";
import React, { ChangeEvent, useEffect, useRef, useState } from "react";
import { IoClose } from "react-icons/io5";
import Modal from "../modal/Modal";
import styles from "./TopicPickerModal.module.css";

interface TopicPickerModalProps {}

const TopicPickerModal: React.FC<TopicPickerModalProps> = ({}) => {
  const { t } = useTranslation();
  const dispatch = useAppDispatch();
  const visible = useTypedSelector(selectTopicPickerModalVisible);
  const inputRef = useRef<HTMLInputElement>(null);
  const [input, setInput] = useState<string>("");
  const [searchValue, setSearchValue] = useState<string>("");
  const [selectedTopics, setSelectedTopics] = useState<TopicDto[]>([]);
  const teamId = useTypedSelector(selectTopicPickerModalTeamId);
  const multiple = useTypedSelector(selectTopicPickerModalMultiple);
  const initialSelectionOnMultiple = useTypedSelector(selectTopicPickerModalInitialSelectionOnMultiple);
  const onPick = useTypedSelector(selectTopicPickerModalOnPick);

  const { data: searchResponse, isFetching } = useSearchTeamTopicsQuery(
    { teamId: teamId || "", nameOrTag: searchValue },
    { skip: teamId == null || teamId == "" }
  );

  useEffect(() => {
    if (initialSelectionOnMultiple != null && initialSelectionOnMultiple?.length != 0) {
      setSelectedTopics(initialSelectionOnMultiple);
    }
  }, [initialSelectionOnMultiple]);

  useDebouncedEffect(() => setSearchValue(input), [input], 500);

  const popCreateNewTopicModal = () => {
    dispatch(popNewTopicModal());
  };

  const close = () => {
    setInput("");
    setSearchValue("");
    setSelectedTopics([]);
    dispatch(closeTopicPickerModal());
  };

  const onTextChange = (e: ChangeEvent<HTMLInputElement>) => {
    setInput(e.target.value);
  };

  const addTaskToSelected = (topicDto: TopicDto) => {
    if (selectedTopics.filter((t) => t.topicId == topicDto.topicId).length == 0) {
      setSelectedTopics([...selectedTopics, topicDto]);
    }
  };

  const removeFromSelected = (selectedTopic: TopicDto) => {
    const filtered = selectedTopics.filter((topic) => topic.topicId != selectedTopic.topicId);
    setSelectedTopics(filtered);
  };

  const pickAndClose = (topicDto: TopicDto) => {
    onPick?.([topicDto]);
    close?.();
  };

  const submitPickedAndClose = () => {
    onPick?.(selectedTopics);
    close?.();
  };

  return (
    <Modal
      visible={visible}
      title={t("topicPickerModalTitle")}
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
          placeholder={t("topicPickerModalFilterPlaceholder")}
          value={input}
          onChange={onTextChange}
        />
      </div>

      <div className={styles.list}>
        {!isFetching && (
          <>
            <Button variant={ButtonVariants.outline} heightVariant={ButtonHeight.short} onClick={popCreateNewTopicModal}>
              {t("newTaskModalNewTopicButton")}
            </Button>

            {searchResponse?.data.map((topicDto) => (
              <Button
                key={`topic-search-result-list-${topicDto.topicId}`}
                variant={ButtonVariants.default}
                className={styles.listItemButton}
                onClick={() => {
                  multiple ? addTaskToSelected(topicDto) : pickAndClose(topicDto);
                }}
              >
                {topicDto.name}
              </Button>
            ))}
          </>
        )}
        <div className={styles.messageContainer}>
          {searchValue?.length != 0 && !isFetching && searchResponse?.data?.length == 0 && (
            <div>{t("topicPickerModalEmptyState")}</div>
          )}
          {isFetching && <CircularProgress size={17} />}
        </div>
      </div>
      {multiple && selectedTopics.length != 0 && (
        <div className={styles.selectedTopicListContainer}>
          {selectedTopics.map((topicDto) => (
            <div key={`selected-topic-${topicDto.topicId}`} className={styles.selectedTopicContainer}>
              <div className={styles.selectedTopicName}>{topicDto.name}</div>
              <Button
                variant={ButtonVariants.filled2}
                className={styles.selectedTopicUnselectButton}
                onClick={() => removeFromSelected(topicDto)}
              >
                <IoClose />
              </Button>
            </div>
          ))}
        </div>
      )}
      {multiple && (
        <div className={styles.actionBar}>
          <Button heightVariant={ButtonHeight.short} onClick={close}>
            {t("topicPickerModalCancelButton")}
          </Button>
          <Button
            heightVariant={ButtonHeight.short}
            variant={ButtonVariants.contrast}
            className={styles.contButton}
            onClick={submitPickedAndClose}
            disabled={selectedTopics?.length == 0 && !multiple}
          >
            {t("topicPickerModalSelectButton")}
          </Button>
        </div>
      )}
    </Modal>
  );
};

export default TopicPickerModal;
