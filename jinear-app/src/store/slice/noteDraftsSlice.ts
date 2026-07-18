import {createSlice, type PayloadAction} from "@reduxjs/toolkit";
import type {RootState} from "@/store";

interface PendingNoteDraft {
    draftId: string;
    workspaceId: string;
    createdAt: number;
    /** Mirrored from the in-doc Y.Text title so lists can label the draft without opening IndexedDB. */
    title?: string;
}

const initialState = {
    pending: {},
    docKeyAliases: {},
} as {
    /** Local drafts whose create hasn't been acked by the server yet, keyed by the draft's URL id. */
    pending: Record<string, PendingNoteDraft>;
    /** Real noteId → original draft id, so the y-indexeddb doc key stays stable after URL canonicalization. */
    docKeyAliases: Record<string, string>;
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
        addDocKeyAlias: (state, action: PayloadAction<{ noteId: string; draftId: string }>) => {
            state.docKeyAliases[action.payload.noteId] = action.payload.draftId;
        },
        resetNoteDrafts: () => initialState,
    },
});

export const {
    addPendingDraft,
    removePendingDraft,
    setPendingDraftTitle,
    addDocKeyAlias,
    resetNoteDrafts
} = slice.actions;
export default slice.reducer;

export const selectPendingDraft = (id: string) => (state: RootState) => state.noteDrafts.pending[id];
export const selectDocKey = (id: string) => (state: RootState) => state.noteDrafts.docKeyAliases[id] ?? id;

export const selectPendingDraftsOrdered = (workspaceId: string) => ((state: RootState) => {
    const pending = state.noteDrafts.pending;
    return Object.values(pending)
        .filter(entry => entry.workspaceId === workspaceId)
        .sort((a, b) => b.createdAt - a.createdAt);
})