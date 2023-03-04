import ProfilePhoto from "@/components/profilePhoto";
import { MediaDto } from "@/model/be/jinear-core";
import React from "react";
import styles from "./AccountProfileInfo.module.css";

interface AccountProfileInfoProps {
  accountId: string;
  email: string;
  username: string;
  profilePicture?: MediaDto | null;
}

const AccountProfileInfo: React.FC<AccountProfileInfoProps> = ({ accountId, email, username, profilePicture }) => {
  return (
    <div className={styles.container}>
      <div className={styles.infoContainer}>
        <div className={styles.username}>{username}</div>
        <div className={styles.email}>{email}</div>
      </div>
      <ProfilePhoto boringAvatarKey={accountId} storagePath={profilePicture?.storagePath} wrapperClassName={styles.profilePic} />
    </div>
  );
};

export default AccountProfileInfo;
