import Button, { ButtonVariants } from "@/components/button";
import Line from "@/components/line/Line";
import { FeedMemberDto } from "@/model/be/jinear-core";
import { useKickFeedMemberMutation } from "@/store/api/feedMemberApi";
import { closeDialogModal, popDialogModal } from "@/store/slice/modalSlice";
import { useAppDispatch } from "@/store/store";
import useTranslation from "locales/useTranslation";
import React from "react";
import { GiHighKick } from "react-icons/gi";
import styles from "./FeedMember.module.scss";

interface FeedMemberProps {
  data: FeedMemberDto;
  isFeedOwner: boolean;
  currentAccountId?: string;
}

const FeedMember: React.FC<FeedMemberProps> = ({ data, isFeedOwner, currentAccountId }) => {
  const { t } = useTranslation();
  const dispatch = useAppDispatch();
  const [kickFeedMember] = useKickFeedMemberMutation();

  const kickTeamMember = () => {
    kickFeedMember({ feedId: data.feedId, accountId: data.accountId });
    dispatch(closeDialogModal());
  };

  const popAreYouSureModalForDeleteMember = () => {
    dispatch(
      popDialogModal({
        visible: true,
        title: t("deleteFeedMemberAreYouSureTitle"),
        content: t("deleteFeedMemberAreYouSureText"),
        confirmButtonLabel: t("deleteFeedMemberAreYouSureConfirmLabel"),
        onConfirm: kickTeamMember,
      })
    );
  };

  return (
    <>
      <div className={styles.container}>
        <div className={styles.titleContainer}>
          <div className={styles.role}>{isFeedOwner ? t("feedMemberListItemOwner") : t(`feedMemberListItemUser`)}</div>
          <div className={styles.title}>{data.account.email}</div>
        </div>

        {isFeedOwner && currentAccountId != data.accountId && (
          <div className={styles.rightInfoContainer}>
            <Button
              variant={ButtonVariants.filled}
              className={styles.button}
              onClick={popAreYouSureModalForDeleteMember}
              data-tooltip-right={t("activeWorkspaceMemberKick")}
            >
              <div className={styles.iconContainer}>
                <GiHighKick size={14} />
              </div>
            </Button>
          </div>
        )}
      </div>
      <Line className={styles.line} />
    </>
  );
};

export default FeedMember;
