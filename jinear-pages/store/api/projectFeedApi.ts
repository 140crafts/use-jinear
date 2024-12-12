import {
  AccountProjectPermissionFlagsResponse, ProjectFeedPaginatedResponse, ProjectFeedPostResponse,
  ProjectListingPaginatedResponse,
  ProjectRetrieveResponse, PublicProjectRetrieveResponse
} from "@/model/be/jinear-core";
import { api } from "./api";

export const projectFeedApi = api.injectEndpoints({
  endpoints: (build) => ({
    //
    retrieveProjectFeedPost: build.query<ProjectFeedPostResponse, { projectId: string, postId: string }>({
      query: ({ projectId, postId }: {
        projectId: string,
        postId: string
      }) => `v1/project/public-feed/${projectId}/${postId}`,
      providesTags: (_result, _err, req) => [
        {
          type: `v1/project/public-feed/{projectId}/{postId}`,
          id: `${req.projectId}-${req.postId}`
        }
      ]
    }),
    //
    retrieveProjectFeed: build.query<ProjectFeedPaginatedResponse, { projectId: string, page?: number }>({
      query: ({ projectId, page = 0 }: {
        projectId: string,
        page?: number
      }) => `v1/project/public-feed/${projectId}?page=${page}`,
      providesTags: (_result, _err, req) => [
        {
          type: `v1/project/public-feed/{projectId}`,
          id: `${req.projectId}-${req.page}`
        }
      ]
    }),
    //
    retrievePublicProjectInfo: build.query<PublicProjectRetrieveResponse, { projectId: string }>({
      query: ({ projectId }: { projectId: string }) => `v1/project/public-feed/${projectId}/info`,
      providesTags: (_result, _err, req) => [
        {
          type: `v1/project/public-feed/{projectId}/info`,
          id: `${req.projectId}`
        }
      ]
    }),
    //
    retrievePublicProjectInfoWithDomain: build.query<PublicProjectRetrieveResponse, { domain: string }>({
      query: ({ domain }: { domain: string }) => `v1/project/public-feed/custom-domain/${domain}/info`,
      providesTags: (_result, _err, req) => [
        {
          type: `v1/project/public-feed/custom-domain/{domain}/info`,
          id: `${req.domain}`
        }
      ]
    })
    //
  })
});

export const {
  useRetrieveProjectFeedPostQuery,
  useRetrieveProjectFeedQuery,
  useRetrievePublicProjectInfoQuery,
  useRetrievePublicProjectInfoWithDomainQuery
} = projectFeedApi;

export const {
  endpoints: {
    retrieveProjectFeedPost,
    retrieveProjectFeed,
    retrievePublicProjectInfo,
    retrievePublicProjectInfoWithDomain
  }
} = projectFeedApi;
