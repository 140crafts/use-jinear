import React from 'react';
import styles from './NoteHeader.module.css';
import useTranslation from "@/locals/useTranslation.ts";

interface NoteHeaderProps {

}

const NoteHeader: React.FC<NoteHeaderProps> = ({}) => {
    const {t} = useTranslation();

    return (
        <div className={styles.container}>
            <label htmlFor={'noteTitle'} className={styles.titleLabel}>
                {t("newNoteTitle")}
            </label>
            <textarea id={'noteTitle'} className={styles.titleTextArea} placeholder={t('newNoteTitlePlaceholder')}/>
        </div>
    );
}

export default NoteHeader;