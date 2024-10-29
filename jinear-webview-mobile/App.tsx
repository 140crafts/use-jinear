import Ionicons from "@expo/vector-icons/Ionicons";
import AsyncStorage from "@react-native-async-storage/async-storage";
import Constants from "expo-constants";
import * as Device from "expo-device";
import * as Notifications from "expo-notifications";
import { StatusBar, StatusBarStyle } from "expo-status-bar";
import { requestTrackingPermissionsAsync } from "expo-tracking-transparency";
import { useEffect, useRef, useState } from "react";
import { BackHandler, Dimensions, Platform, SafeAreaView, StyleSheet, TouchableOpacity, View } from "react-native";
import { SafeAreaView as SafeAreaViewFormContext } from "react-native-safe-area-context";
import WebView, { WebViewMessageEvent } from "react-native-webview";
import { ShouldStartLoadRequest } from "react-native-webview/lib/WebViewTypes";
import Logger from "./utils/logger";

interface IRetrieveExpoPushTokenResult {
  permissionNeeded?: boolean;
  tokenData?: string;
}

const logger = Logger("App");

const SCREEN_WIDTH = Dimensions.get("window").width;
const SCREEN_HEIGHT = Dimensions.get("window").height;

const DARK_THEME = {
  current: "dark" as "light" | "dark",
  statusBarStyle: "light" as StatusBarStyle,
  backgroundColor: "#1f2124",
  backgroundColorModalBackdrop: "#09090A",
  color: "#fdfffc",
};

const LIGHT_THEME = {
  current: "light" as "light" | "dark",
  statusBarStyle: "dark" as StatusBarStyle,
  backgroundColor: "#f5f5f5",
  backgroundColorModalBackdrop: "#656665",
  color: "#16171a",
};

const USER_AGENT =
  Platform.OS == "ios"
    ? "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/17.3 Safari/605.1.15"
    : "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/121.0.0.0 Safari/537.36";

// const URL = __DEV__ ? "http://localhost:3000" : "https://jinear.co";
const URL = "https://jinear.co";

Notifications.setNotificationHandler({
  handleNotification: async () => ({
    shouldShowAlert: true,
    shouldPlaySound: false,
    shouldSetBadge: false,
  }),
});

function setNotificationChannel() {
  if (Platform.OS === "android") {
    Notifications.setNotificationChannelAsync("default", {
      name: "default",
      importance: Notifications.AndroidImportance.MAX,
      vibrationPattern: [0, 250, 250, 250],
      lightColor: "#FF231F7C",
    });
  }
}

const retrieveExpoPushToken = async (): Promise<IRetrieveExpoPushTokenResult | undefined> => {
  setNotificationChannel();
  if (Device.isDevice) {
    const { status: existingStatus } = await Notifications.getPermissionsAsync();
    if (existingStatus == "granted") {
      const token = await Notifications.getExpoPushTokenAsync({
        projectId: Constants?.expoConfig?.extra?.eas.projectId,
      });
      return { tokenData: token.data };
    }
    return { permissionNeeded: true };
  }
};

const askNotificationPermissions = async () => {
  const { status: existingStatus } = await Notifications.getPermissionsAsync();
  if (existingStatus !== "granted") {
    const { status } = await Notifications.requestPermissionsAsync();
    return status;
  }
  return existingStatus;
};

function getInjectableJSMessage(message: any) {
  return `
    (function() {
      document.dispatchEvent(new MessageEvent('app-message', {
        data: ${JSON.stringify(message)}
      }));
    })();
  `;
}

interface IPushWebViewMessage {
  method: "pushNotificationTokenResulted" | "pushNotificationStateResulted";
  payload?: any;
}

const storeTheme = async (value: "light" | "dark") => {
  try {
    await AsyncStorage.setItem("theme", value);
  } catch (e) {
    // saving error
  }
};

const getTheme = async () => {
  try {
    const value = await AsyncStorage.getItem("theme");
    return value == "light" ? LIGHT_THEME : DARK_THEME;
  } catch (e) {
    // error reading value
  }
  return DARK_THEME;
};

