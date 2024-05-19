import Button, { ButtonHeight, ButtonVariants } from "@/components/button";
import Line from "@/components/line/Line";
import { ChannelMemberDto, ChannelMemberRoleType, ThreadDto, WorkspaceMemberDto } from "@/model/be/jinear-core";
import { useKickMemberFromWorkspaceMutation } from "@/store/api/workspaceMemberApi";
import { selectCurrentAccountId } from "@/store/slice/accountSlice";
import { closeDialogModal, popDialogModal } from "@/store/slice/modalSlice";
import { useAppDispatch, useTypedSelector } from "@/store/store";
import Logger from "@/utils/logger";
import useTranslation from "locales/useTranslation";
import React from "react";
import { GiHighKick } from "react-icons/gi";
import styles from "./MemberRow.module.scss";
import {
  useAuthorizeChannelMemberMutation,
  useRemoveChannelMemberMutation,
  useUnAuthorizeChannelMemberMutation
} from "@/api/channelMemberApi";
import { LuCrown, LuX } from "react-icons/lu";
import strings from "@/locals/strings";

interface MemberRowProps {
  member: ChannelMemberDto;
  currentUserRole: ChannelMemberRoleType;
}

const logger = Logger("MemberRow");

const AUTHORIZE_TOOLTIP_MAP = {
  "MEMBER": "makeChannelMemberAdmin",
  "ADMIN": "makeChannelMemberMember",
  "OWNER": "makeChannelMemberMember"
};

const MemberRow: React.FC<MemberRowProps> = ({ member, currentUserRole }) => {
  const { t } = useTranslation();
  const dispatch = useAppDispatch();
  const [removeChannelMember, {}] = useRemoveChannelMemberMutation();
  const [authorizeChannelMember, { isLoading: isAuthorizeChannelMemberLoading }] = useAuthorizeChannelMemberMutation();
  const [unAuthorizeChannelMember, { isLoading: isUnAuthorizeChannelMemberLoading }] = useUnAuthorizeChannelMemberMutation();
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

  const toggleMembershipRole = () => {
    const req = { accountId: member.accountId, channelId: member.channelId };
    const method = (["ADMIN", "OWNER"].includes(member.roleType)) ? unAuthorizeChannelMember : authorizeChannelMember;
    method(req);
  };

  const tooltip = AUTHORIZE_TOOLTIP_MAP[member.roleType] as keyof typeof strings;

  return (
    <>
      <div className={styles.container}>
        <div className={styles.titleContainer}>
          <div className={styles.role}>{t(`channelMemberRole_${member.roleType}`)}</div>
          <div className={styles.title}>{member.account?.email || member.account?.username}</div>
        </div>

        {["OWNER", "ADMIN"].includes(currentUserRole) && !viewingHimself && (
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

            <Button
              variant={ButtonVariants.filled}
              className={styles.button}
              onClick={toggleMembershipRole}
              data-tooltip-right={t(tooltip)}
              heightVariant={ButtonHeight.short}
              loading={isAuthorizeChannelMemberLoading || isUnAuthorizeChannelMemberLoading}
              disabled={isAuthorizeChannelMemberLoading || isUnAuthorizeChannelMemberLoading}
            >
              <div className={styles.iconContainer}>
                {["OWNER", "ADMIN"].includes(member.roleType) && <LuX size={14} />}
                <LuCrown size={14} />
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
