import Button, { ButtonVariants } from "@/components/button";
import Line from "@/components/line/Line";
import { TeamMemberDto } from "@/model/be/jinear-core";
import { useKickMemberFromTeamMutation } from "@/store/api/teamMemberApi";
import { selectCurrentAccountId, selectCurrentAccountsPreferredTeamRoleIsAdmin } from "@/store/slice/accountSlice";
import { closeDialogModal, popDialogModal } from "@/store/slice/modalSlice";
import { useAppDispatch, useTypedSelector } from "@/store/store";
import Logger from "@/utils/logger";
import useTranslation from "locales/useTranslation";
import React from "react";
import { GiHighKick } from "react-icons/gi";
import styles from "./TeamMemberRow.module.scss";

interface TeamMemberRowProps {
  teamMember: TeamMemberDto;
}

const logger = Logger("TeamMemberRow");

const TeamMemberRow: React.FC<TeamMemberRowProps> = ({ teamMember }) => {
  const { t } = useTranslation();
  const dispatch = useAppDispatch();

  const isTeamAdmin = useTypedSelector(selectCurrentAccountsPreferredTeamRoleIsAdmin);
  const currentAccountId = useTypedSelector(selectCurrentAccountId);
  const viewingHimself = teamMember.accountId == currentAccountId;

  const [kickMemberFromTeam, {}] = useKickMemberFromTeamMutation();

  const kickTeamMember = () => {
    logger.log({ kickTeamMember: teamMember });
    kickMemberFromTeam({ teamMemberId: teamMember.teamMemberId });
    dispatch(closeDialogModal());
  };

  const popAreYouSureModalForDeleteMember = () => {
    dispatch(
      popDialogModal({
        visible: true,
        title: t("deleteTeamMemberAreYouSureTitle"),
        content: t("deleteTeamMemberAreYouSureText"),
        confirmButtonLabel: t("deleteTeamMemberAreYouSureConfirmLabel"),
        onConfirm: kickTeamMember,
      })
    );
  };

  return (
    <>
      <div className={styles.container}>
        <div className={styles.titleContainer}>
          <div className={styles.role}>{t(`teamMemberRole_${teamMember.role}`)}</div>
          <div className={styles.title}>{teamMember.account.email}</div>
        </div>

        {isTeamAdmin && !viewingHimself && (
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

export default TeamMemberRow;
