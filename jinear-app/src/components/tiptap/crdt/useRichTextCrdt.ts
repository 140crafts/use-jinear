import type {RichTextFormat, RichTextStateDto} from "@/model/be/jinear-core";
import {store} from "@/store";
import {
    appendRichTextUpdate,
    getRichTextState,
    getRichTextUpdates,
    seedRichText,
    snapshotRichText
} from "@/store/api/richTextSyncApi";
import Logger from "@/util/logger";
import {useEffect, useRef, useState} from "react";
import * as Y from "yjs";
import {fromBase64, toBase64} from "./base64";
import {
    POLL_INTERVAL_MS,
    PUSH_DEBOUNCE_MS,
    PUSH_MAX_INTERVAL_MS,
    REMOTE_ORIGIN,
    SNAPSHOT_UPDATE_THRESHOLD
} from "./constants";
import {buildSeedDoc} from "./seedLegacy";

const logger = Logger("useRichTextCrdt");

export type RichTextCrdtStatus = "connecting" | "ready" | "error";

interface UseRichTextCrdtOptions {
    format: RichTextFormat;
    /** Existing LEGACY HTML value — used to seed the Yjs doc on the first CRDT promotion. */
    legacyHtml?: string | null;
    /** When false, the hook stays inert (no doc, no network). Defaults to true. */
    enabled?: boolean;
}

export interface RichTextCrdtHandle {
    doc: Y.Doc | null;
    status: RichTextCrdtStatus;
    /** Editors call this on every change so the latest HTML can ride along with the next settle flush. */
    provideHtml: (html: string) => void;
    /** Force an immediate settle flush (HTML included) — call on blur / before close. */
    flushNow: () => void;
}

/**
 * Custom Yjs provider over the polling relay (no WebSocket). Bootstraps from `/state` + `/updates`,
 * pushes debounced local deltas, polls for remote deltas, and compacts via `/snapshot`. All CRDT
 * logic is client-side; the backend only stores and orders opaque blobs.
 *
 * Without a `richTextId` the hook runs in draft mode: a fresh local doc, no network. The caller
 * persists the draft via {@link encodeDocState} on whatever create endpoint owns the document,
 * then re-renders with the real `richTextId` (or navigates to a route that has it).
 */
