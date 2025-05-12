import Button, { ButtonVariants } from "@/components/button";
import { Editor } from "@tiptap/react";
import React, { ChangeEvent, useEffect, useRef } from "react";
import {
  LuBold,
  LuCode2,
  LuHeading1,
  LuHeading2,
  LuHeading3,
  LuHeading4,
  LuHeading5,
  LuHeading6,
  LuImage,
  LuItalic,
  LuList,
  LuQuote,
  LuRedo2,
  LuRemoveFormatting,
  LuStrikethrough,
  LuUndo2
} from "react-icons/lu";
import { MdHorizontalRule, MdLayersClear } from "react-icons/md";
import { PiListNumbers } from "react-icons/pi";
import styles from "./ActionBar.module.css";
import { useUploadRichTextImageMutation } from "@/api/richTextImageApi";
import { useAppDispatch } from "@/store/store";
import { changeLoadingModalVisibility } from "@/slice/modalSlice";

export type TipTapActionBarMode = "simple" | "full" | "none";

interface ActionBarProps {
  editor: Editor;
  mode?: TipTapActionBarMode;
  workspaceIdForImages?: string;
}

const ActionBar: React.FC<ActionBarProps> = ({ editor, mode = "full", workspaceIdForImages }) => {
  const dispatch = useAppDispatch();
  const imagePickerButtonRef = useRef<HTMLInputElement>(null);
  const [uploadRichTextImage, { data: uploadRichTextImageResponse, isLoading }] = useUploadRichTextImageMutation();

  useEffect(() => {
    if (uploadRichTextImageResponse?.data && editor) {
      const url = uploadRichTextImageResponse.data.url;
      if (url && editor) {
        editor.chain().focus().setImage({ src: url }).run();
      }
    }
  }, [uploadRichTextImageResponse, editor]);

  useEffect(() => {
    if (dispatch) {
      dispatch(changeLoadingModalVisibility({ visible: isLoading }));
    }
  }, [dispatch, isLoading]);

  const pickPhoto = () => {
    if (!editor) {
      return;
    }
    if (imagePickerButtonRef.current) {
      imagePickerButtonRef.current.value = "";
      imagePickerButtonRef.current.click();
    }
  };

  const onSelectFile = (event: ChangeEvent<HTMLInputElement>) => {
    const target = event.target as HTMLInputElement;
    if (target.files && target.files.length) {
      const file = event.target?.files?.[0];
      if (file && workspaceIdForImages) {
        const formData = new FormData();
        formData.append("file", file);
        uploadRichTextImage({ workspaceId: workspaceIdForImages, formData });
      }
      return;
    }
  };

  return (editor && mode != "none") ? (
    <div className={styles.container}>
      <Button
        onClick={() => editor.chain().focus().toggleBold().run()}
        disabled={!editor.can().chain().focus().toggleBold().run()}
        variant={editor.isActive("bold") ? ButtonVariants.contrast : ButtonVariants.hoverFilled}
      >
        <LuBold />
      </Button>
      <Button
        onClick={() => editor.chain().focus().toggleItalic().run()}
        disabled={!editor.can().chain().focus().toggleItalic().run()}
        variant={editor.isActive("italic") ? ButtonVariants.contrast : ButtonVariants.hoverFilled}
      >
        <LuItalic />
      </Button>
      <Button
        onClick={() => editor.chain().focus().toggleStrike().run()}
        disabled={!editor.can().chain().focus().toggleStrike().run()}
        variant={editor.isActive("strike") ? ButtonVariants.contrast : ButtonVariants.hoverFilled}
      >
        <LuStrikethrough />
      </Button>
      <Button
        onClick={() => editor.chain().focus().unsetAllMarks().run()}
        disabled={!editor.can().chain().focus().unsetAllMarks().run()}
        variant={ButtonVariants.hoverFilled}
      >
        <LuRemoveFormatting />
      </Button>
      {mode != "simple" && (
        <Button
          onClick={() => editor.chain().focus().clearNodes().run()}
          disabled={!editor.can().chain().focus().unsetAllMarks().run()}
          variant={ButtonVariants.hoverFilled}
        >
          <MdLayersClear />
        </Button>
      )}
      {mode != "simple" && (
        <Button
          onClick={() => editor.chain().focus().toggleHeading({ level: 1 }).run()}
          variant={editor.isActive("heading", { level: 1 }) ? ButtonVariants.contrast : ButtonVariants.hoverFilled}
        >
          <LuHeading1 />
        </Button>
      )}
      {mode != "simple" && (
        <Button
          onClick={() => editor.chain().focus().toggleHeading({ level: 2 }).run()}
          variant={editor.isActive("heading", { level: 2 }) ? ButtonVariants.contrast : ButtonVariants.hoverFilled}
        >
          <LuHeading2 />
        </Button>
      )}
      {mode != "simple" && (
        <Button
          onClick={() => editor.chain().focus().toggleHeading({ level: 3 }).run()}
          variant={editor.isActive("heading", { level: 3 }) ? ButtonVariants.contrast : ButtonVariants.hoverFilled}
        >
          <LuHeading3 />
        </Button>
      )}
      {mode != "simple" && (
        <Button
          onClick={() => editor.chain().focus().toggleHeading({ level: 4 }).run()}
          variant={editor.isActive("heading", { level: 4 }) ? ButtonVariants.contrast : ButtonVariants.hoverFilled}
        >
          <LuHeading4 />
        </Button>
      )}
      {mode != "simple" && (
        <Button
          onClick={() => editor.chain().focus().toggleHeading({ level: 5 }).run()}
          variant={editor.isActive("heading", { level: 5 }) ? ButtonVariants.contrast : ButtonVariants.hoverFilled}
        >
          <LuHeading5 />
        </Button>
      )}
      {mode != "simple" && (
        <Button
          onClick={() => editor.chain().focus().toggleHeading({ level: 6 }).run()}
          variant={editor.isActive("heading", { level: 6 }) ? ButtonVariants.contrast : ButtonVariants.hoverFilled}
        >
          <LuHeading6 />
        </Button>
      )}
      {mode != "simple" && (
        <Button
          onClick={() => editor.chain().focus().toggleBulletList().run()}
          disabled={!editor.can().chain().focus().toggleBulletList().run()}
          variant={editor.isActive("bulletList") ? ButtonVariants.contrast : ButtonVariants.hoverFilled}
        >
          <LuList />
        </Button>
      )}
      {mode != "simple" && (
        <Button
          onClick={() => editor.chain().focus().toggleOrderedList().run()}
          disabled={!editor.can().chain().focus().toggleOrderedList().run()}
          variant={editor.isActive("orderedList") ? ButtonVariants.contrast : ButtonVariants.hoverFilled}
        >
          <PiListNumbers />
        </Button>
      )}
      {mode != "simple" && (
        <Button
          onClick={() => editor.chain().focus().toggleCodeBlock().run()}
          disabled={!editor.can().chain().focus().toggleCodeBlock().run()}
          variant={editor.isActive("codeBlock") ? ButtonVariants.contrast : ButtonVariants.hoverFilled}
        >
          <LuCode2 />
        </Button>
      )}
      {mode != "simple" && (
        <Button
          onClick={() => editor.chain().focus().toggleBlockquote().run()}
          disabled={!editor.can().chain().focus().toggleBlockquote().run()}
          variant={editor.isActive("blockquote") ? ButtonVariants.contrast : ButtonVariants.hoverFilled}
        >
          <LuQuote />
        </Button>
      )}
      {mode != "simple" && (
        <Button
          onClick={() => editor.chain().focus().setHorizontalRule().run()}
          disabled={!editor.can().chain().focus().setHorizontalRule().run()}
          variant={ButtonVariants.hoverFilled}
        >
          <MdHorizontalRule />
        </Button>
      )}
      {mode != "simple" && (
        <Button
          onClick={() => editor.chain().focus().undo().run()}
          disabled={!editor.can().chain().focus().undo().run()}
          variant={ButtonVariants.hoverFilled}
        >
          <LuUndo2 />
        </Button>
      )}
      {mode != "simple" && (
        <Button
          onClick={() => editor.chain().focus().redo().run()}
          disabled={!editor.can().chain().focus().redo().run()}
          variant={ButtonVariants.hoverFilled}
        >
          <LuRedo2 />
        </Button>
      )}
      {mode != "simple" && workspaceIdForImages && (
        <>
          <Button
            onClick={pickPhoto}
            disabled={isLoading}
            variant={ButtonVariants.hoverFilled}
          >
            <LuImage />
          </Button>
          <input
            ref={imagePickerButtonRef}
            type="file"
            accept="image/*"
            className={styles.photoInput}
            onChange={onSelectFile}
          />
        </>
      )}
    </div>
  ) : null;
};

export default ActionBar;
