import { NotificationMessageExternalDataDto } from "@/model/be/jinear-core";
import { api } from "@/store/api/api";
import { useInitializeNotificationTargetMutation } from "@/store/api/notificationTargetApi";
import { selectAuthState, selectCurrentAccountId } from "@/store/slice/accountSlice";
// import { popNotificationPermissionModal } from "@/store/slice/modalSlice";
import { markHasUnreadNotification } from "@/store/slice/taskAdditionalDataSlice";
import { useAppDispatch, useTypedSelector } from "@/store/store";
import { __DEV__ } from "@/utils/constants";
import Logger from "@/utils/logger";
import React, { useEffect, useState } from "react";
import OneSignal from "react-onesignal";

interface OneSignalSubscriberProps {}

const logger = Logger("OneSignalSubscriber");

const TASK_UPDATE_NOTIFICATIONS = [
  "EDIT_TASK_TITLE",
  "EDIT_TASK_DESC",
  "TASK_UPDATE_TOPIC",
  "TASK_UPDATE_WORKFLOW_STATUS",
  "TASK_CHANGE_ASSIGNEE",
  "TASK_CHANGE_ASSIGNED_DATE",
  "TASK_CHANGE_DUE_DATE",
  "RELATION_INITIALIZED",
  "RELATION_REMOVED",
  "CHECKLIST_INITIALIZED",
  "CHECKLIST_REMOVED",
  "CHECKLIST_TITLE_CHANGED",
  "CHECKLIST_ITEM_CHECKED_STATUS_CHANGED",
  "CHECKLIST_ITEM_LABEL_CHANGED",
  "CHECKLIST_ITEM_REMOVED",
  "CHECKLIST_ITEM_INITIALIZED",
];

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
  const [oneSignalInitialized, setOneSignalInitialized] = useState<"not-started" | "started" | "completed" | "failed">(
    "not-started"
  );
  const currentAccountId = useTypedSelector(selectCurrentAccountId);

  const [initializeNotificationTarget, {}] = useInitializeNotificationTargetMutation();

  useEffect(() => {
    initializeOneSignal();
  }, []);

  useEffect(() => {
    if (oneSignalInitialized == "completed") {
      if (authState == "LOGGED_IN" && currentAccountId) {
        checkAndPrompt(currentAccountId);
      } else if (authState == "NOT_LOGGED_IN") {
        detachAccount();
      }
    }
  }, [currentAccountId, authState, oneSignalInitialized]);

  const initializeOneSignal = async () => {
    if (oneSignalInitialized != "not-started") {
      return;
    }
    logger.log("Initialize OneSignal has started.");
    setOneSignalInitialized("started");
    try {
      const config = {
        ...ONE_SIGNAL_IDS,
        notifyButton: {
          enable: false,
        },
        allowLocalhostAsSecureOrigin: true,
      };
      logger.log({ oneSignalConfig: config });
      await OneSignal.init(config);
      setOneSignalInitialized("completed");
      OneSignal.on("notificationDisplay", onNotificationDisplay);
      logger.log("Initialize OneSignal has completed.");
    } catch (ex) {
      logger.log("Initialize OneSignal has failed.");
      console.error(ex);
      setOneSignalInitialized("failed");
    }
  };

  const checkAndPrompt = async (currentAccountId: string) => {
    const notificationPermission = await OneSignal.getNotificationPermission();
    if (notificationPermission == "default") {
      // dispatch(popNotificationPermissionModal());
      return;
    }
    attachAccount(currentAccountId);
  };

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

  const onNotificationDisplay = (eventData: Notification) => {
    try {
      const data: NotificationMessageExternalDataDto = eventData.data;
      logger.log({ notificationData: data });
      const notificationType = data.notificationType;
      dispatch(api.util.invalidateTags(["account-workspace-notification-unread-count"]));
      if (notificationType == "TASK_INITIALIZED") {
        dispatch(api.util.invalidateTags(["team-task-list", "team-workflow-task-list", "workspace-task-list"]));
      }
      if (data.taskId && TASK_UPDATE_NOTIFICATIONS.indexOf(notificationType) != -1) {
        dispatch(markHasUnreadNotification({ taskId: data.taskId }));
      }
    } catch (e) {
      console.error(e);
      logger.log({ onNotificationDisplayError: e });
    }
  };

  return null;
};

export default OneSignalSubscriber;
