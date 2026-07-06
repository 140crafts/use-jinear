import React from 'react';
import styles from './index.module.css';
import NoteEditor from "@/components/note-editor/NoteEditor.tsx";

interface NewNotePageProps {

}

const NewNotePage: React.FC<NewNotePageProps> = ({}) => {

    return (
        <div className={styles.container}>
            <NoteEditor/>
        </div>
    );
}

export default NewNotePage;