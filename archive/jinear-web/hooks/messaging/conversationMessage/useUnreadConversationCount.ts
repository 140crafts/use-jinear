import { useTypedSelector } from "@/store/store";
import { selectConversationLastCheckMap, selectConversationMessagesMap } from "@/slice/messagingSlice";
import { isBefore } from "date-fns";

export const useUnreadConversationCount = ({ workspaceId }: {
  workspaceId: string
}) => {
  const conversationLastCheckMap = useTypedSelector(selectConversationLastCheckMap(workspaceId)) || {};
  const conversationMessageMap = useTypedSelector(selectConversationMessagesMap(workspaceId)) || {};
  let total = 0;
  Object.keys(conversationMessageMap).forEach(conversationId => {
    const conversationLastCheck = conversationLastCheckMap[conversationId];
    const messages = conversationMessageMap[conversationId];
    const lastMessage = Object.values(messages).sort((a, b) => new Date(b.createdDate).getTime() - new Date(a.createdDate).getTime())?.[0];
    if (conversationLastCheck && lastMessage) {
      total = isBefore(new Date(conversationLastCheck), new Date(lastMessage.createdDate)) ? total + 1 : total;
    }
  });
  return total;
};