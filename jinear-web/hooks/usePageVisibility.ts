import React from "react";

export function getIsDocumentHidden() {
  if (typeof window !== "undefined") {
    return !document?.hidden;
  }
  return true;
}

export function usePageVisibility() {
  const [isVisible, setIsVisible] = React.useState(getIsDocumentHidden());
  const onVisibilityChange = () => setIsVisible(getIsDocumentHidden());

  React.useEffect(() => {
    if (typeof window !== "undefined") {
      document?.addEventListener?.("visibilitychange", onVisibilityChange, false);
      return () => {
        document?.removeEventListener?.("visibilitychange", onVisibilityChange);
      };
    }
  });

  return isVisible;
}