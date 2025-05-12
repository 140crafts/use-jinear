import Button, { ButtonVariants } from "@/components/button";
import { WorkspaceInvitationInfoResponse } from "@/model/be/jinear-core";
import { useLogoutMutation } from "@/store/api/authApi";
import useTranslation from "locales/useTranslation";
import React, { useEffect } from "react";
import styles from "./InvitationActionContainer.module.css";
import { resetAllStates, useAppDispatch } from "@/store/store";

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

  const [logout, { isLoading, isError, isSuccess }] = useLogoutMutation();

  useEffect(() => {
    if (isSuccess) {
      resetAllStates(dispatch);
    }
  }, [dispatch, isSuccess]);

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
