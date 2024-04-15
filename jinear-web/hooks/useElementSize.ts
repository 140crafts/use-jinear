import { useEffect, useState } from "react";

interface IElementSizeState {
  offsetWidth?: any;
  offsetHeight?: any;
}

function useElementSize(el?: HTMLElement | null): IElementSizeState {
  const [elementSize, setElementSize] = useState<IElementSizeState>({
    offsetWidth: undefined,
    offsetHeight: undefined
  });
  useEffect(() => {
    if (typeof window !== "undefined" && el) {
      const handleResize = () => {
        setElementSize({
          offsetWidth: el.offsetWidth,
          offsetHeight: el.offsetHeight
        });
      };
      window.addEventListener("resize", handleResize);
      handleResize();
      return () => window.removeEventListener("resize", handleResize);
    }
  }, [el]);
  return elementSize;
}

export default useElementSize;
