import Button, { ButtonHeight, ButtonVariants } from "@/components/button";
import Tiptap, { ITiptapRef } from "@/components/tiptap/Tiptap";
import { CommentDto } from "@/model/be/jinear-core";
import { useInitializeTaskCommentMutation } from "@/store/api/taskCommentApi";
import Logger from "@/utils/logger";
import useTranslation from "locales/useTranslation";
import React, { Dispatch, SetStateAction, useEffect, useRef } from "react";
import { IoClose } from "react-icons/io5";
import CommentSimple from "../commentSimple/CommentSimple";
import styles from "./CommentInput.module.scss";

interface CommentInputProps {
  taskId: string;
  quotedComment?: CommentDto;
  setQuotedComment?: Dispatch<SetStateAction<CommentDto | undefined>>;
  toggleInput?: (newValue?: boolean | undefined) => void;
}

const logger = Logger("CommentInput");
const CommentInput: React.FC<CommentInputProps> = ({ taskId, quotedComment, setQuotedComment, toggleInput }) => {
  const { t } = useTranslation();
  const tiptapRef = useRef<ITiptapRef>(null);
  const [initializeTaskComment, { isLoading, isSuccess }] = useInitializeTaskCommentMutation();

  useEffect(() => {
    setTimeout(() => {
      tiptapRef?.current?.focus();
    }, 250);
  }, []);

  useEffect(() => {
    if (tiptapRef && tiptapRef.current && isSuccess) {
      tiptapRef.current.clearContent();
      setQuotedComment?.(undefined);
      closeInput();
    }
  }, [tiptapRef, isSuccess]);

  const submitComment = () => {
    const comment = tiptapRef?.current?.getHTML() || "";
    const quoteCommentId = quotedComment?.commentId;
    initializeTaskComment({ taskId, comment, quoteCommentId });
  };

  const unquoteComment = () => {
    setQuotedComment?.(undefined);
  };

  const closeInput = () => {
    toggleInput?.(false);
  };

  return (
    <div id={"new-task-comment-input-container"} className={styles.container}>
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

            <CommentSimple comment={quotedComment} asQuoted={true} />
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
                  onClick={closeInput}
                  variant={ButtonVariants.hoverFilled2}
                  heightVariant={ButtonHeight.short}
                  className={styles.closeButton}
                >
                  {t("taskCommentsCommentCancel")}
                </Button>

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
