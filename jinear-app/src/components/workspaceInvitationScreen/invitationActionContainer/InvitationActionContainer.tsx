import Button, { ButtonVariants } from "@/components/button";
import type { WorkspaceInvitationInfoResponse } from "@/model/be/jinear-core";
import { useLogoutMutation } from "@/store/api/authApi";
import useTranslation from "@/locales/useTranslation";
import React from "react";
import styles from "./InvitationActionContainer.module.css";
import { performLogoutCleanup, useAppDispatch } from "@/store";
import Logger from "@/util/logger";

const logger = Logger("InvitationActionContainer");

interface InvitationActionContainerProps {
  isRespondLoading: boolean;
  isLoggedInAndViewingOthersInvitation: boolean;
  invitationInfoResponse?: WorkspaceInvitationInfoResponse;
  currentAccountEmail?: string;
  accept: () => void;
  decline: () => void;
}

const InvitationActionContainer: React.FC<InvitationActionContainerProps> = ({
                                                                               isRespondLoading,
                                                                               isLoggedInAndViewingOthersInvitation,
                                                                               invitationInfoResponse,
                                                                               currentAccountEmail,
                                                                               accept,
                                                                               decline
                                                                             }) => {
  const { t } = useTranslation();
  const dispatch = useAppDispatch();
  const message = t("engageWorkspaceInvitationLogOutCurrentAccount")
    .replace("${currentAccountEmail}", currentAccountEmail || "")
    .replace("${invitationToEmail}", invitationInfoResponse?.data?.invitationDto?.email || "");

  const [logoutCall] = useLogoutMutation();

  const logout = async () => {
    try {
      await logoutCall().unwrap();
    } catch (error) {
      // Server-side logout failed (offline, dead session) — still drop everything local.
      logger.error({ message: "Logout call failed", error });
    }
    await performLogoutCleanup(dispatch);
  };

  return (
    <div className={styles.container}>
      {isLoggedInAndViewingOthersInvitation ? (
        <>
          <div dangerouslySetInnerHTML={{ __html: message }} />
          <Button disabled={isRespondLoading} variant={ButtonVariants.contrast} onClick={logout}>
            {t("engageWorkspaceInvitationLogout")}
          </Button>
        </>
      ) : (
        <>
          <Button disabled={isRespondLoading} variant={ButtonVariants.contrast} onClick={accept}>
            {t("engageWorkspaceInvitationAcceptButton")}
          </Button>
          <Button disabled={isRespondLoading} variant={ButtonVariants.filled} onClick={decline}>
            {t("engageWorkspaceInvitationDeclineButton")}
          </Button>
        </>
      )}
    </div>
  );
};

export default InvitationActionContainer;
