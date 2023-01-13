import ProfilePhoto from "@/components/profilePhoto";
import { PlainAccountProfileDto } from "@/model/be/jinear-core";
import React from "react";
import styles from "./CurrentAccountInfo.module.css";

interface CurrentAccountInfoProps {
  assignedToAccount: PlainAccountProfileDto;
}

const CurrentAccountInfo: React.FC<CurrentAccountInfoProps> = ({
  assignedToAccount,
}) => {
  return assignedToAccount.profilePicture ? (
    <ProfilePhoto
      boringAvatarKey={assignedToAccount.accountId}
      storagePath={assignedToAccount.profilePicture?.storagePath}
      wrapperClassName={styles.profilePic}
    />
  ) : (
    <div className={styles.noPicChar}>
      {assignedToAccount.username.substring(0, 1)?.toLocaleUpperCase?.()}
    </div>
  );
};

export default CurrentAccountInfo;
