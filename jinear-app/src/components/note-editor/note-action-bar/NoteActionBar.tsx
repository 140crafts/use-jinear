import React from 'react';
import styles from './NoteActionBar.module.css';
import {useNote, useNotesNotebookId} from "@/components/note-editor/note-editor-context.ts";
import Button, {ButtonHeight, ButtonVariants} from "@/components/button";
import useTranslation from "@/locals/useTranslation.ts";

interface NoteActionBarProps {

}

const NoteActionBar: React.FC<NoteActionBarProps> = ({}) => {
    const {t} = useTranslation();

    const note = useNote();
    const notebookId = useNotesNotebookId();

    return (
        <div className={styles.container}>
            <Button
                variant={ButtonVariants.brandColor}
                heightVariant={ButtonHeight.short}
            >
                {t(notebookId ? 'noteEditorActionBarSave' : 'noteEditorActionBarPublish')}
            </Button>
        </div>
    );
}

export default NoteActionBar;