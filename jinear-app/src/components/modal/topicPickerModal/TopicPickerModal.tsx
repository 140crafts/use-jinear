import Button, {ButtonHeight, ButtonVariants} from "@/components/button";
import {useDebouncedEffect} from "@/hooks/useDebouncedEffect";
import type {TopicDto} from "@/model/be/jinear-core";
import {useSearchTeamTopicsQuery} from "@/store/api/topicListingApi";
import {
    closeTopicPickerModal,
    popNewTopicModal,
    selectTopicPickerModalCurrentTopicId,
    selectTopicPickerModalInitialSelectionOnMultiple,
    selectTopicPickerModalMultiple,
    selectTopicPickerModalOnPick,
    selectTopicPickerModalOnRemove,
    selectTopicPickerModalTeam,
    selectTopicPickerModalTitle,
    selectTopicPickerModalVisible,
    selectTopicPickerModalWorkspace,
} from "@/store/slice/modalSlice";
import {useAppDispatch, useTypedSelector} from "@/store";
import useTranslation from "@/locales/useTranslation";
import React, {type ChangeEvent, useEffect, useRef, useState} from "react";
import {LuCheck, LuCircleSlash, LuPlus} from "react-icons/lu";
import Modal from "../modal/Modal";
import styles from "./TopicPickerModal.module.css";
import CircularLoading from "@/components/circularLoading/CircularLoading.tsx";
import GradientLine from "@/components/gradientLine/GradientLine";

interface TopicPickerModalProps {
}

const TopicPickerModal: React.FC<TopicPickerModalProps> = ({}) => {
    const {t} = useTranslation();
    const dispatch = useAppDispatch();
    const visible = useTypedSelector(selectTopicPickerModalVisible);
    const inputRef = useRef<HTMLInputElement>(null);
    const [input, setInput] = useState<string>("");
    const [searchValue, setSearchValue] = useState<string>("");
    const [selectedTopics, setSelectedTopics] = useState<TopicDto[]>([]);
    const team = useTypedSelector(selectTopicPickerModalTeam);
    const workspace = useTypedSelector(selectTopicPickerModalWorkspace);
    const multiple = useTypedSelector(selectTopicPickerModalMultiple);
    const initialSelectionOnMultiple = useTypedSelector(selectTopicPickerModalInitialSelectionOnMultiple);
    const onPick = useTypedSelector(selectTopicPickerModalOnPick);
    const currentTopicId = useTypedSelector(selectTopicPickerModalCurrentTopicId);
    const onRemove = useTypedSelector(selectTopicPickerModalOnRemove);
    const title = useTypedSelector(selectTopicPickerModalTitle);

    const {currentData: searchResponse, isFetching} = useSearchTeamTopicsQuery(
        {teamId: team?.teamId || "", nameOrTag: searchValue},
        {skip: team == null}
    );
    const topics = searchResponse?.data ?? [];

    useEffect(() => {
        setTimeout(() => {
            if (visible && inputRef.current) {
                inputRef.current.focus?.();
            }
        }, 250);
    }, [visible]);

    useEffect(() => {
        if (initialSelectionOnMultiple != null && initialSelectionOnMultiple?.length != 0) {
            setSelectedTopics(initialSelectionOnMultiple);
        }
    }, [initialSelectionOnMultiple]);

    useDebouncedEffect(() => setSearchValue(input), [input], 500);

    const popCreateNewTopicModal = () => {
        dispatch(popNewTopicModal({visible: true, team, workspace, initialName: input.trim()}));
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

    const isSelected = (topicDto: TopicDto) =>
        multiple ?
            selectedTopics.some((topic) => topic.topicId == topicDto.topicId) :
            topicDto.topicId == currentTopicId;

    const toggleSelected = (topicDto: TopicDto) => {
        if (isSelected(topicDto)) {
            setSelectedTopics(selectedTopics.filter((topic) => topic.topicId != topicDto.topicId));
        } else {
            setSelectedTopics([...selectedTopics, topicDto]);
        }
    };

    const pickAndClose = (topicDto: TopicDto) => {
        onPick?.([topicDto]);
        close();
    };

    const removeAndClose = () => {
        if (currentTopicId) {
            onRemove?.();
        }
        close();
    };

    const submitPickedAndClose = () => {
        onPick?.(selectedTopics);
        close();
    };

    return (
        <Modal
            visible={visible}
            title={title ?? t("topicPickerModalTitle")}
            bodyClass={styles.container}
            hasTitleCloseButton={true}
            requestClose={close}
            width={'large'}
            height={"height-medium-or-full"}
        >
            <div className={styles.searchBar}>
                <input
                    ref={inputRef}
                    type={"text"}
                    className={styles.searchInput}
                    placeholder={t("topicPickerModalFilterPlaceholder")}
                    value={input}
                    onChange={onTextChange}
                />
                <Button className={styles.newTopicButton} onClick={popCreateNewTopicModal} shortcut="pickerNew">
                    <LuPlus/>
                    {t("newTaskModalNewTopicButton")}
                </Button>
            </div>
            <div className={styles.line}>
                <GradientLine/>
            </div>

            <div className={styles.list}>
                {!multiple && onRemove && (
                    <Button className={styles.row} onClick={removeAndClose}>
                        <LuCircleSlash className={styles.rowIcon}/>
                        <span className={styles.name}>{t("topicPickerModalNoTopic")}</span>
                        <div className={styles.state}>{!currentTopicId && <LuCheck/>}</div>
                    </Button>
                )}

                {topics.map((topicDto) => (
                    <Button
                        key={`topic-search-result-list-${topicDto.topicId}`}
                        className={styles.row}
                        onClick={() => multiple ? toggleSelected(topicDto) : pickAndClose(topicDto)}
                    >
                        <div className={styles.rowIcon}>
                            <span className={styles.colorDot} style={{backgroundColor: `#${topicDto.color}`}}/>
                        </div>
                        <span className={styles.name}>{topicDto.name}</span>
                        <div className={styles.state}>{isSelected(topicDto) && <LuCheck/>}</div>
                    </Button>
                ))}

                {isFetching && searchResponse == null && (
                    <div className={styles.messageContainer}>
                        <CircularLoading size={17}/>
                    </div>
                )}

                {searchResponse && topics.length == 0 && (
                    <div className={styles.messageContainer}>{t("topicPickerModalEmptyState")}</div>
                )}
            </div>

            {multiple && (
                <div className={styles.footer}>
                    <GradientLine/>
                    <div className={styles.actionBar}>
                        <span className={styles.selectedCount}>
                            {t("topicPickerModalSelectedCount").replace("${count}", `${selectedTopics.length}`)}
                        </span>
                        <Button heightVariant={ButtonHeight.short} onClick={close}>
                            {t("topicPickerModalCancelButton")}
                        </Button>
                        <Button
                            heightVariant={ButtonHeight.short}
                            variant={ButtonVariants.contrast}
                            className={styles.contButton}
                            onClick={submitPickedAndClose}
                            shortcut="pickerConfirm"
                        >
                            {t("topicPickerModalSelectButton")}
                        </Button>
                    </div>
                </div>
            )}
        </Modal>
    );
};

export default TopicPickerModal;
