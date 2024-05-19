import { useTypedSelector } from "@/store/store";
import { selectThreadMessages } from "@/slice/messagingSlice";
import Logger from "@/utils/logger";

const logger = Logger("useThreadMessages");

export const useThreadMessages = ({ workspaceId, threadId }: {
  workspaceId: string,
  threadId: string
}) => {
  const messages = useTypedSelector(selectThreadMessages({ workspaceId, threadId })) || {};
  logger.log({ messages });
  return messages;
};