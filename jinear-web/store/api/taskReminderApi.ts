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
      invalidatesTags: (_result, _err, req) => [
        "team-task-list",
        "workplace-task-with-name-and-tag",
        "team-workflow-task-list",
        "team-all-task-list",
      ],
    }),
    //
    retrieveNextJob: build.query<ReminderJobResponse, { taskReminderId: string }>({
      query: (req) => {
        const { taskReminderId } = req;
        return `v1/task/reminder/job/${taskReminderId}/next`;
      },
      providesTags: (_result, _err, req) => [
        {
          type: "reminder-next-job",
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
      invalidatesTags: (_result, _err, req) => [{ type: "team-task-list" }, { type: "workplace-task-with-name-and-tag" }],
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
