import Button, { ButtonHeight, ButtonVariants } from "@/components/button";
import ProfilePhoto from "@/components/profilePhoto";
import { CommentDto } from "@/model/be/jinear-core";
import { useDeleteTaskCommentMutation } from "@/store/api/taskCommentApi";
import { selectCurrentAccountId } from "@/store/slice/accountSlice";
import { closeDialogModal, popDialogModal } from "@/store/slice/modalSlice";
import { useAppDispatch, useTypedSelector } from "@/store/store";
import cn from "classnames";
import { differenceInDays, differenceInHours, differenceInMinutes, format } from "date-fns";
import useTranslation from "locales/useTranslation";
import React, { Dispatch, SetStateAction, useMemo } from "react";
import styles from "./Comment.module.css";

interface CommentProps {
  comment: CommentDto;
  setQuotedComment?: Dispatch<SetStateAction<CommentDto | undefined>>;
  asQuoted?: boolean;
}

const Comment: React.FC<CommentProps> = ({ comment, setQuotedComment, asQuoted }) => {
  const { t } = useTranslation();
  const dispatch = useAppDispatch();
  const currentAccountId = useTypedSelector(selectCurrentAccountId);
  const [deleteTaskComment, { isLoading: isDeleteLoading }] = useDeleteTaskCommentMutation();
  const isDeleted = comment.passiveId;

  const createdDate = format(new Date(comment.createdDate), t("dateTimeFormat"));
  const updateDate = comment.lastUpdatedDate && format(new Date(comment.lastUpdatedDate), t("dateTimeFormat"));

  const dateDiff = useMemo(() => {
    const diffInDays = differenceInDays(new Date(), new Date(comment.createdDate));
    if (diffInDays != 0) {
      return t("dateDiffLabelDateInDays")?.replace("${num}", `${diffInDays}`);
    }
    const diffInHours = differenceInHours(new Date(), new Date(comment.createdDate));
    if (diffInHours != 0) {
      return t("dateDiffLabelDateInHours")?.replace("${num}", `${diffInHours}`);
    }
    const diffInMinutes = differenceInMinutes(new Date(), new Date(comment.createdDate));
    if (diffInMinutes != 0) {
      return t("dateDiffLabelDateInMinutes")?.replace("${num}", `${diffInMinutes}`);
    }
    return t("dateDiffLabelDateJustNow");
  }, [comment.createdDate]);

  const updateDateDiff = useMemo(() => {
    const diffInDays = differenceInDays(new Date(), new Date(comment.lastUpdatedDate));
    if (diffInDays != 0) {
      return t("dateDiffLabelDateInDays")?.replace("${num}", `${diffInDays}`);
    }
    const diffInHours = differenceInHours(new Date(), new Date(comment.lastUpdatedDate));
    if (diffInHours != 0) {
      return t("dateDiffLabelDateInHours")?.replace("${num}", `${diffInHours}`);
    }
    const diffInMinutes = differenceInMinutes(new Date(), new Date(comment.lastUpdatedDate));
    if (diffInMinutes != 0) {
      return t("dateDiffLabelDateInMinutes")?.replace("${num}", `${diffInMinutes}`);
    }
    return t("dateDiffLabelDateJustNow");
  }, [comment.lastUpdatedDate]);

  const popAreYouSureModalForDeleteComment = () => {
    dispatch(
      popDialogModal({
        visible: true,
        title: t("deleteCommentAreYouSureTitle"),
        content: t("deleteCommentAreYouSureText"),
        confirmButtonLabel: t("deleteCommentAreYouSureDeleteButton"),
        onConfirm: deleteComment,
      })
    );
  };

  const deleteComment = () => {
    deleteTaskComment({ commentId: comment.commentId });
    dispatch(closeDialogModal());
  };

  const setAsQuoted = () => {
    setQuotedComment?.(comment);
  };

  return (
    <div className={styles.container}>
      <div className={styles.profilePicContainer}>
        <ProfilePhoto
          boringAvatarKey={comment.ownerId}
          storagePath={comment.owner.profilePicture?.storagePath}
          wrapperClassName={styles.profilePic}
        />
      </div>
      <div className={cn(styles.content, asQuoted && styles.contentQuoted, comment.quote && styles.contentWithQuote)}>
        <div className={styles.ownerContainer}>
          <div className={styles.userName}>
            <b>{comment.owner.username}</b>
            {!asQuoted && <span className={styles.addedCommentText}>{t("taskCommentsCommentHeaderText")}</span>}
          </div>

          <div className="flex-1" />

          {!isDeleted && !asQuoted && (
            <div className={styles.actionButtonBar}>
              {currentAccountId == comment.ownerId && !asQuoted && (
                <Button
                  disabled={isDeleteLoading}
                  loading={isDeleteLoading}
                  heightVariant={ButtonHeight.short}
                  variant={ButtonVariants.hoverFilled2}
                  onClick={popAreYouSureModalForDeleteComment}
                >
                  {t("taskCommentDelete")}
                </Button>
              )}

              <Button
                disabled={isDeleteLoading}
                loading={isDeleteLoading}
                heightVariant={ButtonHeight.short}
                variant={ButtonVariants.hoverFilled2}
                onClick={setAsQuoted}
              >
                {t("taskCommentReply")}
              </Button>
            </div>
          )}

          {!asQuoted && <p className={styles.createTime} data-tooltip-right={createdDate}>{`(${dateDiff})`}</p>}
        </div>

        {comment.quote && (
          <div className={styles.quotedCommentContainer}>
            <Comment comment={comment.quote} asQuoted={true} />
          </div>
        )}

        <div className={styles.commentText} dangerouslySetInnerHTML={{ __html: comment.richText?.value }}></div>
        {isDeleted && <i>{t("commentThisCommentDeleted")}</i>}
        {updateDate && (
          <p className={cn(styles.createTime, styles.updateTime)} data-tooltip-right={updateDate}>{`${t(
            "commentLastUpdateDate"
          )} ${updateDateDiff}`}</p>
        )}
      </div>
    </div>
  );
};

export default Comment;
