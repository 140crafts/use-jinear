"use client";
import { useInitializeNotificationTargetMutation } from "@/store/api/notificationTargetApi";
import { selectAuthState, selectCurrentAccountId } from "@/store/slice/accountSlice";
import { popNotificationPermissionModal, selectAnyModalVisible } from "@/store/slice/modalSlice";
import { useAppDispatch, useTypedSelector } from "@/store/store";
import { isWebView, submitAnyModalVisibleChangeWebviewEvent } from "@/utils/webviewUtils";
import React, { useEffect, useState } from "react";

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
  const dispatch = useAppDispatch();
  const _isWebView = isWebView();
  const isAnyModalVisible = useTypedSelector(selectAnyModalVisible);
  const authState = useTypedSelector(selectAuthState);
  const currentAccountId = useTypedSelector(selectCurrentAccountId);
  const [shouldAskNotificationPermission, setShouldAskNotificationPermission] = useState<boolean>(false);
  const [initializeNotificationTarget, {}] = useInitializeNotificationTargetMutation();

  useEffect(() => {
    submitAnyModalVisibleChangeWebviewEvent(isAnyModalVisible);
  }, [isAnyModalVisible]);

  useEffect(() => {
    document.addEventListener("app-message", onMessageReceive);
    return () => document.removeEventListener("app-message", onMessageReceive);
  }, []);

  useEffect(() => {
    console.log({ _isWebView, shouldAskNotificationPermission, currentAccountId, authState });
    if (_isWebView && shouldAskNotificationPermission && currentAccountId && authState == "LOGGED_IN") {
      dispatch(popNotificationPermissionModal({ visible: true, platform: "expo-webview" }));
    } else if (authState == "NOT_LOGGED_IN") {
      // detachAccount();
    }
  }, [_isWebView, shouldAskNotificationPermission, currentAccountId, authState]);

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
    const shouldAsk = result == null || result?.permissionNeeded;
    if (shouldAsk) {
      setShouldAskNotificationPermission(shouldAsk);
    }
    if (result?.tokenData) {
      initializeNotificationTarget({ externalTargetId: result.tokenData, providerType: "EXPO", targetType: "WEBVIEW" });
    }
  };
  return null;
};

export default WebViewEventListener;
