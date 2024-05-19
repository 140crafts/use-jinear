import React from "react";
import styles from "./ChannelListModal.module.css";
import {
  closeChannelListModal,
  selectChannelListModalVisible,
  selectChannelListModalWorkspaceId
} from "@/slice/modalSlice";
import useTranslation from "@/locals/useTranslation";
import { useAppDispatch, useTypedSelector } from "@/store/store";
import useWindowSize from "@/hooks/useWindowSize";
import Modal from "@/components/modal/modal/Modal";
import { useListChannelsQuery } from "@/api/channelApi";
import CircularLoading from "@/components/circularLoading/CircularLoading";
import ChannelButton from "@/components/modal/channelListModal/channelButton/ChannelButton";

interface ChannelListModalProps {

}

const ChannelListModal: React.FC<ChannelListModalProps> = ({}) => {
  const { t } = useTranslation();
  const dispatch = useAppDispatch();
  const { isMobile } = useWindowSize();

  const visible = useTypedSelector(selectChannelListModalVisible);
  const workspaceId = useTypedSelector(selectChannelListModalWorkspaceId);

  const {
    data: channelListResponse,
    isFetching: isChannelListFetching
  } = useListChannelsQuery({ workspaceId: workspaceId || "" }, { skip: workspaceId == null });

  const close = () => {
    dispatch(closeChannelListModal());
  };

  return (
    <Modal
      visible={visible}
      width={isMobile ? "fullscreen" : "default"}
      title={t("channelListModalTitle")}
      hasTitleCloseButton={true}
      requestClose={close}
      bodyClass={styles.modalBody}
      contentContainerClass={styles.modal}
      containerClassName={styles.modalContainer}
    >
      {isChannelListFetching && <CircularLoading />}
      <div className={styles.listContainer}>
      {channelListResponse?.data?.map(channelInfo =>
        <ChannelButton
          key={`channel-list-modal-${channelInfo.channel.channelId}`}
          channelMembershipInfoDto={channelInfo}
        />
      )}
      </div>
    </Modal>
  );
};

export default ChannelListModal;