"use client";
import React from "react";
import styles from "./ChannelScreenHeader.module.css";
import { useChannelFromChannelMemberships } from "@/hooks/messaging/useChannelFromChannelMemberships";
import ChannelMembers from "@/components/channelScreen/channelScreenHeader/channelMembers/ChannelMembers";
import Button, { ButtonHeight } from "@/components/button";
import { LuSettings2 } from "react-icons/lu";
import { useAppDispatch } from "@/store/store";
import { popChannelSettingsModal } from "@/slice/modalSlice";

interface ChannelScreenHeaderProps {
  workspaceName: string;
  channelId: string;
  workspaceId: string;
}

const ChannelScreenHeader: React.FC<ChannelScreenHeaderProps> = ({ workspaceName, channelId, workspaceId }) => {
  const dispatch = useAppDispatch();
  const channel = useChannelFromChannelMemberships({ workspaceName, channelId });

  const popChannelSettings = () => {
    dispatch(popChannelSettingsModal({ visible: true, workspaceName, channelId, workspaceId }));
  };

  return (
    <div className={styles.container}>
      <div className={styles.titleContainer}>
        <Button className={styles.iconButton} heightVariant={ButtonHeight.short} onClick={popChannelSettings}><LuSettings2 /></Button>
        <div className={"spacer-w-1"} />
        {channel && <h3>{channel.title}</h3>}
        <div className={"flex-1"} />
        <div className={"spacer-w-1"} />
        <ChannelMembers channelId={channelId} />
      </div>
      <div className={styles.borderBottom} />
    </div>
  );
};

export default ChannelScreenHeader;