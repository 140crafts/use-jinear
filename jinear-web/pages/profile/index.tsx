import Button from "@/components/button";
import CommunicationPreferences from "@/components/profileScreen/communicationPreferences/CommunicationPreferences";
import PersonalInfoTab from "@/components/profileScreen/personalInfoTab/PersonalInfoTab";
import useTranslation from "locales/useTranslation";
import React from "react";
import styles from "./index.module.css";

interface ProfileScreenProps {}

const ProfileScreen: React.FC<ProfileScreenProps> = ({}) => {
  const { t } = useTranslation();

  return (
    <div className={styles.container}>
      <PersonalInfoTab />
      <CommunicationPreferences title={t("communicationPrefrencesTitle")} />
      <Button href="/debug">.</Button>
    </div>
  );
};

export default ProfileScreen;
