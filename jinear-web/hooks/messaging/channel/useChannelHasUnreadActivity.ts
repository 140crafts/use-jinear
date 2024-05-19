import { useTypedSelector } from "@/store/store";
import { selectChannelLastActivity, selectChannelLastCheck } from "@/slice/messagingSlice";
import { isAfter } from "date-fns";
import Logger from "@/utils/logger";

const logger = Logger("useChannelHasUnreadActivity");

export const useChannelHasUnreadActivity = ({ workspaceId, channelId }: {
  workspaceId: string
  channelId: string,
}) => {
  const channelLastCheck = useTypedSelector(selectChannelLastCheck({ workspaceId, channelId }));
  const channelLastActivity = useTypedSelector(selectChannelLastActivity({ workspaceId, channelId }));
  logger.log({ channelId, channelLastCheck, channelLastActivity });
  if (channelLastCheck && channelLastActivity) {
    return isAfter(new Date(channelLastActivity), new Date(channelLastCheck));
  }
  return false;
};