import Button, { ButtonVariants } from "@/components/button";
import Line from "@/components/line/Line";
import { WorkspaceMemberDto } from "@/model/be/jinear-core";
import { useKickMemberFromWorkspaceMutation } from "@/store/api/workspaceMemberApi";
import { selectCurrentAccountId } from "@/store/slice/accountSlice";
import { closeDialogModal, popDialogModal } from "@/store/slice/modalSlice";
import { useAppDispatch, useTypedSelector } from "@/store/store";
import Logger from "@/utils/logger";
import useTranslation from "locales/useTranslation";
import React from "react";
import { GiHighKick } from "react-icons/gi";
import styles from "./MemberRow.module.scss";

interface MemberRowProps {
  member: WorkspaceMemberDto;
  workspaceRoleIsAdminOrOwner: boolean;
}

const logger = Logger("MemberRow");

const MemberRow: React.FC<MemberRowProps> = ({ member, workspaceRoleIsAdminOrOwner }) => {
  const { t } = useTranslation();
  const dispatch = useAppDispatch();
  const [kickMemberFromWorkspace, {}] = useKickMemberFromWorkspaceMutation();
  const currentAccountId = useTypedSelector(selectCurrentAccountId);
  const viewingHimself = member.accountId == currentAccountId;

  const deleteWorkspaceMember = () => {
    logger.log({ deleteWorkspaceMember: member });
    kickMemberFromWorkspace({ workspaceId: member.workspaceId, workspaceMemberId: member.workspaceMemberId });
    dispatch(closeDialogModal());
  };

  const popAreYouSureModalForDeleteMember = () => {
    dispatch(
      popDialogModal({
        visible: true,
        title: t("deleteWorkspaceMemberAreYouSureTitle"),
        content: t("deleteWorkspaceMemberAreYouSureText"),
        confirmButtonLabel: t("deleteWorkspaceMemberAreYouSureConfirmLabel"),
        onConfirm: deleteWorkspaceMember,
      })
    );
  };

  return (
    <>
      <div className={styles.container}>
        <div className={styles.titleContainer}>
          <div className={styles.role}>{t(`workspaceMemberRole_${member.role}`)}</div>
          <div className={styles.title}>{member.account.email}</div>
        </div>

        {workspaceRoleIsAdminOrOwner && !viewingHimself && (
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

export default MemberRow;
