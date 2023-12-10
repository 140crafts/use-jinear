"use client";
import { __DEV__ } from "@/utils/constants";
import Script from "next/script";
import React from "react";

interface MainHeaderProps {}

const MainHeader: React.FC<MainHeaderProps> = ({}) => {
  return (
    <>
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
    </>
  );
};

export default MainHeader;
