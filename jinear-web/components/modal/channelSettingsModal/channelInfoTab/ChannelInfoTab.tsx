import React, { ChangeEvent, useEffect } from "react";
import styles from "./ChannelInfoTab.module.css";
import { ChannelVisibilityType, PlainChannelDto } from "@/be/jinear-core";
import useTranslation from "@/locals/useTranslation";
import { useChannelMembership } from "@/hooks/messaging/useChannelMembership";
import Button, { ButtonHeight } from "@/components/button";
import { LuPen } from "react-icons/lu";
import { changeLoadingModalVisibility, closeBasicTextInputModal, popBasicTextInputModal } from "@/slice/modalSlice";
import { useAppDispatch } from "@/store/store";
import { useUpdateChannelTitleMutation, useUpdateChannelVisibilityMutation } from "@/api/channelApi";
import { SelectChangeEvent } from "@mui/material";

interface ChannelInfoTabProps {
  channel: PlainChannelDto;
}

const ChannelInfoTab: React.FC<ChannelInfoTabProps> = ({ channel }) => {
  const { t } = useTranslation();
  const dispatch = useAppDispatch();
  const [updateChannelTitle, {}] = useUpdateChannelTitleMutation();
  const [updateChannelVisibility, { isLoading: isUpdateVisibilityLoading }] = useUpdateChannelVisibilityMutation();
  const channelMembership = useChannelMembership({ workspaceId: channel.workspaceId, channelId: channel.channelId });
  const isAdmin = ["ADMIN", "OWNER"].includes(channelMembership?.roleType || "");

  useEffect(() => {
    dispatch(changeLoadingModalVisibility({ visible: isUpdateVisibilityLoading }));
  }, [dispatch, isUpdateVisibilityLoading]);

  const changeTitle = (newTitle: string) => {
    updateChannelTitle({ channelId: channel.channelId, body: { newTitle } });
    dispatch(closeBasicTextInputModal());
  };

  const pickNewTitle = () => {
    dispatch(
      popBasicTextInputModal({
        visible: true,
        title: t("channelTitleChangeModalTitle"),
        infoText: t("channelTitleChangeModalInfoText"),
        initialText: channel.title,
        onSubmit: changeTitle
      })
    );
  };

  const onVisibilityChange = (event: ChangeEvent<HTMLSelectElement>) => {
    const channelVisibilityType = event.target.value as ChannelVisibilityType;
    updateChannelVisibility({ channelId: channel.channelId, body: { channelVisibilityType } });
  };

  return (
    <div className={styles.container}>
      <div className={styles.section}>
        <h3>{t("channelGeneralInfoChannelTitle")}</h3>
        <div className={styles.sectionDataContainer}>
          <div>{channel.title}</div>
          {isAdmin && <Button heightVariant={ButtonHeight.short} onClick={pickNewTitle}><LuPen /></Button>}
        </div>
      </div>

      <div className={styles.section}>
        <h3>{t("channelGeneralInfoChanelVisibility")}</h3>
        <div className={styles.sectionDataContainer}>
          <div>
            <select disabled={!isAdmin} value={channel.channelVisibilityType} onChange={onVisibilityChange}>
              <option value={"EVERYONE"}>{t(`channelGeneralInfoChanelVisibility_EVERYONE`)}</option>
              <option value={"MEMBERS_ONLY"}>{t(`channelGeneralInfoChanelVisibility_MEMBERS_ONLY`)}</option>
            </select>
          </div>
        </div>
      </div>

    </div>
  );
};

export default ChannelInfoTab;