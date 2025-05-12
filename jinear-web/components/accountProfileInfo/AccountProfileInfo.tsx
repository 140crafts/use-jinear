"use client";
import ProfilePhoto from "@/components/profilePhoto";
import { AccessibleMediaDto } from "@/model/be/jinear-core";
import React from "react";
import styles from "./AccountProfileInfo.module.css";

interface AccountProfileInfoProps {
  accountId: string;
  email: string;
  username: string;
  profilePicture?: AccessibleMediaDto | null;
}

const AccountProfileInfo: React.FC<AccountProfileInfoProps> = ({ accountId, email, username, profilePicture }) => {
  return (
    <div className={styles.container}>
      <div className={styles.infoContainer}>
        <div className={styles.username}>{username}</div>
        <div className={styles.email}>{email}</div>
      </div>
      <div className={styles.profilePicContainer}>
        <ProfilePhoto
          boringAvatarKey={accountId}
          url={profilePicture?.url}
          wrapperClassName={styles.profilePic}
        />
      </div>
    </div>
  );
};

export default AccountProfileInfo;
