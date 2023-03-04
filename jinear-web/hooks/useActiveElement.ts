import React from "react";

const useActiveElement = () => {
  const [listenersReady, setListenersReady] = React.useState(false); /** Useful when working with autoFocus */
  const [activeElement, setActiveElement] = React.useState<HTMLElement | null>(); // document.activeElement as HTMLElement | null

  React.useEffect(() => {
    const onFocus = (event: any) => setActiveElement(event.target);
    const onBlur = (event: any) => setActiveElement(null);

    window.addEventListener("focus", onFocus, true);
    window.addEventListener("blur", onBlur, true);

    setListenersReady(true);

    return () => {
      window.removeEventListener("focus", onFocus);
      window.removeEventListener("blur", onBlur);
    };
  }, []);

  return {
    activeElement,
    listenersReady,
  };
};

export default useActiveElement;
