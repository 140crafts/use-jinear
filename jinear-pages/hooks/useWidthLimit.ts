import debounce from "@/utils/debounce";
import { useCallback, useEffect, useState } from "react";

interface IWindowSizeVo {
  limit?: number;
}
function useWidthLimit({ limit = 460 }: IWindowSizeVo): boolean {
  const [state, setState] = useState<boolean>(false);

  const handleResize = useCallback(() => {
    let ticking = false;
    if (!ticking) {
      window.requestAnimationFrame(() => {
        setState(window.innerWidth < limit);
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

export default useWidthLimit;
