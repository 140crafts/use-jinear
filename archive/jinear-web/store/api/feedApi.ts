import { BaseResponse } from "@/model/be/jinear-core";
import { api } from "./api";

export const feedApi = api.injectEndpoints({
  endpoints: (build) => ({
    //
    deleteFeed: build.mutation<BaseResponse, { feedId: string }>({
      query: (req: { feedId: string }) => ({
        url: `v1/feed/${req.feedId}`,
        method: "DELETE",
      }),
      invalidatesTags: (_result, _err, req) => [
        "v1/feed/member/memberships/{workspaceId}",
        "v1/task/feed-item/from-task/req.taskId",
      ],
    }),
    //
  }),
});

export const { useDeleteFeedMutation } = feedApi;

export const {
  endpoints: { deleteFeed },
} = feedApi;
