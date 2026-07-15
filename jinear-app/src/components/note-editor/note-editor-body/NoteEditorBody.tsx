import React, {useEffect} from 'react';
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

    const [initializeNote, {isLoading}] = useInitializeNoteMutation();

    const {doc, status, getState} = useLiveText({
        richTextId: richText?.richTextId ?? noteId ?? "",
        initialRichText: richText
    });

    useEffect(() => {
        const bodyState = getState();
        if (isUnsubmittedDraft && workspace && bodyState && doc) {
            const {workspaceId, username} = workspace;
            initializeNote({workspaceId, bodyState}).then(response => {
                if (response?.data) {
                    const newNoteDto = response?.data?.data;
                    const notebookId = newNoteDto?.notebookId ?? DRAFTS_NOTEBOOK_ID;
                    const noteId = newNoteDto?.noteId;
                    navigate(`/${username}/notebook/${notebookId}/note/${noteId}`);
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