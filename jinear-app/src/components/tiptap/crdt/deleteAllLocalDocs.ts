import {clearDocument} from "y-indexeddb";
import {DOC_DB_PREFIX} from "@/components/tiptap/crdt/constants.ts";
import Logger from "@/util/logger.ts";

const logger = Logger("deleteAllLocalDocs");

/**
 * Every `doc:*` database currently on disk, or null where indexedDB.databases() isn't
 * available (Baseline 2024, so older Firefox/Safari). Null rather than an empty array so
 * the caller can tell "nothing to delete" apart from "can't enumerate".
 */
const enumerateDocDbNames = async (): Promise<string[] | null> => {
    if (typeof indexedDB === "undefined" || typeof indexedDB.databases !== "function") return null;
    try {
        const databases = await indexedDB.databases();
        return databases
            .map(({name}) => name)
            .filter((name): name is string => !!name && name.startsWith(DOC_DB_PREFIX));
    } catch (error: unknown) {
        logger.error({message: "Enumerating indexedDB databases failed", error});
        return null;
    }
};

/**
 * Deletes the local doc database of EVERY note, the whole `doc:*` family. Used on logout and
 * account deletion so note content typed on this device doesn't outlive the session that made it.
 *
 * Enumeration is the source of truth: it also catches orphans no redux state remembers (notes
 * deleted on another device, drafts from an older install). `knownDocKeys` is unioned in as a
 * fallback for engines without indexedDB.databases(), where it's the only thing we have.
 *
 * Safe while editors are still mounted: lib0's openDB closes connections on versionchange, so a
 * live IndexeddbPersistence never blocks the delete (same reasoning as deleteLocalDoc).
 * Never throws; a failed cleanup must not strand the user in a half-logged-out state.
 */
export const deleteAllLocalDocs = async (knownDocKeys: string[] = []): Promise<void> => {
    const enumerated = await enumerateDocDbNames();
    const names = new Set([
        ...(enumerated ?? []),
        ...knownDocKeys.map((docKey) => `${DOC_DB_PREFIX}${docKey}`)
    ]);
    if (names.size == 0) return;

    const results = await Promise.allSettled([...names].map((name) => clearDocument(name)));
    const failed = results.filter((result) => result.status == "rejected");
    if (failed.length != 0) {
        logger.error({message: "Local doc wipe partially failed", total: names.size, failed});
        return;
    }
    logger.log({message: "Local docs deleted", total: names.size, enumerable: enumerated != null});
};
