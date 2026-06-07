"use client";

import Logger from "@/utils/logger";

export type FEATURE_FLAG =
  "EASTER_EGG_LOADING"
  | "EASTER_EGG_NETWORK"
  | "LOGIN_WITH_GOOGLE"
  | "FEEDS"
  | "MESSAGING"
  | "PROJECTS"
  | "EXTENDED_SIDE_MENU_TEAM_ACTION_BUTTONS_VISIBLE";

const logger = Logger("useFeatureFlag");

export const useFeatureFlag = (flag: FEATURE_FLAG) => {
  if (typeof window === "object") {
    const value = localStorage.getItem(flag);
    const boolValue = value ? value.toLowerCase() == "true" : false;
    logger.log({ flag, value, boolValue });
    localStorage.setItem(flag, `${boolValue}`);
    return boolValue;
  }
};
