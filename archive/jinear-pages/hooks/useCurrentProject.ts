import { useRetrievePublicProjectInfoWithDomainQuery } from "@/api/projectFeedApi";

export const useCurrentProject = () => {
  const domain = typeof window == "object" && window?.location?.hostname || "";

  const {
    data: retrievePublicProjectResponse,
    isFetching: isRetrievePublicProjectFetching
  } = useRetrievePublicProjectInfoWithDomainQuery({ domain }, { skip: domain == "" });
  return retrievePublicProjectResponse?.data;
};