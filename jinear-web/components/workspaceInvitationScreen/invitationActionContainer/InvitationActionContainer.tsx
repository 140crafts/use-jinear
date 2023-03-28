import Button, { ButtonVariants } from "@/components/button";
import { WorkspaceInvitationInfoResponse } from "@/model/be/jinear-core";
import { useLogoutRequestMutation } from "@/store/api/accountApi";
import useTranslation from "locales/useTranslation";
import React from "react";
import styles from "./InvitationActionContainer.module.css";

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
  decline,
}) => {
  const { t } = useTranslation();
  const message = t("engageWorkspaceInvitationLogOutCurrentAccount")
    .replace("${currentAccountEmail}", currentAccountEmail || "")
    .replace("${invitationToEmail}", invitationInfoResponse?.data?.invitationDto?.email || "");

  const [logout, { isLoading, isError }] = useLogoutRequestMutation();

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
