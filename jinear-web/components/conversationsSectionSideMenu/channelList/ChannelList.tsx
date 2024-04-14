import React from "react";
import styles from "./ChannelList.module.css";
import { useRetrieveChannelMembershipsQuery } from "@/api/channelMemberApi";
import { WorkspaceDto } from "@/be/jinear-core";
import ChannelButton from "@/components/conversationsSectionSideMenu/channelList/channelButton/ChannelButton";
import MenuGroupTitle from "@/components/sideMenu/menuGroupTitle/MenuGroupTitle";
import useTranslation from "../../../locales/useTranslation";
import CircularLoading from "@/components/circularLoading/CircularLoading";
import Logger from "@/utils/logger";

interface ChannelListProps {
  workspace: WorkspaceDto;
}

const logger = Logger("ChannelList");

const ChannelList: React.FC<ChannelListProps> = ({ workspace }) => {
  const { t } = useTranslation();
  const {
    data: channelMembershipsResponse,
    isSuccess,
    isError,
    error,
    isFetching
  } = useRetrieveChannelMembershipsQuery({ workspaceId: workspace.workspaceId });
  logger.log({ ChannelList: workspace, channelMembershipsResponse, isSuccess, isError, error });

  const onNewChannel = () => {
  };

  const onDetail = () => {
  };

  return (
    <div className={styles.container}>
      <div className="spacer-h-1" />
      <div className={styles.titleContainer}>
        <MenuGroupTitle label={t("sideMenuChannelsTitle")} hasAddButton={true} onAddButtonClick={onNewChannel}
                        hasDetailButton={true} onDetailButtonClick={onDetail} />
      </div>
      {isFetching && <CircularLoading />}
      <div className="spacer-h-1" />

      <div className={styles.channelListContainer}>
        {channelMembershipsResponse?.data
          ?.map(channelMembershipsResponse => channelMembershipsResponse.channel)
          ?.filter(channel => channel != null)
          ?.map(channel => <ChannelButton key={channel.channelId} channel={channel}
                                          workspaceUsername={workspace.username} />)
        }
      </div>

    </div>
  );
};

export default ChannelList;