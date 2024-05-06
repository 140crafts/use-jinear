import React, { ChangeEvent, useEffect } from "react";
import styles from "./ChannelInfoTab.module.css";
import { ChannelParticipationType, ChannelVisibilityType, PlainChannelDto } from "@/be/jinear-core";
import useTranslation from "@/locals/useTranslation";
import { useChannelMembership } from "@/hooks/messaging/useChannelMembership";
import Button, { ButtonHeight } from "@/components/button";
import { LuPen } from "react-icons/lu";
import { changeLoadingModalVisibility, closeBasicTextInputModal, popBasicTextInputModal } from "@/slice/modalSlice";
import { useAppDispatch } from "@/store/store";
import {
  useUpdateChannelParticipationMutation,
  useUpdateChannelTitleMutation,
  useUpdateChannelVisibilityMutation
} from "@/api/channelApi";

interface ChannelInfoTabProps {
  channel: PlainChannelDto;
}

const ChannelInfoTab: React.FC<ChannelInfoTabProps> = ({ channel }) => {
  const { t } = useTranslation();
  const dispatch = useAppDispatch();
  const [updateChannelTitle, { isLoading: isUpdateTitleLoading }] = useUpdateChannelTitleMutation();
  const [updateChannelVisibility, { isLoading: isUpdateVisibilityLoading }] = useUpdateChannelVisibilityMutation();
  const [updateChannelParticipation, { isLoading: isUpdateParticipationLoading }] = useUpdateChannelParticipationMutation();
  const channelMembership = useChannelMembership({ workspaceId: channel.workspaceId, channelId: channel.channelId });
  const isAdmin = ["ADMIN", "OWNER"].includes(channelMembership?.roleType || "");

  useEffect(() => {
    dispatch(changeLoadingModalVisibility({ visible: isUpdateVisibilityLoading || isUpdateParticipationLoading || isUpdateTitleLoading }));
  }, [dispatch, isUpdateVisibilityLoading, isUpdateParticipationLoading, isUpdateTitleLoading]);

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

  const onParticipationChange = (event: ChangeEvent<HTMLSelectElement>) => {
    const participationType = event.target.value as ChannelParticipationType;
    updateChannelParticipation({ channelId: channel.channelId, body: { participationType } });
  };

  return (
    <div className={styles.container}>
      <div className={styles.section}>
        <h3>{t("channelGeneralInfoChannelTitle")}</h3>
        <div className={styles.sectionDataContainer}>
          <div>{channel.title}</div>
          {isAdmin && <Button className={styles.iconButton} heightVariant={ButtonHeight.short}
                              onClick={pickNewTitle}><LuPen /></Button>}
        </div>
      </div>

      <div className={styles.section}>
        <h3>{t("channelGeneralInfoChanelVisibility")}</h3>
        <div className={styles.sectionDataContainer}>
          <div className={styles.selectContainer}>
            <select disabled={!isAdmin} className={styles.select} value={channel.channelVisibilityType}
                    onChange={onVisibilityChange}>
              <option className={styles.option}
                      value={"EVERYONE"}>{t(`channelGeneralInfoChanelVisibility_EVERYONE`)}</option>
              <option className={styles.option}
                      value={"MEMBERS_ONLY"}>{t(`channelGeneralInfoChanelVisibility_MEMBERS_ONLY`)}</option>
            </select>
            <div>
              {t(`channelGeneralInfoChanelVisibility_description_${channel.channelVisibilityType}`)}
            </div>
          </div>
        </div>
      </div>

      <div className={styles.section}>
        <h3>{t("channelGeneralInfoChanelParticipation")}</h3>
        <div className={styles.sectionDataContainer}>
          <div className={styles.selectContainer}>
            <select disabled={!isAdmin} className={styles.select} value={channel.participationType}
                    onChange={onParticipationChange}>
              <option className={styles.option}
                      value={"EVERYONE"}>{t(`channelGeneralInfoChanelParticipation_EVERYONE`)}</option>
              <option className={styles.option}
                      value={"ADMINS_CAN_START_CONVERSATION_EVERYONE_CAN_REPLY"}>{t(`channelGeneralInfoChanelParticipation_ADMINS_CAN_START_CONVERSATION_EVERYONE_CAN_REPLY`)}</option>
              <option className={styles.option}
                      value={"READ_ONLY"}>{t(`channelGeneralInfoChanelParticipation_READ_ONLY`)}</option>
            </select>
            <div>
              {t(`channelGeneralInfoChanelParticipation_description_${channel.participationType}`)}
            </div>
          </div>
        </div>
      </div>

    </div>
  );
};

export default ChannelInfoTab;