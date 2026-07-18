import {useCallback, useEffect, useRef, useState} from "react";
import * as Y from "yjs";
import {richTextSyncApi} from "@/api/richTextSyncApi.ts";
import type {RichTextDto} from "@/be/jinear-core.ts";
import {fromBase64, toBase64} from "@/components/tiptap/crdt/base64.ts";
import {
    POLL_INTERVAL_MS,
    REMOTE_ORIGIN,
    SNAPSHOT_RETRY_COOLDOWN_MS,
    SNAPSHOT_UPDATE_THRESHOLD,
    TITLE_FIELD
} from "@/components/tiptap/crdt/constants.ts";
import Logger from "@/util/logger.ts";
import {IndexeddbPersistence} from "y-indexeddb";
import {useAppDispatch} from "@/store";

export type LiveTextStatus = "booting" | "saved_locally" | "syncing" | "synced" | "error";

interface IUseLiveTextProps {
    /** Stable local identity of the doc — the IndexedDB key. Never changes for the life of a note. */
    docKey: string;
    /** Gates doc creation so junk IndexedDB entries aren't created for URLs that resolve to nothing. */
    enabled: boolean;
    /** Server identity of the body. Undefined while the note isn't created server-side — no syncing then. */
    richTextId?: string;
    /** Server baseline snapshot for richTextId. Applied idempotently to both docs when syncing starts. */
    initialRichText?: RichTextDto;
    /** Seed for the in-doc title when the server baseline has none (notes born before title-in-doc). */
    seedTitle?: string;
    getHtml?: () => string | null;
}

const logger = Logger('useLiveText');

