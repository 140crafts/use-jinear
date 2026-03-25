"use client";
import { env } from "next-runtime-env";

export const __DEV__ = !process.env.NODE_ENV || process.env.NODE_ENV === "development";

export const HOST = env("NEXT_PUBLIC_HOST") != null ? env("NEXT_PUBLIC_HOST") : __DEV__ ? "http://localhost:3000" : "https://jinear.co";
// export const HOST = "http://localhost:3000";

export const SERVER = __DEV__ ? "staging.api" : "api";

export const API_ROOT = env("NEXT_PUBLIC_API_ROOT") != null ? env("NEXT_PUBLIC_API_ROOT") : __DEV__ ? "http://localhost:8085/" : `https://${SERVER}.jinear.co/`;

// export const SOCKET_ROOT = (!process.env.NODE_ENV || process.env.NODE_ENV === "development") ? "ws://localhost:3001/" : "https://message.jinear.co/";
export const SOCKET_ROOT = env("NEXT_PUBLIC_SOCKET_ROOT") != null ? env("NEXT_PUBLIC_SOCKET_ROOT") : __DEV__ ? "ws://localhost:3001/" : "https://message.jinear.co/";

export const ROUTE_IF_LOGGED_IN = "/home";
export const PROJECT_FEED_URL = HOST + "/shared/[workspaceUsername]/feed/[accessKey]";

export const APP_STORE_URL = "https://apps.apple.com/us/app/jinear/id6478519984";
export const PLAY_STORE_URL = "https://play.google.com/store/apps/details?id=co.jinear.app";

export const GOD_MODE_WHITELIST = ["01gp94s0sk9q4g8g3m9jpsvd0t"];
export const isInGodModeWhitelist = (accountId?: string) => accountId != undefined && GOD_MODE_WHITELIST.indexOf(accountId) != -1;

export const PADDLE_VENDOR_ID = __DEV__ ? 5713 : 145466;
export const PADDLE_CATALOG = {
  business_daily: { sandbox: 63817, prod: -1, price: "$3.30" },
  business_monthly: { sandbox: 63716, prod: 848738, price: "$24.90" },
  business_yearly: { sandbox: 63717, prod: 848737, price: "$249" }
};

// Apple Sign In Configuration
export const APPLE_CLIENT_ID = env("NEXT_PUBLIC_APPLE_CLIENT_ID");//"co.jinear.app.web"
export const APPLE_REDIRECT_URI = env("NEXT_PUBLIC_APPLE_REDIRECT_URI");//"https://jinear.co"
