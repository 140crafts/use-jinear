import useTranslation from "locales/useTranslation";
import React from "react";
import styles from "./InvitedAccountDifferentCurrentAccountInfo.module.css";

interface InvitedAccountDifferentCurrentAccountInfoProps {
  isLoggedInAndViewingOthersInvitation: boolean;
  currentAccountEmail?: string;
}

const InvitedAccountDifferentCurrentAccountInfo: React.FC<InvitedAccountDifferentCurrentAccountInfoProps> = ({
  isLoggedInAndViewingOthersInvitation,
  currentAccountEmail,
}) => {
  const { t } = useTranslation();
  return isLoggedInAndViewingOthersInvitation ? (
    <div className={styles.loggedInAsContainer}>
      <div>{t("engageWorkspaceInvitationYoureLoggedInAs")}</div>
      <b>{currentAccountEmail}</b>
    </div>
  ) : null;
};

export default InvitedAccountDifferentCurrentAccountInfo;
