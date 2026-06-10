import Tiptap from "@/components/tiptap/Tiptap";
import { CommentDto, ProjectPostCommentDto } from "@/model/be/jinear-core";
import useTranslation from "locales/useTranslation";
import React from "react";

import styles from "./CommentBody.module.css";

interface CommentBodyProps {
  comment: ProjectPostCommentDto;
}

const CommentBody: React.FC<CommentBodyProps> = ({ comment }) => {
  const { t } = useTranslation();
  const isDeleted = comment.passiveId;

  return isDeleted ? (
    <i>{t("commentThisCommentDeleted")}</i>
  ) : (
    <Tiptap content={comment.commentBody?.value} className={styles.editor} editorClassName={styles.editor}
            editable={false} />
  );
};

export default CommentBody;
