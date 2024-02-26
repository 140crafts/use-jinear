import Ionicons from "@expo/vector-icons/Ionicons";
import { StatusBar, StatusBarStyle } from "expo-status-bar";
import { useEffect, useRef, useState } from "react";
import { BackHandler, Dimensions, Platform, SafeAreaView, StyleSheet, TouchableOpacity, View } from "react-native";
import WebView, { WebViewMessageEvent } from "react-native-webview";

import { ShouldStartLoadRequest } from "react-native-webview/lib/WebViewTypes";
import Logger from "./utils/logger";

const logger = Logger("App");

const SCREEN_WIDTH = Dimensions.get("window").width;
const SCREEN_HEIGHT = Dimensions.get("window").height;

const DARK_THEME = {
  current: "dark" as "light" | "dark",
  statusBarStyle: "light" as StatusBarStyle,
  backgroundColor: "#16171a",
  backgroundColorModalBackdrop: "#09090A",
  color: "#fdfffc",
};

const LIGHT_THEME = {
  current: "light" as "light" | "dark",
  statusBarStyle: "dark" as StatusBarStyle,
  backgroundColor: "#fdfffc",
  backgroundColorModalBackdrop: "#656665",
  color: "#16171a",
};

const USER_AGENT =
  Platform.OS == "ios"
    ? "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/17.3 Safari/605.1.15"
    : "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/121.0.0.0 Safari/537.36";

const URL = "http://localhost:3000";
// const URL = "https://jinear.co";

export default function App() {
  const webViewRef = useRef<WebView>(null);
  const [theme, setTheme] = useState(DARK_THEME);
  const [webViewUrl, setWebViewUrl] = useState<string>(URL);
  const [webViewLoading, setWebViewLoading] = useState<boolean>(true);
  const [lastAppUrl, setLastAppUrl] = useState<string>();
  const [showWebViewNavBar, setShowWebViewNavBar] = useState<boolean>(false);

  useEffect(() => {
    if (Platform.OS === "android") {
      BackHandler.addEventListener("hardwareBackPress", onAndroidBackPress);
      return () => {
        BackHandler.removeEventListener("hardwareBackPress", onAndroidBackPress);
      };
    }
  }, []);

  const onAndroidBackPress = () => {
    if (webViewRef.current) {
      webViewRef.current.goBack();
      return true; // prevent default behavior (exit app)
    }
    return false;
  };

  const onWebViewMessage = (event: WebViewMessageEvent) => {
    const message = JSON.parse(event.nativeEvent.data);
    switch (message.method) {
      case "themeChange":
        onThemeChangeMessage(message.payload);
        break;
      case "modalVisible":
        onModalVisibleChangeMessage(message.payload);
        break;
      default:
        break;
    }
  };

  const onThemeChangeMessage = (theme: string) => {
    logger.log({ onThemeChangeMessage: theme });
    setTheme(theme == "light" ? LIGHT_THEME : DARK_THEME);
  };

  const onModalVisibleChangeMessage = (visible: boolean) => {
    logger.log({ onModalVisibleChangeMessage: visible });
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
    logger.log({ on: "onShouldStartLoadWithRequest", isInternal: isInternal, url: event?.url });
    if (isInternal) {
      logger.log({ setLastAppUrl: event?.url });
      setLastAppUrl(event?.url);
    } else {
      setShowWebViewNavBar(!isInternal);
    }
    return true;
  };

  return (
    <SafeAreaView style={{ ...styles.container, ...theme }}>
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
        ref={webViewRef}
        injectedJavaScriptBeforeContentLoaded={`
        window.isWebView=true;
      `}
        onMessage={onWebViewMessage}
        webviewDebuggingEnabled={true}
        bounces={false}
        overScrollMode="content"
        style={styles.webview}
        userAgent={USER_AGENT}
        source={{ uri: webViewUrl }}
        onLoadStart={loadingHandler}
        onLoadEnd={loadingHandler}
        onShouldStartLoadWithRequest={_onShouldStartLoadWithRequest}
      />
    </SafeAreaView>
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
