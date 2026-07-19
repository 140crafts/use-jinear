import React, {type ChangeEventHandler} from 'react';
import styles from './NotePath.module.css';
import {useNoteEditorContext} from "@/components/note-editor/note-editor-context.ts";
import Button, {ButtonHeight} from "@/components/button";
import {DRAFTS_NOTEBOOK_ID} from "@/components/tiptap/crdt/constants.ts";
import {useAllWorkspaceNotebooks} from "@/hooks/useAllWorkspaceNotebooks.ts";
import {LuNotebook} from "react-icons/lu";
import useTranslation from "@/locals/useTranslation.ts";
import cn from "classnames";
import {shortenStringIfMoreThanMaxLength} from "@/util/textUtil.ts";

interface NotePathProps {

}

const NotePath: React.FC<NotePathProps> = ({}) => {
    const {t} = useTranslation();
    const {workspace, note} = useNoteEditorContext();
    const path = note?.path;
    const notebookId = note?.notebookId ?? DRAFTS_NOTEBOOK_ID;
    const {notebooks} = useAllWorkspaceNotebooks(workspace?.workspaceId);

    const onNotebookChange = (e: React.ChangeEvent<HTMLSelectElement>) => {
        const nextNotebookId = e.target.value;
    }

    return (
        <div className={styles.container}>

            <div className={styles.selectWrapper}>
                <LuNotebook className={'icon'}/>
                <select
                    className={styles.notebookSelect}
                    value={notebookId}
                    onChange={onNotebookChange}
                >
                    <option key={DRAFTS_NOTEBOOK_ID} value={DRAFTS_NOTEBOOK_ID}>
                        {t('notebookDraftsTitle')}
                    </option>
                    {notebooks?.map(notebook => (
                        <option key={notebook.notebookId} value={notebook.notebookId}>
                            {notebook.title}
                        </option>
                    ))}
                </select>
            </div>

            <span className={styles.separator}>/</span>

            {path?.path?.map((notePath, index) =>
                <React.Fragment key={`note-path-${notePath.noteId}`}>
                    <Button
                        href={`/${workspace?.username}/notebook/${notebookId}/note/${notePath.noteId}`}
                        heightVariant={ButtonHeight.short}
                        className={cn(styles.breadcrumbButton, 'line-clamp')}
                        data-tooltip-multiline={notePath.title?.length > 12 ? notePath.title : undefined}
                    >
                        {shortenStringIfMoreThanMaxLength({text: notePath.title, maxLength: 12})}
                    </Button>
                    {index < path.path.length - 1 && <span className={styles.separator}>/</span>}
                </React.Fragment>
            )}
        </div>
    );
}

export default NotePath;