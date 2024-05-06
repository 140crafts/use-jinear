import { useConversationMessagesSorted } from "@/hooks/messaging/conversationMessage/useConversationMessagesSorted";

export const useConversationEarliestMessage = ({ workspaceId, conversationId }: {
  workspaceId: string
  conversationId: string
}) => {
  const sortedMessages = useConversationMessagesSorted({ workspaceId, conversationId });
  return sortedMessages ? sortedMessages[sortedMessages.length - 1] : null;
};