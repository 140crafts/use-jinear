import React from 'react';
import styles from './NoteEditor.module.css';
import NoteHeader from "@/components/note-editor/note-header/NoteHeader.tsx";
import NoteActionBar from "@/components/note-editor/note-action-bar/NoteActionBar.tsx";
import NoteEditorBody from "@/components/note-editor/note-editor-body/NoteEditorBody.tsx";
import {NoteEditorContext} from "@/components/note-editor/note-editor-context.ts";
import type {WorkspaceDto} from "@/be/jinear-core.ts";
import {DRAFT_ID_PREFIX, DRAFTS_NOTEBOOK_ID} from "@/components/tiptap/crdt/constants.ts";
import {useFilterNotesQuery} from "@/api/noteFilterApi.ts";
import Logger from "@/util/logger.ts";

interface NoteEditorProps {
    workspace: WorkspaceDto;
    noteId: string;
    notebookId: string;
}

const logger = Logger("NoteEditor");

const NoteEditor: React.FC<NoteEditorProps> = ({workspace, notebookId, noteId}) => {
    const isUnsubmittedDraft = noteId.indexOf(DRAFT_ID_PREFIX) != -1;
    const isSubmittedDraft = notebookId.indexOf(DRAFTS_NOTEBOOK_ID) != -1;
    const {currentData: retrieveNoteResponse, isLoading} = useFilterNotesQuery({
        workspaceId: workspace.workspaceId,
        notebookId: isSubmittedDraft ? undefined : notebookId,
        noteId: noteId,
        page: 0
    }, {skip: isUnsubmittedDraft});

    logger.log({retrieveNoteResponse})

    return (
        <NoteEditorContext.Provider value={{
            workspace,
            notebookId,
            noteId,
            note: retrieveNoteResponse?.data?.content?.[0]
        }}>
            {!isLoading &&
                <div className={styles.container}>
                    <NoteActionBar/>
                    <NoteHeader/>
                    <NoteEditorBody/>
                </div>}
        </NoteEditorContext.Provider>
    );
}

export default NoteEditor;
