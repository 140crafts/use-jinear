const enabled = true;

const Logger = (tag = "NoTag") => {
  return {
    tag: tag,
    log: (_) => {
      if (__DEV__ && enabled) {
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
