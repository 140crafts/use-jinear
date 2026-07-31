import React from 'react';
import styles from './NotePath.module.css';
import {useNoteEditorContext} from "@/components/note-editor/note-editor-context.ts";
import Button, {ButtonHeight} from "@/components/button";
import {DRAFTS_NOTEBOOK_ID} from "@/components/tiptap/crdt/constants.ts";
import useTranslation from "@/locals/useTranslation.ts";
import cn from "classnames";
import {shortenStringIfMoreThanMaxLength} from "@/util/textUtil.ts";
import {LuNotebook} from "react-icons/lu";

interface NotePathProps {

}

const NotePath: React.FC<NotePathProps> = ({}) => {
    const {t} = useTranslation();
    const {workspace, note, notebook, notebookId: urlNotebookId} = useNoteEditorContext();
    const path = note?.path;
    // The url segment is already the drafts sentinel for workspace-level drafts, so this one
    // expression covers created notes, notebook-born drafts and plain drafts alike.
    const notebookId = note?.notebookId ?? urlNotebookId ?? DRAFTS_NOTEBOOK_ID;
    const isDraftsNotebook = notebookId === DRAFTS_NOTEBOOK_ID;
    // Only unresolved when the notebook listing hasn't loaded — labelling it "Drafts" there would
    // claim something false, so fall back to a neutral label instead.
    const notebookTitle = notebook?.title
        ?? (isDraftsNotebook ? t('notebookDraftsTitle') : t('notePathNotebookFallback'));

    return (
        <div className={styles.container}>

            <Button
                href={`/${workspace?.username}/notebook/${notebookId}`}
                heightVariant={ButtonHeight.short}
                className={cn(styles.breadcrumbButton, 'line-clamp')}
                data-tooltip-multiline={notebookTitle?.length > 12 ? notebookTitle : undefined}
            >
                <div className={styles.buttonContent}>
                    <LuNotebook className={'icon'}/>
                    {shortenStringIfMoreThanMaxLength({text: notebookTitle, maxLength: 12})}
                </div>
            </Button>
            <span className={styles.separator}>{"/"}</span>

            {path?.path?.map((notePath, index) =>
                <React.Fragment key={`note-path-${notePath.noteId}`}>
                    <Button
                        href={`/${workspace?.username}/notebook/${notebookId}/note/${notePath.noteId}`}
                        heightVariant={ButtonHeight.short}
                        className={cn(styles.breadcrumbButton, 'line-clamp', index == path.path.length - 1 && styles.bold)}
                        data-tooltip-multiline={notePath.title?.length > 24 ? notePath.title : undefined}
                    >
                        {shortenStringIfMoreThanMaxLength({
                            text: notePath.title == '' || notePath.title == null ? t('untitledNote') : notePath.title,
                            maxLength: 24,
                            shortenFromMiddle: true
                        })}
                    </Button>
                    {index < path.path.length - 1 && <span className={styles.separator}>{"/"}</span>}
                </React.Fragment>
            )}
        </div>
    );
}

export default NotePath;