import {createContext, type Dispatch, type SetStateAction, useContext} from "react";
import type {NoteDto, WorkspaceDto} from "@/be/jinear-core.ts";

export interface INoteEditorContext {
    workspace?: WorkspaceDto,
    note?: NoteDto;
    notebookId?: string,
    noteId?: string,
    title?: string,
    setTitle?: Dispatch<SetStateAction<string>>,
}

const noop = () => {
};

export const NoteEditorContext = createContext<INoteEditorContext>({});

export function useNoteEditorContext() {
    return useContext(NoteEditorContext);
}
