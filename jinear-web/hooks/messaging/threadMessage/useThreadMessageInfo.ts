import { useTypedSelector } from "@/store/store";
import { selectThreadMessageInfo } from "@/slice/messagingSlice";

export const useThreadMessageInfo = ({ workspaceId, threadId }: {
  workspaceId: string,
  threadId: string
}) => {
  return useTypedSelector(selectThreadMessageInfo({ workspaceId, threadId }));
};