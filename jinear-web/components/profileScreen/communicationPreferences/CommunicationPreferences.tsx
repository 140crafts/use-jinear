import { useRetrievePermissionsQuery, useSetPermissionsMutation } from "@/store/api/accountCommunicationPermissionApi";
import { changeLoadingModalVisibility } from "@/store/slice/modalSlice";
import { useAppDispatch } from "@/store/store";
import useTranslation from "locales/useTranslation";
import React, { useEffect } from "react";
import SettingsCheckbox from "../settingsCheckbox/SettingsCheckbox";
import styles from "./CommunicationPreferences.module.css";

interface CommunicationPreferencesProps {
  title?: string;
}

const CommunicationPreferences: React.FC<CommunicationPreferencesProps> = ({ title }) => {
  const { t } = useTranslation();
  const dispatch = useAppDispatch();

  const { data: permissionsResponse, isFetching } = useRetrievePermissionsQuery();
  const [setPermissions, { isLoading: isSetPermissionsLoading }] = useSetPermissionsMutation();

  useEffect(() => {
    dispatch(changeLoadingModalVisibility({ visible: isFetching || isSetPermissionsLoading }));
  }, [isFetching, isSetPermissionsLoading]);

  const onEmailPrefChange = (permitted: boolean) => {
    if (permissionsResponse) {
      setPermissions({ ...permissionsResponse.data, email: permitted });
    }
  };

  const onPushNotifPrefChange = (permitted: boolean) => {
    if (permissionsResponse) {
      setPermissions({ ...permissionsResponse.data, pushNotification: permitted });
    }
  };

  return (
    <div className={styles.container}>
      {title && <h2>{title}</h2>}
      <SettingsCheckbox
        id={"communication-prefrences-email"}
        label={t("communicationPrefrencesEmail")}
        text={t("communicationPrefrencesEmailText")}
        checked={permissionsResponse?.data?.email || false}
        onChange={onEmailPrefChange}
      />

      <SettingsCheckbox
        id={"communication-prefrences-push-notifications"}
        label={t("communicationPrefrencesPushNotifications")}
        text={t("communicationPrefrencesPushNotificationsText")}
        checked={permissionsResponse?.data?.pushNotification || false}
        onChange={onPushNotifPrefChange}
      />
    </div>
  );
};

export default CommunicationPreferences;
