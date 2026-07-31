import {generateJSON, getSchema} from "@tiptap/core";
import {prosemirrorJSONToYDoc} from "@tiptap/y-tiptap";
import * as Y from "yjs";
import {buildEditorExtensions} from "../editorExtensions";
import {CRDT_FIELD} from "./constants";

/**
 * Builds a fresh Yjs document whose {@link CRDT_FIELD} fragment holds the given LEGACY HTML value.
 * Used to produce the `state` bytes for `POST /seed` when promoting a LEGACY rich text to CRDT.
 *
 * The schema is derived from the same extension set the editors use, so the resulting Yjs structure
 * is byte-compatible with what the Collaboration extension expects when it later binds to the doc.
 */
export const buildSeedDoc = (html: string | null | undefined): Y.Doc => {
    const extensions = buildEditorExtensions({history: false});
    const schema = getSchema(extensions);
    const json = generateJSON(html ?? "", extensions);
    return prosemirrorJSONToYDoc(schema, json, CRDT_FIELD);
};
