import { ProjectPostCommentDto } from "@/model/be/jinear-core";
import cn from "classnames";
import useTranslation from "locales/useTranslation";
import React, { Dispatch, SetStateAction } from "react";
import styles from "./CommentSimple.module.css";
import CommentBody from "./commentBody/CommentBody";
import CommentFooter from "./commentFooter/CommentFooter";
import CommentActionBar
  from "@/components/projectFeedPostDetailScreen/postCommentSimple/commentActionBar/CommentActionBar";

interface CommentSimpleProps {
  comment: ProjectPostCommentDto;
  setQuotedComment?: Dispatch<SetStateAction<ProjectPostCommentDto | undefined>>;
  asQuoted?: boolean;
  className?: string;
  withDivider?: boolean;
  canComment: boolean;
  hasExplicitAdminDeleteAccess?: boolean;
}

const CommentSimple: React.FC<CommentSimpleProps> = ({
                                                       comment,
                                                       setQuotedComment,
                                                       asQuoted,
                                                       withDivider = true,
                                                       className,
                                                       canComment,
                                                       hasExplicitAdminDeleteAccess = false
                                                     }) => {
  const { t } = useTranslation();
  return (
    <div className={cn(styles.container, className)}>
      {comment.quote && !asQuoted && (
        <div className={styles.quoteContainer}>
          <b className={styles.quoteLabel}>{t("taskCommentQuote")}</b>
          <CommentSimple comment={comment.quote}
                         asQuoted={true}
                         className={styles.quote}
                         canComment={canComment}
                         hasExplicitAdminDeleteAccess={hasExplicitAdminDeleteAccess} />
        </div>
      )}

      <CommentBody comment={comment} />
      <CommentActionBar comment={comment} asQuoted={asQuoted} setQuotedComment={setQuotedComment}
                        canComment={canComment}
                        hasExplicitAdminDeleteAccess={hasExplicitAdminDeleteAccess}/>
      <CommentFooter comment={comment} />
      {!asQuoted && withDivider && <div className={styles.divider} />}
    </div>
  );
};

export default CommentSimple;
