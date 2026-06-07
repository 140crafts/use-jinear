import { BaseResponse, NotificationTargetInitializeRequest } from "@/model/be/jinear-core";
import { api } from "./api";

export const notificationTargetApi = api.injectEndpoints({
  endpoints: (build) => ({
    //
    initializeNotificationTarget: build.mutation<BaseResponse, NotificationTargetInitializeRequest>({
      query: (req) => ({
        url: `v1/notification/target`,
        method: "POST",
        body: req,
      }),
    }),
    //
  }),
});

export const { useInitializeNotificationTargetMutation } = notificationTargetApi;

export const {
  endpoints: { initializeNotificationTarget },
} = notificationTargetApi;
