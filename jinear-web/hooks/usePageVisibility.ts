import React from "react";

export function getIsDocumentHidden() {
  return !document.hidden;
}

export function usePageVisibility() {
  const [isVisible, setIsVisible] = React.useState(getIsDocumentHidden());
  const onVisibilityChange = () => setIsVisible(getIsDocumentHidden());

  React.useEffect(() => {
    document.addEventListener("visibilitychange", onVisibilityChange, false);
    return () => {
      document.removeEventListener("visibilitychange", onVisibilityChange);
    };
  });

  return isVisible;
}