import React, {useEffect, useRef} from 'react';
import styles from './NoteEditorBody.module.css';
import CollaborativeRichText from "@/components/tiptap/CollaborativeRichText.tsx";
import Logger from "@/util/logger.ts";
import {useLiveText} from "@/components/tiptap/crdt/useLiveText.ts";
import {useNoteEditorContext} from "@/components/note-editor/note-editor-context.ts";
import {DRAFT_ID_PREFIX, DRAFTS_NOTEBOOK_ID} from "@/components/tiptap/crdt/constants.ts";
import {useInitializeNoteMutation} from "@/api/noteOperationApi.ts";
import {useNavigate} from "react-router-dom";

interface NoteEditorBodyProps {
}

const logger = Logger("NoteEditorBody");

const NoteEditorBody: React.FC<NoteEditorBodyProps> = ({}) => {
    const navigate = useNavigate();
    const {workspace, noteId, note} = useNoteEditorContext();
    const richText = note?.richText
    const isUnsubmittedDraft = noteId?.indexOf(DRAFT_ID_PREFIX) != -1
    const submittedRef = useRef(false);

    const [initializeNote, {isLoading}] = useInitializeNoteMutation();

    const {doc, status, getState, onPromoted} = useLiveText({
        richTextId: richText?.richTextId ?? noteId ?? "",
        initialRichText: richText
    });

    useEffect(() => {
        const bodyState = getState();
        if (!submittedRef.current && isUnsubmittedDraft && workspace && bodyState && doc) {
            submittedRef.current = true;
            const {workspaceId, username} = workspace;
            initializeNote({workspaceId, bodyState}).then(response => {
                if (response?.data) {
                    const newNoteDto = response?.data?.data;
                    const notebookId = newNoteDto?.notebookId ?? DRAFTS_NOTEBOOK_ID;
                    const noteId = newNoteDto?.noteId;
                    const realRichTextId = newNoteDto?.richTextId;
                    const realRichText = newNoteDto?.richText;
                    onPromoted(realRichTextId, realRichText).then(() => {
                        // navigate(`/${username}/notebook/${notebookId}/note/${noteId}`, {replace: true});
                        window.history.replaceState(null, "", `/${username}/notebook/${notebookId}/note/${noteId}`);
                    });
                }
            });
        }
    }, [isUnsubmittedDraft, getState, doc, workspace]);

    return (doc ? <CollaborativeRichText
        editable={isUnsubmittedDraft || !isLoading}
        doc={doc}
        className={styles.container}
    /> : null);
}

export default NoteEditorBody;