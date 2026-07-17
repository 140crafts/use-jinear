import React, {type ChangeEvent, useState} from 'react';
import styles from './NoteHeader.module.css';
import useTranslation from "@/locals/useTranslation.ts";
import {useNoteEditorContext} from "@/components/note-editor/note-editor-context.ts";

const NoteHeader: React.FC = () => {
    const {t} = useTranslation();
    const {title, setTitle} = useNoteEditorContext();

    const onTitleChange = (event: ChangeEvent<HTMLTextAreaElement>) => {
        const nextTitle = event.target.value;
        setTitle?.(nextTitle);
    }

    return (
        <div className={styles.container}>
            <label htmlFor={'noteTitle'} className={styles.titleLabel}>
                {t("newNoteTitle")}
            </label>
            <textarea
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
