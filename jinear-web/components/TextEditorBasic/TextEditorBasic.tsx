import Logger from "@/utils/logger";
import useTranslation from "locales/useTranslation";
import React, { useEffect, useRef, useState } from "react";
import ContentEditable, { ContentEditableEvent } from "react-contenteditable";
import { UseFormRegister, UseFormSetValue } from "react-hook-form";
import sanitizeHtml from "sanitize-html";
import EditorButton from "./editorButton/EditorButton";
import styles from "./TextEditorBasic.module.css";
interface TextEditorBasicProps {
  variant?: "simple" | "full";
  htmlInputId?: string;
  placeholder?: string;
  register?: UseFormRegister<any>;
  formSetValue?: UseFormSetValue<any>;
  readOnly?: boolean;
  initialValue?: string;
}

const logger = Logger("TextEditorBasic");

const ALLOWED_TAGS = [
  "h1",
  "p",
  "b",
  "i",
  "em",
  "strong",
  "a",
  "br",
  "li",
  "ul",
  "ol",
  "blockquote",
  "br",
];

const SANITIZE_CONFIG = {
  allowedTags: ALLOWED_TAGS,
  allowedAttributes: {
    "*": ["href"],
  },
};

const TextEditorBasic: React.FC<TextEditorBasicProps> = ({
  variant = "simple",
  htmlInputId,
  placeholder,
  register,
  formSetValue,
  readOnly = false,
  initialValue,
}) => {
  const { t } = useTranslation();
  const [value, setValue] = useState<string>(initialValue || "");
  const text = useRef<string>(initialValue || "");
  const contentEditableRef = useRef<HTMLDivElement>(null);

  useEffect(() => {
    if (contentEditableRef.current) {
      contentEditableRef?.current?.focus?.();
    }
  }, [readOnly]);

  useEffect(() => {
    if (initialValue) {
      text.current = initialValue;
      sanitizeData();
    }
  }, [initialValue, formSetValue, htmlInputId]);

  const handleChange = (event: ContentEditableEvent) => {
    const data = event.target.value;
    text.current = data;
  };

  const sanitizeData = () => {
    const value = text.current;
    logger.log({ sanitizeData: value });
    const sanitizedData = sanitizeHtml(value, SANITIZE_CONFIG);
    setValue(sanitizedData);
    text.current = sanitizedData;
    if (htmlInputId && formSetValue) {
      formSetValue(htmlInputId, sanitizedData);
    }
  };

  return (
    <div className={styles.container}>
      {!readOnly && (
        <div className={styles.toolbar}>
          <EditorButton cmd={"italic"} label={t("textEditorItalic")} />
          <EditorButton cmd={"bold"} label={t("textEditorBold")} />
          <EditorButton
            cmd={"formatBlock"}
            arg={"h1"}
            label={t("textEditorHeading")}
          />
          <EditorButton
            cmd={"formatBlock"}
            arg={"blockquote"}
            label={t("textEditorQuote")}
          />
          <EditorButton cmd={"insertOrderedList"} label={t("textEditorOl")} />
          <EditorButton cmd={"insertUnorderedList"} label={t("textEditorUl")} />
        </div>
      )}
      <ContentEditable
        innerRef={contentEditableRef}
        className={styles.contentEditable}
        html={text.current}
        disabled={readOnly}
        onChange={handleChange}
        onBlur={sanitizeData}
        placeholder={placeholder}
      />
      {htmlInputId && (
        <input
          id={htmlInputId}
          type="hidden"
          {...register?.(htmlInputId)}
          value={value}
        />
      )}
    </div>
  );
};

export default TextEditorBasic;
