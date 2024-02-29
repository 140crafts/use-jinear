"use client";
import { selectAuthState, selectCurrentAccountId } from "@/store/slice/accountSlice";
import { popNotificationPermissionModal, selectAnyModalVisible } from "@/store/slice/modalSlice";
import { useAppDispatch, useTypedSelector } from "@/store/store";
import {
  isWebView,
  submitAnyModalVisibleChangeWebviewEvent,
  submitNotificationStateRequestWebviewEvent,
} from "@/utils/webviewUtils";
import React, { useEffect, useState } from "react";

interface WebViewEventListenerProps {}
interface IPushWebViewMessage {
  method: "pushNotificationTokenResulted";
  data?: any;
}

const WebViewEventListener: React.FC<WebViewEventListenerProps> = ({}) => {
  const dispatch = useAppDispatch();
  const _isWebView = isWebView();
  const isAnyModalVisible = useTypedSelector(selectAnyModalVisible);
  const authState = useTypedSelector(selectAuthState);
  const currentAccountId = useTypedSelector(selectCurrentAccountId);
  const [shouldAskNotificationPermission, setShouldAskNotificationPermission] = useState<boolean>(false);

  useEffect(() => {
    submitAnyModalVisibleChangeWebviewEvent(isAnyModalVisible);
  }, [isAnyModalVisible]);

  useEffect(() => {
    document.addEventListener("app-message", onMessageReceive);
    setTimeout(() => {
      submitNotificationStateRequestWebviewEvent();
    }, 5000);
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
    const method = message.method;
    switch (method) {
      case "pushNotificationStateResulted":
        onPushNotificationStateResulted(message?.payload?.data?.settings);
        break;
      case "pushNotificationTokenResulted":
        onPushNotificationTokenResulted(message?.payload?.data?.token);
        break;
      default:
        break;
    }
  };

  const onPushNotificationStateResulted = (settings: any) => {
    const shouldAsk = settings.status == "undetermined" && settings.canAskAgain;
    setShouldAskNotificationPermission(shouldAsk);
  };

  const onPushNotificationTokenResulted = (token: any) => {
    alert(JSON.stringify(token));
  };
  return null;
};

export default WebViewEventListener;
