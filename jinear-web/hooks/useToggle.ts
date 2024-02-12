import Logger from "@/utils/logger";
import { Dispatch, SetStateAction, useCallback, useState } from "react";

const logger = Logger("useToggle");

export function useToggle(defaultValue?: boolean): [boolean, () => void, Dispatch<SetStateAction<boolean>>] {
  const [value, setValue] = useState(!!defaultValue);
  logger.log({ defaultValue, value });
  const toggle = useCallback(() => {
    setValue((x) => !x);
  }, []);

  return [value, toggle, setValue];
}
