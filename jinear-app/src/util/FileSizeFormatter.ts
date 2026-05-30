export const humanReadibleFileSize = (size?: number) => {
  if (size) {
    const i = size == 0 ? 0 : Math.floor(Math.log(size) / Math.log(1024));
    const _s = size / Math.pow(1024, i);
    return _s.toFixed(2) + " " + ["B", "kB", "MB", "GB", "TB"][i];
  }
  return "";
};
