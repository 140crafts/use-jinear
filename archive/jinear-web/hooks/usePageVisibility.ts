import React from "react";
import Logger from "@/utils/logger";

function getIsPageVisible() {
  const result = typeof document !== "undefined" && document.visibilityState === "visible";
  logger.log({ getIsPageVisible: result });
  return result;
}

const logger = Logger("usePageVisibility");

export function usePageVisibility() {
  const [isVisible, setIsVisible] = React.useState(getIsPageVisible());

  const handleVisibilityChange = () => {
    setIsVisible(getIsPageVisible());
  };

  const setAsVisible = () => {
    setIsVisible(true);
  };

  const setAsNotVisible = () => {
    setIsVisible(false);
  };


  React.useEffect(() => {
    if (typeof window !== "undefined") {
      // document.addEventListener("visibilitychange", handleVisibilityChange);
      window.addEventListener("focus",setAsVisible)
      window.addEventListener("blur",setAsNotVisible)
      return () => {
        // document.removeEventListener("visibilitychange", handleVisibilityChange);
        window.removeEventListener("focus",setAsVisible)
        window.removeEventListener("blur",setAsNotVisible)
      };
    }
  }, []);

  return isVisible;
}