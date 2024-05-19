"use client";
export const __DEV__ = !process.env.NODE_ENV || process.env.NODE_ENV === "development";
export const HOST = __DEV__ ? "http://localhost:3000" : "https://jinear.co";

export const SERVER = __DEV__ ? "staging.api" : "api";
export const API_ROOT = __DEV__ ? "http://localhost:8085/" : `https://${SERVER}.jinear.co/`;
// : "http://localhost:8085/";
export const SOCKET_ROOT = (!process.env.NODE_ENV || process.env.NODE_ENV === "development") ? "ws://localhost:3001/" : "https://message.jinear.co/";

export const S3_BASE = "https://storage.googleapis.com/jinear-b0/";

export const ROUTE_IF_LOGGED_IN = "/home";
