import {useEffect, useRef, useState} from "react";
import type {NoteDto, WorkspaceDto} from "@/be/jinear-core.ts";
import {useInitializeNoteMutation} from "@/api/noteOperationApi.ts";
import {DRAFTS_NOTEBOOK_ID, EMPTY_YDOC_STATE, POLL_INTERVAL_MS} from "@/components/tiptap/crdt/constants.ts";
import {useAppDispatch, useTypedSelector} from "@/store";
import {addDocKeyAlias, removePendingDraft, selectPendingDraft} from "@/slice/noteDraftsSlice.ts";
import Logger from "@/util/logger.ts";

const logger = Logger("useDraftNoteCreate");

interface IUseDraftNoteCreateProps {
    workspace: WorkspaceDto;
    urlNoteId: string;
    /** Current in-doc title, read at send time — best effort; the title mirror corrects divergence post-ack. */
    getTitle: () => string;
}

/**
 * The one place that knows a URL id can be a local, not-yet-created draft. While a pending entry
 * exists for urlNoteId it (re)tries the create until the server acks, then canonicalizes the URL
 * and hands the created note back. The note's id never changes client-side — no promotion concept.
 */
export const useDraftNoteCreate = ({workspace, urlNoteId, getTitle}: IUseDraftNoteCreateProps) => {
    const dispatch = useAppDispatch();
    const pendingDraft = useTypedSelector(selectPendingDraft(urlNoteId));
    const [ackedNote, setAckedNote] = useState<NoteDto>();
    const [initializeNote] = useInitializeNoteMutation();
    const inFlightRef = useRef(false);
    const getTitleRef = useRef(getTitle);
    useEffect(() => {
        getTitleRef.current = getTitle;
    });

    const username = workspace.username;

    useEffect(() => {
        if (!pendingDraft) return;
        const {workspaceId} = pendingDraft;

        const attempt = async () => {
            if (inFlightRef.current) return;
            inFlightRef.current = true;
            try {
                const response = await initializeNote({
                    workspaceId,
                    title: getTitleRef.current(),
                    bodyState: EMPTY_YDOC_STATE
                }).unwrap();
                const note = response?.data;
                if (!note) return;
                setAckedNote(note);
                dispatch(addDocKeyAlias({noteId: note.noteId, draftId: urlNoteId}));
                const notebookId = note.notebookId ?? DRAFTS_NOTEBOOK_ID;
                window.history.replaceState(null, "", `/${username}/notebook/${notebookId}/note/${note.noteId}`);
                dispatch(removePendingDraft({draftId: urlNoteId}));
            } catch (error) {
                // Network failures and 5xx retry on the interval (create is transactional server-side,
                // a failed attempt leaves no rows). A 4xx can never succeed by retrying — drop the draft.
                const status = (error as { status?: number | string })?.status;
                logger.error({message: "Note create attempt failed", status, error});
                if (typeof status === "number" && status >= 400 && status < 500) {
                    dispatch(removePendingDraft({draftId: urlNoteId}));
                }
            } finally {
                inFlightRef.current = false;
            }
        };

        void attempt();
        const interval = setInterval(attempt, POLL_INTERVAL_MS);
        return () => clearInterval(interval);
    }, [pendingDraft, urlNoteId, username, initializeNote, dispatch]);

    return {isPendingCreate: !!pendingDraft, ackedNote};
};
