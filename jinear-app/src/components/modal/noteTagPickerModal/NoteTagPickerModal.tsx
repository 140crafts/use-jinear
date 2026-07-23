import Button, {ButtonHeight, ButtonVariants} from "@/components/button";
import CircularLoading from "@/components/circularLoading/CircularLoading.tsx";
import Modal from "@/components/modal/modal/Modal";
import {useDebouncedEffect} from "@/hooks/useDebouncedEffect";
import type {NoteTagDto} from "@/model/be/jinear-core";
import {useInitializeNoteTagMutation, useListNoteTagsQuery} from "@/store/api/noteTagApi";
import {
    closeNoteTagPickerModal,
    selectNoteTagPickerModalInitialSelection,
    selectNoteTagPickerModalNotebookId,
    selectNoteTagPickerModalOnPick,
    selectNoteTagPickerModalVisible
} from "@/store/slice/modalSlice";
import {useAppDispatch, useTypedSelector} from "@/store";

import useTranslation from "@/locales/useTranslation";
import React, {type ChangeEvent, useEffect, useRef, useState} from "react";
import {IoClose} from "react-icons/io5";
import {LuCheck, LuPlus} from "react-icons/lu";
import styles from "./NoteTagPickerModal.module.css";
import Logger from "@/util/logger.ts";
import {getHashedColor} from "@/util/colorHelper.ts";

interface NoteTagPickerModalProps {
}

const logger = Logger("NoteTagPickerModal");

