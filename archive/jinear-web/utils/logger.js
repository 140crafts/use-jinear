import { __DEV__ } from "./constants";
const enabled = true;

export const getLoggerEnabledOnProd = () => {
  if (typeof window === "object") {
    return localStorage.getItem("DEBUG") != null || localStorage.getItem("debug") != null;
  }
  return false;
};

const Logger = (tag = "NoTag") => {
  let runOnProd = getLoggerEnabledOnProd();
  runOnProd = __DEV__ || runOnProd;
  return {
    runOnProd,
    tag: tag,
    log: (_) => {
      if ((__DEV__ || runOnProd) && enabled) {
        const __ = { tag: tag };
        if (!(typeof _ === "object" && _ !== null)) {
          _ = { message: _ };
        }
        console.log({ ...__, ..._ });
      }
    },
  };
};
export default Logger;
