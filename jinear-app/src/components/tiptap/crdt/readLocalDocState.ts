import * as Y from "yjs";
import {IndexeddbPersistence} from "y-indexeddb";
import {toBase64} from "@/components/tiptap/crdt/base64.ts";
import {TITLE_FIELD} from "@/components/tiptap/crdt/constants.ts";

/**
 * Loads a note's locally persisted Yjs doc without mounting an editor — used by
 * PendingDraftSubmitter so a draft submitted in the background carries its full
 * content (title layer + body), not just metadata. An absent/empty doc encodes
 * to the empty state ("AAA="), which the backend accepts as a valid CRDT seed.
 */
export const readLocalDocState = async (docKey: string): Promise<{ bodyState: string; title: string }> => {
    const doc = new Y.Doc();
    const persistence = new IndexeddbPersistence(`doc:${docKey}`, doc);
    try {
        await persistence.whenSynced;
        return {
            bodyState: toBase64(Y.encodeStateAsUpdate(doc)),
            title: doc.getText(TITLE_FIELD).toString()
        };
    } finally {
        persistence.destroy();
        doc.destroy();
    }
};
