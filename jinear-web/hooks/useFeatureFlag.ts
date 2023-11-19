import { useLocalStorage } from "@uidotdev/usehooks";

export type FEATURE_FLAG = "EASTER_EGG_LOADING" | "EASTER_EGG_NETWORK" | "LOGIN_WITH_GOOGLE";

export const useFeatureFlag = (flag: FEATURE_FLAG) => {
  const [_flag, _setFlag] = useLocalStorage<any>(flag, false);
  return _flag;
};
