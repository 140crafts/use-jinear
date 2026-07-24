import React, {useEffect, useRef} from 'react';
import {useDeleteNoteMutation, useInitializeNoteMutation} from "@/api/noteOperationApi.ts";
import {store, useAppDispatch, useTypedSelector} from "@/store";
import {draftSubmitted, markDraftFailed} from "@/slice/noteDraftsSlice.ts";
import {selectCurrentAccountId} from "@/slice/accountSlice";
import {useOnlineStatus} from "@/hooks/useOnlineStatus.ts";
import {EMPTY_YDOC_STATE, POLL_INTERVAL_MS} from "@/components/tiptap/crdt/constants.ts";
import {readLocalDocState} from "@/components/tiptap/crdt/readLocalDocState.ts";
import {deleteLocalDoc} from "@/components/tiptap/crdt/deleteLocalDoc.ts";
import Logger from "@/util/logger.ts";

const logger = Logger("PendingDraftSubmitter");

/**
 * The ONLY place that creates notes on the server. Watches the persisted pending-drafts map and
 * submits each draft (with its locally persisted title + body) whenever the app is online — no
 * matter where in the app the user is, or whether the draft's editor was ever reopened. An open
 * note editor merely reacts to the ack (see useDraftNoteAck) and canonicalizes its URL.
 */
const PendingDraftSubmitter: React.FC = () => {
    const dispatch = useAppDispatch();
    const [initializeNote] = useInitializeNoteMutation();
    const [deleteNote] = useDeleteNoteMutation();
    const pending = useTypedSelector(state => state.noteDrafts.pending);
    const accountId = useTypedSelector(selectCurrentAccountId);
    const online = useOnlineStatus();
    const inFlightRef = useRef(false);

    useEffect(() => {
        // No session → never submit (and never drop: a 401 must not cost the user their drafts).
        if (!accountId || !online || Object.keys(pending).length === 0) return;

        const submitAll = async () => {
            if (inFlightRef.current) return;
            inFlightRef.current = true;
            try {
                for (const entry of Object.values(pending)) {
                    // Marked unretryable on an earlier tick — keep the draft (and its local doc)
                    // for the user to recover, but never re-submit it.
                    if (entry.failedAt) continue;
                    try {
                        const {bodyState, title} = await readLocalDocState(entry.draftId)
                            .catch(() => ({bodyState: EMPTY_YDOC_STATE, title: entry.title ?? ""}));
                        const response = await initializeNote({
                            workspaceId: entry.workspaceId,
                            title,
                            bodyState,
                            // Idempotency key: the dormant BaseRequest.conversationId carries the draft id,
                            // stable across retries and tabs — jinear-core dedupes creates on it via Redis.
                            conversationId: entry.draftId
                        }).unwrap();
                        const noteId = response?.data?.noteId;
                        if (!noteId) continue;
                        // Re-check live state, not the closure snapshot: the user may have discarded
                        // this draft while the create was in flight.
                        const stillPending = !!store.getState().noteDrafts.pending[entry.draftId];
                        if (stillPending) {
                            dispatch(draftSubmitted({draftId: entry.draftId, noteId}));
                        } else {
                            // Discarded mid-flight: delete the ghost server note, and re-clear the local
                            // doc (readLocalDocState above recreated the DB the discard just deleted).
                            logger.log({message: "Draft discarded during submit; deleting created note", draftId: entry.draftId, noteId});
                            void deleteNote({workspaceId: entry.workspaceId, noteId});
                            void deleteLocalDoc(entry.draftId);
                        }
                    } catch (error) {
                        const status = (error as { status?: number | string })?.status;
                        logger.error({message: "Draft submit attempt failed", draftId: entry.draftId, status, error});
                        // Unretryable rejection — except 401 (expired session) and 423 (create lock
                        // contention, e.g. another tab mid-submit), which must keep retrying. Mark the
                        // draft failed instead of deleting it: a transient lie-fi 4xx (e.g. a truncated
                        // body → 400) must never cost the user their content. The local doc is kept so
                        // they can open, read/copy, and discard it deliberately.
                        if (typeof status === "number" && status >= 400 && status < 500 && status !== 401 && status !== 423) {
                            dispatch(markDraftFailed({draftId: entry.draftId, status}));
                        }
                    }
                }
            } finally {
                inFlightRef.current = false;
            }
        };

        void submitAll();
        const interval = setInterval(submitAll, POLL_INTERVAL_MS);
        return () => clearInterval(interval);
    }, [pending, accountId, online, initializeNote, deleteNote, dispatch]);

    return null;
}

export default PendingDraftSubmitter;
