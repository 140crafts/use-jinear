import React, {type ChangeEvent, useEffect, useRef, useState} from 'react';
import styles from './NoteHeader.module.css';
import useTranslation from "@/locals/useTranslation.ts";
import {useNoteEditorContext} from "@/components/note-editor/note-editor-context.ts";
import {useUpdateNoteMutation} from "@/api/noteOperationApi.ts";
import {useDebouncedEffect} from "@/hooks/useDebouncedEffect.ts";

const NoteHeader: React.FC = () => {
    const {t} = useTranslation();
    const {workspace, setTitleTextAreaRef, note} = useNoteEditorContext();
    const [title, setTitle] = useState<string>("");
    const titleTextAreaRef = useRef<HTMLTextAreaElement>(null)
    const [updateNote] = useUpdateNoteMutation();

    const onTitleChange = (event: ChangeEvent<HTMLTextAreaElement>) => {
        const nextTitle = event.target.value;
        setTitle?.(nextTitle);
    }

    useEffect(() => {
        if (titleTextAreaRef) {
            setTitleTextAreaRef?.(titleTextAreaRef);
        }
    }, [titleTextAreaRef, setTitleTextAreaRef]);

    useEffect(() => {
        if (note) {
            setTitle(note.title)
        }
    }, [note]);

    useDebouncedEffect(() => {
        if (workspace && note && title && title != note?.title) {
            updateNote({noteId: note.noteId, title, workspaceId: workspace.workspaceId})
        }
    }, [title, note, workspace], 2000);

    // logger.log({title,noteId});

    return (
        <div className={styles.container}>
            <label htmlFor={'noteTitle'} className={styles.titleLabel}>
                {t("newNoteTitle")}
            </label>
            <textarea
                ref={titleTextAreaRef}
                id={'noteTitle'}
                className={styles.titleTextArea}
                placeholder={t('newNoteTitlePlaceholder')}
                value={title}
                onChange={onTitleChange}
            />
        </div>
    );
}

export default NoteHeader;
