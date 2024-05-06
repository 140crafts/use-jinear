import {
  useConversationFromParticipated
} from "@/hooks/messaging/conversationParticipant/useConversationFromParticipated";
import { useConversationEarliestMessage } from "@/hooks/messaging/conversationMessage/useConversationEarliestMessage";
import Logger from "@/utils/logger";

const logger = Logger("useConversationHasMoreMessages");

export const useConversationHasMoreMessages = ({ conversationId, workspaceId }: {
  conversationId: string,
  workspaceId: string
}) => {
  const conversation = useConversationFromParticipated({ conversationId, workspaceId });
  const stateEarliestMessage = useConversationEarliestMessage({ workspaceId, conversationId });
  logger.log({ conversation, stateEarliestMessage });
  if (conversation && stateEarliestMessage) {
    return conversation.conversationMessageInfo.initialMessage.messageId != stateEarliestMessage.messageId;
  }
  return true;
};