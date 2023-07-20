import { __DEV__ } from "@/utils/constants";
import Head from "next/head";
import React from "react";

interface MainHeaderProps {}

const MainHeader: React.FC<MainHeaderProps> = ({}) => {
  return (
    <>
      <Head>
        <meta charSet="utf-8" />
        <meta
          name="viewport"
          content="minimum-scale=1, initial-scale=1, width=device-width, shrink-to-fit=no, user-scalable=no, viewport-fit=cover"
        />
        <meta name="application-name" content="Jinear" />
        <meta name="apple-mobile-web-app-capable" content="yes" />
        <meta name="apple-mobile-web-app-status-bar-style" content="default" />
        <meta name="apple-mobile-web-app-title" content="Jinear" />
        <meta name="description" content="Manage your tasks" />
        <meta name="format-detection" content="telephone=no" />
        <meta name="mobile-web-app-capable" content="yes" />
        <meta name="theme-color" content="#16171a" />

        <link rel="apple-touch-icon" href="/icons/apple-touch-icon-180x180.png" />
        <link rel="apple-touch-icon" sizes="152x152" href="/icons/apple-touch-icon-152x152.png" />
        <link rel="apple-touch-icon" sizes="180x180" href="/icons/apple-touch-icon-180x180.png" />
        <link rel="apple-touch-icon" sizes="120x120" href="/icons/apple-touch-icon-120x120.png" />
        <link rel="apple-touch-icon" sizes="76x76" href="/icons/apple-touch-icon-76x76.png" />

        <link rel="icon" type="image/png" sizes="32x32" href="/icons/favicon-32x32.png" />
        <link rel="icon" type="image/png" sizes="16x16" href="/icons/favicon-16x16.png" />
        <link rel="manifest" href="/manifest.json" />
        {/* <link rel="mask-icon" href="/icons/safari-pinned-tab.svg" color="#5bbad5" /> */}
        <link rel="shortcut icon" href="/favicon-new.ico" />
        <link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Roboto:300,400,500" />

        <meta name="twitter:card" content="summary" />
        <meta name="twitter:url" content="https://jinear.co" />
        <meta name="twitter:title" content="Jinear" />
        <meta name="twitter:description" content="Manage your tasks easily" />
        <meta name="twitter:image" content="https://jinear.co/icons/android-chrome-192x192.png" />
        <meta name="twitter:creator" content="@cagdastunca" />
        <meta property="og:type" content="website" />
        <meta property="og:title" content="Jinear" />
        <meta property="og:description" content="Manage your tasks easily" />
        <meta property="og:site_name" content="Jinear" />
        <meta property="og:url" content="https://jinear.co" />
        <meta property="og:image" content="https://jinear.co/icons/apple-touch-icon-180x180.png" />

        <link rel="preload" href="/images/gif/loading-1.gif" as="image" />
        <link rel="preload" href="/images/gif/loading-2.gif" as="image" />
      </Head>

      {!__DEV__ && (
        <>
          <script
            defer
            type="text/javascript"
            src="https://datapulse.app/datapulse.min.js"
            id="datapulse"
            data-endpoint="https://datapulse.app/api/v1/event"
            data-workspace="clkammqdb2jf8e937nd3n2ow7"
          ></script>
        </>
      )}
    </>
  );
};

export default MainHeader;
