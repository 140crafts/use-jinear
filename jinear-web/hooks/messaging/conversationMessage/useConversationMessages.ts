import { selectConversationMessages } from "@/slice/messagingSlice";
import { useTypedSelector } from "@/store/store";
import Logger from "@/utils/logger";

const logger = Logger("useConversationMessages");

export const useConversationMessages = ({ workspaceId, conversationId }: {
  workspaceId: string
  conversationId: string
}) => {
  const messages = useTypedSelector(selectConversationMessages({ workspaceId, conversationId }));
  logger.log({ conversationId, messages });
  return messages;
};