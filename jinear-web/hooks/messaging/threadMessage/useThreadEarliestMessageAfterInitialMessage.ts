import {
  useThreadInitialMessageFromMessageInfo
} from "@/hooks/messaging/threadMessage/useThreadInitialMessageFromMessageInfo";
import { useThreadMessagesSorted } from "@/hooks/messaging/threadMessage/useThreadMessagesSorted";

export const useThreadEarliestMessageAfterInitialMessage = ({ workspaceId, threadId }: {
  workspaceId: string,
  threadId: string
}) => {
  const initialMessage = useThreadInitialMessageFromMessageInfo({ workspaceId, threadId });
  const sortedMessages = useThreadMessagesSorted({ workspaceId, threadId });
  const earliest = sortedMessages?.[sortedMessages?.length - 1];
  const oneBefore = sortedMessages?.[sortedMessages?.length - 2];
  return earliest?.messageId != initialMessage.messageId ? earliest : oneBefore;
};