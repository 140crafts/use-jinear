import {Extension} from "@tiptap/core";
import Image from "@tiptap/extension-image";
import Link from "@tiptap/extension-link";
import Placeholder from "@tiptap/extension-placeholder";
import StarterKit from "@tiptap/starter-kit";

interface BuildEditorExtensionsOptions {
    placeholder?: string;
    emptyEditorClass?: string;
    /** Set false to disable StarterKit undo/redo — required when Yjs/y-prosemirror owns history. */
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
    ...extra
];
