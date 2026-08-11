import {Extension, mergeAttributes} from "@tiptap/core";
import Image from "@tiptap/extension-image";
import Link from "@tiptap/extension-link";
import {TaskItem, TaskList} from "@tiptap/extension-list";
import Placeholder from "@tiptap/extension-placeholder";
import {TableKit} from "@tiptap/extension-table";
import StarterKit from "@tiptap/starter-kit";
import {NoNestedTables} from "./noNestedTables/NoNestedTables";

interface BuildEditorExtensionsOptions {
    placeholder?: string;
    emptyEditorClass?: string;
    /** Set false to disable StarterKit undo/redo, required when Yjs/y-prosemirror owns history. */
    history?: boolean;
    extra?: Extension[];
}

/**
 * Single source of truth for the rich-text node/mark schema, shared by the legacy {@link Tiptap}
 * editor, the collaborative editor and the CRDT seed parser. Keeping one factory guarantees the
 * ProseMirror schema matches across all three, which is what lets a LEGACY HTML value round-trip
 * cleanly into a Yjs document.
 */
export const buildEditorExtensions = ({
    placeholder,
    emptyEditorClass,
    history = true,
    extra = []
}: BuildEditorExtensionsOptions = {}) => [
    StarterKit.configure(history ? {} : {undoRedo: false}),
    Placeholder.configure({
        placeholder,
        showOnlyWhenEditable: false,
        emptyEditorClass
    }),
    Link.configure({
        HTMLAttributes: {
            rel: "noopener noreferrer",
            target: null
        }
    }),
    Image.configure({
        allowBase64: true
    }),
    TableKit.configure({
        table: {
            resizable: true,
            // Emit the .tableWrapper div from renderHTML too, not just from the editable TableView,
            // so read-only renders get the horizontal scroll container as well.
            renderWrapper: true,
            lastColumnResizable: false
        }
    }),
    // Task lists serialize their state into data attributes only. Tiptap's stock renderHTML emits
    // <label><input type="checkbox"><span></span></label>, none of which survives jinear-core's
    // OWASP allowlist; the sanitized value would come back as a plain bullet list. The interactive
    // checkbox in the editor comes from TaskItem's node view, which never goes through renderHTML,
    // so dropping it here costs nothing. parseHTML is inherited and already reads both attributes,
    // which is what lets this markup round-trip back out of the backend and through the CRDT seed.
    TaskList.extend({
        renderHTML({HTMLAttributes}) {
            return ["ul", mergeAttributes(HTMLAttributes, {"data-type": "taskList"}), 0];
        }
    }),
    TaskItem.extend({
        renderHTML({node, HTMLAttributes}) {
            return ["li", mergeAttributes(HTMLAttributes, {
                "data-type": "taskItem",
                "data-checked": node.attrs.checked
            }), ["div", 0]];
        }
        // Checklists stay flat: this restricts a task item's content to paragraphs only, so the
        // schema itself rejects a nested list rather than relying on the Tab shortcut being absent.
        // Pasted or legacy nested markup gets normalised on the way in.
    }).configure({nested: false}),
    NoNestedTables,
    ...extra
];
