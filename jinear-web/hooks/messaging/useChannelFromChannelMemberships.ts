import { useWorkspaceFromName } from "@/hooks/useWorkspaceFromName";
import { useRetrieveMembershipsQuery } from "@/api/channelMemberApi";

export const useChannelFromChannelMemberships = (vo: { workspaceName: string, channelId: string }) => {
  const workspace = useWorkspaceFromName(vo.workspaceName);
  const {
    data: channelMembershipsResponse
  } = useRetrieveMembershipsQuery({ workspaceId: workspace?.workspaceId || "" }, { skip: workspace == null });

  return channelMembershipsResponse?.data
    ?.find(channelMember => channelMember.channelId == vo.channelId)
    ?.channel;
};