"use client";
export const __DEV__ = !process.env.NODE_ENV || process.env.NODE_ENV === "development";
export const HOST = __DEV__ ? "http://localhost:3000" : "https://jinear.co";

export const SERVER = __DEV__ ? "staging.api" : "api";
// export const API_ROOT = __DEV__ ? "http://localhost:8085/" : `https://${SERVER}.jinear.co/`;
export const API_ROOT = "http://localhost:8085/";

// : "http://localhost:8085/";
export const SOCKET_ROOT = (!process.env.NODE_ENV || process.env.NODE_ENV === "development") ? "ws://localhost:3001/" : "https://message.jinear.co/";

// export const S3_BASE = "https://storage.googleapis.com/jinear-b0/";
export const S3_BASE = "https://files.jinear.co/jinear-b0/";

export const ROUTE_IF_LOGGED_IN = "/";

export const APP_STORE_URL = "https://apps.apple.com/us/app/jinear/id6478519984";
export const PLAY_STORE_URL = "https://play.google.com/store/apps/details?id=co.jinear.app";

export const GOD_MODE_WHITELIST = ["01gp94s0sk9q4g8g3m9jpsvd0t"];
export const isInGodModeWhitelist = (accountId?: string) => accountId != undefined && GOD_MODE_WHITELIST.indexOf(accountId) != -1;
