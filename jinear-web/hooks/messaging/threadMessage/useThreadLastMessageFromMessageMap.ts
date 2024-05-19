import { useThreadMessagesSorted } from "@/hooks/messaging/threadMessage/useThreadMessagesSorted";

export const useThreadLastMessageFromMessageMap = ({ workspaceId, threadId }: {
  workspaceId: string,
  threadId: string
}) => {
  const sortedMessages = useThreadMessagesSorted({ workspaceId, threadId });
  return sortedMessages?.[0];
};