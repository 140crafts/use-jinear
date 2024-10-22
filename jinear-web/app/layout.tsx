import AuthCheck from "@/components/authCheck/AuthCheck";
import { PureClientOnly } from "@/components/clientOnly/ClientOnly";
import FirebaseConfigration from "@/components/firebaseConfiguration/FirebaseConfigration";
import OnboardListener from "@/components/onboardListener/OnboardListener";

import WorkspaceAndTeamChangeListener from "@/components/workspaceAndTeamChangeListener/WorkspaceAndTeamChangeListener";
import Logger from "@/utils/logger";
import { Metadata, Viewport } from "next";

import DateFnsConfigration from "@/components/dateFnsConfigration/DateFnsConfigration";
import ModalProvider from "@/components/modalProvider/ModalProvider";
import ReduxProvider from "@/components/reduxProvider/ReduxProvider";
// import Root from "@/components/root/Root";
import BodyFixer from "@/components/bodyFixer/BodyFixer";
import ErrorBoundary from "@/components/errorBoundary/ErrorBoundary";
import OfflineListener from "@/components/offlineListener/OfflineListener";
import Root from "@/components/root/Root";
import Scripts from "@/components/scripts/Scripts";
import ThemeProvider from "@/components/themeProvider/ThemeProvider";
import ToasterProvider from "@/components/toasterProvider/ToasterProvider";
import WebViewEventListener from "@/components/webViewEventListener/WebViewEventListener";
import { AxiomWebVitals } from "next-axiom";
import "../styles/app.scss";
import "../styles/fonts.css";
import { __DEV__ } from "utils/constants";
import WebsocketHandler from "@/components/websockerHandler/WebsocketHandler";
import NextTopLoader from "nextjs-toploader";
import { CSPostHogProvider } from "@/components/postHogProvider/CSPostHogProvider";
import PostHogPageView from "@/components/postHogPageView/PostHogPageView";
import OnInstallPromptEventProvider from "@/components/onInstallPromptEventProvider/OnInstallPromptEventProvider";
import React from "react";

const logger = Logger("_app");

export const viewport: Viewport = {
  themeColor: "#16171a",
  width: "device-width",
  initialScale: 1,
  maximumScale: 1,
  minimumScale: 1,
  userScalable: false,
  viewportFit: "cover",
  interactiveWidget: "resizes-content"
};

