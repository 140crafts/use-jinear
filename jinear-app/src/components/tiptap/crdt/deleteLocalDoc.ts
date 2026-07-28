import {clearDocument} from "y-indexeddb";
import {DOC_DB_PREFIX} from "@/components/tiptap/crdt/constants.ts";
import Logger from "@/util/logger.ts";

const logger = Logger("deleteLocalDoc");

/**
 * Deletes the y-indexeddb database backing a doc. Safe while a live
 * IndexeddbPersistence is open: connections self-close on versionchange
 * (lib0 openDB), so the delete completes without blocking.
 */
export const deleteLocalDoc = (docKey: string): Promise<void> =>
    clearDocument(`${DOC_DB_PREFIX}${docKey}`)
        .then(() => undefined)
        .catch((error: unknown) => {
            logger.error({message: "Local doc delete failed", docKey, error});
        });
