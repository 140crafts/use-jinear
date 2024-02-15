"use client";
import { api } from "@/store/api/api";
import { useInitializeNotificationTargetMutation } from "@/store/api/notificationTargetApi";
import { selectAuthState, selectCurrentAccountId, selectCurrentSessionId } from "@/store/slice/accountSlice";
import { selectFirebase, selectMessaging, setFirebase, setMessaging } from "@/store/slice/firebaseSlice";
import { popNotificationPermissionModal } from "@/store/slice/modalSlice";
import { markHasUnreadNotification } from "@/store/slice/taskAdditionalDataSlice";
import { useAppDispatch, useTypedSelector } from "@/store/store";
import Logger from "@/utils/logger";
import { initializeApp } from "firebase/app";
import { MessagePayload, deleteToken, getMessaging, getToken, onMessage } from "firebase/messaging";
import { useRouter } from "next/navigation";
import React, { useEffect } from "react";
import { toast } from "react-hot-toast";
import ForegroundNotification from "../foregroundNotification/ForegroundNotification";

interface FirebaseConfigrationProps {}

const logger = Logger("FirebaseConfigration");

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

const firebaseConfig = {
  apiKey: "AIzaSyBZq8Pg2pDDweDSNqTwdrCR-xBe1mJGBco",
  authDomain: "jinear-f3ab4.firebaseapp.com",
  projectId: "jinear-f3ab4",
  storageBucket: "jinear-f3ab4.appspot.com",
  messagingSenderId: "72155538781",
  appId: "1:72155538781:web:767cb1558cd358cfacf4b4",
  measurementId: "G-FMXGQ5XM95",
};

export const VAPID_PUBLIC_KEY = "BFO8Qjsa5Y1W32XyMCa8owjYxkCziaKzl8M2TzMZuHKbEPmtSeowuZzPhdot9aMC64qr7zGRpzrCyg6MzN5nkQc";

const FirebaseConfigration: React.FC<FirebaseConfigrationProps> = ({}) => {
  const dispatch = useAppDispatch();
  const router = useRouter();
  const authState = useTypedSelector(selectAuthState);
  const currentAccountId = useTypedSelector(selectCurrentAccountId);
  const currentSessionId = useTypedSelector(selectCurrentSessionId);

  const firebaseApp = useTypedSelector(selectFirebase);
  const messaging = useTypedSelector(selectMessaging);

  const [initializeNotificationTarget, {}] = useInitializeNotificationTargetMutation();

  useEffect(() => {
    initializeFirebase();
  }, []);

  useEffect(() => {
    if (messaging && currentSessionId) {
      onMessage(messaging, onForegroundMessage);
    }
  }, [messaging, currentSessionId]);

  useEffect(() => {
    if (firebaseApp) {
      if (firebaseApp && currentAccountId && authState == "LOGGED_IN") {
        checkAndPrompt(currentAccountId);
      } else if (firebaseApp && messaging && authState == "NOT_LOGGED_IN") {
        detachAccount();
      }
    }
  }, [currentAccountId, authState, firebaseApp, messaging]);

  const initializeFirebase = () => {
    console.log(`initializeFirebase has started.`);
    const app = initializeApp(firebaseConfig);
    const messaging = getMessaging(firebaseApp);
    dispatch(setFirebase(app));
    dispatch(setMessaging(messaging));
  };

  const checkAndPrompt = async (currentAccountId: string) => {
    const notificationPermission = Notification.permission;
    if (notificationPermission == "default") {
      dispatch(popNotificationPermissionModal());
      return;
    } else if (notificationPermission == "granted" && currentAccountId) {
      attachAccount(currentAccountId);
    }
  };

  const attachAccount = async (accountId: string) => {
    try {
      if (messaging) {
        const currentFirebaseToken = await getToken(messaging, { vapidKey: VAPID_PUBLIC_KEY });
        logger.log(
          `Firebase token retrieved attaching now. accountId: ${accountId}, currentFirebaseToken: ${currentFirebaseToken}`
        );
        if (currentFirebaseToken) {
          logger.log(`Attach notification target api call has started.`);
          initializeNotificationTarget({ externalTargetId: currentFirebaseToken, providerType: "FIREBASE" });
        }
      }
    } catch (e) {
      console.error(e);
      setTimeout(() => {
        attachAccount(accountId);
      }, 2500);
    }
  };

  const detachAccount = async () => {
    logger.log(`Detach firebase messaging has started.`);
    if (messaging) {
      try {
        deleteToken(messaging);
      } catch (e) {
        console.error(e);
      }
    }
  };

  const onForegroundMessage = (payload: MessagePayload) => {
    logger.log({ onForegroundMessage: payload });
    if (payload.notification) {
      const { title = "", body = "" } = payload.notification;
      const launchUrl = payload?.data?.launchUrl;
      const senderSessionInfoId = payload?.data?.senderSessionInfoId;
      if (currentSessionId != senderSessionInfoId) {
        toast((t) => <ForegroundNotification title={title} body={body} launchUrl={launchUrl} />, {
          position: window.innerWidth < 768 ? "top-center" : "top-right",
          duration: 6000,
        });
        const notification = new Notification(title, { body, icon: "https://jinear.co/icons/notification-icon.png" });
        if (launchUrl) {
          notification.addEventListener("click", () => {
            router.push(launchUrl);
          });
        }
      }

      const notificationType = payload?.data?.notificationType || "";
      dispatch(api.util.invalidateTags(["v1/notification/event/{workspaceId}/unread-count"]));
      if (notificationType == "TASK_INITIALIZED") {
        dispatch(api.util.invalidateTags(["v1/task/list/filter", "v1/calendar/event/filter"]));
      }
      if (
        currentSessionId != senderSessionInfoId &&
        payload?.data?.taskId &&
        TASK_UPDATE_NOTIFICATIONS.indexOf(notificationType) != -1
      ) {
        dispatch(markHasUnreadNotification({ taskId: payload?.data?.taskId }));
      }
    }
  };

  return null;
};

export default FirebaseConfigration;
