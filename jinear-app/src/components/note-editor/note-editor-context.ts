import {createContext, type Dispatch, type RefObject, type SetStateAction, useContext} from "react";
import type {NoteDto, WorkspaceDto} from "@/be/jinear-core.ts";

export interface INoteEditorContext {
    workspace?: WorkspaceDto,
    note?: NoteDto;
    notebookId?: string,
    editingNoteId?: string,
    setEditingNoteId?: Dispatch<SetStateAction<string>>,
    titleTextAreaRef?: RefObject<HTMLTextAreaElement | null>,
    setTitleTextAreaRef?: Dispatch<SetStateAction<RefObject<HTMLTextAreaElement | null> | undefined>>
}

const noop = () => {
};

export const NoteEditorContext = createContext<INoteEditorContext>({});

export function useNoteEditorContext() {
    return useContext(NoteEditorContext);
}
