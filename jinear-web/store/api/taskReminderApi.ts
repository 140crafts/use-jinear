import { BaseResponse, ReminderJobResponse, TaskReminderInitializeRequest } from "@/model/be/jinear-core";
import { api } from "./api";

export const taskReminderApi = api.injectEndpoints({
  endpoints: (build) => ({
    initializeTaskReminder: build.mutation<BaseResponse, TaskReminderInitializeRequest>({
      query: (req: TaskReminderInitializeRequest) => ({
        url: `v1/task/reminder`,
        method: "POST",
        body: req,
      }),
      invalidatesTags: (_result, _err, req) => ["v1/task/from-workspace/{workspaceName}/{taskTag}"],
    }),
    //
    retrieveNextJob: build.query<ReminderJobResponse, { taskReminderId: string }>({
      query: (req) => {
        const { taskReminderId } = req;
        return `v1/task/reminder/job/${taskReminderId}/next`;
      },
      providesTags: (_result, _err, req) => [
        {
          type: "v1/task/reminder/job/{taskReminderId}/next",
          id: req.taskReminderId,
        },
      ],
    }),
    //
    passivizeTaskReminder: build.mutation<BaseResponse, { taskReminderId: string }>({
      query: (body: { taskReminderId: string }) => ({
        url: `v1/task/reminder/${body.taskReminderId}`,
        method: "DELETE",
      }),
      invalidatesTags: (_result, _err, req) => [{ type: "v1/task/from-workspace/{workspaceName}/{taskTag}" }],
    }),
    //
    //
    //
  }),
});

export const { useInitializeTaskReminderMutation, useRetrieveNextJobQuery, usePassivizeTaskReminderMutation } = taskReminderApi;

export const {
  endpoints: { initializeTaskReminder },
} = taskReminderApi;
