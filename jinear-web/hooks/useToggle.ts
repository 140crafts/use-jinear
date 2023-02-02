import { useCallback, useEffect, useState } from "react";

export const useToggle = (initialState = false) => {
  const [current, setCurrent] = useState(initialState);
  const toggle = useCallback(() => setCurrent((state) => !state), []);
  useEffect(() => {
    setCurrent(initialState);
  }, [initialState]);
  return { current, toggle };
};
