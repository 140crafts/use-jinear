import { CommentDto, ProjectPostCommentDto } from "@/model/be/jinear-core";
import { differenceInDays, differenceInHours, differenceInMinutes, format } from "date-fns";
import useTranslation from "locales/useTranslation";
import React, { useMemo } from "react";
import styles from "./CommentFooter.module.css";

interface CommentFooterProps {
  comment: ProjectPostCommentDto;
}

const CommentFooter: React.FC<CommentFooterProps> = ({ comment }) => {
  const { t } = useTranslation();

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
  }, [t, comment.lastUpdatedDate]);

  return (
    <div className={styles.container}>
      <span data-tooltip-right={`${t("taskCommentCreateDate")} ${createdDate}`}>{dateDiff}</span>
      &nbsp;
      {updateDate && (
        <span data-tooltip-right={`${t("taskCommentUpdateDate")} ${updateDate}`}>{"~ " + updateDateDiff}&nbsp;</span>
      )}
      <b>{"- " + comment.account.username}</b>
    </div>
  );
};

export default CommentFooter;
