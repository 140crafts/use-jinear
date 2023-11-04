import AuthCheck from "@/components/authCheck/AuthCheck";
import { PureClientOnly } from "@/components/clientOnly/ClientOnly";
import FirebaseConfigration from "@/components/firebaseConfiguration/FirebaseConfigration";
import OnboardListener from "@/components/onboardListener/OnboardListener";
import TitleHandler from "@/components/titleHandler/TitleHandler";
import InternalWorkspacePrefChangeListener from "@/components/workspaceAndTeamChangeListener/InternalWorkspacePrefChangeListener";
import WorkspaceAndTeamChangeListener from "@/components/workspaceAndTeamChangeListener/WorkspaceAndTeamChangeListener";
import Logger from "@/utils/logger";
import { Metadata, Viewport } from "next";

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

export const metadata: Metadata = {
  manifest: "/manifest.json", // we are accessing our manifest file here
  title: "Jinear",
  applicationName: "Jinear",
  description: "Manage your tasks",
  twitter: {
    // card: 'summary_large_image',
    title: "Jinear",
    description: "Manage Your Tasks",
    // siteId: '1467726470533754880',
    creator: "@cagdastunca",
    // creatorId: '1467726470533754880',
    // images: ['https://nextjs.org/og.png'],
  },
  formatDetection: {
    email: true,
    address: true,
    telephone: true,
  },
  appleWebApp: {
    title: "Jinear",
    statusBarStyle: "default",
  },
  icons: {
    icon: [
      {
        url: "/icons/favicon-16x16.png",
        sizes: "16x16",
        type: "image/png",
      },
      {
        url: "/icons/favicon-32x32.png",
        sizes: "32x32",
        type: "image/png",
      },
    ],
    shortcut: "/favicon-new.ico",
    apple: [
      { url: "/icons/apple-touch-icon-180x180.png" },
      { url: "/icons/apple-touch-icon-76x76.png", sizes: "76x76", type: "image/png" },
      { url: "/icons/apple-touch-icon-120x120.png", sizes: "120x120", type: "image/png" },
      { url: "/icons/apple-touch-icon-152x152.png", sizes: "152x152", type: "image/png" },
      { url: "/icons/apple-touch-icon-180x180.png", sizes: "180x180", type: "image/png" },
    ],
    other: {
      rel: "apple-touch-icon-precomposed",
      url: "/apple-touch-icon-precomposed.png",
    },
  },
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
