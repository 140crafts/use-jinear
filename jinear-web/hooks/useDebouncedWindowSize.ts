import debounce from "@/utils/debounce";
import { useCallback, useEffect, useState } from "react";

interface IWindowSizeVo {
  width?: any;
  height?: any;
}
function useDebouncedWindowSize(): IWindowSizeVo {
  const [state, setState] = useState<IWindowSizeVo>({});

  const handleResize = useCallback(() => {
    let ticking = false;
    if (!ticking) {
      window.requestAnimationFrame(() => {
        setState({
          width: window.innerWidth,
          height: window.innerHeight,
        });
        ticking = false;
      });
      ticking = true;
    }
  }, []);
  const debouncedHandleResize = debounce(handleResize, 330);
  useEffect(() => {
    if (typeof window !== "undefined") {
      handleResize();
      window.addEventListener("resize", debouncedHandleResize);
      return () => window.removeEventListener("resize", debouncedHandleResize);
    }
  }, []);

  return state;
}

export default useDebouncedWindowSize;

export function useDebouncedWindowHeight(): number {
  const [height, setHeight] = useState<number>(0);

  const handleResize = useCallback(() => {
    let ticking = false;
    if (!ticking) {
      window.requestAnimationFrame(() => {
        setHeight(window.innerHeight);
        ticking = false;
      });
      ticking = true;
    }
  }, []);
  const debouncedHandleResize = debounce(handleResize, 330);
  useEffect(() => {
    if (typeof window !== "undefined") {
      handleResize();
      window.addEventListener("resize", debouncedHandleResize);
      return () => window.removeEventListener("resize", debouncedHandleResize);
    }
  }, []);

  return height;
}
