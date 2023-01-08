import cn from "classnames";
import { ContentState, convertToRaw, EditorState } from "draft-js";
import draftToHtml from "draftjs-to-html";
import React, { useEffect, useState } from "react";
import { Editor } from "react-draft-wysiwyg";
import { UseFormRegister, UseFormSetValue } from "react-hook-form";
import "../../node_modules/react-draft-wysiwyg/dist/react-draft-wysiwyg.css";
import styles from "./TextEditor.module.scss";
interface TextEditorProps {
  variant?: "simple" | "full";
  htmlInputId?: string;
  placeholder?: string;
  register?: UseFormRegister<any>;
  formSetValue?: UseFormSetValue<any>;
  readOnly?: boolean;
  initialValue?: string;
}

const TextEditor: React.FC<TextEditorProps> = ({
  variant = "simple",
  htmlInputId,
  placeholder,
  register,
  formSetValue,
  readOnly = false,
  initialValue,
}) => {
  const [editorState, setEditorState] = useState<EditorState>();
  let htmlToDraft: any = null;
  if (typeof window === "object") {
    htmlToDraft = require("html-to-draftjs").default;
  }

  useEffect(() => {
    if (initialValue) {
      const blocksFromHtml = htmlToDraft(initialValue);
      const { contentBlocks, entityMap } = blocksFromHtml;
      const contentState = ContentState.createFromBlockArray(
        contentBlocks,
        entityMap
      );
      const editorState = EditorState.createWithContent(contentState);
      setEditorState(editorState);
    }
  }, [initialValue, setEditorState]);

  useEffect(() => {
    if (htmlInputId && editorState) {
      formSetValue?.(
        htmlInputId,
        draftToHtml(convertToRaw(editorState.getCurrentContent()))
      );
    }
  }, [htmlInputId, editorState]);

  const toolbar = toolbarConfig(variant);
  return (
    <>
      <Editor
        toolbarHidden={readOnly}
        editorState={editorState}
        onEditorStateChange={setEditorState}
        wrapperClassName={cn(
          styles.wrapper,
          readOnly ? styles["wrapper-read-only"] : undefined
        )}
        editorClassName={styles.editor}
        toolbarClassName={styles.toolbar}
        toolbar={toolbar}
        placeholder={placeholder}
        readOnly={readOnly}
        // mention={{
        //   separator: " ",
        //   trigger: "@",
        //   suggestions: [
        //     { text: "APPLE", value: "apple-qwe", url: "apple" },
        //     { text: "BANANA", value: "banana", url: "banana" },
        //     { text: "CHERRY", value: "cherry", url: "cherry" },
        //     { text: "DURIAN", value: "durian", url: "durian" },
        //     { text: "EGGFRUIT", value: "eggfruit", url: "eggfruit" },
        //     { text: "FIG", value: "fig", url: "fig" },
        //     { text: "GRAPEFRUIT", value: "grapefruit", url: "grapefruit" },
        //     { text: "HONEYDEW", value: "honeydew", url: "honeydew" },
        //   ],
        // }}
      />
      {editorState && htmlInputId && (
        <input
          id={htmlInputId}
          type="hidden"
          {...register?.(htmlInputId)}
          value={draftToHtml(convertToRaw(editorState.getCurrentContent()))}
        />
      )}
    </>
  );
};

export default TextEditor;

