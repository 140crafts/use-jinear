import AccountDeleteButton from "@/components/profile-screen/accountDeleteButton/AccountDeleteButton";
import CommunicationPreferences from "@/components/profile-screen/communicationPreferences/CommunicationPreferences";
import PersonalInfoTab from "@/components/profile-screen/personalInfoTab/PersonalInfoTab";
import useTranslation from "@/locales/useTranslation";
import React from "react";
import styles from "./index.module.css";

interface ProfileScreenProps {
}

const ProfileScreen: React.FC<ProfileScreenProps> = ({}) => {
    const {t} = useTranslation();

    return (
        <div className={styles.container}>
            <div className="spacer-h-4"/>
            <PersonalInfoTab/>
            <CommunicationPreferences title={t("communicationPrefrencesTitle")}/>
            <div className="spacer-h-4"/>
            <AccountDeleteButton/>
        </div>
    );
};

export default ProfileScreen;
