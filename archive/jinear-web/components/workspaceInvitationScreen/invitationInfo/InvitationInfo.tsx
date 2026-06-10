import ProfilePhoto from "@/components/profilePhoto";
import { WorkspaceInvitationInfoResponse } from "@/model/be/jinear-core";
import useTranslation from "locales/useTranslation";
import React from "react";
import styles from "./InvitationInfo.module.css";

interface InvitationInfoProps {
  invitationInfoResponse?: WorkspaceInvitationInfoResponse;
}

const InvitationInfo: React.FC<InvitationInfoProps> = ({ invitationInfoResponse }) => {
  const { t } = useTranslation();
  return (
    <div className={styles.infoContainer}>
      <div>
        <ProfilePhoto
          boringAvatarKey={invitationInfoResponse?.data.workspaceDto?.workspaceId || ""}
          url={invitationInfoResponse?.data.workspaceDto?.profilePicture?.url}
          wrapperClassName={styles.profilePic}
        />
      </div>
      <div className={styles.title}>
        <div className={styles.accountProfilePictureContainer}>
          <ProfilePhoto
            boringAvatarKey={invitationInfoResponse?.data.accountDto?.accountId || ""}
            url={invitationInfoResponse?.data.accountDto?.profilePicture?.url}
            wrapperClassName={styles.profilePicAccount}
          />
        </div>
        {t("engageWorkspaceInvitationTitle")
          ?.replace("${fromName}", invitationInfoResponse?.data.accountDto.username || "")
          ?.replace("${workspaceName}", invitationInfoResponse?.data.workspaceDto.title || "")}
      </div>
      <div>{t("engageWorkspaceInvitationAboutJinear")}</div>
    </div>
  );
};

export default InvitationInfo;
