import {createContext, useContext} from "react";
import type {NoteDto, WorkspaceDto} from "@/be/jinear-core.ts";

export interface INoteEditorContext {
    workspace?: WorkspaceDto,
    note?: NoteDto;
    notebookId?: string,
    noteId?: string,
}

const noop = () => {
};

export const NoteEditorContext = createContext<INoteEditorContext>({});

export function useNoteEditorContext() {
    return useContext(NoteEditorContext);
}
