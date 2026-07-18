import {useRef} from "react";
import type * as Y from "yjs";
import type {NoteDto} from "@/be/jinear-core.ts";
import {useUpdateNoteMutation} from "@/api/noteOperationApi.ts";
import {PUSH_DEBOUNCE_MS, TITLE_FIELD} from "@/components/tiptap/crdt/constants.ts";
import {useYText} from "@/components/tiptap/crdt/useYText.ts";
import {useDebouncedEffect} from "@/hooks/useDebouncedEffect.ts";

interface IUseNoteTitleMirrorProps {
    doc: Y.Doc | null;
    note?: NoteDto;
    workspaceId?: string;
}

/**
 * Mirrors the in-doc title into the note.title column — the projection lists and filters read.
 * The doc is the source of truth; this is display denormalization, the column's only writer.
 */
export const useNoteTitleMirror = ({doc, note, workspaceId}: IUseNoteTitleMirrorProps) => {
    const [titleFromDoc] = useYText(doc, TITLE_FIELD);
    const [updateNote] = useUpdateNoteMutation();
    const lastSentRef = useRef<string>(undefined);

    useDebouncedEffect(() => {
        if (!note || !workspaceId) return;
        if (titleFromDoc === (note.title ?? "")) return;
        if (titleFromDoc === lastSentRef.current) return;
        lastSentRef.current = titleFromDoc;
        updateNote({noteId: note.noteId, title: titleFromDoc, workspaceId});
    }, [titleFromDoc, note, workspaceId], PUSH_DEBOUNCE_MS);
};
