import React, { useMemo } from "react";
import styles from "./ChannelList.module.css";
import { useRetrieveChannelMembershipsQuery } from "@/api/channelMemberApi";
import { WorkspaceDto } from "@/be/jinear-core";
import ChannelButton from "@/components/conversationsSectionSideMenu/channelList/channelButton/ChannelButton";
import MenuGroupTitle from "@/components/sideMenu/menuGroupTitle/MenuGroupTitle";
import useTranslation from "../../../locales/useTranslation";
import CircularLoading from "@/components/circularLoading/CircularLoading";
import Logger from "@/utils/logger";
import NewChannelButton from "@/components/conversationsSectionSideMenu/channelList/newChannelButton/NewChannelButton";
import { useListChannelsQuery } from "@/api/channelApi";
import JoinableChannelListButton
  from "@/components/conversationsSectionSideMenu/channelList/joinableChannelListButton/JoinableChannelListButton";

interface ChannelListProps {
  workspace: WorkspaceDto;
}

const logger = Logger("ChannelList");

const ChannelList: React.FC<ChannelListProps> = ({ workspace }) => {
  const { t } = useTranslation();
  const {
    data: channelMembershipsResponse,
    isLoading: isRetrieveChannelMembershipsLoading
  } = useRetrieveChannelMembershipsQuery({ workspaceId: workspace.workspaceId });
  const {
    data: channelListResponse,
    isLoading: isChannelListLoading
  } = useListChannelsQuery({ workspaceId: workspace.workspaceId });

  const joinable = useMemo(() => channelListResponse?.data?.filter(channelMembershipInfoDto => !channelMembershipInfoDto.isJoined) || [], [channelListResponse]);
  const isLoading = isRetrieveChannelMembershipsLoading || isChannelListLoading;

  logger.log({ ChannelList: workspace, channelMembershipsResponse });

  return (
    <div className={styles.container}>
      <div className="spacer-h-1" />
      <div className={styles.titleContainer}>
        <MenuGroupTitle label={t("sideMenuChannelsTitle")} />
      </div>
      {isLoading && <CircularLoading />}
      <div className="spacer-h-1" />
      {!isLoading && <>
        <div className={styles.channelListContainer}>
          {channelMembershipsResponse?.data
            ?.map(channelMembershipsResponse => channelMembershipsResponse.channel)
            ?.filter(channel => channel != null)
            ?.map(channel =>
              <ChannelButton
                key={channel.channelId}
                channel={channel}
                workspaceUsername={workspace.username} />)
          }
          <JoinableChannelListButton workspaceId={workspace.workspaceId} count={joinable.length} />
          <NewChannelButton workspaceId={workspace.workspaceId} />
        </div>
      </>
      }

    </div>
  );
};

export default ChannelList;