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
    <div className={styles.commentText} dangerouslySetInnerHTML={{ __html: comment.richText?.value }}></div>
  );
};

export default CommentBody;
