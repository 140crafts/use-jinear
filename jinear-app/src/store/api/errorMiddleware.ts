import Logger from "@/util/logger";
import type { Middleware, MiddlewareAPI } from "@reduxjs/toolkit";
import { isRejectedWithValue } from "@reduxjs/toolkit";
import { toast } from "react-hot-toast";
import {getTranslatedMessage} from "@/locals/useTranslation.ts";

const logger = Logger("rtkQueryErrorLogger");

export const rtkQueryErrorLogger: Middleware = (api: MiddlewareAPI) => (next) => (action) => {
  if (isRejectedWithValue(action)) {
    logger.log({ rtkQueryErrorLogger: action });
    //@ts-ignore
    const status = action?.payload?.status;
    if (status == 413) {
      const message = getTranslatedMessage("apiFileTooLargeError");
      toast(message);
    } else if (status !== 401) {
      //@ts-ignore
      const message = action?.payload?.data?.consumerErrorMessage || getTranslatedMessage("genericError");
      toast(message);
    }
  }
  return next(action);
};
