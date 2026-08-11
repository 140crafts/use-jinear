import { useRetrievePermissionsQuery, useSetPermissionsMutation } from "@/store/api/accountCommunicationPermissionApi";
import { changeLoadingModalVisibility, popNotificationPermissionModal } from "@/store/slice/modalSlice";
import { useAppDispatch } from "@/store";
import useTranslation from "@/locales/useTranslation";
import React, { useEffect, useState } from "react";
import SettingsCheckbox from "../settingsCheckbox/SettingsCheckbox";
import styles from "./CommunicationPreferences.module.css";
import Button, { ButtonHeight, ButtonVariants } from "@/components/button";
import { removeLocalStorage } from "@/hooks/useLocalStorage";
import {
  isNotificationSupported,
  NOTIFICATIONS_REJECT_KEY,
  onNotificationPermissionChange,
  readNotificationPermission,
} from "@/util/notificationPermission";

interface CommunicationPreferencesProps {
  title?: string;
}

type BrowserPermissionState = NotificationPermission | "unsupported" | "unknown";

const CommunicationPreferences: React.FC<CommunicationPreferencesProps> = ({ title }) => {
  const { t } = useTranslation();
  const dispatch = useAppDispatch();

  const { data: permissionsResponse, isFetching } = useRetrievePermissionsQuery();
  const [setPermissions, { isLoading: isSetPermissionsLoading }] = useSetPermissionsMutation();
  const [browserPermission, setBrowserPermission] = useState<BrowserPermissionState>("unknown");

  useEffect(() => {
    dispatch(changeLoadingModalVisibility({ visible: isSetPermissionsLoading }));
  }, [isSetPermissionsLoading]);

  useEffect(() => {
    if (!isNotificationSupported()) {
      setBrowserPermission("unsupported");
      return;
    }
    let cancelled = false;
    void readNotificationPermission().then((permission) => {
      if (!cancelled) {
        setBrowserPermission(permission);
      }
    });
    const unsubscribe = onNotificationPermissionChange(setBrowserPermission);
    return () => {
      cancelled = true;
      unsubscribe();
    };
  }, []);

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

  // Re-opens the permission modal, which owns the whole grant + token registration
  // flow. Clearing the reject flag also restores the automatic prompt on next open.
  const onEnableBrowserNotifications = () => {
    removeLocalStorage({ key: NOTIFICATIONS_REJECT_KEY });
    dispatch(popNotificationPermissionModal({ visible: true, platform: "web" }));
  };

  const browserPermissionText = () => {
    switch (browserPermission) {
      case "granted":
        return t("communicationPrefrencesBrowserNotificationsEnabled");
      case "denied":
        return t("communicationPrefrencesBrowserNotificationsBlocked");
      case "unsupported":
        return t("communicationPrefrencesBrowserNotificationsUnsupported");
      default:
        return t("communicationPrefrencesBrowserNotificationsDisabled");
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

      {browserPermission != "unknown" && (
        <div className={styles.browserNotifications}>
          <div className={styles.browserNotificationsText}>
            <h3>{t("communicationPrefrencesBrowserNotifications")}</h3>
            <span>{browserPermissionText()}</span>
          </div>
          {browserPermission == "default" && (
            <Button
              heightVariant={ButtonHeight.short}
              variant={ButtonVariants.filled}
              onClick={onEnableBrowserNotifications}
            >
              {t("communicationPrefrencesBrowserNotificationsEnableButton")}
            </Button>
          )}
        </div>
      )}
    </div>
  );
};

export default CommunicationPreferences;
