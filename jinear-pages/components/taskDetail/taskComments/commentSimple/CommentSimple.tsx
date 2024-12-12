import { CommentDto } from "@/model/be/jinear-core";
import cn from "classnames";
import useTranslation from "locales/useTranslation";
import React, { Dispatch, SetStateAction } from "react";
import styles from "./CommentSimple.module.css";
import CommentActionBar from "./commentActionBar/CommentActionBar";
import CommentBody from "./commentBody/CommentBody";
import CommentFooter from "./commentFooter/CommentFooter";

interface CommentSimpleProps {
  comment: CommentDto;
  setQuotedComment?: Dispatch<SetStateAction<CommentDto | undefined>>;
  asQuoted?: boolean;
  className?: string;
  withDivider?: boolean;
}

const CommentSimple: React.FC<CommentSimpleProps> = ({ comment, setQuotedComment, asQuoted, withDivider = true, className }) => {
  const { t } = useTranslation();
  return (
    <div className={cn(styles.container, className)}>
      {comment.quote && !asQuoted && (
        <div className={styles.quoteContainer}>
          <b className={styles.quoteLabel}>{t("taskCommentQuote")}</b>
          <CommentSimple comment={comment.quote} asQuoted={true} className={styles.quote} />
        </div>
      )}

      <CommentBody comment={comment} />
      <CommentActionBar comment={comment} asQuoted={asQuoted} setQuotedComment={setQuotedComment} />
      <CommentFooter comment={comment} />
      {!asQuoted && withDivider && <div className={styles.divider} />}
    </div>
  );
};

export default CommentSimple;
