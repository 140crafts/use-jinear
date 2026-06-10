import { TaskSubscriptionResponse } from "@/model/be/jinear-core";
import { api } from "./api";

export const taskSubscriptionApi = api.injectEndpoints({
  endpoints: (build) => ({
    initializeTaskSubscription: build.mutation<TaskSubscriptionResponse, { taskId: string }>({
      query: (req) => ({
        url: `v1/task/${req.taskId}/subscription`,
        method: "POST",
      }),
      invalidatesTags: ["v1/task/{taskId}/subscription/list", "v1/task/{taskId}/subscription"],
    }),
    //
    removeTaskSubscription: build.mutation<TaskSubscriptionResponse, { taskId: string }>({
      query: (req) => ({
        url: `v1/task/${req.taskId}/subscription`,
        method: "DELETE",
      }),
      invalidatesTags: ["v1/task/{taskId}/subscription/list", "v1/task/{taskId}/subscription"],
    }),
    //
    retrieveSubscription: build.query<TaskSubscriptionResponse, { taskId: string }>({
      query: (req) => ({
        url: `v1/task/${req.taskId}/subscription`,
      }),
      providesTags: (_result, _err, req) => [
        {
          type: "v1/task/{taskId}/subscription",
          id: req.taskId,
        },
      ],
    }),
    //
    retrieveAllSubscribers: build.query<TaskSubscriptionResponse, { taskId: string }>({
      query: (req) => ({
        url: `v1/task/${req.taskId}/subscription/list`,
      }),
      providesTags: (_result, _err, req) => [
        {
          type: "v1/task/{taskId}/subscription/list",
          id: req.taskId,
        },
      ],
    }),
    //
  }),
});

export const {
  useInitializeTaskSubscriptionMutation,
  useRemoveTaskSubscriptionMutation,
  useRetrieveSubscriptionQuery,
  useRetrieveAllSubscribersQuery,
} = taskSubscriptionApi;

export const {
  endpoints: { initializeTaskSubscription, removeTaskSubscription, retrieveSubscription, retrieveAllSubscribers },
} = taskSubscriptionApi;
