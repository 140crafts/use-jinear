import React from 'react';
import styles from './NoteTags.module.css';
import {useNoteEditorContext} from "@/components/note-editor/note-editor-context.ts";
import Button, {ButtonHeight, ButtonVariants} from "@/components/button";
import useTranslation from "@/locals/useTranslation.ts";
import {LuPlus} from "react-icons/lu";
import {useAppDispatch} from "@/store";
import {popNoteTagPickerModal} from "@/store/slice/modalSlice";
import {useUpdateNoteTagAssignmentsMutation} from "@/store/api/noteTagApi";
import type {NoteTagDto} from "@/model/be/jinear-core";
import getCssVariable from "@/util/cssHelper.ts";

interface NoteTagsProps {

}

const NoteTags: React.FC<NoteTagsProps> = ({}) => {
    const {t} = useTranslation();
    const dispatch = useAppDispatch();
    const {note, isPendingCreate} = useNoteEditorContext();
    const notesExistingTags = note?.noteTagAssignments;
    const [updateNoteTagAssignments, {isLoading: isUpdateNoteTagAssignmentsLoading}] = useUpdateNoteTagAssignmentsMutation();

    const openNoteTagPickerModal = () => {
        if (note == null) {
            return;
        }
        const noteId = note.noteId;
        dispatch(popNoteTagPickerModal({
            visible: true,
            notebookId: note.notebookId,
            initialSelection: note.noteTagAssignments?.map(noteTagAssignment => noteTagAssignment.noteTag) ?? [],
            onPick: (pickedList: NoteTagDto[]) => {
                updateNoteTagAssignments({noteId, noteTagIds: pickedList.map(tag => tag.noteTagId)});
            }
        }));
    };

    return (
        <>
            {notesExistingTags?.map(tag => {
                    const bgColor = tag?.noteTag?.color ?? getCssVariable("--c-primary-shade-1");
                    return (
                        <div key={tag.noteTagAssignmentId}
                             className={styles.noteTag}>
                            <div className={styles.circle} style={{backgroundColor: bgColor}}/>
                            <span>{tag.noteTag.name}</span>
                        </div>
                    )
                }
            )}

            <Button
                heightVariant={ButtonHeight.short}
                variant={ButtonVariants.outline}
                onClick={openNoteTagPickerModal}
                disabled={isPendingCreate || isUpdateNoteTagAssignmentsLoading}
                loading={isUpdateNoteTagAssignmentsLoading}
                className={styles.addNoteTagButton}
            >
                <LuPlus className={'icon'}/>
                <div className={'spacer-w-1'}/>
                {t('addNoteTag')}
            </Button>
        </>
    );
}

export default NoteTags;
