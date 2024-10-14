import Button, { ButtonHeight, ButtonVariants } from "@/components/button";
import { CommentDto, ProjectPostCommentDto } from "@/model/be/jinear-core";
import { useDeleteTaskCommentMutation } from "@/store/api/taskCommentApi";
import { selectCurrentAccountId } from "@/store/slice/accountSlice";
import { closeDialogModal, popDialogModal } from "@/store/slice/modalSlice";
import { useAppDispatch, useTypedSelector } from "@/store/store";
import { getOffset } from "@/utils/htmlUtils";
import useTranslation from "locales/useTranslation";
import React, { Dispatch, SetStateAction } from "react";
import { IoArrowForward, IoClose } from "react-icons/io5";
import styles from "./CommentActionBar.module.css";
import { useDeletePostCommentMutation } from "@/api/projectPostCommentApi";
import { LuTrash } from "react-icons/lu";
import Logger from "@/utils/logger";

interface CommentActionBarProps {
  comment: ProjectPostCommentDto;
  asQuoted?: boolean;
  setQuotedComment?: Dispatch<SetStateAction<ProjectPostCommentDto | undefined>>;
  canComment: boolean;
  hasExplicitAdminDeleteAccess?: boolean;
}

const logger = Logger("CommentActionBar");

const CommentActionBar: React.FC<CommentActionBarProps> = ({
                                                             comment,
                                                             asQuoted,
                                                             setQuotedComment,
                                                             canComment,
                                                             hasExplicitAdminDeleteAccess = false
                                                           }) => {
  const { t } = useTranslation();
  const dispatch = useAppDispatch();
  const currentAccountId = useTypedSelector(selectCurrentAccountId);
  const [deletePostComment, { isLoading: isDeleteLoading }] = useDeletePostCommentMutation();
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
    deletePostComment({ commentId: comment.projectPostCommentId });
    dispatch(closeDialogModal());
  };

  const setAsQuoted = () => {
    setQuotedComment?.(comment);
    try {
      const inputElement = document.getElementById("new-task-comment-input");
      const inputElementContainer = document.getElementById("new-post-comment-input-container");
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
      {!asQuoted && comment.passiveId == null && canComment && (
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
      {(currentAccountId == comment.accountId || hasExplicitAdminDeleteAccess) && comment.passiveId == null && !asQuoted && (
        <Button
          disabled={isDeleteLoading}
          loading={isDeleteLoading}
          heightVariant={ButtonHeight.short}
          variant={ButtonVariants.hoverFilled2}
          onClick={popAreYouSureModalForDeleteComment}
          data-tooltip-right={t("taskCommentDelete")}
        >
          <LuTrash size={11} />
        </Button>
      )}
    </div>
  );
};

export default CommentActionBar;
