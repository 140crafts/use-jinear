import React from "react";
import styles from "./ChannelList.module.css";
import { useRetrieveMembershipsQuery } from "@/api/channelMemberApi";
import { WorkspaceDto } from "@/be/jinear-core";
import ChannelButton from "@/components/conversationsSectionSideMenu/channelList/channelButton/ChannelButton";
import MenuGroupTitle from "@/components/sideMenu/menuGroupTitle/MenuGroupTitle";
import useTranslation from "../../../locales/useTranslation";
import CircularLoading from "@/components/circularLoading/CircularLoading";

interface ChannelListProps {
  workspace: WorkspaceDto;
}

const ChannelList: React.FC<ChannelListProps> = ({ workspace }) => {
  const { t } = useTranslation();

  const onNewChannel = () => {
  };

  const onDetail = () => {
  };

  const {
    data: channelMembershipsResponse,
    isSuccess,
    isFetching
  } = useRetrieveMembershipsQuery({ workspaceId: workspace.workspaceId });

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
          ?.map(channel => <ChannelButton key={channel?.channelId} channel={channel} workspaceUsername={workspace.username}/>)
        }
      </div>

    </div>
  );
};

export default ChannelList;