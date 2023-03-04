import { urlify } from "@/utils/isUrl";
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

const URL_REGEX =
  /^(?:(?:ht|f)tp(?:s?)\:\/\/|~\/|\/)?(?:\w+:\w+@)?((?:(?:[-\w\d{1-3}]+\.)+(?:com|org|net|gov|mil|biz|info|mobi|name|aero|jobs|edu|co\.uk|ac\.uk|it|fr|tv|museum|asia|local|travel|[a-z]{2}))|((\b25[0-5]\b|\b[2][0-4][0-9]\b|\b[0-1]?[0-9]?[0-9]\b)(\.(\b25[0-5]\b|\b[2][0-4][0-9]\b|\b[0-1]?[0-9]?[0-9]\b)){3}))(?::[\d]{1,5})?(?:(?:(?:\/(?:[-\w~!$+|.,=]|%[a-f\d]{2})+)+|\/)+|\?|#)?(?:(?:\?(?:[-\w~!$+|.,*:]|%[a-f\d{2}])+=?(?:[-\w~!$+|.,*:=]|%[a-f\d]{2})*)(?:&(?:[-\w~!$+|.,*:]|%[a-f\d{2}])+=?(?:[-\w~!$+|.,*:=]|%[a-f\d]{2})*)*)*(?:#(?:[-\w~!$ |\/.,*:;=]|%[a-f\d]{2})*)?$/i;

const ALLOWED_TAGS = ["h1", "p", "b", "i", "em", "strong", "a", "br", "li", "ul", "ol", "blockquote", "br"];

const SANITIZE_CONFIG = {
  allowedTags: ALLOWED_TAGS,
  allowedAttributes: {
    a: ["href", "target"],
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
    if (initialValue != null) {
      text.current = initialValue;
      sanitizeData();
    }
  }, [initialValue, formSetValue, htmlInputId]);

  const handleChange = (event: ContentEditableEvent) => {
    let data = event.target.value;
    text.current = data;
  };

  const sanitizeData = () => {
    const value = text.current;
    const sanitizedData = sanitizeHtml(urlify(value), SANITIZE_CONFIG);
    logger.log({ sanitizeData: value, sanitizedData });
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
          <EditorButton cmd={"formatBlock"} arg={"h1"} label={t("textEditorHeading")} />
          <EditorButton cmd={"formatBlock"} arg={"blockquote"} label={t("textEditorQuote")} />
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
      />

      {(!value || value == "") && readOnly && <div className={styles.placeholder}>{placeholder}</div>}

      {htmlInputId && <input id={htmlInputId} type="hidden" {...register?.(htmlInputId)} value={value} />}
    </div>
  );
};

export default TextEditorBasic;
