import { useCallback, useState } from "react";

export const useToggle = (initialState = false) => {
  const [current, setCurrent] = useState(initialState);
  const toggle = useCallback(() => setCurrent((state) => !state), []);
  return { current, toggle };
};
