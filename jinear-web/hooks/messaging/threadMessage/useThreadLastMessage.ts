import {
  useThreadLastMessageFromMessageInfo
} from "@/hooks/messaging/threadMessage/useThreadLastMessageFromMessageInfo";
import { useThreadLastMessageFromMessageMap } from "@/hooks/messaging/threadMessage/useThreadLastMessageFromMessageMap";
import { isAfter } from "date-fns";

export const useThreadLastMessage = ({ workspaceId, threadId }: {
  workspaceId: string,
  threadId: string
}) => {
  const latestMessageFromThreadMessageInfo = useThreadLastMessageFromMessageInfo({ workspaceId, threadId });
  const latestMessageFromMessageMap = useThreadLastMessageFromMessageMap({ workspaceId, threadId });
  if (latestMessageFromThreadMessageInfo && latestMessageFromMessageMap) {
    return isAfter(new Date(latestMessageFromThreadMessageInfo.createdDate), new Date(latestMessageFromMessageMap.createdDate)) ? latestMessageFromThreadMessageInfo : latestMessageFromMessageMap;
  }
  return latestMessageFromThreadMessageInfo;
};