import React from 'react';
import styles from './NoteEditorBody.module.css';
import CollaborativeRichText from "@/components/tiptap/CollaborativeRichText.tsx";
import {useNoteEditorContext} from "@/components/note-editor/note-editor-context.ts";
import useTranslation from "@/locals/useTranslation.ts";

const NoteEditorBody: React.FC = () => {
    const {t} = useTranslation();
    const {workspace, doc, flushNow, registerEditor} = useNoteEditorContext();

    return (doc ?
        <CollaborativeRichText
            ref={registerEditor}
            editable
            doc={doc}
            className={styles.container}
            editorWrapperClassName={styles.editorWrapperClassName}
            editorClassName={styles.editorClassName}
            workspaceIdForImages={workspace?.workspaceId}
            onSettle={flushNow}
            actionBarClassName={styles.actionBarClassName}
            withOutlineOnFocus={false}
            placeholder={t('emptyNotePlaceholder')}
        /> : null);
}

export default NoteEditorBody;
