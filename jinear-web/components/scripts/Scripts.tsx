"use client";
import { __DEV__, APPLE_CLIENT_ID, APPLE_REDIRECT_URI } from "@/utils/constants";
import Logger from "@/utils/logger";
import Script from "next/script";
import React from "react";

const logger = Logger("Scripts");

interface ScriptsProps {}

const Scripts: React.FC<ScriptsProps> = ({}) => {

  const initAppleSignIn = () => {
    try {
      if (typeof window !== "undefined" && window.AppleID && APPLE_REDIRECT_URI && APPLE_CLIENT_ID) {
        window.AppleID.auth.init({
          clientId: APPLE_CLIENT_ID,
          scope: "name email",
          redirectURI: APPLE_REDIRECT_URI,
          usePopup: true,
        });
        logger.log("Apple Sign In initialized globally");
      }
    } catch (err) {
      logger.log({ m:"Failed to initialize Apple Sign In", err });
    }
  };

  return (
    <>
      <Script
        src="https://cdn.paddle.com/paddle/paddle.js"
        onLoad={() => {
          if (__DEV__) {
            // @ts-ignore
            Paddle.Environment.set("sandbox");
          }
          // @ts-ignore
          Paddle.Setup({
            vendor: 5713,
          });
        }}
      />
      <Script src="https://player.vimeo.com/api/player.js" />
      <Script
        src="https://appleid.cdn-apple.com/appleauth/static/jsapi/appleid/1/en_US/appleid.auth.js"
        strategy="lazyOnload"
        onLoad={initAppleSignIn}
      />
    </>
  );
};

export default Scripts;
