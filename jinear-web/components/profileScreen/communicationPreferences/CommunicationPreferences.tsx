import useTranslation from "locales/useTranslation";
import React from "react";
import SettingsCheckbox from "../settingsCheckbox/SettingsCheckbox";
import styles from "./CommunicationPreferences.module.css";

interface CommunicationPreferencesProps {}

const CommunicationPreferences: React.FC<CommunicationPreferencesProps> = ({}) => {
  const { t } = useTranslation();

  return (
    <div className={styles.container}>
      <h3>{t("communicationPrefrencesTitle")}</h3>
      <SettingsCheckbox id={"communication-prefrences-email"} label={t("communicationPrefrencesEmail")} checked={true} />
      <SettingsCheckbox
        id={"communication-prefrences-push-notifications"}
        label={t("communicationPrefrencesPushNotifications")}
        checked={false}
      />
    </div>
  );
};

export default CommunicationPreferences;
