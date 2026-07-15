import React from 'react';
import styles from './NoteActionBar.module.css';
import {useNoteEditorContext} from "@/components/note-editor/note-editor-context.ts";
import Button, {ButtonHeight, ButtonVariants} from "@/components/button";
import useTranslation from "@/locals/useTranslation.ts";

const NoteActionBar: React.FC = () => {
    const {t} = useTranslation();

    return (
        <div className={styles.container}>
            <Button
                variant={ButtonVariants.brandColor}
                heightVariant={ButtonHeight.short}
            >
                {/*{t('noteEditorActionBarPublish' : 'noteEditorActionBarSave')}*/}
            </Button>
        </div>
    );
}

export default NoteActionBar;
