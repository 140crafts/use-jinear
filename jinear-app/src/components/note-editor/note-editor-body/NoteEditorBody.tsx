import React, {useEffect, useRef} from 'react';
import styles from './NoteEditorBody.module.css';
import CollaborativeRichText, {type ICollaborativeRichTextRef} from "@/components/tiptap/CollaborativeRichText.tsx";
import Logger from "@/util/logger.ts";
import {useLiveText} from "@/components/tiptap/crdt/useLiveText.ts";
import {useNoteEditorContext} from "@/components/note-editor/note-editor-context.ts";
import {DRAFT_ID_PREFIX, DRAFTS_NOTEBOOK_ID} from "@/components/tiptap/crdt/constants.ts";
import {useInitializeNoteMutation} from "@/api/noteOperationApi.ts";

interface NoteEditorBodyProps {
}

const logger = Logger("NoteEditorBody");

const NoteEditorBody: React.FC<NoteEditorBodyProps> = ({}) => {
    const {workspace, noteId, note, title} = useNoteEditorContext();
    const richText = note?.richText
    const isUnsubmittedDraft = noteId?.indexOf(DRAFT_ID_PREFIX) != -1
    const submittedRef = useRef(false);
    const editorRef = useRef<ICollaborativeRichTextRef | null>(null);

    const [initializeNote, {isLoading}] = useInitializeNoteMutation();

    const {doc, status, getState, onPromoted} = useLiveText({
        richTextId: richText?.richTextId ?? noteId ?? "",
        initialRichText: richText,
        getHtml: () => editorRef.current?.getHTML() ?? null
    });

    logger.log({title})

    useEffect(() => {
        const bodyState = getState();
        if (!submittedRef.current && isUnsubmittedDraft && workspace && bodyState && doc) {
            submittedRef.current = true;
            const {workspaceId, username} = workspace;
            logger.log({workspaceId, bodyState, title})
            initializeNote({workspaceId, bodyState, title}).then(response => {
                if (response?.data) {
                    const newNoteDto = response?.data?.data;
                    const notebookId = newNoteDto?.notebookId ?? DRAFTS_NOTEBOOK_ID;
                    const noteId = newNoteDto?.noteId;
                    const realRichTextId = newNoteDto?.richTextId;
                    const realRichText = newNoteDto?.richText;
                    onPromoted(realRichTextId, realRichText).then(() => {
                        window.history.replaceState(null, "", `/${username}/notebook/${notebookId}/note/${noteId}`);
                    });
                }
            });
        }
    }, [isUnsubmittedDraft, getState, doc, workspace, title]);

    return (doc ?
        <CollaborativeRichText
            ref={editorRef}
            editable
            doc={doc}
            className={styles.container}
        /> : null);
}

export default NoteEditorBody;