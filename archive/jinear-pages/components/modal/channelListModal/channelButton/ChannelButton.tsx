import React, { useEffect } from "react";
import styles from "./ChannelButton.module.css";
import { ChannelMembershipInfoDto } from "@/be/jinear-core";
import Button, { ButtonHeight } from "@/components/button";
import useTranslation from "@/locals/useTranslation";
import { LuGlobe2, LuHash, LuLock } from "react-icons/lu";
import { useJoinChannelMutation } from "@/api/channelMemberApi";
import { useAppDispatch } from "@/store/store";
import { closeChannelListModal } from "@/slice/modalSlice";

interface ChannelButtonProps {
  channelMembershipInfoDto: ChannelMembershipInfoDto;
}

const CHANNEL_VISIBILITY_ICON_MAP = {
  EVERYONE: LuHash,
  MEMBERS_ONLY: LuLock,
  PUBLIC_WITH_GUESTS: LuGlobe2
};

const ChannelButton: React.FC<ChannelButtonProps> = ({ channelMembershipInfoDto }) => {
  const { t } = useTranslation();
  const dispatch = useAppDispatch();
  const Icon = CHANNEL_VISIBILITY_ICON_MAP[channelMembershipInfoDto.channel.channelVisibilityType];
  const [joinChannel, { isLoading, isSuccess }] = useJoinChannelMutation();

  const onJoin = () => {
    joinChannel({ channelId: channelMembershipInfoDto.channel.channelId });
  };

  useEffect(() => {
    if (isSuccess) {
      dispatch(closeChannelListModal());
    }
  }, [dispatch, isSuccess]);

  return (
    <div className={styles.container}>
      <div className={styles.iconContainer}><Icon /></div>
      <div className={styles.name}>
        {channelMembershipInfoDto.channel.title}
      </div>
      <Button
        loading={isLoading}
        disabled={isLoading}
        className={styles.button}
        heightVariant={ButtonHeight.short}
        onClick={onJoin}
      >
        {t("channelListModalJoinChannel")}
      </Button>
    </div>
  );
};

export default ChannelButton;