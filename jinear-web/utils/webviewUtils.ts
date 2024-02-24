import { ToolkitStore } from "@reduxjs/toolkit/dist/configureStore";
import Logger from "./logger";

const logger = Logger("WebViewUtils");

export interface IWebViewMessage {
  method: "themeChange" | "modalVisible";
  payload?: any;
}

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

export const postWebviewMessage = (message: IWebViewMessage) => {
  // @ts-ignore
  if (typeof window !== "undefined" && window.ReactNativeWebView) {
    // @ts-ignore
    window.ReactNativeWebView.postMessage(JSON.stringify(message));
    logger.log({ postWebviewMessage: JSON.stringify(message) });
  }
};
