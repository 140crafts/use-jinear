import {
  AccountCommunicationPermissionsResponse,
  BaseResponse,
  SetCommunicationPermissionsRequest,
} from "@/model/be/jinear-core";
import { api } from "./api";

export const accountCommunicationPermissionApi = api.injectEndpoints({
  endpoints: (build) => ({
    //
    retrievePermissions: build.query<AccountCommunicationPermissionsResponse, void>({
      query: () => `v1/account/communication-permission`,
      providesTags: ["account-communication-permissions"],
    }),
    //
    setPermissions: build.mutation<BaseResponse, SetCommunicationPermissionsRequest>({
      query: (body: SetCommunicationPermissionsRequest) => ({
        url: `v1/account/communication-permission`,
        method: "POST",
        body,
      }),
      invalidatesTags: ["account-communication-permissions"],
    }),
    //
  }),
});

export const { useRetrievePermissionsQuery, useSetPermissionsMutation } = accountCommunicationPermissionApi;

export const {
  endpoints: { retrievePermissions, setPermissions },
} = accountCommunicationPermissionApi;
