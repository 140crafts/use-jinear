export const __DEV__ = !process.env.NODE_ENV || process.env.NODE_ENV === "development";
export const HOST = __DEV__ ? "http://localhost:3000" : "https://jinear.140crafts.com";

export const ROUTE_IF_LOGGED_IN = "/home";
