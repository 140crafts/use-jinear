import { useRetrieveChannelMembershipsQuery } from "@/api/channelMemberApi";

export const useChannelMembership = (vo: { workspaceId?: string, channelId?: string }) => {
  const { data: channelMembershipsResponse } = useRetrieveChannelMembershipsQuery({ workspaceId: vo.workspaceId || "" }, { skip: vo.workspaceId == null });
  return channelMembershipsResponse?.data?.find(channelMember => channelMember.channelId == vo.channelId);
};