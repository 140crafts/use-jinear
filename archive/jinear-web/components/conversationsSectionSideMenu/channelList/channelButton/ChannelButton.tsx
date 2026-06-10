import React from "react";
import styles from "./ChannelButton.module.css";
import { PlainChannelDto } from "@/be/jinear-core";
import { shortenStringIfMoreThanMaxLength } from "@/utils/textUtil";
import cn from "classnames";
import Button, { ButtonHeight, ButtonVariants } from "@/components/button";
import { LuGlobe, LuHash, LuLock } from "react-icons/lu";
import { useLiveQuery } from "dexie-react-hooks";
import { getChannelLastActivity, getChannelLastCheck } from "../../../../repository/IndexedDbRepository";
import { isAfter } from "date-fns";
import Logger from "@/utils/logger";

interface ChannelButtonProps {
  channel: PlainChannelDto;
  workspaceUsername: string;
}

const CHANNEL_VISIBILITY_ICON_MAP = {
  EVERYONE: LuHash,
  MEMBERS_ONLY: LuLock,
  PUBLIC_WITH_GUESTS: LuGlobe
};

const logger = Logger("ChannelButton");

const ChannelButton: React.FC<ChannelButtonProps> = ({ channel, workspaceUsername }) => {
  const Icon = CHANNEL_VISIBILITY_ICON_MAP[channel.channelVisibilityType];
  const channelLastCheck = useLiveQuery(() => getChannelLastCheck(channel.channelId));
  const channelLastActivity = useLiveQuery(() => getChannelLastActivity(channel.channelId));
  const unread =
    (!channelLastCheck || !channelLastCheck?._timestamp || Number.isNaN(channelLastCheck?._timestamp)) ||
    (channelLastCheck && channelLastActivity && isAfter(new Date(channelLastActivity._timestamp), new Date(channelLastCheck._timestamp)));

  logger.log({
    channelLastCheck: channelLastCheck?._timestamp,
    channelLastActivity: channelLastActivity?._timestamp,
    unread
  });

  return (
    <Button className={styles.container} heightVariant={ButtonHeight.short2x} variant={ButtonVariants.hoverFilled2}
            href={`/${workspaceUsername}/conversations/channel/${channel.channelId}`}>
      <Icon />
      <span className={cn(styles.channelName, "single-line", unread ? styles.unread : undefined)}>
        {shortenStringIfMoreThanMaxLength({
          text: channel.title,
          maxLength: 29
        })}
      </span>
    </Button>
  );
};

export default ChannelButton;