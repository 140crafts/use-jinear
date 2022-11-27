import { __DEV__ } from "@/utils/constants";
import { createApi, fetchBaseQuery } from "@reduxjs/toolkit/query/react";

export const server = __DEV__ ? "staging.jinear" : "api.jinear";
export const root = __DEV__
  ? "http://localhost:8085/"
  : `https://${server}.140crafts.com/`;

export const s3Base = "https://storage.googleapis.com/jienar-b0/";

const baseQuery = fetchBaseQuery({
  baseUrl: root,
  prepareHeaders: (headers, { getState }) => {
    return headers;
  },
  credentials: "include",
});

export const tagTypes = ["Account-Current"];

export const api = createApi({
  baseQuery: baseQuery,
  tagTypes,
  endpoints: () => ({}),
});
