import React, {useCallback, useRef} from 'react';
import styles from './NoteEditor.module.css';
import NoteHeader from "@/components/note-editor/note-header/NoteHeader.tsx";
import NoteActionBar from "@/components/note-editor/note-action-bar/NoteActionBar.tsx";
import NoteEditorBody from "@/components/note-editor/note-editor-body/NoteEditorBody.tsx";
import {NoteEditorContext} from "@/components/note-editor/note-editor-context.ts";
import type {WorkspaceDto} from "@/be/jinear-core.ts";
import {DRAFTS_NOTEBOOK_ID} from "@/components/tiptap/crdt/constants.ts";
import {useLiveText} from "@/components/tiptap/crdt/useLiveText.ts";
import {useDraftNoteAck} from "@/components/note-editor/useDraftNoteAck.ts";
import {useNoteTitleMirror} from "@/components/note-editor/useNoteTitleMirror.ts";
import type {ICollaborativeRichTextRef} from "@/components/tiptap/CollaborativeRichText.tsx";
import {useFilterNotesQuery} from "@/api/noteFilterApi.ts";
import {useTypedSelector} from "@/store";
import {selectDocKey} from "@/slice/noteDraftsSlice.ts";
import useTranslation from "@/locals/useTranslation.ts";
import CircularLoading from "@/components/circularLoading/CircularLoading.tsx";
import NotePropertyBar from "@/components/note-editor/note-property-bar/NotePropertyBar.tsx";

interface NoteEditorProps {
    workspace: WorkspaceDto;
    noteId: string;
    notebookId: string;
}

const NoteEditor: React.FC<NoteEditorProps> = ({workspace, notebookId, noteId}) => {
    const {t} = useTranslation();
    const editorRef = useRef<ICollaborativeRichTextRef | null>(null);

    // The doc's local identity never changes: for draft-born notes it stays the original draft id
    // (aliased from the real noteId after creation), so IndexedDB content survives URL canonicalization.
    const docKey = useTypedSelector(selectDocKey(noteId));

    const {isPendingCreate, ackedNoteId} = useDraftNoteAck({workspace, urlNoteId: noteId});

    const isDraftsNotebook = notebookId === DRAFTS_NOTEBOOK_ID;
    const {currentData: retrieveNoteResponse, isLoading} = useFilterNotesQuery({
        workspaceId: workspace.workspaceId,
        notebookId: isDraftsNotebook ? undefined : notebookId,
        noteId: ackedNoteId ?? noteId,
        includePath: true,
        page: 0
    }, {skip: isPendingCreate});

    const note = retrieveNoteResponse?.data?.content?.[0];
    // ackedNoteId bridges the ack → fetch gap so the doc/editor stays alive (no flash mid-typing).
    const shouldEdit = isPendingCreate || !!ackedNoteId || !!note;

    const {doc, status, flushNow} = useLiveText({
        docKey,
        enabled: shouldEdit,
        richTextId: note?.richText?.richTextId,
        initialRichText: note?.richText,
        seedTitle: note?.title,
        getHtml: () => editorRef.current?.getHTML() ?? null
    });

    useNoteTitleMirror({
        doc,
        note,
        workspaceId: workspace.workspaceId,
        pendingDraftId: isPendingCreate ? noteId : undefined
    });

    const registerEditor = useCallback((editor: ICollaborativeRichTextRef | null) => {
        editorRef.current = editor;
    }, []);

    const notFound = !isPendingCreate && !ackedNoteId && !isLoading && !note;

    return (
        <NoteEditorContext.Provider value={{
            workspace,
            notebookId,
            noteId,
            note,
            isPendingCreate,
            doc,
            status,
            flushNow,
            registerEditor
        }}>
            <div className={styles.container} key={noteId}>
                <NoteActionBar/>
                {notFound
                    ? <div className={styles.notFound}>{t("noteEditorNoteNotFound")}</div>
                    : <>
                        <NoteHeader/>
                        <NotePropertyBar/>
                        {doc ? <NoteEditorBody/> : <CircularLoading/>}
                    </>}
            </div>
        </NoteEditorContext.Provider>
    );
}

export default NoteEditor;
