import { useRetrieveCalendarMembershipsQuery } from "@/api/calendarMemberApi";
import Logger from "@/util/logger";

const logger = Logger("useHasExternalCalendars");

export const useHasExternalCalendars = (workspaceId?: string) => {
  const {
    data: membershipsResponse,
    isSuccess,
    isFetching
  } = useRetrieveCalendarMembershipsQuery({ workspaceId: workspaceId || "" }, { skip: workspaceId == null });
  logger.log({ l: membershipsResponse?.data?.length });
  return membershipsResponse?.data?.length != 0 && membershipsResponse?.data?.length != null;
};