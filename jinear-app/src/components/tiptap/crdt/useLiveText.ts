import {useCallback, useEffect, useRef, useState} from "react";
import * as Y from "yjs";
import {richTextSyncApi} from "@/api/richTextSyncApi.ts";
import type {RichTextDto} from "@/be/jinear-core.ts";
import {fromBase64, toBase64} from "@/components/tiptap/crdt/base64.ts";
import {POLL_INTERVAL_MS, REMOTE_ORIGIN, TITLE_FIELD} from "@/components/tiptap/crdt/constants.ts";
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
    const isDirtyRef = useRef<boolean>(false);
    const inFlightRef = useRef(false);
    const lastFetchedUpdateSeq = useRef<number>(0);
    const flushNowRef = useRef<(() => void) | null>(null);

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
        };

        docRef.current = liveDoc;
        setDoc(liveDoc);
        liveDoc.on("update", onUpdate);

        persistence.whenSynced.then(() => setStatus("saved_locally"));

        return () => {
            liveDoc.off("update", onUpdate);
            persistence.destroy();
            liveDoc.destroy();
            persistenceRef.current = null;
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

        const armDelta = () => {
            const delta = Y.encodeStateAsUpdate(liveDoc, Y.encodeStateVector(serverDoc));
            if (delta.length > 2) isDirtyRef.current = true;
        };
        armDelta();

        let cancelled = false;

        const fetchUpdates = async (since: number) => {
            const result = await dispatch(
                richTextSyncApi.endpoints.getRichTextUpdates.initiate(
                    {richTextId, since}, {forceRefetch: true}
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
            const html = getHtmlRef.current?.() ?? null;   // capture BEFORE await, matches `update`
            isDirtyRef.current = false;
            try {
                setStatus("syncing");
                await dispatch(
                    richTextSyncApi.endpoints.appendRichTextUpdate.initiate(
                        {
                            richTextId,
                            update: toBase64(update),
                            html
                        }
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
