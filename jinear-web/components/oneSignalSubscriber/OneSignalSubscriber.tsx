import { NotificationMessageExternalDataDto } from "@/model/be/jinear-core";
import { useInitializeNotificationTargetMutation } from "@/store/api/notificationTargetApi";
import { selectAuthState, selectCurrentAccountId } from "@/store/slice/accountSlice";
import { popNotificationPermissionModal } from "@/store/slice/modalSlice";
import { useAppDispatch, useTypedSelector } from "@/store/store";
import { __DEV__ } from "@/utils/constants";
import Logger from "@/utils/logger";
import React, { useEffect, useState } from "react";
import OneSignal from "react-onesignal";

interface OneSignalSubscriberProps {}

const logger = Logger("OneSignalSubscriber");

const ONE_SIGNAL_IDS = __DEV__
  ? {
      appId: "a36e9464-5bbf-42f0-bdc6-bd8925cb30da",
      safari_web_id: "web.onesignal.auto.5f257b48-e003-493b-a584-79b35812c24b",
    }
  : {
      appId: "932e9bfe-a922-4c6e-b6dd-64b1f49ce6a7",
      safari_web_id: "web.onesignal.auto.3b8b9214-66ac-44d1-a7fb-a9dc856242cb",
    };

const OneSignalSubscriber: React.FC<OneSignalSubscriberProps> = ({}) => {
  const dispatch = useAppDispatch();
  const authState = useTypedSelector(selectAuthState);
  const [oneSignalInitialized, setOneSignalInitialized] = useState<boolean>(false);
  const currentAccountId = useTypedSelector(selectCurrentAccountId);

  const [initializeNotificationTarget, {}] = useInitializeNotificationTargetMutation();

  useEffect(() => {
    initializeOneSignal();
  }, []);

  useEffect(() => {
    if (authState == "LOGGED_IN") {
      if (currentAccountId) {
        checkAndPrompt(currentAccountId);
      } else {
        detachAccount();
      }
    }
  }, [currentAccountId, authState]);

  const attachAccount = async (accountId: string) => {
    OneSignal.setSubscription(true);
    const userId = await OneSignal.getUserId();
    OneSignal.setExternalUserId(accountId);
    logger.log(`Setting OneSignal account. accountId: ${accountId}, oneSignalUserId: ${userId}`);
    if (userId) {
      logger.log(`Attach notification target api call has started.`);
      initializeNotificationTarget({ externalTargetId: userId });
    }
  };

  const detachAccount = async () => {
    OneSignal.setSubscription(false);
    const userId = await OneSignal.getUserId();
    OneSignal.removeExternalUserId();
    logger.log(`Detach OneSignal account. oneSignalUserId: ${userId}`);
    if (userId) {
      logger.log(`Detach notification target api call has started.`);
    }
  };

  const initializeOneSignal = async () => {
    if (oneSignalInitialized) {
      return;
    }
    logger.log("Initialize OneSignal has started.");
    setOneSignalInitialized(true);
    await OneSignal.init({
      ...ONE_SIGNAL_IDS,
      notifyButton: {
        enable: false,
      },
      subdomainName: "jinear",
      allowLocalhostAsSecureOrigin: true,
    });
    OneSignal.on("notificationDisplay", onNotificationDisplay);
    logger.log("Initialize OneSignal has completed.");
  };

  const checkAndPrompt = async (currentAccountId: string) => {
    const notificationPermission = await OneSignal.getNotificationPermission();
    if (notificationPermission == "default") {
      dispatch(popNotificationPermissionModal());
      return;
    }
    attachAccount(currentAccountId);
  };

  const onNotificationDisplay = (eventData: Notification) => {
    const data: NotificationMessageExternalDataDto = eventData.data;
    logger.log({ notificationData: data });
  };

  return null;
};

export default OneSignalSubscriber;
