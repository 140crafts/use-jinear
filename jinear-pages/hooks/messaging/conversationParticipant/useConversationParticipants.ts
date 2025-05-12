import { useRetrieveParticipatedConversationsQuery } from "@/api/conversationApi";

export const useConversationParticipants = ({ workspaceId, conversationId }: {
  workspaceId: string,
  conversationId: string
}) => {
  const {
    data: participatedConversationsResponse
  } = useRetrieveParticipatedConversationsQuery({ workspaceId });
  return participatedConversationsResponse
    ?.data
    .map(conversationParticipantDto => conversationParticipantDto.conversation)
    .filter(conversation => conversation.conversationId == conversationId)
    .map(conversation => conversation.participants)
    ?.[0];
};
