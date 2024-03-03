"use client";
import { useInitializeNotificationTargetMutation } from "@/store/api/notificationTargetApi";
import { selectAuthState, selectCurrentAccountId } from "@/store/slice/accountSlice";
import { selectAnyModalVisible } from "@/store/slice/modalSlice";
import { useTypedSelector } from "@/store/store";
import {
  isWebView,
  submitAnyModalVisibleChangeWebviewEvent,
  submitAskPermissionsAndSendTokenEvent,
  submitRemovePushNotificationTokenRequestEvent,
} from "@/utils/webviewUtils";
import React, { useEffect } from "react";

interface WebViewEventListenerProps {}
interface IPushWebViewMessage {
  method: "pushNotificationTokenResulted";
  data?: any;
}

interface IRetrieveExpoPushTokenResult {
  permissionNeeded?: boolean;
  tokenData?: string;
}

const WebViewEventListener: React.FC<WebViewEventListenerProps> = ({}) => {
  const _isWebView = isWebView();
  const isAnyModalVisible = useTypedSelector(selectAnyModalVisible);
  const authState = useTypedSelector(selectAuthState);
  const currentAccountId = useTypedSelector(selectCurrentAccountId);
  const [initializeNotificationTarget, {}] = useInitializeNotificationTargetMutation();

  useEffect(() => {
    submitAnyModalVisibleChangeWebviewEvent(isAnyModalVisible);
  }, [isAnyModalVisible]);

  useEffect(() => {
    document.addEventListener("app-message", onMessageReceive);
    return () => document.removeEventListener("app-message", onMessageReceive);
  }, []);

  useEffect(() => {
    if (_isWebView && currentAccountId && authState == "LOGGED_IN") {
      submitAskPermissionsAndSendTokenEvent();
    } else if (authState == "NOT_LOGGED_IN") {
      submitRemovePushNotificationTokenRequestEvent();
    }
  }, [_isWebView, currentAccountId, authState]);

  const onMessageReceive = (message: any) => {
    console.log({ onMessageReceive: message });
    const method = message?.data?.method;
    switch (method) {
      case "pushNotificationTokenResulted":
        onPushNotificationTokenResulted(message?.data?.payload?.result);
        break;
      default:
        break;
    }
  };

  const onPushNotificationTokenResulted = (result?: IRetrieveExpoPushTokenResult) => {
    if (result?.tokenData) {
      initializeNotificationTarget({ externalTargetId: result.tokenData, providerType: "EXPO", targetType: "WEBVIEW" });
    }
  };
  return null;
};

export default WebViewEventListener;
