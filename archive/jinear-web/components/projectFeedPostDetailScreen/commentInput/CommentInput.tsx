import Button, { ButtonHeight, ButtonVariants } from "@/components/button";
import Tiptap, { ITiptapRef } from "@/components/tiptap/Tiptap";
import { ProjectPostCommentDto } from "@/model/be/jinear-core";
import Logger from "@/utils/logger";
import useTranslation from "locales/useTranslation";
import React, { Dispatch, SetStateAction, useEffect, useRef } from "react";
import { IoClose } from "react-icons/io5";
import styles from "./CommentInput.module.scss";
import CommentSimple from "@/components/projectFeedPostDetailScreen/postCommentSimple/CommentSimple";
import { useAddPostCommentMutation } from "@/api/projectPostCommentApi";

interface CommentInputProps {
  postId: string;
  projectId: string;
  quotedComment?: ProjectPostCommentDto;
  setQuotedComment?: Dispatch<SetStateAction<ProjectPostCommentDto | undefined>>;
}

const logger = Logger("CommentInput");
const CommentInput: React.FC<CommentInputProps> = ({
                                                     postId,
                                                     projectId,
                                                     quotedComment,
                                                     setQuotedComment
                                                   }) => {
  const { t } = useTranslation();
  const tiptapRef = useRef<ITiptapRef>(null);
  const [addPostComment, { isLoading, isSuccess }] = useAddPostCommentMutation();

  useEffect(() => {
    setTimeout(() => {
      tiptapRef?.current?.focus();
    }, 250);
  }, []);

  useEffect(() => {
    if (tiptapRef && tiptapRef.current && isSuccess) {
      tiptapRef.current.clearContent();
      setQuotedComment?.(undefined);
    }
  }, [tiptapRef, isSuccess]);

  const submitComment = () => {
    const body = tiptapRef?.current?.getHTML() || "";
    const quoteId = quotedComment?.projectPostCommentId;
    addPostComment({ projectId, postId, body, quoteId });
  };

  const unquoteComment = () => {
    setQuotedComment?.(undefined);
  };

  return (
    <div id={"new-post-comment-input-container"} className={styles.container}>
      <div className={styles.content}>
        {quotedComment && (
          <div className={styles.quotedCommentContainer}>
            <div className={styles.unquoteCommentButtonContainer}>
              <Button
                className={styles.unquoteCommentButton}
                heightVariant={ButtonHeight.short}
                variant={ButtonVariants.filled2}
                onClick={unquoteComment}
              >
                <IoClose />
              </Button>
            </div>

            <CommentSimple comment={quotedComment} asQuoted={true} canComment={true} />
          </div>
        )}

        <div className={styles.commentText}>
          <div className={styles.newCommentContainer}>
            <div className={styles.inputContainer}>
              <Tiptap
                ref={tiptapRef}
                className={styles.input}
                editorClassName={styles.input}
                placeholder={t("taskCommentsInputPlaceholder")}
                editable={true}
              />

              <div className={styles.actionContainer}>

                <Button
                  variant={ButtonVariants.contrast}
                  heightVariant={ButtonHeight.short}
                  onClick={submitComment}
                  loading={isLoading}
                  disabled={isLoading}
                  className={styles.submitButton}
                >
                  {t("taskCommentsCommentSubmit")}
                </Button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default CommentInput;
