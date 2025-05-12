import Button, { ButtonVariants } from "@/components/button";
import ProfilePhoto from "@/components/profilePhoto";
import { WorkspaceInvitationInfoResponse } from "@/model/be/jinear-core";
import { changeLoginWith2FaMailModalVisibility } from "@/store/slice/modalSlice";
import { useAppDispatch } from "@/store/store";
import useTranslation from "locales/useTranslation";
import React, { useEffect } from "react";
import styles from "./NotLoggedInUserAfterInvitationAcceptedActions.module.css";

interface NotLoggedInUserAfterInvitationAcceptedActionsProps {
  invitationInfoResponse?: WorkspaceInvitationInfoResponse;
}

const NotLoggedInUserAfterInvitationAcceptedActions: React.FC<NotLoggedInUserAfterInvitationAcceptedActionsProps> = ({
                                                                                                                       invitationInfoResponse
                                                                                                                     }) => {
  const { t } = useTranslation();
  const dispatch = useAppDispatch();

  const loginWithEmailCode = () => {
    dispatch(
      changeLoginWith2FaMailModalVisibility({
        visible: true,
        autoSubmitEmail: invitationInfoResponse?.data?.invitationDto?.email
      })
    );
  };

  useEffect(() => {
    loginWithEmailCode();
  }, [invitationInfoResponse]);

  return (
    <div className={styles.container}>
      <div>
        <ProfilePhoto
          boringAvatarKey={invitationInfoResponse?.data.workspaceDto?.workspaceId || ""}
          url={invitationInfoResponse?.data.workspaceDto?.profilePicture?.url}
          wrapperClassName={styles.profilePic}
        />
      </div>
      <div
        className={styles.successTitle}
        dangerouslySetInnerHTML={{
          __html: t("engageWorkspaceInvitationAccepted")?.replace(
            "${workspaceName}",
            invitationInfoResponse?.data.workspaceDto.title || ""
          )
        }}
      />

      <div className={styles.infoText}>{t("engageWorkspaceInvitationAcceptedLoginInfoText")}</div>

      <div className={styles.actionButtonContainer}>
        <Button variant={ButtonVariants.filled2} onClick={loginWithEmailCode}>
          <b>{t("engageWorkspaceInvitationAcceptedLoginWithEmailCode")}</b>
        </Button>
        {/* <Button variant={ButtonVariants.filled} href={"/login"}>
          {t("engageWorkspaceInvitationAcceptedLoginWithPassword")}
        </Button>
        <Button variant={ButtonVariants.default} href={"/forgot-password"}>
          {t("engageWorkspaceInvitationAcceptedResetPassword")}
        </Button> */}
      </div>
    </div>
  );
};

export default NotLoggedInUserAfterInvitationAcceptedActions;
