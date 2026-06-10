import Button, { ButtonHeight, ButtonVariants } from "@/components/button";
import { CommentDto } from "@/model/be/jinear-core";
import { useDeleteTaskCommentMutation } from "@/store/api/taskCommentApi";
import { selectCurrentAccountId } from "@/store/slice/accountSlice";
import { closeDialogModal, popDialogModal } from "@/store/slice/modalSlice";
import { useAppDispatch, useTypedSelector } from "@/store/store";
import { getOffset } from "@/utils/htmlUtils";
import useTranslation from "locales/useTranslation";
import React, { Dispatch, SetStateAction } from "react";
import { IoArrowForward, IoClose } from "react-icons/io5";
import styles from "./CommentActionBar.module.css";
import Logger from "@/utils/logger";

interface CommentActionBarProps {
  comment: CommentDto;
  asQuoted?: boolean;
  setQuotedComment?: Dispatch<SetStateAction<CommentDto | undefined>>;
}

const logger = Logger("CommentActionBar");

const CommentActionBar: React.FC<CommentActionBarProps> = ({ comment, asQuoted, setQuotedComment }) => {
  const { t } = useTranslation();
  const dispatch = useAppDispatch();
  const currentAccountId = useTypedSelector(selectCurrentAccountId);
  const [deleteTaskComment, { isLoading: isDeleteLoading }] = useDeleteTaskCommentMutation();
  logger.log({ comment });
  const popAreYouSureModalForDeleteComment = () => {
    dispatch(
      popDialogModal({
        visible: true,
        title: t("deleteCommentAreYouSureTitle"),
        content: t("deleteCommentAreYouSureText"),
        confirmButtonLabel: t("deleteCommentAreYouSureDeleteButton"),
        onConfirm: deleteComment
      })
    );
  };

  const deleteComment = () => {
    deleteTaskComment({ commentId: comment.commentId });
    dispatch(closeDialogModal());
  };

  const setAsQuoted = () => {
    setQuotedComment?.(comment);
    try {
      const inputElement = document.getElementById("new-task-comment-input");
      const inputElementContainer = document.getElementById("new-task-comment-input-container");
      const containerOffset = inputElementContainer && getOffset(inputElementContainer);
      window.scrollTo({ left: 0, top: parseInt(`${containerOffset?.top}` || "0"), behavior: "smooth" });
      setTimeout(() => {
        inputElement?.focus();
      }, 450);
    } catch (error) {
    }
  };

  return (
    <div className={styles.actionBar}>
      {!asQuoted && comment.passiveId == null && (
        <Button
          disabled={isDeleteLoading}
          heightVariant={ButtonHeight.short}
          variant={ButtonVariants.hoverFilled2}
          onClick={setAsQuoted}
          data-tooltip-right={t("taskCommentReply")}
        >
          <IoArrowForward />
        </Button>
      )}
      {currentAccountId == comment.ownerId && comment.passiveId == null && !asQuoted && (
        <Button
          disabled={isDeleteLoading}
          loading={isDeleteLoading}
          heightVariant={ButtonHeight.short}
          variant={ButtonVariants.hoverFilled2}
          onClick={popAreYouSureModalForDeleteComment}
          data-tooltip-right={t("taskCommentDelete")}
        >
          <IoClose size={11} />
        </Button>
      )}
    </div>
  );
};

export default CommentActionBar;