export const metadata: Metadata = {
  manifest: "/manifest.json",
  title: "Jinear",
  applicationName: "Jinear",
  description: "Lean Project Management",
  twitter: {
    title: "Jinear",
    description: "Lean Project Management",
    creator: "@usejinear"
  },
  formatDetection: {
    email: true,
    address: true,
    telephone: true
  },
  appleWebApp: {
    title: "Jinear",
    statusBarStyle: "default",
    startupImage: [
      {
        media:
          "screen and (device-width: 430px) and (device-height: 932px) and (-webkit-device-pixel-ratio: 3) and (orientation: landscape)",
        url: "images/splash_screens/iPhone_15_Pro_Max__iPhone_15_Plus__iPhone_14_Pro_Max_landscape.png"
      },
      {
        media:
          "screen and (device-width: 393px) and (device-height: 852px) and (-webkit-device-pixel-ratio: 3) and (orientation: landscape)",
        url: "images/splash_screens/iPhone_15_Pro__iPhone_15__iPhone_14_Pro_landscape.png"
      },
      {
        media:
          "screen and (device-width: 428px) and (device-height: 926px) and (-webkit-device-pixel-ratio: 3) and (orientation: landscape)",
        url: "images/splash_screens/iPhone_14_Plus__iPhone_13_Pro_Max__iPhone_12_Pro_Max_landscape.png"
      },
      {
        media:
          "screen and (device-width: 390px) and (device-height: 844px) and (-webkit-device-pixel-ratio: 3) and (orientation: landscape)",
        url: "images/splash_screens/iPhone_14__iPhone_13_Pro__iPhone_13__iPhone_12_Pro__iPhone_12_landscape.png"
      },
      {
        media:
          "screen and (device-width: 375px) and (device-height: 812px) and (-webkit-device-pixel-ratio: 3) and (orientation: landscape)",
        url: "images/splash_screens/iPhone_13_mini__iPhone_12_mini__iPhone_11_Pro__iPhone_XS__iPhone_X_landscape.png"
      },
      {
        media:
          "screen and (device-width: 414px) and (device-height: 896px) and (-webkit-device-pixel-ratio: 3) and (orientation: landscape)",
        url: "images/splash_screens/iPhone_11_Pro_Max__iPhone_XS_Max_landscape.png"
      },
      {
        media:
          "screen and (device-width: 414px) and (device-height: 896px) and (-webkit-device-pixel-ratio: 2) and (orientation: landscape)",
        url: "images/splash_screens/iPhone_11__iPhone_XR_landscape.png"
      },
      {
        media:
          "screen and (device-width: 414px) and (device-height: 736px) and (-webkit-device-pixel-ratio: 3) and (orientation: landscape)",
        url: "images/splash_screens/iPhone_8_Plus__iPhone_7_Plus__iPhone_6s_Plus__iPhone_6_Plus_landscape.png"
      },
      {
        media:
          "screen and (device-width: 375px) and (device-height: 667px) and (-webkit-device-pixel-ratio: 2) and (orientation: landscape)",
        url: "images/splash_screens/iPhone_8__iPhone_7__iPhone_6s__iPhone_6__4.7__iPhone_SE_landscape.png"
      },
      {
        media:
          "screen and (device-width: 320px) and (device-height: 568px) and (-webkit-device-pixel-ratio: 2) and (orientation: landscape)",
        url: "images/splash_screens/4__iPhone_SE__iPod_touch_5th_generation_and_later_landscape.png"
      },
      {
        media:
          "screen and (device-width: 1024px) and (device-height: 1366px) and (-webkit-device-pixel-ratio: 2) and (orientation: landscape)",
        url: "images/splash_screens/12.9__iPad_Pro_landscape.png"
      },
      {
        media:
          "screen and (device-width: 834px) and (device-height: 1194px) and (-webkit-device-pixel-ratio: 2) and (orientation: landscape)",
        url: "images/splash_screens/11__iPad_Pro__10.5__iPad_Pro_landscape.png"
      },
      {
        media:
          "screen and (device-width: 820px) and (device-height: 1180px) and (-webkit-device-pixel-ratio: 2) and (orientation: landscape)",
        url: "images/splash_screens/10.9__iPad_Air_landscape.png"
      },
      {
        media:
          "screen and (device-width: 834px) and (device-height: 1112px) and (-webkit-device-pixel-ratio: 2) and (orientation: landscape)",
        url: "images/splash_screens/10.5__iPad_Air_landscape.png"
      },
      {
        media:
          "screen and (device-width: 810px) and (device-height: 1080px) and (-webkit-device-pixel-ratio: 2) and (orientation: landscape)",
        url: "images/splash_screens/10.2__iPad_landscape.png"
      },
      {
        media:
          "screen and (device-width: 768px) and (device-height: 1024px) and (-webkit-device-pixel-ratio: 2) and (orientation: landscape)",
        url: "images/splash_screens/9.7__iPad_Pro__7.9__iPad_mini__9.7__iPad_Air__9.7__iPad_landscape.png"
      },
      {
        media:
          "screen and (device-width: 744px) and (device-height: 1133px) and (-webkit-device-pixel-ratio: 2) and (orientation: landscape)",
        url: "images/splash_screens/8.3__iPad_Mini_landscape.png"
      },
      {
        media:
          "screen and (device-width: 430px) and (device-height: 932px) and (-webkit-device-pixel-ratio: 3) and (orientation: portrait)",
        url: "images/splash_screens/iPhone_15_Pro_Max__iPhone_15_Plus__iPhone_14_Pro_Max_portrait.png"
      },
      {
        media:
          "screen and (device-width: 393px) and (device-height: 852px) and (-webkit-device-pixel-ratio: 3) and (orientation: portrait)",
        url: "images/splash_screens/iPhone_15_Pro__iPhone_15__iPhone_14_Pro_portrait.png"
      },
      {
        media:
          "screen and (device-width: 428px) and (device-height: 926px) and (-webkit-device-pixel-ratio: 3) and (orientation: portrait)",
        url: "images/splash_screens/iPhone_14_Plus__iPhone_13_Pro_Max__iPhone_12_Pro_Max_portrait.png"
      },
      {
        media:
          "screen and (device-width: 390px) and (device-height: 844px) and (-webkit-device-pixel-ratio: 3) and (orientation: portrait)",
        url: "images/splash_screens/iPhone_14__iPhone_13_Pro__iPhone_13__iPhone_12_Pro__iPhone_12_portrait.png"
      },
      {
        media:
          "screen and (device-width: 375px) and (device-height: 812px) and (-webkit-device-pixel-ratio: 3) and (orientation: portrait)",
        url: "images/splash_screens/iPhone_13_mini__iPhone_12_mini__iPhone_11_Pro__iPhone_XS__iPhone_X_portrait.png"
      },
      {
        media:
          "screen and (device-width: 414px) and (device-height: 896px) and (-webkit-device-pixel-ratio: 3) and (orientation: portrait)",
        url: "images/splash_screens/iPhone_11_Pro_Max__iPhone_XS_Max_portrait.png"
      },
      {
        media:
          "screen and (device-width: 414px) and (device-height: 896px) and (-webkit-device-pixel-ratio: 2) and (orientation: portrait)",
        url: "images/splash_screens/iPhone_11__iPhone_XR_portrait.png"
      },
      {
        media:
          "screen and (device-width: 414px) and (device-height: 736px) and (-webkit-device-pixel-ratio: 3) and (orientation: portrait)",
        url: "images/splash_screens/iPhone_8_Plus__iPhone_7_Plus__iPhone_6s_Plus__iPhone_6_Plus_portrait.png"
      },
      {
        media:
          "screen and (device-width: 375px) and (device-height: 667px) and (-webkit-device-pixel-ratio: 2) and (orientation: portrait)",
        url: "images/splash_screens/iPhone_8__iPhone_7__iPhone_6s__iPhone_6__4.7__iPhone_SE_portrait.png"
      },
      {
        media:
          "screen and (device-width: 320px) and (device-height: 568px) and (-webkit-device-pixel-ratio: 2) and (orientation: portrait)",
        url: "images/splash_screens/4__iPhone_SE__iPod_touch_5th_generation_and_later_portrait.png"
      },
      {
        media:
          "screen and (device-width: 1024px) and (device-height: 1366px) and (-webkit-device-pixel-ratio: 2) and (orientation: portrait)",
        url: "splash_screens/12.9__iPad_Pro_portrait.png"
      },
      {
        media:
          "screen and (device-width: 834px) and (device-height: 1194px) and (-webkit-device-pixel-ratio: 2) and (orientation: portrait)",
        url: "images/splash_screens/11__iPad_Pro__10.5__iPad_Pro_portrait.png"
      },
      {
        media:
          "screen and (device-width: 820px) and (device-height: 1180px) and (-webkit-device-pixel-ratio: 2) and (orientation: portrait)",
        url: "images/splash_screens/10.9__iPad_Air_portrait.png"
      },
      {
        media:
          "screen and (device-width: 834px) and (device-height: 1112px) and (-webkit-device-pixel-ratio: 2) and (orientation: portrait)",
        url: "images/splash_screens/10.5__iPad_Air_portrait.png"
      },
      {
        media:
          "screen and (device-width: 810px) and (device-height: 1080px) and (-webkit-device-pixel-ratio: 2) and (orientation: portrait)",
        url: "images/splash_screens/10.2__iPad_portrait.png"
      },
      {
        media:
          "screen and (device-width: 768px) and (device-height: 1024px) and (-webkit-device-pixel-ratio: 2) and (orientation: portrait)",
        url: "images/splash_screens/9.7__iPad_Pro__7.9__iPad_mini__9.7__iPad_Air__9.7__iPad_portrait.png"
      },
      {
        media:
          "screen and (device-width: 744px) and (device-height: 1133px) and (-webkit-device-pixel-ratio: 2) and (orientation: portrait)",
        url: "images/splash_screens/8.3__iPad_Mini_portrait.png"
      }
    ]
  },
  icons: {
    icon: [
      {
        url: "images/icon/icon-72x72.png",
        sizes: "72x72",
        type: "image/png"
      },
      {
        url: "images/icon/icon-96x96.png",
        sizes: "96x96",
        type: "image/png"
      },
      {
        url: "images/icon/icon-128x128.png",
        sizes: "128x128",
        type: "image/png"
      },
      {
        url: "images/icon/icon-144x144.png",
        sizes: "144x144",
        type: "image/png"
      },
      {
        url: "images/icon/icon-152x152.png",
        sizes: "152x152",
        type: "image/png"
      },
      {
        url: "images/icon/icon-192x192.png",
        sizes: "192x192",
        type: "image/png"
      },
      {
        url: "images/icon/icon-384x384.png",
        sizes: "384x384",
        type: "image/png"
      },
      {
        url: "images/icon/icon-512x512.png",
        sizes: "512x512",
        type: "image/png"
      }
    ],
    shortcut: "/favicon.ico",
    apple: [],
    other: {
      rel: "apple-touch-icon-precomposed",
      url: "/apple-touch-icon-precomposed.png"
    }
  }
};