export const useLiveText = ({docKey, enabled, richTextId, initialRichText, seedTitle, getHtml}: IUseLiveTextProps) => {
    const dispatch = useAppDispatch();
    const [doc, setDoc] = useState<Y.Doc | null>(null);
    const [status, setStatus] = useState<LiveTextStatus>("booting");

    // Mirrored each render so async work always reads the latest values without re-running effects.
    const getHtmlRef = useRef(getHtml);
    const initialRichTextRef = useRef(initialRichText);
    const seedTitleRef = useRef(seedTitle);
    useEffect(() => {
        getHtmlRef.current = getHtml;
        initialRichTextRef.current = initialRichText;
        seedTitleRef.current = seedTitle;
    });

    const docRef = useRef<Y.Doc | null>(null);
    /*
    * Shadow doc: holds ONLY bytes we've confirmed the server has (boot snapshot + every fetched delta + our own acked flushes).
    * Its state vector IS the upload cursor — always exact.
    * */
    const serverDocRef = useRef<Y.Doc | null>(null);

    const persistenceRef = useRef<IndexeddbPersistence | null>(null);
    const hydratedRef = useRef(false);
    const isDirtyRef = useRef<boolean>(false);
    const inFlightRef = useRef(false);
    const lastFetchedUpdateSeq = useRef<number>(0);
    const flushNowRef = useRef<(() => void) | null>(null);

    // Snapshot compaction bookkeeping: seq of the last known server snapshot, so we can tell
    // how far the update log has grown past it.
    const snapshotBaselineSeq = useRef<number>(0);
    const snapshotInFlightRef = useRef(false);
    const snapshotCooldownUntil = useRef<number>(0);

    /** Settle-flush (e.g. editor blur). No-op while there's no server side to flush to. */
    const flushNow = useCallback(() => {
        flushNowRef.current?.();
    }, []);

    // ── DOC effect: purely local. Live doc + IndexedDB persistence for docKey — no server knowledge.
    useEffect(() => {
        if (!enabled) return;

        const liveDoc = new Y.Doc();
        const persistence = new IndexeddbPersistence(`doc:${docKey}`, liveDoc);
        persistenceRef.current = persistence;

        const onUpdate = (_update: Uint8Array, origin: unknown) => {
            if (origin === REMOTE_ORIGIN || origin === persistence) return;
            isDirtyRef.current = true;
            setStatus("saved_locally");
        };

        docRef.current = liveDoc;
        setDoc(liveDoc);
        liveDoc.on("update", onUpdate);

        persistence.whenSynced.then(() => {
            if (docRef.current !== liveDoc) return;   // stale mount
            hydratedRef.current = true;
            // Hydrated updates carry origin === persistence, so the dirty listener skipped them.
            // Diff against the server-confirmed shadow doc to catch unflushed local edits
            // (offline typing + reload) — without this they'd never upload again.
            const serverDoc = serverDocRef.current;
            const base = serverDoc ? Y.encodeStateVector(serverDoc) : Y.encodeStateVector(new Y.Doc());
            const diff = Y.encodeStateAsUpdate(liveDoc, base);
            if (diff.length > 2) {
                isDirtyRef.current = true;
                setStatus("saved_locally");
            } else {
                setStatus(serverDoc ? "synced" : "saved_locally");
            }
        });

        return () => {
            liveDoc.off("update", onUpdate);
            persistence.destroy();
            liveDoc.destroy();
            persistenceRef.current = null;
            hydratedRef.current = false;
            docRef.current = null;
            setDoc(null);
            setStatus("booting");
        };
    }, [docKey, enabled]);

    // ── SYNC effect: keyed on richTextId. No richTextId → local-only draft, nothing to do.
    //    Applies the server baseline to both docs (CRDT merge — idempotent), arms the initial
    //    delta (drafts / offline edits from IndexedDB), and runs the only poll interval.
    useEffect(() => {
        const liveDoc = doc;
        if (!richTextId || !liveDoc) return;

        const serverDoc = new Y.Doc();
        serverDocRef.current = serverDoc;
        const baseline = initialRichTextRef.current;
        if (baseline?.yjsState) {
            const baselineBytes = fromBase64(baseline.yjsState);
            Y.applyUpdate(liveDoc, baselineBytes, REMOTE_ORIGIN);
            Y.applyUpdate(serverDoc, baselineBytes);
            lastFetchedUpdateSeq.current = baseline.yjsStateSeq;
        } else {
            lastFetchedUpdateSeq.current = 0;
        }
        snapshotBaselineSeq.current = lastFetchedUpdateSeq.current;

        const armDelta = () => {
            const delta = Y.encodeStateAsUpdate(liveDoc, Y.encodeStateVector(serverDoc));
            if (delta.length > 2) isDirtyRef.current = true;
        };
        armDelta();
        // Hydration already done and nothing local-only (e.g. draft promoted after boot) → in sync.
        if (!isDirtyRef.current && hydratedRef.current) setStatus("synced");

        let cancelled = false;

        const fetchUpdates = async (since: number) => {
            // Every distinct `since` is a new RTK cache entry; release the subscription so the
            // entry is dropped (keepUnusedDataFor: 0) instead of accumulating for the session.
            const subscription = dispatch(
                richTextSyncApi.endpoints.getRichTextUpdates.initiate(
                    {richTextId, since}, {forceRefetch: true}
                )
            );
            try {
                const result = await subscription;
                if (cancelled || !result.data) return;
                const {headSeq, updates} = result.data;
                updates.forEach((u) => {
                    const bytes = fromBase64(u);
                    Y.applyUpdate(liveDoc, bytes, REMOTE_ORIGIN);
                    Y.applyUpdate(serverDoc, bytes);
                });
                lastFetchedUpdateSeq.current = headSeq;
            } finally {
                subscription.unsubscribe();
            }
        };

        const flush = async () => {
            if (!isDirtyRef.current || inFlightRef.current) return;
            inFlightRef.current = true;
            const update = Y.encodeStateAsUpdate(liveDoc, Y.encodeStateVector(serverDoc));
            const html = getHtmlRef.current?.() ?? null;   // capture BEFORE await, matches `update`
            isDirtyRef.current = false;
            // Manually dispatched mutations keep their result in the store until reset().
            const mutation = dispatch(
                richTextSyncApi.endpoints.appendRichTextUpdate.initiate(
                    {
                        richTextId,
                        update: toBase64(update),
                        html
                    }
                )
            );
            try {
                setStatus("syncing");
                await mutation.unwrap();
                if (!cancelled) Y.applyUpdate(serverDoc, update);
                // A keystroke may have landed while the append was in flight — don't claim synced.
                setStatus(isDirtyRef.current ? "saved_locally" : "synced");
            } catch (e) {
                logger.error({e});
                isDirtyRef.current = true;
                setStatus("error");
            } finally {
                mutation.reset();
                inFlightRef.current = false;
            }
        };

        /*
        * Compaction: fold the update log into a fresh yjsState snapshot once it grows past the
        * threshold, letting the server prune superseded rows. Built from serverDoc — confirmed
        * bytes only, so un-acked local edits can never end up in a snapshot without a log row.
        * upToSeq = lastFetchedUpdateSeq: everything ≤ it is guaranteed applied to serverDoc;
        * newer acked appends riding along in the state are fine (updates > upToSeq survive
        * pruning and re-apply idempotently). Concurrent snapshots from other clients are
        * serialized by the server lock.
        * */
        const maybeSnapshot = async () => {
            if (snapshotInFlightRef.current) return;
            if (Date.now() < snapshotCooldownUntil.current) return;
            if (lastFetchedUpdateSeq.current - snapshotBaselineSeq.current <= SNAPSHOT_UPDATE_THRESHOLD) return;
            snapshotInFlightRef.current = true;
            const upToSeq = lastFetchedUpdateSeq.current;
            const state = toBase64(Y.encodeStateAsUpdate(serverDoc));
            const mutation = dispatch(
                richTextSyncApi.endpoints.snapshotRichText.initiate({richTextId, state, upToSeq})
            );
            try {
                await mutation.unwrap();
                snapshotBaselineSeq.current = upToSeq;
            } catch (e) {
                logger.error({message: "Snapshot failed", e});
                snapshotCooldownUntil.current = Date.now() + SNAPSHOT_RETRY_COOLDOWN_MS;
            } finally {
                mutation.reset();
                snapshotInFlightRef.current = false;
            }
        };

        const tick = async () => {
            if (inFlightRef.current) return;
            await fetchUpdates(lastFetchedUpdateSeq.current);
            await flush();
            await maybeSnapshot();
        };

        // Title seeding for notes whose title predates the title-in-doc layer: wait for both the
        // baseline (applied above) and IndexedDB hydration, then seed only if still absent.
        persistenceRef.current?.whenSynced.then(() => {
            const seed = seedTitleRef.current;
            if (cancelled || !seed) return;
            const titleText = liveDoc.getText(TITLE_FIELD);
            if (titleText.length === 0) {
                titleText.insert(0, seed);
                armDelta();
            }
        });

        flushNowRef.current = () => {
            void flush();   // self-guards on dirty + in-flight
        };

        void tick();
        const interval = setInterval(tick, POLL_INTERVAL_MS);
        return () => {
            cancelled = true;
            clearInterval(interval);
            flushNowRef.current = null;
            serverDoc.destroy();
            serverDocRef.current = null;
        };
    }, [richTextId, doc, dispatch]);

    return {doc, status, flushNow};
}
