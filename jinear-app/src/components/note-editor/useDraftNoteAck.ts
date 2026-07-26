import {useEffect, useState} from "react";
import type {WorkspaceDto} from "@/be/jinear-core.ts";
import {DRAFTS_NOTEBOOK_ID} from "@/components/tiptap/crdt/constants.ts";
import {useAppDispatch, useTypedSelector} from "@/store";
import {clearSubmittedDraft, selectPendingDraft, selectSubmittedNoteId} from "@/slice/noteDraftsSlice.ts";

interface IUseDraftNoteAckProps {
    workspace: WorkspaceDto;
    urlNoteId: string;
    /** Notebook segment currently in the url — a real notebook id, or the drafts sentinel. */
    notebookId: string;
}

/**
 * The editor-side half of draft submission. Creation itself happens in one global place
 * (PendingDraftSubmitter); this hook only watches for the ack of the draft currently open —
 * then canonicalizes the URL (cosmetic replaceState, no remount) and hands back the real
 * noteId so NoteEditor can fetch the created note.
 */
export const useDraftNoteAck = ({workspace, urlNoteId, notebookId}: IUseDraftNoteAckProps) => {
    const dispatch = useAppDispatch();
    const pendingDraft = useTypedSelector(selectPendingDraft(urlNoteId));
    const submittedNoteId = useTypedSelector(selectSubmittedNoteId(urlNoteId));
    const [ackedNoteId, setAckedNoteId] = useState<string>();

    useEffect(() => {
        if (!submittedNoteId) return;
        setAckedNoteId(submittedNoteId);
        // Keep whichever notebook segment the draft was opened under — a notebook-born note stays in
        // its notebook, a workspace-level one keeps the sentinel. Only the note id becomes canonical.
        window.history.replaceState(null, "",
            `/${workspace.username}/notebook/${notebookId || DRAFTS_NOTEBOOK_ID}/note/${submittedNoteId}`);
        dispatch(clearSubmittedDraft({draftId: urlNoteId}));
    }, [submittedNoteId, urlNoteId, notebookId, workspace.username, dispatch]);

    return {isPendingCreate: !!pendingDraft, ackedNoteId};
};
