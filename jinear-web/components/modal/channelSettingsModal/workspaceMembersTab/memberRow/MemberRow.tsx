import Button, { ButtonHeight, ButtonVariants } from "@/components/button";
import Line from "@/components/line/Line";
import { ChannelMemberDto, WorkspaceMemberDto } from "@/model/be/jinear-core";
import { useKickMemberFromWorkspaceMutation } from "@/store/api/workspaceMemberApi";
import { selectCurrentAccountId } from "@/store/slice/accountSlice";
import { closeDialogModal, popDialogModal } from "@/store/slice/modalSlice";
import { useAppDispatch, useTypedSelector } from "@/store/store";
import Logger from "@/utils/logger";
import useTranslation from "locales/useTranslation";
import React from "react";
import { GiHighKick } from "react-icons/gi";
import styles from "./MemberRow.module.scss";
import { useRemoveChannelMemberMutation } from "@/api/channelMemberApi";

interface MemberRowProps {
  member: ChannelMemberDto;
}

const logger = Logger("MemberRow");

const MemberRow: React.FC<MemberRowProps> = ({ member }) => {
  const { t } = useTranslation();
  const dispatch = useAppDispatch();
  const [removeChannelMember, {}] = useRemoveChannelMemberMutation();
  const currentAccountId = useTypedSelector(selectCurrentAccountId);
  const viewingHimself = member.accountId == currentAccountId;

  const deleteChannelMember = () => {
    logger.log({ deleteChannelMember: member });
    removeChannelMember({ accountId: member.accountId, channelId: member.channelId });
    dispatch(closeDialogModal());
  };

  const popAreYouSureModalForDeleteMember = () => {
    dispatch(
      popDialogModal({
        visible: true,
        title: t("deleteChannelMemberAreYouSureTitle"),
        content: t("deleteChannelMemberAreYouSureText"),
        confirmButtonLabel: t("deleteChannelMemberAreYouSureConfirmLabel"),
        onConfirm: deleteChannelMember
      })
    );
  };

  return (
    <>
      <div className={styles.container}>
        <div className={styles.titleContainer}>
          <div className={styles.role}>{t(`channelMemberRole_${member.roleType}`)}</div>
          <div className={styles.title}>{member.account?.email || member.account?.username}</div>
        </div>

        {["OWNER", "ADMIN"].includes(member.roleType) && !viewingHimself && (
          <div className={styles.rightInfoContainer}>
            <Button
              variant={ButtonVariants.filled}
              className={styles.button}
              onClick={popAreYouSureModalForDeleteMember}
              data-tooltip-right={t("activeWorkspaceMemberKick")}
              heightVariant={ButtonHeight.short}
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

export default MemberRow;
