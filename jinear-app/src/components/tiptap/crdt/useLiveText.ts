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

    const [syncId, setSyncId] = useState<string>(richTextId);  // starts as draft id
    const promotedBaselineRef = useRef<RichTextDto | undefined>(undefined);

    const isUnsubmittedDraft = richTextId?.indexOf(DRAFT_ID_PREFIX) != -1;
    const isReady = isUnsubmittedDraft || !!initialRichText;

    const docRef = useRef<Y.Doc | null>(null);
    /*
    * Shadow doc: holds ONLY bytes we've confirmed the server has (boot snapshot + every fetched delta + our own acked flushes).
    * Its state vector IS the upload cursor — always exact.
    * */
    const serverDocRef = useRef<Y.Doc | null>(null);

    const persistenceRef = useRef<IndexeddbPersistence | null>(null);
    const isDirtyRef = useRef<boolean>(false);
    const inFlightRef = useRef(false);

    const fetchIntervalRef = useRef<number>(undefined);
    const lastFetchedUpdateSeq = useRef<number>(0);

    logger.log({doc, syncId, richTextId, initialRichText, isUnsubmittedDraft, isReady});

    const getState = useCallback((): string | null => {
        const d = docRef.current;
        if (!d) return null;
        return toBase64(Y.encodeStateAsUpdate(d));
    }, []);

    const onPromoted = useCallback(async (realId: string, serverRichText?: RichTextDto) => {
        const liveDoc = docRef.current;
        if (!liveDoc) return;

        // Build the server baseline inline — don't wait for the sync effect.
        const serverDoc = new Y.Doc();
        if (serverRichText) {
            Y.applyUpdate(serverDoc, fromBase64(serverRichText.yjsState));
            lastFetchedUpdateSeq.current = serverRichText.yjsStateSeq;
        }

        // The delta = everything typed since the initializeNote snapshot.
        const delta = Y.encodeStateAsUpdate(liveDoc, Y.encodeStateVector(serverDoc));

        // Ship it NOW, before anyone navigates. Await it.
        if (delta.length > 2) {
            await dispatch(
                richTextSyncApi.endpoints.appendRichTextUpdate.initiate(
                    {richTextId: realId, update: toBase64(delta)}
                )
            ).unwrap();
            Y.applyUpdate(serverDoc, delta);
        }

        serverDocRef.current = serverDoc;
        promotedBaselineRef.current = serverRichText;
        setSyncId(realId);   // now the interval takes over for subsequent edits
    }, [dispatch]);

    // ── DOC effect: mount-stable. Keyed on isReady ONLY, never on richTextId.
    //    Runs once when ready; survives the draft→real promotion untouched.
    useEffect(() => {
        if (!isReady) return;

        const docKey = richTextId;// captured once; stays the draft key
        const liveDoc = new Y.Doc();
        const persistence = new IndexeddbPersistence(`doc:${docKey}`, liveDoc);
        persistenceRef.current = persistence;

        // Present only on a direct real-id mount (opening an existing note).
        if (initialRichText) {
            Y.applyUpdate(liveDoc, fromBase64(initialRichText.yjsState), REMOTE_ORIGIN);
        }

        const onUpdate = (update: Uint8Array, origin: unknown) => {
            if (origin === REMOTE_ORIGIN || origin === persistence) return;
            isDirtyRef.current = true;
        };

        docRef.current = liveDoc;
        setDoc(liveDoc);
        liveDoc.on("update", onUpdate);

        persistence.whenSynced.then(() => {
            const s = serverDocRef.current;
            const base = s ? Y.encodeStateVector(s) : Y.encodeStateVector(new Y.Doc());
            const diff = Y.encodeStateAsUpdate(liveDoc, base);
            if (diff.length > 2) isDirtyRef.current = true;
            setStatus("saved_locally");
        });

        return () => {
            liveDoc.off("update", onUpdate);
            persistence.destroy();
            liveDoc.destroy();
            persistenceRef.current = null;
            docRef.current = null;
        };
        // richTextId intentionally omitted — the doc's identity is the MOUNT,
        // not the id. Depending on it rebuilds the doc on promotion (paragraph loss).
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [isReady]);

    // ── SYNC effect: keyed on syncId. Owns the shadow doc, the delta arm,
    //    and the ONLY poll interval. Methods live here so they close over syncId.
    useEffect(() => {
        if (!isReady) return;
        if (syncId.indexOf(DRAFT_ID_PREFIX) !== -1) return;   // drafts don't sync
        const liveDoc = docRef.current;
        if (!liveDoc) return;

        let serverDoc = serverDocRef.current;
        const freshlyPromoted = !!serverDoc;   // onPromoted already built + flushed
        if (!serverDoc) {
            serverDoc = new Y.Doc();
            const baseline = promotedBaselineRef.current ?? initialRichText;
            if (baseline) {
                Y.applyUpdate(serverDoc, fromBase64(baseline.yjsState));
                lastFetchedUpdateSeq.current = baseline.yjsStateSeq;
            }
            serverDocRef.current = serverDoc;
        }
        if (!freshlyPromoted) {
            const delta = Y.encodeStateAsUpdate(liveDoc, Y.encodeStateVector(serverDoc));
            if (delta.length > 2) isDirtyRef.current = true;
        }

        // Baseline = what the server holds. On promotion, the initialize response;
        // on direct real mount, initialRichText. Fallback to empty is SAFE:
        // a too-empty baseline just re-sends known bytes, which are idempotent.
        const baseline = promotedBaselineRef.current ?? initialRichText;
        if (baseline) {
            Y.applyUpdate(serverDoc, fromBase64(baseline.yjsState));
            lastFetchedUpdateSeq.current = baseline.yjsStateSeq;
        }
        serverDocRef.current = serverDoc;

        // The lie-fi paragraph: liveDoc is ahead of the server baseline.
        const delta = Y.encodeStateAsUpdate(liveDoc, Y.encodeStateVector(serverDoc));
        if (delta.length > 2) isDirtyRef.current = true;

        let cancelled = false;

        const fetchUpdates = async (since: number) => {
            const result = await dispatch(
                richTextSyncApi.endpoints.getRichTextUpdates.initiate(
                    {richTextId: syncId, since}, {forceRefetch: true}
                )
            );
            if (cancelled || !result.data) return;
            const {headSeq, updates} = result.data;
            updates.forEach((u) => {
                const bytes = fromBase64(u);
                Y.applyUpdate(liveDoc, bytes, REMOTE_ORIGIN);
                Y.applyUpdate(serverDoc, bytes);
            });
            lastFetchedUpdateSeq.current = headSeq;
        };

        const flush = async () => {
            if (!isDirtyRef.current || inFlightRef.current) return;
            inFlightRef.current = true;
            const update = Y.encodeStateAsUpdate(liveDoc, Y.encodeStateVector(serverDoc));
            isDirtyRef.current = false;
            try {
                setStatus("syncing");
                await dispatch(
                    richTextSyncApi.endpoints.appendRichTextUpdate.initiate(
                        {richTextId: syncId, update: toBase64(update)}
                    )
                ).unwrap();
                if (!cancelled) Y.applyUpdate(serverDoc, update);
                setStatus("synced");
            } catch (e) {
                logger.error({e});
                isDirtyRef.current = true;
                setStatus("error");
            } finally {
                inFlightRef.current = false;
            }
        };

        const tick = async () => {
            if (inFlightRef.current) return;
            await fetchUpdates(lastFetchedUpdateSeq.current);
            await flush();
        };

        const interval = setInterval(tick, POLL_INTERVAL_MS);
        return () => {
            cancelled = true;
            clearInterval(interval);
            serverDoc.destroy();
            serverDocRef.current = null;
        };
    }, [syncId, isReady, dispatch, initialRichText]);

    return {doc, status, getState, onPromoted};
}