const NoteTagPickerModal: React.FC<NoteTagPickerModalProps> = ({}) => {
    const {t} = useTranslation();
    const dispatch = useAppDispatch();
    const visible = useTypedSelector(selectNoteTagPickerModalVisible);
    const inputRef = useRef<HTMLInputElement>(null);
    const [input, setInput] = useState<string>("");
    const [searchValue, setSearchValue] = useState<string>("");
    const [selectedTags, setSelectedTags] = useState<NoteTagDto[]>([]);
    const notebookId = useTypedSelector(selectNoteTagPickerModalNotebookId);
    const initialSelection = useTypedSelector(selectNoteTagPickerModalInitialSelection);
    const onPick = useTypedSelector(selectNoteTagPickerModalOnPick);

    const [initializeNoteTag, {isLoading: isCreateLoading}] = useInitializeNoteTagMutation();

    const {currentData: noteTagListingResponse, isFetching} = useListNoteTagsQuery(
        {notebookId: notebookId || ""},
        {
            skip: !visible || notebookId == null
        }
    );

    const allTags = noteTagListingResponse?.data || [];
    const filteredList = allTags.filter(
        (noteTagDto) => searchValue == "" || noteTagDto.name.toLowerCase().indexOf(searchValue.toLowerCase()) != -1
    );
    const trimmedInput = input.trim();
    const hasExactMatch = allTags.some((noteTagDto) => noteTagDto.name.toLowerCase() == trimmedInput.toLowerCase());
    const createTagVisible = trimmedInput != "" && !hasExactMatch;

    useEffect(() => {
        setTimeout(() => {
            if (visible && inputRef.current) {
                inputRef.current.focus?.();
            }
        }, 250);
    }, [visible]);

    useEffect(() => {
        if (initialSelection != null && initialSelection?.length != 0) {
            setSelectedTags(initialSelection);
        }
    }, [initialSelection]);

    useDebouncedEffect(() => setSearchValue(input), [input], 500);

    const onTextChange = (e: ChangeEvent<HTMLInputElement>) => {
        setInput(e.target.value);
    };

    const close = () => {
        setSearchValue("");
        setInput("");
        setSelectedTags([]);
        dispatch(closeNoteTagPickerModal());
    };

    const isSelected = (noteTagDto: NoteTagDto) => selectedTags.some((tag) => tag.noteTagId == noteTagDto.noteTagId);

    const toggleSelected = (noteTagDto: NoteTagDto) => {
        isSelected(noteTagDto) ? removeFromSelected(noteTagDto) : setSelectedTags([...selectedTags, noteTagDto]);
    };

    const removeFromSelected = (selectedNoteTagDto: NoteTagDto) => {
        const filtered = selectedTags.filter((noteTagDto) => noteTagDto.noteTagId != selectedNoteTagDto.noteTagId);
        setSelectedTags(filtered);
    };

    const submitPickedAndClose = () => {
        onPick?.(selectedTags);
        close();
    };

    const createTagAndSelect = () => {
        logger.log({
            notebookId,
            trimmedInput,
            isCreateLoading
        })
        if (notebookId == null || trimmedInput == "" || isCreateLoading) {
            return;
        }
        initializeNoteTag({notebookId, name: trimmedInput, color: getHashedColor({text: trimmedInput})})
            .unwrap()
            .then((response) => {
                const createdTag = {noteTagId: response.data, notebookId, name: trimmedInput} as NoteTagDto;
                setSelectedTags((prev) => [...prev, createdTag]);
                setInput("");
                setSearchValue("");
                inputRef.current?.focus?.();
            });
    };

    return (
        <Modal
            visible={visible}
            title={t("noteTagPickerModalTitle")}
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
                    placeholder={t("noteTagPickerModalSearchOrCreatePlaceholder")}
                    value={input}
                    onChange={onTextChange}
                />
            </div>

            <div className={styles.list}>
                {filteredList.map((noteTagDto) => (
                    <Button
                        key={`note-tag-list-${noteTagDto.noteTagId}`}
                        variant={ButtonVariants.default}
                        className={styles.listItemButton}
                        onClick={() => toggleSelected(noteTagDto)}
                    >
                        <div className={styles.colorDot}
                             style={noteTagDto.color ? {backgroundColor: noteTagDto.color} : undefined}/>
                        <span className={styles.tagName}>{noteTagDto.name}</span>
                        {isSelected(noteTagDto) && <LuCheck className={styles.checkIcon}/>}
                    </Button>
                ))}

                {createTagVisible && (
                    <Button
                        variant={ButtonVariants.default}
                        className={styles.listItemButton}
                        disabled={isCreateLoading}
                        onClick={createTagAndSelect}
                    >
                        {isCreateLoading ? <CircularLoading size={14}/> : <LuPlus size={14}/>}
                        <span
                            className={styles.tagName}>{`${t("noteTagPickerModalCreateTag")} "${trimmedInput}"`}</span>
                    </Button>
                )}

                {!createTagVisible && (
                    <div className={styles.messageContainer}>
                        {noteTagListingResponse && allTags.length == 0 &&
                            <div>{t("noteTagPickerModalEmptyState")}</div>}
                        {noteTagListingResponse && allTags.length != 0 && filteredList.length == 0 && (
                            <div>{t("noteTagPickerModalNoMatch")}</div>
                        )}
                        {isFetching && noteTagListingResponse == null && <CircularLoading size={17}/>}
                    </div>
                )}
            </div>

            {selectedTags.length != 0 && (
                <div className={styles.selectedTagListContainer}>
                    {selectedTags.map((noteTagDto) => (
                        <div key={`selected-note-tag-${noteTagDto.noteTagId}`} className={styles.selectedTagContainer}>
                            <div className={styles.selectedTagName}>
                                <div className={styles.colorDot}
                                     style={noteTagDto.color ? {backgroundColor: noteTagDto.color} : undefined}/>
                                {noteTagDto.name}
                            </div>
                            <Button
                                variant={ButtonVariants.filled2}
                                className={styles.selectedTagUnselectButton}
                                onClick={() => removeFromSelected(noteTagDto)}
                            >
                                <IoClose/>
                            </Button>
                        </div>
                    ))}
                </div>
            )}
            <div className={styles.actionBar}>
                <Button heightVariant={ButtonHeight.short} onClick={close}>
                    {t("noteTagPickerModalCancelButton")}
                </Button>
                <Button
                    heightVariant={ButtonHeight.short}
                    variant={ButtonVariants.contrast}
                    className={styles.contButton}
                    onClick={submitPickedAndClose}
                >
                    {t("noteTagPickerModalSelectButton")}
                </Button>
            </div>
        </Modal>
    );
};

export default NoteTagPickerModal;
