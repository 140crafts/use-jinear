import { useConversationMessages } from "@/hooks/messaging/conversationMessage/useConversationMessages";

export const useConversationMessagesSorted = ({ workspaceId, conversationId }: {
  workspaceId: string,
  conversationId: string
}) => {
  const messages = useConversationMessages({ workspaceId, conversationId }) || {};
  return Object.values(messages).sort((a, b) => new Date(b.createdDate).getTime() - new Date(a.createdDate).getTime());
};

export const useConversationMessagesSortedIDateAsc = ({ workspaceId, conversationId }: {
  workspaceId: string,
  conversationId: string
}) => {
  const messages = useConversationMessages({ workspaceId, conversationId }) || {};
  return Object.values(messages).sort((a, b) => new Date(a.createdDate).getTime() - new Date(b.createdDate).getTime());
};