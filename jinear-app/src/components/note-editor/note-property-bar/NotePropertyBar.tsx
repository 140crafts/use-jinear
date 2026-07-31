import React from 'react';
import styles from './NotePropertyBar.module.css';
import Button, {ButtonHeight, ButtonVariants} from "@/components/button";
import useTranslation from "@/locals/useTranslation.ts";
import {useAppDispatch} from "@/store";
import {closeDialogModal, popDialogModal, popNotebookPickerModal} from "@/slice/modalSlice.ts";
import {useNoteEditorContext} from "@/components/note-editor/note-editor-context.ts";
import {useChangeNotebookMutation} from "@/api/noteOperationApi.ts";
import type {NotebookDto} from "@/be/jinear-core.ts";
import {useNavigate} from "react-router-dom";
import {LuNotebook} from "react-icons/lu";
import {shortenStringIfMoreThanMaxLength} from "@/util/textUtil.ts";
import NoteTags from "@/components/note-editor/note-property-bar/note-tags/NoteTags.tsx";

interface NotePropertyBarProps {

}

const NotePropertyBar: React.FC<NotePropertyBarProps> = ({}) => {
    const navigate = useNavigate();
    const {t} = useTranslation();
    const {workspace, note, notebook, isPendingCreate} = useNoteEditorContext();
    const dispatch = useAppDispatch();
    const [changeNotebook, {isLoading: isChangeNotebookLoading}] = useChangeNotebookMutation();

    // Sourced from the resolved notebook, not the note: a draft created inside a notebook belongs to
    // it from the first keystroke, well before the server has a note to read a notebookId off.
    const originalNotebookTitle = notebook?.title;
    const originalNotebookTitleTooLong = (originalNotebookTitle ?? '').length > 12;
    const changeNotebookButtonLabel = notebook ? shortenStringIfMoreThanMaxLength({
        text: originalNotebookTitle ?? '',
        maxLength: 24,
        shortenFromMiddle: true
    }) : t('changeNoteNotebook');

    const notebookTooltip = !notebook ? undefined : originalNotebookTitleTooLong ? originalNotebookTitle : t('changeNoteNotebook');

    const popAreYouSureToMoveToNotebookModal = (notebook: NotebookDto) => {
        dispatch(
            popDialogModal({
                visible: true,
                title: t("moveNoteToNotebookAreYouSureTitle"),
                content: t("moveNoteToNotebookAreYouSureText").replace("{notebookTitle}", notebook.title),
                confirmButtonLabel: t("moveNoteToNotebookAreYouSureConfirmLabel"),
                onConfirm: () => {
                    workspace && note && changeNotebook({
                        workspaceId: workspace?.workspaceId,
                        noteId: note?.noteId,
                        notebookId: notebook.notebookId
                    }).then(() => {
                        notebook && note && navigate(`/${workspace?.username}/notebook/${notebook.notebookId}/note/${note.noteId}`)
                    })
                    dispatch(closeDialogModal());
                }
            })
        );
    }

    const popNotebookPicker = () => {
        dispatch(popNotebookPickerModal({
            workspaceId: workspace?.workspaceId,
            onPick: popAreYouSureToMoveToNotebookModal,
            visible: true
        }));
    }
    return (
        <div className={styles.container}>
            <Button
                variant={ButtonVariants.outline}
                heightVariant={ButtonHeight.short}
                onClick={popNotebookPicker}
                className={styles.button}
                data-tooltip-multiline={notebookTooltip}
                disabled={isPendingCreate}
            >
                <LuNotebook className={styles.icon}/>
                <span>{changeNotebookButtonLabel}</span>
            </Button>

            <div className={styles.seperator}/>

            <NoteTags/>

        </div>
    );
}

export default NotePropertyBar;