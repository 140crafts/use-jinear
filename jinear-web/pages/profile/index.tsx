import CommunicationPreferences from "@/components/profileScreen/communicationPreferences/CommunicationPreferences";
import React from "react";
import styles from "./index.module.css";

interface ProfileScreenProps {}

const ProfileScreen: React.FC<ProfileScreenProps> = ({}) => {
  return (
    <div className={styles.container}>
      <CommunicationPreferences />
    </div>
  );
};

export default ProfileScreen;
