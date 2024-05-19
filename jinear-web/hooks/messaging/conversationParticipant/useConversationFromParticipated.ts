import { useParticipation } from "@/hooks/messaging/conversationParticipant/useParticipation";

export const useConversationFromParticipated = ({ conversationId, workspaceId }: {
  conversationId: string,
  workspaceId?: string
}) => {
  const participation = useParticipation({ conversationId, workspaceId });
  return participation?.conversation;
};