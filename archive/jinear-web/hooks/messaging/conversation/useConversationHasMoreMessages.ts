import {
  useConversationFromParticipated
} from "@/hooks/messaging/conversationParticipant/useConversationFromParticipated";
import { getConversationEarliestMessage } from "../../../repository/IndexedDbRepository";
import { useLiveQuery } from "dexie-react-hooks";

export const useConversationHasMoreMessages = ({ conversationId, workspaceId }: {
  conversationId: string,
  workspaceId: string
}) => {
  const conversation = useConversationFromParticipated({ conversationId, workspaceId });
  const stateEarliestMessage = useLiveQuery(() => getConversationEarliestMessage(conversationId));

  if (conversation && stateEarliestMessage) {
    return conversation.conversationMessageInfo.initialMessage.messageId != stateEarliestMessage.messageId;
  }
  return true;
};