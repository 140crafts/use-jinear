import * as Y from "yjs";
import {toBase64} from "./base64";

/**
 * Encodes a doc's full state the same way the sync hook does for `/seed` and `/snapshot`.
 * Used to persist a draft doc through create endpoints (e.g. `NoteInitializeRequest.bodyState`).
 */
export const encodeDocState = (doc: Y.Doc): string => toBase64(Y.encodeStateAsUpdate(doc));