const toolbarConfig = (variant: "simple" | "full") => {
  const baseOptions = ["inline", "link"];
  const additionalOptions = [
    "blockType",
    "fontSize",
    "colorPicker",
    "embedded",
    "emoji",
    "image",
    "list",
    "remove",
    "history",
    "textAlign",
  ];
  const options =
    variant == "simple"
      ? [...baseOptions]
      : [...baseOptions, ...additionalOptions];
  return {
    options,
    inline: {
      inDropdown: false,
      options: ["bold", "italic", "underline", "strikethrough"],
      bold: { className: styles["toolbar-button"] },
      italic: { className: styles["toolbar-button"] },
      underline: { className: styles["toolbar-button"] },
      strikethrough: { className: styles["toolbar-button"] },
      monospace: { className: styles["toolbar-button"] },
      superscript: { className: styles["toolbar-button"] },
      subscript: { className: styles["toolbar-button"] },
    },
    blockType: {
      inDropdown: true,
      options: ["Normal", "H1", "H2", "H3", "Blockquote", "Code"],
      className: styles["toolbar-dropdown-button"],
      dropdownClassName: styles["toolbar-dropdown"],
    },
    fontSize: {
      options: [8, 9, 10, 11, 12, 14, 16, 18, 24, 30, 36, 48, 60, 72, 96],
      className: styles["toolbar-dropdown-button"],
      dropdownClassName: styles["toolbar-dropdown"],
    },
    list: {
      inDropdown: false,
      options: [
        "unordered",
        "ordered",
        //   "indent",
        //    "outdent"
      ],
      unordered: { className: styles["toolbar-button"] },
      ordered: { className: styles["toolbar-button"] },
      indent: { className: styles["toolbar-button"] },
      outdent: { className: styles["toolbar-button"] },
    },
    textAlign: {
      inDropdown: false,
      //   className: styles["toolbar-button"],
      popupClassName: styles["tool-bar-icon-popup"],
      options: ["left", "center", "right", "justify"],
      left: { className: styles["toolbar-button"] },
      center: { className: styles["toolbar-button"] },
      right: { className: styles["toolbar-button"] },
      justify: { className: styles["toolbar-button"] },
    },
    colorPicker: {
      className: styles["toolbar-button"],
      popupClassName: styles["tool-bar-icon-popup"],
      colors: [
        "rgb(97,189,109)",
        "rgb(26,188,156)",
        "rgb(84,172,210)",
        "rgb(44,130,201)",
        "rgb(147,101,184)",
        "rgb(71,85,119)",
        "rgb(204,204,204)",
        "rgb(65,168,95)",
        "rgb(0,168,133)",
        "rgb(61,142,185)",
        "rgb(41,105,176)",
        "rgb(85,57,130)",
        "rgb(40,50,78)",
        "rgb(0,0,0)",
        "rgb(247,218,100)",
        "rgb(251,160,38)",
        "rgb(235,107,86)",
        "rgb(226,80,65)",
        "rgb(163,143,132)",
        "rgb(239,239,239)",
        "rgb(255,255,255)",
        "rgb(250,197,28)",
        "rgb(243,121,52)",
        "rgb(209,72,65)",
        "rgb(184,49,47)",
        "rgb(124,112,107)",
        "rgb(209,213,216)",
      ],
    },
    link: {
      inDropdown: false,
      //   className: styles["toolbar-button"],
      popupClassName: styles["tool-bar-icon-popup"],
      //   dropdownClassName: undefined,
      showOpenOptionOnHover: true,
      defaultTargetOption: "_blank",
      options: ["link", "unlink"],
      link: {
        className: styles["toolbar-button"],
      },
      unlink: {
        className: styles["toolbar-button"],
      },
    },
    emoji: {
      className: styles["toolbar-button"],
      popupClassName: styles["tool-bar-icon-popup"],
      emojis: [
        "😀",
        "😁",
        "😂",
        "😃",
        "😉",
        "😋",
        "😎",
        "😍",
        "😗",
        "🤗",
        "🤔",
        "😣",
        "😫",
        "😴",
        "😌",
        "🤓",
        "😛",
        "😜",
        "😠",
        "😇",
        "😷",
        "😈",
        "👻",
        "😺",
        "😸",
        "😹",
        "😻",
        "😼",
        "😽",
        "🙀",
        "🙈",
        "🙉",
        "🙊",
        "👼",
        "👮",
        "🕵",
        "💂",
        "👳",
        "🎅",
        "👸",
        "👰",
        "👲",
        "🙍",
        "🙇",
        "🚶",
        "🏃",
        "💃",
        "⛷",
        "🏂",
        "🏌",
        "🏄",
        "🚣",
        "🏊",
        "⛹",
        "🏋",
        "🚴",
        "👫",
        "💪",
        "👈",
        "👉",
        "👉",
        "👆",
        "🖕",
        "👇",
        "🖖",
        "🤘",
        "🖐",
        "👌",
        "👍",
        "👎",
        "✊",
        "👊",
        "👏",
        "🙌",
        "🙏",
        "🐵",
        "🐶",
        "🐇",
        "🐥",
        "🐸",
        "🐌",
        "🐛",
        "🐜",
        "🐝",
        "🍉",
        "🍄",
        "🍔",
        "🍤",
        "🍨",
        "🍪",
        "🎂",
        "🍰",
        "🍾",
        "🍷",
        "🍸",
        "🍺",
        "🌍",
        "🚑",
        "⏰",
        "🌙",
        "🌝",
        "🌞",
        "⭐",
        "🌟",
        "🌠",
        "🌨",
        "🌩",
        "⛄",
        "🔥",
        "🎄",
        "🎈",
        "🎉",
        "🎊",
        "🎁",
        "🎗",
        "🏀",
        "🏈",
        "🎲",
        "🔇",
        "🔈",
        "📣",
        "🔔",
        "🎵",
        "🎷",
        "💰",
        "🖊",
        "📅",
        "✅",
        "❎",
        "💯",
      ],
    },
    embedded: {
      className: styles["toolbar-button"],
      popupClassName: styles["tool-bar-icon-popup"],
      defaultSize: {
        height: "auto",
        width: "auto",
      },
    },
    image: {
      className: styles["toolbar-button"],
      popupClassName: styles["tool-bar-icon-popup"],
      urlEnabled: true,
      uploadEnabled: false,
      alignmentEnabled: false,
      uploadCallback: undefined,
      previewImage: false,
      inputAccept: "image/gif,image/jpeg,image/jpg,image/png,image/svg",
      alt: { present: false, mandatory: false },
      defaultSize: {
        height: "auto",
        width: "auto",
      },
    },
    remove: {
      className: styles["toolbar-button"],
    },
    history: {
      inDropdown: false,
      options: ["undo", "redo"],
      undo: {
        className: styles["toolbar-button"],
      },
      redo: {
        className: styles["toolbar-button"],
      },
    },
  };
};
