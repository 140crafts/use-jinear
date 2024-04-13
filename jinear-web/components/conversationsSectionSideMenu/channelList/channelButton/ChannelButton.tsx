import React from "react";
import styles from "./ChannelButton.module.css";
import { PlainChannelDto } from "@/be/jinear-core";
import { shortenStringIfMoreThanMaxLength } from "@/utils/textUtil";
import cn from "classnames";
import Button, { ButtonHeight } from "@/components/button";
import { LuGlobe2, LuHash, LuLock } from "react-icons/lu";

interface ChannelButtonProps {
  channel: PlainChannelDto;
  workspaceUsername: string;
}

const CHANNEL_VISIBILITY_ICON_MAP = {
  EVERYONE: LuHash,
  MEMBERS_ONLY: LuLock,
  PUBLIC_WITH_GUESTS: LuGlobe2
};

const ChannelButton: React.FC<ChannelButtonProps> = ({ channel, workspaceUsername }) => {
  const Icon = CHANNEL_VISIBILITY_ICON_MAP[channel.channelVisibilityType];

  return (
    <Button className={styles.container} heightVariant={ButtonHeight.short2x} href={`/${workspaceUsername}/conversations/channel/${channel.channelId}`}>
      <Icon />
      <span className={cn(styles.channelName, "single-line")}>
        {shortenStringIfMoreThanMaxLength({
          text: channel.title,
          maxLength: 29
        })}
      </span>
    </Button>
  );
};

export default ChannelButton;