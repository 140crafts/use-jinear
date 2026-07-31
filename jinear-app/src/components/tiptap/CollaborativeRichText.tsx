import Collaboration from "@tiptap/extension-collaboration";
import {EditorContent, useEditor} from "@tiptap/react";
import cn from "classnames";
import {forwardRef, useImperativeHandle} from "react";
import type * as Y from "yjs";
import ActionBar, {type TipTapActionBarMode} from "./actionBar/ActionBar";
import {CRDT_FIELD} from "./crdt/constants";
import {buildEditorExtensions} from "./editorExtensions";
import styles from "./Tiptap.module.css";

export interface ICollaborativeRichTextRef {
    getHTML: () => string | undefined;
    focus: () => void;
}

interface CollaborativeRichTextProps {
    /** The Yjs document this editor binds to. Yjs is the source of truth — no `content` prop. */
    doc: Y.Doc;
    placeholder?: string;
    editable?: boolean;
    className?: string;
    editorClassName?: string;
    editorWrapperClassName?: string;
    actionBarMode?: TipTapActionBarMode;
    workspaceIdForImages?: string;
    /** Called on every change with the rendered HTML ("" when empty), to feed the CRDT projection. */
    onHtml?: (html: string) => void;
    /** Called on blur so the transport can force a settle flush (HTML included). */
    onSettle?: () => void;
    actionBarClassName?: string;
    withOutlineOnFocus?: boolean
}

const CollaborativeRichText = (
    {
        doc,
        placeholder,
        editable = true,
        className,
        editorClassName,
        editorWrapperClassName,
        actionBarMode = "full",
        workspaceIdForImages,
        onHtml,
        onSettle,
        actionBarClassName,
        withOutlineOnFocus = true
    }: CollaborativeRichTextProps,
    ref: any
) => {
    const editor = useEditor(
        {
            editorProps: {
                attributes: {
                    class: cn(editorClassName, styles.tiptap, withOutlineOnFocus && styles.withOutlineOnFocus)
                }
            },
            extensions: [
                // StarterKit history is OFF — Yjs/y-prosemirror own undo/redo via the Collaboration ext.
                ...buildEditorExtensions({
                    placeholder,
                    emptyEditorClass: styles["is-editor-empty"],
                    history: false
                }),
                Collaboration.configure({
                    document: doc,
                    field: CRDT_FIELD
                })
            ],
            editable,
            onUpdate({editor}) {
                onHtml?.(editor.isEmpty ? "" : editor.getHTML());
            },
            onBlur() {
                onSettle?.();
            }
        },
        [doc]
    );

    useImperativeHandle(ref, () => ({
        getHTML: () => editor?.getHTML(),
        focus: () => editor?.commands.focus()
    }));

    return (
        <div className={cn(styles.container, className)}>
            {editor && editable && actionBarMode !== "none" && (
                <ActionBar
                    editor={editor}
                    mode={actionBarMode}
                    workspaceIdForImages={workspaceIdForImages}
                    className={actionBarClassName}
                />
            )}
            <EditorContent editor={editor} className={editorWrapperClassName}/>
        </div>
    );
};

export default forwardRef<ICollaborativeRichTextRef, CollaborativeRichTextProps>(CollaborativeRichText);
