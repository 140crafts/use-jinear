import { __DEV__ } from "./constants";
const enabled = true;
const Logger = (tag = "NoTag") => {
  let runOnProd = false;
  if (typeof window !== "undefined") {
    runOnProd = window.debug;
  }
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
