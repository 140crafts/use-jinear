import { PureClientOnly } from "@/components/clientOnly/ClientOnly";
import InvitationRetrieveInfoError from "@/components/workspaceInvitationScreen/error/InvitationRetrieveInfoError";
import InvitationActionContainer from "@/components/workspaceInvitationScreen/invitationActionContainer/InvitationActionContainer";
import InvitationInfo from "@/components/workspaceInvitationScreen/invitationInfo/InvitationInfo";
import InvitedAccountDifferentCurrentAccountInfo from "@/components/workspaceInvitationScreen/invitedAccountDifferentCurrentAccountInfo/InvitedAccountDifferentCurrentAccountInfo";
import LoadingBar from "@/components/workspaceInvitationScreen/loading/LoadingBar";
import NotLoggedInUserAfterInvitationAcceptedActions from "@/components/workspaceInvitationScreen/notLoggedInUserAfterInvitationAcceptedActions/NotLoggedInUserAfterInvitationAcceptedActions";
import { useUpdatePreferredWorkspaceMutation } from "@/store/api/workspaceDisplayPreferenceApi";
import { useRespondInvitationMutation, useRetrieveInvitationInfoQuery } from "@/store/api/workspaceMemberInvitationApi";
import { selectCurrentAccount } from "@/store/slice/accountSlice";
import { changeLoadingModalVisibility } from "@/store/slice/modalSlice";
import { useAppDispatch, useTypedSelector } from "@/store/store";
import { ROUTE_IF_LOGGED_IN } from "@/utils/constants";
import Logger from "@/utils/logger";
import useTranslation from "locales/useTranslation";
import { useRouter } from "next/router";
import React, { useEffect, useState } from "react";
import styles from "./index.module.css";

interface WorkspaceInvitationResponseScreenProps {}

const logger = Logger("WorkspaceInvitationResponseScreen");

const WorkspaceInvitationResponseScreen: React.FC<WorkspaceInvitationResponseScreenProps> = ({}) => {
  const { t } = useTranslation();
  const dispatch = useAppDispatch();
  const router = useRouter();
  const [accepted, setAccepted] = useState<boolean>(false);
  const token: string = router.query?.token as string;
  const currentAccount = useTypedSelector(selectCurrentAccount);
  const {
    data: invitationInfoResponse,
    error: responseError,
    isFetching,
    isError,
  } = useRetrieveInvitationInfoQuery(token, { skip: !token });

  const currEmail = currentAccount?.email;
  const invitationFromEmail = invitationInfoResponse?.data?.accountDto?.email;
  const invitationToEmail = invitationInfoResponse?.data?.invitationDto.email;

  const isLoggedInAndViewingOthersInvitation =
    currEmail != null && invitationFromEmail != null && (currEmail == invitationFromEmail || currEmail != invitationToEmail);
  logger.log({ currEmail, invitationFromEmail, invitationToEmail, isLoggedInAndViewingOthersInvitation });
  const [respondInvitation, { isLoading: isRespondLoading, isSuccess: isResponseSuccess, isError: isRespondError }] =
    useRespondInvitationMutation();

  const [
    updatePreferredWorkspace,
    { isLoading: isUpdatePreferredWorkspaceLoading, isSuccess: isUpdatePreferredWorkspaceSuccess },
  ] = useUpdatePreferredWorkspaceMutation();

  useEffect(() => {
    dispatch(changeLoadingModalVisibility({ visible: false }));
    if (isResponseSuccess && invitationInfoResponse && accepted) {
      updatePreferredWorkspace({ workspaceId: invitationInfoResponse.data.workspaceDto.workspaceId });
      if (currentAccount) {
        // router.replace(ROUTE_IF_LOGGED_IN);
      }
    }
    if (isResponseSuccess && invitationInfoResponse && !accepted) {
      router.replace(ROUTE_IF_LOGGED_IN);
    }
  }, [isResponseSuccess, isRespondError, invitationInfoResponse, currentAccount]);

  const accept = () => {
    dispatch(changeLoadingModalVisibility({ visible: true }));
    setAccepted(true);
    respondInvitation({ token, accepted: true });
  };

  const decline = () => {
    dispatch(changeLoadingModalVisibility({ visible: true }));
    setAccepted(false);
    respondInvitation({ token, accepted: false });
  };

  return (
    <PureClientOnly>
      <LoadingBar isFetching={isFetching} />
      <InvitationRetrieveInfoError responseError={responseError} isError={isError} />

      {!isFetching && !isError && (
        <div className={styles.container}>
          <InvitedAccountDifferentCurrentAccountInfo
            isLoggedInAndViewingOthersInvitation={isLoggedInAndViewingOthersInvitation}
            currentAccountEmail={currentAccount?.email}
          />

          {!isResponseSuccess && (
            <>
              <InvitationInfo invitationInfoResponse={invitationInfoResponse} />
              <InvitationActionContainer
                isRespondLoading={isRespondLoading}
                accept={accept}
                decline={decline}
                invitationInfoResponse={invitationInfoResponse}
                isLoggedInAndViewingOthersInvitation={isLoggedInAndViewingOthersInvitation}
                currentAccountEmail={currentAccount?.email}
              />
            </>
          )}
          {!currentAccount && isResponseSuccess && invitationInfoResponse && accepted && (
            <NotLoggedInUserAfterInvitationAcceptedActions invitationInfoResponse={invitationInfoResponse} />
          )}
        </div>
      )}
    </PureClientOnly>
  );
};

export default WorkspaceInvitationResponseScreen;
