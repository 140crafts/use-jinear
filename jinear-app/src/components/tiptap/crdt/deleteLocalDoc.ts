import {clearDocument} from "y-indexeddb";
import Logger from "@/util/logger.ts";

const logger = Logger("deleteLocalDoc");

/**
 * Deletes the y-indexeddb database backing a doc. Safe while a live
 * IndexeddbPersistence is open: connections self-close on versionchange
 * (lib0 openDB), so the delete completes without blocking.
 */
export const deleteLocalDoc = (docKey: string): Promise<void> =>
    clearDocument(`doc:${docKey}`)
        .then(() => undefined)
        .catch((error: unknown) => {
            logger.error({message: "Local doc delete failed", docKey, error});
        });
