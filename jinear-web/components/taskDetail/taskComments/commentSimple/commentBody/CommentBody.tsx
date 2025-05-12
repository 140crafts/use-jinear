import Tiptap from "@/components/tiptap/Tiptap";
import { CommentDto } from "@/model/be/jinear-core";
import useTranslation from "locales/useTranslation";
import React from "react";

import styles from "./CommentBody.module.css";

interface CommentBodyProps {
  comment: CommentDto;
}

const CommentBody: React.FC<CommentBodyProps> = ({ comment }) => {
  const { t } = useTranslation();
  const isDeleted = comment.passiveId;

  return isDeleted ? (
    <i>{t("commentThisCommentDeleted")}</i>
  ) : (
    <Tiptap content={comment.richText?.value} className={styles.editor} editorClassName={styles.editor} editable={false} />
  );
};

export default CommentBody;
