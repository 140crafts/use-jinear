import {useRef} from "react";
import type * as Y from "yjs";
import type {NoteDto} from "@/be/jinear-core.ts";
import {useUpdateNoteMutation} from "@/api/noteOperationApi.ts";
import {PUSH_DEBOUNCE_MS, TITLE_FIELD} from "@/components/tiptap/crdt/constants.ts";
import {useYText} from "@/components/tiptap/crdt/useYText.ts";
import {useDebouncedEffect} from "@/hooks/useDebouncedEffect.ts";
import {useAppDispatch} from "@/store";
import {setPendingDraftTitle} from "@/slice/noteDraftsSlice.ts";

interface IUseNoteTitleMirrorProps {
    doc: Y.Doc | null;
    /** Gate: until the doc has hydrated its title reads "", which must never be mirrored outwards. */
    hydrated: boolean;
    note?: NoteDto;
    workspaceId?: string;
    /** Set while the note is a pending local draft, title mirrors into its slice entry instead. */
    pendingDraftId?: string;
}

/**
 * Mirrors the in-doc title into the note.title column, the projection lists, filters and search
 * read. The doc is the source of truth; this is display denormalization, the column's only writer.
 *
 * The flow is one-way by design. Nothing seeds the column back into the doc: doing so reintroduces
 * the same value from two directions, and Yjs merges two independent inserts of one string rather
 * than deduping them, which is what turned "lorem ipsum" into "lorem ipsumlorem ipsum".
 */
export const useNoteTitleMirror = ({doc, hydrated, note, workspaceId, pendingDraftId}: IUseNoteTitleMirrorProps) => {
    const dispatch = useAppDispatch();
    const [titleFromDoc] = useYText(doc, TITLE_FIELD);
    const [updateNote] = useUpdateNoteMutation();
    const lastSentRef = useRef<string>(undefined);

    useDebouncedEffect(() => {
        // Pre-hydration the doc is empty, so titleFromDoc is "" for every note. Publishing that
        // would blank the column, and nothing restores it until the user next edits the title.
        if (!hydrated) return;
        if (!note && pendingDraftId) {
            dispatch(setPendingDraftTitle({draftId: pendingDraftId, title: titleFromDoc}));
            return;
        }
        if (!note || !workspaceId) return;
        if (titleFromDoc === (note.title ?? "")) return;
        if (titleFromDoc === lastSentRef.current) return;
        lastSentRef.current = titleFromDoc;
        updateNote({noteId: note.noteId, title: titleFromDoc, workspaceId});
    }, [titleFromDoc, hydrated, note, workspaceId, pendingDraftId], PUSH_DEBOUNCE_MS);
};
