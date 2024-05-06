import { selectConversationLastCheck } from "@/slice/messagingSlice";
import { useTypedSelector } from "@/store/store";
import { useConversationLastMessage } from "@/hooks/messaging/conversationMessage/useConversationLastMessage";
import { isBefore } from "date-fns";
import Logger from "@/utils/logger";

const logger = Logger("useConversationHasUnreadMessages");

export const useConversationHasUnreadMessages = ({ workspaceId, conversationId }: {
  workspaceId: string
  conversationId: string
}) => {
  const conversationLastCheck = useTypedSelector(selectConversationLastCheck({ workspaceId, conversationId }));
  const lastMessage = useConversationLastMessage({ workspaceId, conversationId });
  logger.log({ conversationLastCheck, lastMessage });
  if (conversationLastCheck && lastMessage) {
    return isBefore(new Date(conversationLastCheck), new Date(lastMessage.createdDate));
  }
  return false;
};