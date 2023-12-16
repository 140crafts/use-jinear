"use client";

export type FEATURE_FLAG = "EASTER_EGG_LOADING" | "EASTER_EGG_NETWORK" | "LOGIN_WITH_GOOGLE";

export const useFeatureFlag = (flag: FEATURE_FLAG) => {
  if (typeof window === "object") {
    const value = localStorage.getItem(flag);
    const boolValue = value ? new Boolean(value) : false;
    localStorage.setItem(flag, `${boolValue}`);
    return boolValue;
  }
};
