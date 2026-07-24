import {createSlice, type PayloadAction} from "@reduxjs/toolkit";
import type {RootState} from "@/store";

interface PendingNoteDraft {
    draftId: string;
    workspaceId: string;
    createdAt: number;
    /** Mirrored from the in-doc Y.Text title so lists can label the draft without opening IndexedDB. */
    title?: string;
    /**
     * Set when the server rejected the create with an unretryable error. The draft stays in
     * `pending` (its local doc is kept) but PendingDraftSubmitter skips it — the user recovers
     * the content by opening it, and discards it deliberately. Undefined ⇒ still being retried.
     */
    failedAt?: number;
    /** HTTP status that caused the failure (diagnostics). */
    failStatus?: number;
}

const initialState = {
    pending: {},
    docKeyAliases: {},
    submittedNoteIdByDraftId: {},
} as {
    /** Local drafts whose create hasn't been acked by the server yet, keyed by the draft's URL id. */
    pending: Record<string, PendingNoteDraft>;
    /** Real noteId → original draft id, so the y-indexeddb doc key stays stable after URL canonicalization. */
    docKeyAliases: Record<string, string>;
    /** Draft id → created noteId, so an open editor learns its draft was submitted by PendingDraftSubmitter. */
    submittedNoteIdByDraftId: Record<string, string>;
};

const slice = createSlice({
    name: "noteDrafts",
    initialState,
    reducers: {
        addPendingDraft: (state, action: PayloadAction<{ draftId: string; workspaceId: string }>) => {
            const {draftId, workspaceId} = action.payload;
            state.pending[draftId] = {draftId, workspaceId, createdAt: Date.now()};
        },
        removePendingDraft: (state, action: PayloadAction<{ draftId: string }>) => {
            delete state.pending[action.payload.draftId];
        },
        setPendingDraftTitle: (state, action: PayloadAction<{ draftId: string; title: string }>) => {
            const entry = state.pending[action.payload.draftId];
            if (entry) entry.title = action.payload.title;
        },
        /**
         * The server rejected the create with an unretryable error. Keep the draft (and its local
         * doc) but stop auto-retrying it, so the user never loses content to a transient lie-fi 4xx.
         */
        markDraftFailed: (state, action: PayloadAction<{ draftId: string; status?: number }>) => {
            const entry = state.pending[action.payload.draftId];
            if (entry) {
                entry.failedAt = Date.now();
                entry.failStatus = action.payload.status;
            }
        },
        /** A pending draft was created server-side (by PendingDraftSubmitter). One atomic transition. */
        draftSubmitted: (state, action: PayloadAction<{ draftId: string; noteId: string }>) => {
            const {draftId, noteId} = action.payload;
            delete state.pending[draftId];
            state.docKeyAliases[noteId] = draftId;
            // ??= guards state persisted before this field existed (redux-persist merges per-slice).
            state.submittedNoteIdByDraftId ??= {};
            state.submittedNoteIdByDraftId[draftId] = noteId;
        },
        /** Called by the editor's ack-watcher after it canonicalized the URL. */
        clearSubmittedDraft: (state, action: PayloadAction<{ draftId: string }>) => {
            delete state.submittedNoteIdByDraftId?.[action.payload.draftId];
        },
        resetNoteDrafts: () => initialState,
    },
});

export const {
    addPendingDraft,
    removePendingDraft,
    setPendingDraftTitle,
    markDraftFailed,
    draftSubmitted,
    clearSubmittedDraft,
    resetNoteDrafts
} = slice.actions;
export default slice.reducer;

export const selectPendingDraft = (id: string) => (state: RootState) => state.noteDrafts.pending[id];
export const selectDocKey = (id: string) => (state: RootState) => state.noteDrafts.docKeyAliases[id] ?? id;
export const selectSubmittedNoteId = (draftId: string) => (state: RootState) =>
    state.noteDrafts.submittedNoteIdByDraftId?.[draftId];

export const selectPendingDraftsOrdered = (workspaceId: string) => ((state: RootState) => {
    const pending = state.noteDrafts.pending;
    return Object.values(pending)
        .filter(entry => entry.workspaceId === workspaceId)
        .sort((a, b) => b.createdAt - a.createdAt);
})