export const useRichTextCrdt = (
    richTextId: string | null | undefined,
    {format, legacyHtml, enabled = true}: UseRichTextCrdtOptions
): RichTextCrdtHandle => {
    const [doc, setDoc] = useState<Y.Doc | null>(null);
    const [status, setStatus] = useState<RichTextCrdtStatus>("connecting");

    const docRef = useRef<Y.Doc | null>(null);
    const cursorRef = useRef<number>(0);
    const headSeqRef = useRef<number>(0);
    const snapshotSeqRef = useRef<number>(0);
    const bufferRef = useRef<Uint8Array[]>([]);
    const latestHtmlRef = useRef<string | null>(null);
    const editedRef = useRef<boolean>(false);

    const pushDebounceTimer = useRef<ReturnType<typeof setTimeout> | null>(null);
    const pushCeilingTimer = useRef<ReturnType<typeof setTimeout> | null>(null);
    const pollTimer = useRef<ReturnType<typeof setInterval> | null>(null);
    const flushingRef = useRef<boolean>(false);
    const pollingRef = useRef<boolean>(false);
    const snapshotInFlightRef = useRef<boolean>(false);
    const cancelledRef = useRef<boolean>(false);

    // Bridges the per-effect `flush` closure out to the stable `flushNow` handle.
    const flushRef = useRef<((includeHtml: boolean) => Promise<void>) | null>(null);

    useEffect(() => {
        if (!enabled) {
            return;
        }
        if (!richTextId) {
            const liveDoc = new Y.Doc();
            docRef.current = liveDoc;
            editedRef.current = false;
            setDoc(liveDoc);
            setStatus("ready");
            return () => {
                liveDoc.destroy();
                if (docRef.current === liveDoc) docRef.current = null;
                setDoc(null);
                setStatus("connecting");
            };
        }
        cancelledRef.current = false;
        bufferRef.current = [];

        const dispatch = store.dispatch;

        const queryState = (): Promise<RichTextStateDto> =>
            dispatch(getRichTextState.initiate({richTextId}, {subscribe: false, forceRefetch: true})).unwrap();

        const queryUpdates = (since: number) =>
            dispatch(getRichTextUpdates.initiate({richTextId, since}, {subscribe: false, forceRefetch: true})).unwrap();

        const mutateAppend = async (update: string, html: string | null) => {
            const req = dispatch(appendRichTextUpdate.initiate({richTextId, update, html}));
            try {
                return await req.unwrap();
            } finally {
                req.reset();
            }
        };

        const mutateSeed = async (state: string) => {
            const req = dispatch(seedRichText.initiate({richTextId, state}));
            try {
                return await req.unwrap();
            } finally {
                req.reset();
            }
        };

        const mutateSnapshot = async (state: string, upToSeq: number) => {
            const req = dispatch(snapshotRichText.initiate({richTextId, state, upToSeq}));
            try {
                await req.unwrap();
            } finally {
                req.reset();
            }
        };

        const clearPushTimers = () => {
            if (pushDebounceTimer.current) {
                clearTimeout(pushDebounceTimer.current);
                pushDebounceTimer.current = null;
            }
            if (pushCeilingTimer.current) {
                clearTimeout(pushCeilingTimer.current);
                pushCeilingTimer.current = null;
            }
        };

        const compactIfNeeded = async (force: boolean) => {
            if (snapshotInFlightRef.current || !docRef.current) return;
            const target = cursorRef.current;
            if (target <= snapshotSeqRef.current) return;
            if (!force && headSeqRef.current - snapshotSeqRef.current <= SNAPSHOT_UPDATE_THRESHOLD) return;
            snapshotInFlightRef.current = true;
            try {
                const state = toBase64(Y.encodeStateAsUpdate(docRef.current));
                await mutateSnapshot(state, target);
                snapshotSeqRef.current = target;
            } catch (error) {
                logger.warn({message: "Snapshot failed", richTextId, error});
            } finally {
                snapshotInFlightRef.current = false;
            }
        };

        function scheduleFlush() {
            if (pushDebounceTimer.current) clearTimeout(pushDebounceTimer.current);
            pushDebounceTimer.current = setTimeout(() => void flush(true), PUSH_DEBOUNCE_MS);
            if (!pushCeilingTimer.current) {
                pushCeilingTimer.current = setTimeout(() => void flush(false), PUSH_MAX_INTERVAL_MS);
            }
        }

        // includeHtml: true on a "settle" flush (debounce/blur/close), false on the mid-typing ceiling flush.
        async function flush(includeHtml: boolean) {
            clearPushTimers();
            if (flushingRef.current) {
                scheduleFlush();
                return;
            }
            if (bufferRef.current.length === 0) return;
            const merged = Y.mergeUpdates(bufferRef.current);
            bufferRef.current = [];
            flushingRef.current = true;
            try {
                const html = includeHtml ? latestHtmlRef.current : null;
                const result = await mutateAppend(toBase64(merged), html);
                cursorRef.current = Math.max(cursorRef.current, result.seq);
                headSeqRef.current = Math.max(headSeqRef.current, result.seq);
                await compactIfNeeded(false);
            } catch (error) {
                // Offline / failed POST: requeue the merged blob and retry. CRDT merge makes replay safe.
                logger.warn({message: "Append failed, requeueing", richTextId, error});
                bufferRef.current = [merged, ...bufferRef.current];
                scheduleFlush();
            } finally {
                flushingRef.current = false;
            }
        }

        flushRef.current = flush;

        const onLocalUpdate = (update: Uint8Array, origin: unknown) => {
            if (origin === REMOTE_ORIGIN) return;
            editedRef.current = true;
            bufferRef.current.push(update);
            scheduleFlush();
        };

        const poll = async () => {
            if (cancelledRef.current || pollingRef.current || !docRef.current) return;
            if (typeof document !== "undefined" && document.hidden) return;
            pollingRef.current = true;
            try {
                const res = await queryUpdates(cursorRef.current);
                res.updates.forEach((u) => Y.applyUpdate(docRef.current!, fromBase64(u), REMOTE_ORIGIN));
                cursorRef.current = Math.max(cursorRef.current, res.headSeq);
                headSeqRef.current = Math.max(headSeqRef.current, res.headSeq);
            } catch (error) {
                logger.warn({message: "Poll failed", richTextId, error});
            } finally {
                pollingRef.current = false;
            }
        };

        const bootstrap = async () => {
            setStatus("connecting");
            try {
                const state = await queryState();
                let snapshotB64 = state.snapshot;
                let snapshotSeq = state.snapshotSeq;
                let headSeq = state.headSeq;

                if (format === "LEGACY" && state.format === "LEGACY") {
                    // Promote LEGACY -> CRDT. The seed response is authoritative for both the win and the
                    // loser-adopts-winner case: we always rebuild the live doc from the returned state.
                    const seedDoc = buildSeedDoc(legacyHtml);
                    const seeded = await mutateSeed(toBase64(Y.encodeStateAsUpdate(seedDoc)));
                    seedDoc.destroy();
                    snapshotB64 = seeded.snapshot;
                    snapshotSeq = seeded.snapshotSeq;
                    headSeq = seeded.headSeq;
                }

                if (cancelledRef.current) return;

                const liveDoc = new Y.Doc();
                if (snapshotB64) {
                    Y.applyUpdate(liveDoc, fromBase64(snapshotB64), REMOTE_ORIGIN);
                }
                let cursor = snapshotSeq;
                if (headSeq > cursor) {
                    const updates = await queryUpdates(cursor);
                    updates.updates.forEach((u) => Y.applyUpdate(liveDoc, fromBase64(u), REMOTE_ORIGIN));
                    cursor = updates.headSeq;
                    headSeq = updates.headSeq;
                }

                if (cancelledRef.current) {
                    liveDoc.destroy();
                    return;
                }

                docRef.current = liveDoc;
                cursorRef.current = cursor;
                headSeqRef.current = Math.max(headSeq, cursor);
                snapshotSeqRef.current = snapshotSeq;
                editedRef.current = false;
                liveDoc.on("update", onLocalUpdate);

                setDoc(liveDoc);
                setStatus("ready");
                pollTimer.current = setInterval(() => void poll(), POLL_INTERVAL_MS);
            } catch (error) {
                logger.error({message: "CRDT bootstrap failed", richTextId, error});
                if (!cancelledRef.current) setStatus("error");
            }
        };

        void bootstrap();

        return () => {
            cancelledRef.current = true;
            flushRef.current = null;
            if (pollTimer.current) {
                clearInterval(pollTimer.current);
                pollTimer.current = null;
            }
            clearPushTimers();
            const liveDoc = docRef.current;
            liveDoc?.off("update", onLocalUpdate);
            // Best-effort drain: flush buffered edits, force a final compaction, then tear down.
            void (async () => {
                try {
                    await flush(true);
                    if (editedRef.current) {
                        await compactIfNeeded(true);
                    }
                } finally {
                    liveDoc?.destroy();
                    if (docRef.current === liveDoc) docRef.current = null;
                }
            })();
            setDoc(null);
            setStatus("connecting");
        };
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [richTextId, enabled, format]);

    const provideHtml = (html: string) => {
        latestHtmlRef.current = html;
    };

    const flushNow = () => {
        void flushRef.current?.(true);
    };

    return {doc, status, provideHtml, flushNow};
};
