import Button, { ButtonVariants } from "@/components/button";
import Line from "@/components/line/Line";
import { TeamMemberDto } from "@/model/be/jinear-core";
import { selectCurrentAccountId, selectCurrentAccountsPreferredTeamRoleIsAdmin } from "@/store/slice/accountSlice";
import { useAppDispatch, useTypedSelector } from "@/store/store";
import useTranslation from "locales/useTranslation";
import React from "react";
import { GiHighKick } from "react-icons/gi";
import styles from "./TeamMemberRow.module.scss";

interface TeamMemberRowProps {
  teamMember: TeamMemberDto;
}

const TeamMemberRow: React.FC<TeamMemberRowProps> = ({ teamMember }) => {
  const { t } = useTranslation();
  const dispatch = useAppDispatch();

  const isTeamAdmin = useTypedSelector(selectCurrentAccountsPreferredTeamRoleIsAdmin);
  const currentAccountId = useTypedSelector(selectCurrentAccountId);
  const viewingHimself = teamMember.accountId == currentAccountId;

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
              //   onClick={popAreYouSureModalForDeleteMember}
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
