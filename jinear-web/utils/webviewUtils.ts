import { store } from "@/store/store";
import Logger from "./logger";
import { __DEV__ } from "./constants";

const logger = Logger("WebViewUtils");

export interface IWebViewMessage {
  method:
    | "themeChange"
    | "modalVisible"
    | "pushNotificationTokenRequest"
    | "askPermissionsAndSendToken"
    | "removePushNotificationTokenRequest"
    | "askAppTrackingPermission";
  payload?: any;
}

//@ts-ignore
export const isWebView = () => typeof window !== "undefined" && window?.isWebView;

export const makeStoreAccessibleFromWindow = (_store: typeof store) => {
  //@ts-ignore
  if (typeof window !== "undefined" && (window?.isWebView || __DEV__)) {
    //@ts-ignore
    window.store = _store;
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

export const askAppTrackingPermission = () => {
  postWebviewMessage({ method: "askAppTrackingPermission" });
};

export const postWebviewMessage = (message: IWebViewMessage) => {
  // @ts-ignore
  if (typeof window !== "undefined" && window.ReactNativeWebView) {
    // @ts-ignore
    window.ReactNativeWebView.postMessage(JSON.stringify(message));
    logger.log({ postWebviewMessage: JSON.stringify(message) });
  }
};
