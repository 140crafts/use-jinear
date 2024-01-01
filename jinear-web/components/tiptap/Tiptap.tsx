"use client";
import Link from "@tiptap/extension-link";
import Placeholder from "@tiptap/extension-placeholder";
import { EditorContent, useEditor } from "@tiptap/react";
import StarterKit from "@tiptap/starter-kit";
import React, { useEffect, useState } from "react";
import { UseFormRegister, UseFormSetValue } from "react-hook-form";
import styles from "./Tiptap.module.css";
import ActionBar from "./actionBar/ActionBar";

interface TiptapProps {
  content?: string;
  placeholder?: string;
  editable?: boolean;
  htmlInputId?: string;
  register?: UseFormRegister<any>;
  formSetValue?: UseFormSetValue<any>;
}

const Tiptap: React.FC<TiptapProps> = ({ content, placeholder, editable = true, htmlInputId, register, formSetValue }) => {
  const [html, setHtml] = useState<string>();
  const editor = useEditor({
    extensions: [
      StarterKit,
      Placeholder.configure({
        placeholder,
        showOnlyWhenEditable: false,
      }),
      Link.configure({
        HTMLAttributes: {
          rel: "noopener noreferrer",
          target: null,
        },
      }),
    ],
    content,
    editable,
    onUpdate({ editor }) {
      setHtml(editor.getHTML());
    },
  });

  useEffect(() => {
    editor?.setEditable(editable);
  }, [editable, editor]);

  return (
    <div className={styles.container}>
      {editor && editable && <ActionBar editor={editor} />}
      <EditorContent editor={editor} />
      {htmlInputId && <input id={htmlInputId} type="hidden" {...register?.(htmlInputId)} value={html} />}
    </div>
  );
};

export default Tiptap;
