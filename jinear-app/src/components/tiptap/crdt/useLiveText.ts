import {useCallback, useEffect, useRef, useState} from "react";
import * as Y from "yjs";
import {
    richTextSyncApi,
    useAppendRichTextUpdateMutation,
    useLazyGetRichTextStateQuery,
    useLazyGetRichTextUpdatesQuery
} from "@/api/richTextSyncApi.ts";
import type {RichTextDto} from "@/be/jinear-core.ts";
import {fromBase64, toBase64} from "@/components/tiptap/crdt/base64.ts";
import {DRAFT_ID_PREFIX, POLL_INTERVAL_MS, REMOTE_ORIGIN} from "@/components/tiptap/crdt/constants.ts";
import Logger from "@/util/logger.ts";
import {IndexeddbPersistence} from "y-indexeddb";
import {useAppDispatch} from "@/store";

export type LiveTextStatus = "booting" | "saving" | "saved_locally" | "syncing" | "synced" | "error";

interface IUseLiveTextProps {
    richTextId: string;
    initialRichText?: RichTextDto;
}

const logger = Logger('useLiveText');

export const useLiveText = ({richTextId, initialRichText}: IUseLiveTextProps) => {
    const dispatch = useAppDispatch();
    const [doc, setDoc] = useState<Y.Doc | null>(null);
    const [status, setStatus] = useState<LiveTextStatus>("booting");

    const isUnsubmittedDraft = richTextId?.indexOf(DRAFT_ID_PREFIX) != -1;
    const isReady = isUnsubmittedDraft || !!initialRichText;

    const docRef = useRef<Y.Doc | null>(null);
    // Shadow doc: holds ONLY bytes we've confirmed the server has
    // (boot snapshot + every fetched delta + our own acked flushes).
    // Its state vector IS the upload cursor — always exact.
    const serverDocRef = useRef<Y.Doc | null>(null);

    const persistenceRef = useRef<IndexeddbPersistence | null>(null);
    const isDirtyRef = useRef<boolean>(false);

    const fetchIntervalRef = useRef<number>(undefined);

    const inFlightRef = useRef(false);
    // as download cursor
    const lastFetchedUpdateSeq = useRef<number>(0);

    logger.log({doc, richTextId, initialRichText, isUnsubmittedDraft, isReady});

    const onUpdate = (update: Uint8Array, origin: unknown) => {
        if (origin === REMOTE_ORIGIN || origin === persistenceRef.current) return;
        logger.log({onUpdate: origin})
        isDirtyRef.current = true;
    };

    const flush = async () => {
        const doc = docRef.current;
        const serverDoc = serverDocRef.current;
        if (!doc || !serverDoc || !isDirtyRef.current || inFlightRef.current) {
            logger.log({
                message: "Skipping flush",
                doc,
                serverDoc,
                isDirtyRef: isDirtyRef.current,
                inFlightRef: inFlightRef.current
            });
            return;
        }

        inFlightRef.current = true;

        // Diff live doc against exactly what the server has → only unsent bytes.
        const update = Y.encodeStateAsUpdate(doc, Y.encodeStateVector(serverDoc));
        isDirtyRef.current = false;
        try {
            setStatus("syncing");
            await dispatch(richTextSyncApi.endpoints.appendRichTextUpdate.initiate({
                richTextId,
                update: toBase64(update)
            })).unwrap();
            // Server now has it → fold into shadow so future diffs exclude it.
            Y.applyUpdate(serverDoc, update);
            setStatus("synced");
        } catch (e) {
            logger.error({e});
            isDirtyRef.current = true;
            setStatus("error");
        } finally {
            inFlightRef.current = false;
        }
    }

    const fetchUpdates = async (richTextId: string, since: number) => {
        const doc = docRef.current;
        const serverDoc = serverDocRef.current;
        if (!doc || !serverDoc) return;

        const result = await dispatch(richTextSyncApi.endpoints.getRichTextUpdates.initiate({
            richTextId,
            since
        }, {forceRefetch: true}));
        if (!result.data) return;

        const {headSeq, updates} = result.data;
        updates.forEach((u) => {
            const bytes = fromBase64(u);
            Y.applyUpdate(doc, bytes, REMOTE_ORIGIN)
            Y.applyUpdate(serverDoc, bytes);
        });
        lastFetchedUpdateSeq.current = headSeq;
    }

    const tick = async () => {
        if (inFlightRef.current) return;
        await fetchUpdates(richTextId, lastFetchedUpdateSeq.current);
        await flush();
    };

    const getState = useCallback((): string | null => {
        const d = docRef.current;
        if (!d) return null;
        return toBase64(Y.encodeStateAsUpdate(d));
    }, []);

    useEffect(() => {
        if (!isReady) return;
        const liveDoc = new Y.Doc();
        const persistence = new IndexeddbPersistence(`doc:${richTextId}`, liveDoc);
        const serverDoc = new Y.Doc();

        persistenceRef.current = persistence;

        if (initialRichText) {
            const {yjsState, yjsStateSeq, updateSeq} = initialRichText;
            const snapshot = fromBase64(yjsState);
            Y.applyUpdate(liveDoc, snapshot, REMOTE_ORIGIN);
            Y.applyUpdate(serverDoc, snapshot); // shadow starts at server snapshot
            lastFetchedUpdateSeq.current = yjsStateSeq;
        }

        docRef.current = liveDoc;
        serverDocRef.current = serverDoc;
        setDoc(liveDoc);

        liveDoc.on("update", onUpdate);
        persistence.whenSynced.then(() => {
            const d = docRef.current;
            const s = serverDocRef.current;
            if (!d || !s) return;
            const diff = Y.encodeStateAsUpdate(d, Y.encodeStateVector(s));
            if (diff.length > 2) isDirtyRef.current = true; // empty update ≈ 2 bytes
            setStatus("saved_locally");
        });

        if (!isUnsubmittedDraft) {
            fetchIntervalRef.current = setInterval(tick, POLL_INTERVAL_MS);
        }

        return (() => {
            try {
                persistence.destroy();
                liveDoc.destroy();
                serverDoc.destroy();
                persistenceRef.current = null;
                docRef.current = null;
                serverDocRef.current = null;
                if (fetchIntervalRef.current != null) {
                    clearInterval(fetchIntervalRef.current)
                }
            } catch (e) {
                console.error(e);
            }
        })
    }, [richTextId, isReady]);

    return {doc, status, getState};
}