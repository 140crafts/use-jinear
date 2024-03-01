import { ToolkitStore } from "@reduxjs/toolkit/dist/configureStore";
import Logger from "./logger";

const logger = Logger("WebViewUtils");

export interface IWebViewMessage {
  method:
    | "themeChange"
    | "modalVisible"
    | "pushNotificationTokenRequest"
    | "askPermissionsAndSendToken"
    | "removePushNotificationTokenRequest";
  payload?: any;
}

//@ts-ignore
export const isWebView = () => typeof window !== "undefined" && window?.isWebView;

export const makeStoreAccessibleFromWindow = (store: ToolkitStore) => {
  //@ts-ignore
  if (typeof window !== "undefined" && window?.isWebView) {
    //@ts-ignore
    window.store = store;
  }
};

export const submitThemeChangeWebviewEvent = (theme: "light" | "dark") => {
  postWebviewMessage({ method: "themeChange", payload: theme });
};

export const submitAnyModalVisibleChangeWebviewEvent = (visibility: boolean) => {
  postWebviewMessage({ method: "modalVisible", payload: visibility });
};

export const submitNotificationPermissionRequestWebviewEvent = () => {
  postWebviewMessage({ method: "pushNotificationTokenRequest" });
};

export const submitAskPermissionsAndSendTokenEvent = () => {
  postWebviewMessage({ method: "askPermissionsAndSendToken" });
};

export const submitRemovePushNotificationTokenRequestEvent = () => {
  postWebviewMessage({ method: "removePushNotificationTokenRequest" });
};

export const postWebviewMessage = (message: IWebViewMessage) => {
  // @ts-ignore
  if (typeof window !== "undefined" && window.ReactNativeWebView) {
    // @ts-ignore
    window.ReactNativeWebView.postMessage(JSON.stringify(message));
    logger.log({ postWebviewMessage: JSON.stringify(message) });
  }
};
