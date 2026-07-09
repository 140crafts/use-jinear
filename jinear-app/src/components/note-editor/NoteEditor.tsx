import React from 'react';
import styles from './NoteEditor.module.css';
import NoteHeader from "@/components/note-editor/note-header/NoteHeader.tsx";
import NoteActionBar from "@/components/note-editor/note-action-bar/NoteActionBar.tsx";
import NoteEditorBody from "@/components/note-editor/note-editor-body/NoteEditorBody.tsx";
import type {NoteDto} from "@/be/jinear-core.ts";
import {NoteEditorContext} from "@/components/note-editor/note-editor-context.ts";

interface NoteEditorProps {
    note: NoteDto
}

const NoteEditor: React.FC<NoteEditorProps> = ({note}) => {

    return (
        <NoteEditorContext.Provider value={{note}}>
            <div className={styles.container}>
                <NoteActionBar/>
                <NoteHeader/>
                <NoteEditorBody/>
            </div>
        </NoteEditorContext.Provider>
    );
}

export default NoteEditor;