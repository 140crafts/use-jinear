const isPwa = () => {
  if (typeof window !== "undefined") {
    return window.matchMedia("(display-mode: standalone)").matches;
  }
  return false;
};

export default isPwa;
