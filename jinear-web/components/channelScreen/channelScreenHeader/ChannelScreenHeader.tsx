"use client";
import React from "react";
import styles from "./ChannelScreenHeader.module.css";
import { useChannelFromChannelMemberships } from "@/hooks/messaging/useChannelFromChannelMemberships";
import ChannelMembers from "@/components/channelScreen/channelScreenHeader/channelMembers/ChannelMembers";

interface ChannelScreenHeaderProps {
  workspaceName: string;
  channelId: string;
}

const ChannelScreenHeader: React.FC<ChannelScreenHeaderProps> = ({ workspaceName, channelId }) => {
  const channel = useChannelFromChannelMemberships({ workspaceName, channelId });
  return (
    <div className={styles.container}>
      <div className={styles.titleContainer}>
        {channel && <h3>{channel.title}</h3>}
        <ChannelMembers channelId={channelId} />
      </div>
      <div className={styles.borderBottom} />
    </div>
  );
};

export default ChannelScreenHeader;