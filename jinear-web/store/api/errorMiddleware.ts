import Logger from "@/utils/logger";
import type { Middleware, MiddlewareAPI } from "@reduxjs/toolkit";
import { isRejectedWithValue } from "@reduxjs/toolkit";
import { getTranslatedMessage } from "locales/useTranslation";
import { toast } from "react-hot-toast";

const logger = Logger("rtkQueryErrorLogger");

export const rtkQueryErrorLogger: Middleware = (api: MiddlewareAPI) => (next) => (action) => {
  logger.log(action);
  if (isRejectedWithValue(action)) {
    const status = action?.payload?.status;
    const originalStatus = action?.payload?.originalStatus;
    if (status !== 401) {
      const message = action?.payload?.data?.consumerErrorMessage || getTranslatedMessage("genericError");
      toast(message);
      console.error({ message, originalStatus });
    }
  }
  return next(action);
};
