import InvitationRetrieveInfoError from "@/components/workspaceInvitationScreen/error/InvitationRetrieveInfoError";
import InvitationActionContainer
    from "@/components/workspaceInvitationScreen/invitationActionContainer/InvitationActionContainer";
import InvitationInfo from "@/components/workspaceInvitationScreen/invitationInfo/InvitationInfo";
import InvitedAccountDifferentCurrentAccountInfo
    from "@/components/workspaceInvitationScreen/invitedAccountDifferentCurrentAccountInfo/InvitedAccountDifferentCurrentAccountInfo";
import LoadingBar from "@/components/workspaceInvitationScreen/loading/LoadingBar";
import NotLoggedInUserAfterInvitationAcceptedActions
    from "@/components/workspaceInvitationScreen/notLoggedInUserAfterInvitationAcceptedActions/NotLoggedInUserAfterInvitationAcceptedActions";
import {useRespondInvitationMutation, useRetrieveInvitationInfoQuery} from "@/store/api/workspaceMemberInvitationApi";
import {selectCurrentAccount} from "@/store/slice/accountSlice";
import {changeLoadingModalVisibility} from "@/store/slice/modalSlice";
import {useAppDispatch, useTypedSelector} from "@/store";
import {ROUTE_IF_LOGGED_IN} from "@/util/constants";
import Logger from "@/util/logger";
import useTranslation from "@/locales/useTranslation";
import React, {useEffect, useState} from "react";
import styles from "./index.module.css";
import {useNavigate, useSearchParams} from "react-router-dom";

interface WorkspaceInvitationResponseScreenProps {
}

const logger = Logger("WorkspaceInvitationResponseScreen");

const WorkspaceInvitationResponseScreen: React.FC<WorkspaceInvitationResponseScreenProps> = ({}) => {
    const {t} = useTranslation();
    const dispatch = useAppDispatch();
    const navigate = useNavigate();
    const [params] = useSearchParams();
    const [accepted, setAccepted] = useState<boolean>(false);
    const token = params?.get("token");
    const currentAccount = useTypedSelector(selectCurrentAccount);
    const {
        data: invitationInfoResponse,
        error: responseError,
        isFetching,
        isError,
    } = useRetrieveInvitationInfoQuery(token ?? '', {skip: !token});

    const currEmail = currentAccount?.email;
    const invitationFromEmail = invitationInfoResponse?.data?.accountDto?.email;
    const invitationToEmail = invitationInfoResponse?.data?.invitationDto.email;

    const isLoggedInAndViewingOthersInvitation =
        currEmail != null && invitationFromEmail != null && (currEmail == invitationFromEmail || currEmail != invitationToEmail);
    logger.log({currEmail, invitationFromEmail, invitationToEmail, isLoggedInAndViewingOthersInvitation});
    const [respondInvitation, {isLoading: isRespondLoading, isSuccess: isResponseSuccess, isError: isRespondError}] =
        useRespondInvitationMutation();

    useEffect(() => {
        dispatch(changeLoadingModalVisibility({visible: false}));
        if (isResponseSuccess && invitationInfoResponse) {
            if (accepted) {
                if (currentAccount) {
                    navigate(`/${invitationInfoResponse.data.workspaceDto.username}`);
                }
            } else {
                //declined
                if (currentAccount) {
                    navigate(ROUTE_IF_LOGGED_IN, {replace: true});
                }
            }
        }
    }, [isResponseSuccess, isRespondError, invitationInfoResponse, currentAccount]);

    const accept = () => {
        dispatch(changeLoadingModalVisibility({visible: true}));
        setAccepted(true);
        token && respondInvitation({token, accepted: true});
    };

    const decline = () => {
        dispatch(changeLoadingModalVisibility({visible: true}));
        setAccepted(false);
        token && respondInvitation({token, accepted: false});
    };

    return (
        <>
            <LoadingBar isFetching={isFetching}/>
            <InvitationRetrieveInfoError responseError={responseError} isError={isError}/>

            {!isFetching && !isError && (
                <div className={styles.container}>
                    <InvitedAccountDifferentCurrentAccountInfo
                        isLoggedInAndViewingOthersInvitation={isLoggedInAndViewingOthersInvitation}
                        currentAccountEmail={currentAccount?.email}
                    />

                    {!isResponseSuccess && (
                        <>
                            <InvitationInfo invitationInfoResponse={invitationInfoResponse}/>
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
                        <NotLoggedInUserAfterInvitationAcceptedActions invitationInfoResponse={invitationInfoResponse}/>
                    )}
                </div>
            )}
        </>
    );
};

export default WorkspaceInvitationResponseScreen;
