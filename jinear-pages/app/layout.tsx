import AuthCheck from "@/components/authCheck/AuthCheck";
import FirebaseConfigration from "@/components/firebaseConfiguration/FirebaseConfigration";
import OnboardListener from "@/components/onboardListener/OnboardListener";

import WorkspaceAndTeamChangeListener from "@/components/workspaceAndTeamChangeListener/WorkspaceAndTeamChangeListener";
import Logger from "@/utils/logger";
import {Viewport} from "next";

import DateFnsConfigration from "@/components/dateFnsConfigration/DateFnsConfigration";
import ModalProvider from "@/components/modalProvider/ModalProvider";
import ReduxProvider from "@/components/reduxProvider/ReduxProvider";
import BodyFixer from "@/components/bodyFixer/BodyFixer";
import ErrorBoundary from "@/components/errorBoundary/ErrorBoundary";
import OfflineListener from "@/components/offlineListener/OfflineListener";
import Root from "@/components/root/Root";
import Scripts from "@/components/scripts/Scripts";
import ThemeProvider from "@/components/themeProvider/ThemeProvider";
import ToasterProvider from "@/components/toasterProvider/ToasterProvider";
import WebViewEventListener from "@/components/webViewEventListener/WebViewEventListener";
import {AxiomWebVitals} from "next-axiom";
import "../styles/app.scss";
import "../styles/fonts.css";
import {__DEV__} from "utils/constants";
import WebsocketHandler from "@/components/websockerHandler/WebsocketHandler";
import NextTopLoader from "nextjs-toploader";
import {CSPostHogProvider} from "@/components/postHogProvider/CSPostHogProvider";
import PostHogPageView from "@/components/postHogPageView/PostHogPageView";
import OnInstallPromptEventProvider from "@/components/onInstallPromptEventProvider/OnInstallPromptEventProvider";
import React from "react";
import styles from "./layout.module.scss";
import ProjectFeedLayoutHeader from "@/components/projectFeedLayoutHeader/ProjectFeedLayoutHeader";
import DynamicTitleUpdateHandler from "@/components/dynamicTitleUpdateHandler/DynamicTitleUpdateHandler";

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

function MyApp({ children }: { children: React.ReactNode }) {
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
          <DynamicTitleUpdateHandler />
          <DateFnsConfigration />
          <ThemeProvider>
            <OnInstallPromptEventProvider>
              <AuthCheck />
              <WorkspaceAndTeamChangeListener />
              <OnboardListener />
              <WebViewEventListener />
              {/*@ts-ignore*/}
              <ErrorBoundary message={"Firebase Configuration Failed"}>
                <FirebaseConfigration />
              </ErrorBoundary>
              <WebsocketHandler />
              {!__DEV__ && <AxiomWebVitals />}
              <PostHogPageView />

              <div id="project-feed-layout-container" className={styles.container}>
                <div id="project-feed-layout-header" className={styles.header}>
                  <ProjectFeedLayoutHeader />
                </div>
                <div id="project-feed-layout-content" className={styles.content}>
                  <div id="workspace-layout-page-content" className={styles.pageContent}>
                    {children}
                  </div>
                </div>
              </div>

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
