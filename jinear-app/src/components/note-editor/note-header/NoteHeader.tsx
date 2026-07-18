import React, {type ChangeEvent} from 'react';
import styles from './NoteHeader.module.css';
import useTranslation from "@/locals/useTranslation.ts";
import {useNoteEditorContext} from "@/components/note-editor/note-editor-context.ts";
import {TITLE_FIELD} from "@/components/tiptap/crdt/constants.ts";
import {useYText} from "@/components/tiptap/crdt/useYText.ts";

const NoteHeader: React.FC = () => {
    const {t} = useTranslation();
    const {doc} = useNoteEditorContext();
    // The title is a Y.Text layer in the note's doc — same offline persistence and sync path
    // as the body. Remote edits arrive through the poll and land here via the observer.
    const [title, setTitle] = useYText(doc, TITLE_FIELD);

    const onTitleChange = (event: ChangeEvent<HTMLTextAreaElement>) => {
        setTitle(event.target.value);
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
