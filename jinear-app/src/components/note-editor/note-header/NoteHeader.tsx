import React, {type ChangeEvent} from 'react';
import styles from './NoteHeader.module.css';
import useTranslation from "@/locals/useTranslation.ts";
import {useNoteEditorContext} from "@/components/note-editor/note-editor-context.ts";

const NoteHeader: React.FC = () => {
    const {t} = useTranslation();

    const onTitleChange = (event: ChangeEvent<HTMLTextAreaElement>) => {
        // event.target.value
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
                // value={title}
                onChange={onTitleChange}
            />
        </div>
    );
}

export default NoteHeader;
