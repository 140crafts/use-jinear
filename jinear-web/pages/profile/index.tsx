import CommunicationPreferences from "@/components/profileScreen/communicationPreferences/CommunicationPreferences";
import PersonalInfoTab from "@/components/profileScreen/personalInfoTab/PersonalInfoTab";
import { selectCurrentAccountsPreferredTeam, selectCurrentAccountsPreferredWorkspace } from "@/store/slice/accountSlice";
import { useTypedSelector } from "@/store/store";
import useTranslation from "locales/useTranslation";
import React from "react";
import styles from "./index.module.css";

interface ProfileScreenProps {}

const ProfileScreen: React.FC<ProfileScreenProps> = ({}) => {
  const { t } = useTranslation();

  const currentWorkspace = useTypedSelector(selectCurrentAccountsPreferredWorkspace);
  const currentTeam = useTypedSelector(selectCurrentAccountsPreferredTeam);

  return (
    <div className={styles.container}>
      <PersonalInfoTab />
      <CommunicationPreferences title={t("communicationPrefrencesTitle")} />
    </div>
  );
};

export default ProfileScreen;
