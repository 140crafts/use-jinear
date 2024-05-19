import { useTypedSelector } from "@/store/store";
import { selectThreadMessageInfo } from "@/slice/messagingSlice";

export const useThreadInitialMessage = ({ workspaceId, threadId }: { workspaceId: string, threadId: string }) => {
  const threadMessageInfo = useTypedSelector(selectThreadMessageInfo({ workspaceId, threadId }));
  return threadMessageInfo.initialMessage;
};