import { useThreadMessages } from "@/hooks/messaging/threadMessage/useThreadMessages";

export const useThreadMessagesSorted = ({ workspaceId, threadId }: {
  workspaceId: string,
  threadId: string
}) => {
  const messages = useThreadMessages({ workspaceId, threadId }) || {};
  return Object.values(messages).sort((a, b) => new Date(b.createdDate).getTime() - new Date(a.createdDate).getTime());
};