import React, {type ChangeEvent, useEffect, useState} from 'react';
import styles from './NoteHeader.module.css';
import useTranslation from "@/locals/useTranslation.ts";
import {useNote} from "@/components/note-editor/note-editor-context.ts";
import {useDebouncedEffect} from "@/hooks/useDebouncedEffect.ts";
import Logger from "@/util/logger.ts";

interface NoteHeaderProps {

}

const logger = Logger("NoteHeader");

const NoteHeader: React.FC<NoteHeaderProps> = ({}) => {
    const {t} = useTranslation();
    const note = useNote();
    const [title, setTitle] = useState(note?.title)

    useEffect(() => {
        if (note) {
            setTitle(note.title)
        }
    }, [note, setTitle]);

    useDebouncedEffect(() => {
        logger.log({noteHeader: title})
    }, [title, note], 1250);

    const onTitleChange = (event: ChangeEvent<HTMLTextAreaElement>) => {
        const value = event.target.value;
        setTitle(value);
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