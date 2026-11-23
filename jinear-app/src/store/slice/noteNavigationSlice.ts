import {createSlice, type PayloadAction} from "@reduxjs/toolkit";
import type {RootState} from "@/store";

export interface LastOpenNote {
    notebookId: string;
    noteId: string;
}

interface WorkspaceNoteNavigation {
    draftsOpen?: boolean;
    openNotebooks: Record<string, boolean>;
    openNotes: Record<string, boolean>;
    lastOpenNote?: LastOpenNote;
}

interface NoteNavigationState {
    byWorkspace: Record<string, WorkspaceNoteNavigation>;
}

const initialState: NoteNavigationState = {
    byWorkspace: {}
};

const emptyWorkspaceNavigation = (): WorkspaceNoteNavigation => ({
    openNotebooks: {},
    openNotes: {}
});

const workspaceNavigation = (state: NoteNavigationState, workspaceId: string) => {
    state.byWorkspace[workspaceId] ??= emptyWorkspaceNavigation();
    return state.byWorkspace[workspaceId];
};

const slice = createSlice({
    name: "noteNavigation",
    initialState,
    reducers: {
        setDraftsOpen: (state, action: PayloadAction<{ workspaceId: string; open: boolean }>) => {
            workspaceNavigation(state, action.payload.workspaceId).draftsOpen = action.payload.open;
        },
        setNotebookOpen: (state, action: PayloadAction<{ workspaceId: string; notebookId: string; open: boolean }>) => {
            workspaceNavigation(state, action.payload.workspaceId).openNotebooks[action.payload.notebookId] = action.payload.open;
        },
        setNoteOpen: (state, action: PayloadAction<{ workspaceId: string; noteId: string; open: boolean }>) => {
            workspaceNavigation(state, action.payload.workspaceId).openNotes[action.payload.noteId] = action.payload.open;
        },
        setLastOpenNote: (state, action: PayloadAction<{ workspaceId: string } & LastOpenNote>) => {
            const {workspaceId, notebookId, noteId} = action.payload;
            workspaceNavigation(state, workspaceId).lastOpenNote = {notebookId, noteId};
        },
        resetNoteNavigation: () => initialState
    }
});

export const {
    setDraftsOpen,
    setNotebookOpen,
    setNoteOpen,
    setLastOpenNote,
    resetNoteNavigation
} = slice.actions;
export default slice.reducer;

export const selectDraftsOpen = (workspaceId?: string) => (state: RootState) =>
    workspaceId ? state.noteNavigation.byWorkspace[workspaceId]?.draftsOpen : undefined;

export const selectNotebookOpen = (workspaceId?: string, notebookId?: string) => (state: RootState) =>
    workspaceId && notebookId ? state.noteNavigation.byWorkspace[workspaceId]?.openNotebooks[notebookId] : undefined;

export const selectNoteOpen = (workspaceId?: string, noteId?: string) => (state: RootState) =>
    workspaceId && noteId ? state.noteNavigation.byWorkspace[workspaceId]?.openNotes[noteId] : undefined;

export const selectLastOpenNote = (workspaceId?: string) => (state: RootState) =>
    workspaceId ? state.noteNavigation.byWorkspace[workspaceId]?.lastOpenNote : undefined;
