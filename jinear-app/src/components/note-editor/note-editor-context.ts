import {createContext, useContext} from "react";
import type * as Y from "yjs";
import type {NotebookDto, NoteDto, WorkspaceDto} from "@/be/jinear-core.ts";
import type {LiveTextStatus} from "@/components/tiptap/crdt/useLiveText.ts";
import type {ICollaborativeRichTextRef} from "@/components/tiptap/CollaborativeRichText.tsx";

export interface INoteEditorContext {
    workspace?: WorkspaceDto;
    note?: NoteDto;
    notebookId?: string;
    /**
     * The note's notebook. Server truth once the note exists, falling back to the notebook the url
     * points at, so a draft still being created renders in the notebook it was started in rather
     * than looking like a workspace-level draft. Undefined ⇒ genuinely not in a notebook.
     */
    notebook?: NotebookDto;
    noteId?: string;
    /** Local doc identity, the original draft id for draft-born notes (see selectDocKey). */
    docKey?: string;
    isPendingCreate?: boolean;
    doc?: Y.Doc | null;
    status?: LiveTextStatus;
    flushNow?: () => void;
    registerEditor?: (editor: ICollaborativeRichTextRef | null) => void;
}

export const NoteEditorContext = createContext<INoteEditorContext>({});

export function useNoteEditorContext() {
    return useContext(NoteEditorContext);
}
