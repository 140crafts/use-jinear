import React, {type RefObject, useEffect, useState} from 'react';
import styles from './NoteEditor.module.css';
import NoteHeader from "@/components/note-editor/note-header/NoteHeader.tsx";
import NoteActionBar from "@/components/note-editor/note-action-bar/NoteActionBar.tsx";
import NoteEditorBody from "@/components/note-editor/note-editor-body/NoteEditorBody.tsx";
import {NoteEditorContext} from "@/components/note-editor/note-editor-context.ts";
import type {NoteDto, WorkspaceDto} from "@/be/jinear-core.ts";
import {DRAFT_ID_PREFIX, DRAFTS_NOTEBOOK_ID} from "@/components/tiptap/crdt/constants.ts";
import {useFilterNotesQuery} from "@/api/noteFilterApi.ts";
import Logger from "@/util/logger.ts";
import CircularLoading from "@/components/circularLoading/CircularLoading.tsx";

interface NoteEditorProps {
    workspace: WorkspaceDto;
    noteId: string;
    notebookId: string;
}

const logger = Logger("NoteEditor");

const NoteEditor: React.FC<NoteEditorProps> = ({workspace, notebookId, noteId}) => {
    const [titleTextAreaRef, setTitleTextAreaRef] = useState<RefObject<HTMLTextAreaElement | null>>();
    const [editingNoteId, setEditingNoteId] = useState<string>(noteId);
    const [viewingNote, setViewingNote] = useState<NoteDto>();

    const isUnsubmittedDraft = editingNoteId.indexOf(DRAFT_ID_PREFIX) != -1;
    const isSubmittedDraft = notebookId.indexOf(DRAFTS_NOTEBOOK_ID) != -1;

    const {currentData: retrieveNoteResponse, isLoading} = useFilterNotesQuery({
        workspaceId: workspace.workspaceId,
        notebookId: isSubmittedDraft ? undefined : notebookId,
        noteId: editingNoteId,
        page: 0
    }, {skip: isUnsubmittedDraft});

    const note = retrieveNoteResponse?.data?.content?.[0];
    const isReady = isUnsubmittedDraft || (!isLoading && !!note);

    useEffect(() => {
        if (note) {
            setViewingNote(note);
        }
    }, [note]);

    return (
        <NoteEditorContext.Provider value={{
            workspace,
            notebookId,
            editingNoteId,
            setEditingNoteId,
            note: viewingNote,
            titleTextAreaRef,
            setTitleTextAreaRef
        }}>

            <div className={styles.container} key={noteId}>
                <NoteActionBar/>
                <NoteHeader/>
                {isReady ? <NoteEditorBody/> : <CircularLoading/>}
            </div>
        </NoteEditorContext.Provider>
    );
}

export default NoteEditor;