function MyApp({ children }: { children: React.ReactNode }) {
  // TODO cgds-275
  // const router = useRouter();
  // router.events?.on("routeChangeStart", () => {
  //   logger.log({ routeChangeStart: router.asPath });
  // });
  // router.events?.on("routeChangeComplete", () => {
  //   logger.log({ routeChangeComplete: router.asPath });
  // });
  // usePreserveScroll();

  return (
    <html>
    <CSPostHogProvider>
      <body>
      <NextTopLoader
        color="#2299DD"
        showSpinner={false}
      />
      <Root>
        <ReduxProvider>
          <DateFnsConfigration />
          <ThemeProvider>
            <OnInstallPromptEventProvider>
              <AuthCheck />
              <WorkspaceAndTeamChangeListener />
              <OnboardListener />
              <WebViewEventListener />
              <PureClientOnly>
                {/*@ts-ignore*/}
                <ErrorBoundary message={"Firebase Configuration Failed"}>
                  <FirebaseConfigration />
                </ErrorBoundary>
                <WebsocketHandler />
              </PureClientOnly>
              {!__DEV__ && <AxiomWebVitals />}
              <PostHogPageView />
              {children}
              <ToasterProvider />
              <BodyFixer />
              <OfflineListener />
              <ModalProvider />
            </OnInstallPromptEventProvider>
          </ThemeProvider>
        </ReduxProvider>
      </Root>
      <Scripts />
      </body>
    </CSPostHogProvider>
    </html>
  );
}

export default MyApp;
