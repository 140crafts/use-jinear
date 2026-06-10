import React from "react";
import styles from "./ConversationSettingsModal.module.scss";
import Modal from "@/components/modal/modal/Modal";
import TabbedPanel from "@/components/tabbedPanel/TabbedPanel";
import TabView from "@/components/tabbedPanel/tabView/TabView";
import CircularLoading from "@/components/circularLoading/CircularLoading";
import { useAppDispatch, useTypedSelector } from "@/store/store";
import {
  selectConversationSettingsModalConversationId,
  selectConversationSettingsModalVisible,
  selectConversationSettingsModalWorkspaceId
} from "@/slice/modalSlice";
import useTranslation from "@/locals/useTranslation";
import useWindowSize from "@/hooks/useWindowSize";

interface ConversationSettingsModalProps {

}

const ConversationSettingsModal: React.FC<ConversationSettingsModalProps> = ({}) => {
  const { t } = useTranslation();
  const visible = useTypedSelector(selectConversationSettingsModalVisible);
  const workspaceId = useTypedSelector(selectConversationSettingsModalWorkspaceId);
  const conversationId = useTypedSelector(selectConversationSettingsModalConversationId);
  const dispatch = useAppDispatch();
  const { isMobile } = useWindowSize();

  const close = () => {
  };

  return (<Modal
      visible={visible}
      width={isMobile ? "fullscreen" : "xxlarge"}
      title={t("conversationsScreenTitle")}
      hasTitleCloseButton={true}
      requestClose={close}
      bodyClass={styles.modalBody}
      contentContainerClass={styles.modal}
      containerClassName={styles.modalContainer}
    >
      {conversationId && workspaceId ?
        <TabbedPanel initialTabName="conversation-info">
          <TabView name="conversation-info" label={t("channelSettingsModalInfoTab")}>
            {/*<ChannelInfoTab channel={channel} />*/}
          </TabView>
          <TabView name="conversation-files" label={t("channelSettingsModalMembersTab")}>
            {/*<WorkspaceMembersTab channelId={channelId} workspaceId={workspaceId} />*/}
          </TabView>
        </TabbedPanel> :
        <CircularLoading />
      }
    </Modal>
  );
};

export default ConversationSettingsModal;