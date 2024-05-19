import { useTypedSelector } from "@/store/store";
import { selectCurrentAccountId } from "@/slice/accountSlice";
import { useConversationParticipants } from "@/hooks/messaging/conversationParticipant/useConversationParticipants";

export const useOtherConversationParticipants = ({ workspaceId, conversationId }: {
  workspaceId: string,
  conversationId: string
}) => {
  const currentAccountId = useTypedSelector(selectCurrentAccountId);
  const plainConversationParticipantDtos = useConversationParticipants({ workspaceId, conversationId });
  return plainConversationParticipantDtos?.filter(plainConversationParticipantDto => plainConversationParticipantDto.accountId != currentAccountId);
};