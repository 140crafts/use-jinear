import {EditorContent, useEditor} from "@tiptap/react";
import cn from "classnames";
import {forwardRef, useEffect, useImperativeHandle, useState} from "react";
import type {UseFormRegister, UseFormSetValue} from "react-hook-form";
import styles from "./Tiptap.module.css";
import ActionBar, {type TipTapActionBarMode} from "./actionBar/ActionBar";
import {Extension} from "@tiptap/core";
import {buildEditorExtensions} from "./editorExtensions";

export interface ITiptapRef {
    clearContent: () => void;
    getHTML: () => string;
    focus: (position?: "start" | "end" | "all" | number | boolean | null, options?: {
        scrollIntoView?: boolean
    }) => void;
}

interface TiptapProps {
    content?: string;
    className?: string;
    editorClassName?: string;
    overwriteEditorClassName?: string;
    editorWrapperClassName?: string;
    placeholder?: string;
    editable?: boolean;
    htmlInputId?: string;
    register?: UseFormRegister<any>;
    registerKey?: string,
    formSetValue?: UseFormSetValue<any>;
    actionBarMode?: TipTapActionBarMode;
    hideActionBarWhenEmpty?: boolean;
    extensions?: Extension[];
    onFocus?: () => void;
    workspaceIdForImages?: string
}

const Tiptap = (
    {
        content,
        className,
        editorClassName,
        overwriteEditorClassName,
        editorWrapperClassName,
        placeholder,
        editable = true,
        htmlInputId,
        register,
        registerKey,
        formSetValue,
        actionBarMode = "full",
        hideActionBarWhenEmpty = false,
        extensions = [],
        onFocus,
        workspaceIdForImages
    }: TiptapProps,
    ref: any
) => {
    const [html, setHtml] = useState<string>("");
    const editor = useEditor({
        editorProps: {
            attributes: {
                class: cn(styles.tiptap, editorClassName)
            }
        },
        extensions: buildEditorExtensions({
            placeholder,
            emptyEditorClass: styles["is-editor-empty"],
            extra: extensions
        }),
        content,
        editable,
        onUpdate({editor}) {
            setHtml(editor.getHTML());
        }
    });
    const shouldHideActionBar = editor && editor.isEmpty && hideActionBarWhenEmpty;

    useImperativeHandle(ref, () => ({
        clearContent: () => editor?.commands.clearContent(true),
        getHTML: () => editor?.getHTML(),
        focus: (position?: "start" | "end" | "all" | number | boolean | null, options?: { scrollIntoView?: boolean }) =>
            editor?.commands.focus(position, options)
    }));

    useEffect(() => {
        editor?.setEditable(editable);
    }, [editable, editor]);

    return (
        <div className={cn(styles.container, className)} onFocus={onFocus}>
            {editor && !shouldHideActionBar && editable &&
                <ActionBar editor={editor} mode={actionBarMode} workspaceIdForImages={workspaceIdForImages}/>}
            <EditorContent editor={editor} className={editorWrapperClassName}/>
            {htmlInputId &&
                <input id={htmlInputId} type="hidden" {...register?.(registerKey ? registerKey : htmlInputId)}
                       value={html}/>}
        </div>
    );
};

export default forwardRef<ITiptapRef, TiptapProps>(Tiptap);
