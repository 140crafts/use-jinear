import { useThreadMessageInfo } from "@/hooks/messaging/threadMessage/useThreadMessageInfo";

export const useThreadLastMessageFromMessageInfo = ({ workspaceId, threadId }: {
  workspaceId: string,
  threadId: string
}) => {
  const threadMessageInfo = useThreadMessageInfo({ workspaceId, threadId });
  return threadMessageInfo.latestMessage;
};