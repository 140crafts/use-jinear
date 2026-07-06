import React from 'react';
import styles from './NoteEditor.module.css';
import NoteHeader from "@/components/note-editor/note-header/NoteHeader.tsx";
import NoteActionBar from "@/components/note-editor/note-action-bar/NoteActionBar.tsx";

interface NoteEditorProps {
    noteId?: string
}

const NoteEditor: React.FC<NoteEditorProps> = ({noteId}) => {

    return (
        <div className={styles.container}>
            <NoteActionBar/>
            <NoteHeader/>

        </div>
    );
}

export default NoteEditor;