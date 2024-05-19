import { useThreadMessageInfo } from "@/hooks/messaging/threadMessage/useThreadMessageInfo";
import { useThreadMessages } from "@/hooks/messaging/threadMessage/useThreadMessages";

export const useThreadHasMoreMessages = ({ workspaceId, threadId }: { workspaceId: string, threadId: string }) => {
  const threadMessages = useThreadMessages({ workspaceId, threadId });
  const threadMessageInfo = useThreadMessageInfo({ workspaceId, threadId });
  return threadMessageInfo.messageCount != Object.keys(threadMessages).length;
};