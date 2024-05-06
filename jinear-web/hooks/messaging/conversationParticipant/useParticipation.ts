import { useRetrieveParticipatedConversationsQuery } from "@/api/conversationApi";

export const useParticipation = ({ conversationId, workspaceId }: {
  conversationId: string,
  workspaceId?: string
}) => {
  const {
    data: participatedConversationsResponse,
    isFetching
  } = useRetrieveParticipatedConversationsQuery({ workspaceId: workspaceId || "" }, { skip: workspaceId == null });
  return participatedConversationsResponse
    ?.data
    .filter(conversationParticipantDto => conversationParticipantDto.conversationId == conversationId)
    ?.[0];
};