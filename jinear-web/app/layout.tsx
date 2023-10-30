import AuthCheck from "@/components/authCheck/AuthCheck";
import { PureClientOnly } from "@/components/clientOnly/ClientOnly";
import FirebaseConfigration from "@/components/firebaseConfiguration/FirebaseConfigration";
import OnboardListener from "@/components/onboardListener/OnboardListener";
import TitleHandler from "@/components/titleHandler/TitleHandler";
import InternalWorkspacePrefChangeListener from "@/components/workspaceAndTeamChangeListener/InternalWorkspacePrefChangeListener";
import WorkspaceAndTeamChangeListener from "@/components/workspaceAndTeamChangeListener/WorkspaceAndTeamChangeListener";
import Logger from "@/utils/logger";
import { Viewport } from "next";

import DateFnsConfigration from "@/components/dateFnsConfigration/DateFnsConfigration";
import MainHeader from "@/components/mainHeader/MainHeader";
import ModalProvider from "@/components/modalProvider/ModalProvider";
import ReduxProvider from "@/components/reduxProvider/ReduxProvider";
// import Root from "@/components/root/Root";
import BodyFixer from "@/components/bodyFixer/BodyFixer";
import OfflineListener from "@/components/offlineListener/OfflineListener";
import Root from "@/components/root/Root";
import ThemeProvider from "@/components/themeProvider/ThemeProvider";
import ToasterProvider from "@/components/toasterProvider/ToasterProvider";
import "../styles/app.css";

const logger = Logger("_app");

export const viewport: Viewport = {
  themeColor: "#16171a",
  width: "device-width",
  initialScale: 1,
  maximumScale: 1,
  minimumScale: 1,
  userScalable: false,
  viewportFit: "cover",
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
      <MainHeader />
      <body>
        <Root>
          <ReduxProvider>
            <DateFnsConfigration />
            <ThemeProvider>
              <TitleHandler />
              <AuthCheck />
              <InternalWorkspacePrefChangeListener />
              <WorkspaceAndTeamChangeListener />
              <OnboardListener />
              <PureClientOnly>
                <FirebaseConfigration />
              </PureClientOnly>
              <PureClientOnly>{/* <SseListener /> */}</PureClientOnly>
              {children}
              <ToasterProvider />
              <BodyFixer />
              <OfflineListener />
              <ModalProvider />
            </ThemeProvider>
          </ReduxProvider>
        </Root>
      </body>
    </html>
  );
}

export default MyApp;
