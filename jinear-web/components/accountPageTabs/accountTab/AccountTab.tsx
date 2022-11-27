import React from "react";
import styles from "./AccountTab.module.css";
import ChangePasswordCard from "./changePasswordCard/ChangePasswordCard";
import PersonalInfoCard from "./personalInfoCard/PersonalInfoCard";

interface AccountTabProps {}

const AccountTab: React.FC<AccountTabProps> = ({}) => {
  return (
    <div className={styles.container}>
      <PersonalInfoCard />
      <ChangePasswordCard />
    </div>
  );
};

export default AccountTab;
