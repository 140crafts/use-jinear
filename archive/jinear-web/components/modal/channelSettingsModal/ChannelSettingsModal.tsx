import React from "react";
import styles from "./ChannelSettingsModal.module.scss";
import Modal from "@/components/modal/modal/Modal";
import useTranslation from "@/locals/useTranslation";
import { useAppDispatch, useTypedSelector } from "@/store/store";
import useWindowSize from "@/hooks/useWindowSize";
import {
  closeChannelSettingsModal,
  selectChannelSettingsModalChannelId,
  selectChannelSettingsModalVisible, selectChannelSettingsModalWorkspaceId,
  selectChannelSettingsModalWorkspaceName
} from "@/slice/modalSlice";
import { useChannelFromChannelMemberships } from "@/hooks/messaging/useChannelFromChannelMemberships";
import TabView from "@/components/tabbedPanel/tabView/TabView";
import TabbedPanel from "@/components/tabbedPanel/TabbedPanel";
import WorkspaceMembersTab from "@/components/modal/channelSettingsModal/workspaceMembersTab/WorkspaceMembersTab";
import ChannelInfoTab from "@/components/modal/channelSettingsModal/channelInfoTab/ChannelInfoTab";
import CircularLoading from "@/components/circularLoading/CircularLoading";

interface ChannelSettingsModalProps {

}

const ChannelSettingsModal: React.FC<ChannelSettingsModalProps> = ({}) => {
  const { t } = useTranslation();
  const dispatch = useAppDispatch();
  const { isMobile } = useWindowSize();

  const visible = useTypedSelector(selectChannelSettingsModalVisible);
  const channelId = useTypedSelector(selectChannelSettingsModalChannelId);
  const workspaceName = useTypedSelector(selectChannelSettingsModalWorkspaceName);
  const workspaceId = useTypedSelector(selectChannelSettingsModalWorkspaceId);
  const channel = useChannelFromChannelMemberships({ workspaceName, channelId });

  const close = () => {
    dispatch(closeChannelSettingsModal());
  };

  return (<Modal
      visible={visible}
      width={isMobile ? "fullscreen" : "xxlarge"}
      title={channel?.title || ""}
      hasTitleCloseButton={true}
      requestClose={close}
      bodyClass={styles.modalBody}
      contentContainerClass={styles.modal}
      containerClassName={styles.modalContainer}
    >
      {channel && channelId && workspaceId ?
        <TabbedPanel initialTabName="channel-info">
          <TabView name="channel-info" label={t("channelSettingsModalInfoTab")}>
            <ChannelInfoTab channel={channel} />
          </TabView>
          <TabView name="channel-members" label={t("channelSettingsModalMembersTab")}>
            <WorkspaceMembersTab channelId={channelId} workspaceId={workspaceId} />
          </TabView>
        </TabbedPanel> :
        <CircularLoading />
      }
    </Modal>
  );
};

export default ChannelSettingsModal;