import {createContext, useContext} from "react";
import type {PathAwareNoteDto} from "@/be/jinear-core.ts";

interface INoteEditorContext {
    note?: PathAwareNoteDto
}

export const NoteEditorContext = createContext<INoteEditorContext>({});

export function useNote() {
    const ctx = useContext(NoteEditorContext);
    return ctx.note;
}

export function useIsDraft() {
    const ctx = useContext(NoteEditorContext);
    return ctx.note == null;
}

export function useNotesNotebookId() {
    const ctx = useContext(NoteEditorContext);
    return ctx.note?.notebookId;
}
