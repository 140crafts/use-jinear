import {Extension} from "@tiptap/core";
import type {Fragment, Node as ProseMirrorNode} from "@tiptap/pm/model";
import {Plugin, PluginKey} from "@tiptap/pm/state";
import {ySyncPluginKey} from "@tiptap/y-tiptap";

const TABLE = "table";

/** True when the fragment holds a table anywhere inside it. */
const fragmentHasTable = (fragment: Fragment): boolean => {
    let found = false;
    fragment.descendants((node) => {
        if (found) return false;
        if (node.type.name === TABLE) {
            found = true;
            return false;
        }
        return true;
    });
    return found;
};

/** True when any table in the doc has another table as an ancestor. */
const docHasNestedTable = (doc: ProseMirrorNode): boolean => {
    let nested = false;
    doc.descendants((node, pos) => {
        if (nested) return false;
        if (node.type.name !== TABLE) return true;
        const $pos = doc.resolve(pos);
        for (let depth = $pos.depth; depth > 0; depth -= 1) {
            if ($pos.node(depth).type.name === TABLE) {
                nested = true;
                return false;
            }
        }
        // Keep descending; the nesting may be deeper in.
        return true;
    });
    return nested;
};

/**
 * Rejects any transaction that would put a table inside another table.
 *
 * prosemirror-tables does not support nested tables: its row/column commands operate on the
 * innermost table but rewrite structure as if it were top-level, which can produce a node that no
 * longer satisfies the schema. Under Yjs that is destructive rather than cosmetic; y-prosemirror
 * deletes any Y element it cannot turn back into a ProseMirror node (see the catch in
 * `createNodeFromYElement`), so the table is dropped from the doc, from IndexedDB and from the
 * server. Blocking the nesting up front is the only way to keep that from happening.
 *
 * Plugin-only: registers no nodes or marks, so the ProseMirror schema, and therefore the CRDT
 * seed, is unchanged. Docs that already contain a nested table still load; this only stops new
 * ones being created.
 */
export const NoNestedTables = Extension.create({
    name: "noNestedTables",

    addProseMirrorPlugins() {
        return [
            new Plugin({
                key: new PluginKey("noNestedTables"),
                filterTransaction(transaction) {
                    if (!transaction.docChanged) return true;
                    // Never veto a Yjs sync transaction; the editor has to stay a faithful view of
                    // the Y.Doc, or local and remote state silently diverge.
                    if (transaction.getMeta(ySyncPluginKey)) return true;

                    const insertsTable = transaction.steps.some((step) => {
                        const slice = (step as { slice?: { content: Fragment } }).slice;
                        return slice ? fragmentHasTable(slice.content) : false;
                    });
                    if (!insertsTable) return true;

                    return !docHasNestedTable(transaction.doc);
                }
            })
        ];
    }
});

export default NoNestedTables;
