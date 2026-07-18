import React from 'react';
import styles from './NoteEditorBody.module.css';
import CollaborativeRichText from "@/components/tiptap/CollaborativeRichText.tsx";
import {useNoteEditorContext} from "@/components/note-editor/note-editor-context.ts";

const NoteEditorBody: React.FC = () => {
    const {workspace, doc, flushNow, registerEditor} = useNoteEditorContext();

    return (doc ?
        <CollaborativeRichText
            ref={registerEditor}
            editable
            doc={doc}
            className={styles.container}
            workspaceIdForImages={workspace?.workspaceId}
            onSettle={flushNow}
        /> : null);
}

export default NoteEditorBody;