export default function App() {
  const webViewRef = useRef<WebView>(null);
  const [theme, setTheme] = useState(DARK_THEME);
  const [webViewUrl, setWebViewUrl] = useState<string>(URL);
  const [webViewLoading, setWebViewLoading] = useState<boolean>(true);
  const [lastAppUrl, setLastAppUrl] = useState<string>();
  const [showWebViewNavBar, setShowWebViewNavBar] = useState<boolean>(false);
  const [autoIncrementingNumber, setAutoIncrementingNumber] = useState(0);

  useEffect(() => {
    setStoredTheme();
    if (Platform.OS === "android") {
      BackHandler.addEventListener("hardwareBackPress", onAndroidBackPress);
      return () => {
        BackHandler.removeEventListener("hardwareBackPress", onAndroidBackPress);
      };
    }
  }, []);

  const askAppTrackingPermission = async () => {
    await requestTrackingPermissionsAsync();
  };

  const setStoredTheme = async () => {
    const storedTheme = await getTheme();
    setTheme(storedTheme);
  };

  const onAndroidBackPress = () => {
    if (webViewRef.current) {
      webViewRef.current.goBack();
      return true; // prevent default behavior (exit app)
    }
    return false;
  };

  const onWebViewMessage = (event: WebViewMessageEvent) => {
    const message = JSON.parse(event.nativeEvent.data);
    logger.log({ message });
    switch (message.method) {
      case "themeChange":
        onThemeChangeMessage(message.payload);
        break;
      case "modalVisible":
        onModalVisibleChangeMessage(message.payload);
        break;
      case "pushNotificationTokenRequest":
        onPushNotificationTokenRequest();
        break;
      case "askPermissionsAndSendToken":
        onAskPermissionsAndSendToken();
        break;
      case "removePushNotificationTokenRequest":
        onRemovePushNotificationTokenRequest();
        break;
      case "askAppTrackingPermission":
        askAppTrackingPermission();
        break;
      default:
        break;
    }
  };

  const pushWebViewMessage = (message: IPushWebViewMessage) => {
    logger.log({ pushWebViewMessage: message });
    webViewRef.current?.injectJavaScript(getInjectableJSMessage(message));
  };

  const onThemeChangeMessage = (theme: "light" | "dark") => {
    storeTheme(theme);
    setTheme(theme == "light" ? LIGHT_THEME : DARK_THEME);
  };

  const onModalVisibleChangeMessage = (visible: boolean) => {
    setTheme((theme) => {
      return {
        ...theme,
        backgroundColor: visible
          ? theme.current == "light"
            ? LIGHT_THEME.backgroundColorModalBackdrop
            : DARK_THEME.backgroundColorModalBackdrop
          : theme.current == "light"
          ? LIGHT_THEME.backgroundColor
          : DARK_THEME.backgroundColor,
      };
    });
  };

  const onPushNotificationTokenRequest = async () => {
    const result = await retrieveExpoPushToken();
    pushWebViewMessage({ method: "pushNotificationTokenResulted", payload: { result } });
  };

  const onAskPermissionsAndSendToken = async () => {
    const status = await askNotificationPermissions();
    if (status == "granted") {
      const result = await retrieveExpoPushToken();
      pushWebViewMessage({ method: "pushNotificationTokenResulted", payload: { result } });
    }
  };

  const onRemovePushNotificationTokenRequest = async () => {
    await Notifications.unregisterForNotificationsAsync();
  };

  const loadingHandler = (syntheticEvent: any) => {
    const loading = syntheticEvent?.nativeEvent?.loading;
    const isExternal = syntheticEvent.nativeEvent.url?.indexOf(URL) == -1;
    setWebViewLoading(loading);
    if (!loading && syntheticEvent.nativeEvent.url) {
      setShowWebViewNavBar(isExternal);
    }
  };

  const _onShouldStartLoadWithRequest = (event?: ShouldStartLoadRequest) => {
    const isInternal = event?.url?.indexOf(URL) != -1;
    if (isInternal) {
      setLastAppUrl(event?.url);
    } else {
      setShowWebViewNavBar(!isInternal);
    }
    return true;
  };

  const SafeAreaToUse = Platform.OS == "ios" ? SafeAreaView : SafeAreaViewFormContext;

  return (
    <SafeAreaToUse style={{ ...styles.container, ...theme }}>
      <StatusBar style={theme.statusBarStyle} />
      {showWebViewNavBar && (
        <View style={{ ...styles.webviewNav, ...theme }}>
          <TouchableOpacity
            onPress={() => {
              if (lastAppUrl) setWebViewUrl(lastAppUrl);
            }}
          >
            <Ionicons name="close" size={24} color={theme.color} />
          </TouchableOpacity>
        </View>
      )}
      <WebView
        key={autoIncrementingNumber}
        ref={webViewRef}
        injectedJavaScriptBeforeContentLoaded={`
        window.isWebView=true;
      `}
        onMessage={onWebViewMessage}
        webviewDebuggingEnabled={true}
        onContentProcessDidTerminate={(syntheticEvent) => {
          const { nativeEvent } = syntheticEvent;
          console.warn("Content process terminated, reloading", nativeEvent);
          setAutoIncrementingNumber(autoIncrementingNumber + 1);
          try {
            webViewRef.current?.reload();
          } catch (e) {
            console.error(e);
          }
        }}
        onRenderProcessGone={(syntheticEvent) => {
          const { nativeEvent } = syntheticEvent;
          setAutoIncrementingNumber(autoIncrementingNumber + 1);
          try {
            webViewRef.current?.reload();
          } catch (e) {
            console.error(e);
          }
        }}
        bounces={false}
        overScrollMode="content"
        style={styles.webview}
        userAgent={USER_AGENT}
        source={{ uri: webViewUrl }}
        onLoadStart={loadingHandler}
        onLoadEnd={loadingHandler}
        onShouldStartLoadWithRequest={_onShouldStartLoadWithRequest}
      />
    </SafeAreaToUse>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: DARK_THEME.backgroundColor,
    justifyContent: "center",
  },
  webviewNav: {
    padding: 7.5,
  },
  webview: {
    height: SCREEN_HEIGHT,
    width: SCREEN_WIDTH,
    backgroundColor: DARK_THEME.backgroundColor,
  },
});
