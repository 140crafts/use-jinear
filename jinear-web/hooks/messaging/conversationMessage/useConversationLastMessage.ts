import { useConversationMessagesSorted } from "@/hooks/messaging/conversationMessage/useConversationMessagesSorted";

export const useConversationLastMessage = ({ workspaceId, conversationId }: {
  workspaceId: string
  conversationId: string
}) => {
  const sortedMessages = useConversationMessagesSorted({ workspaceId, conversationId });
  return sortedMessages?.[0];
};