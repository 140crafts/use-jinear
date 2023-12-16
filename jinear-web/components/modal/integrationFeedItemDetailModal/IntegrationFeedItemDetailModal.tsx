import FeedItemDetail from "@/components/feedItemDetail/FeedItemDetail";
import useWindowSize from "@/hooks/useWindowSize";
import {
  closeIntegrationFeedItemDetailModal,
  selectIntegrationFeedItemDetailModalFeedId,
  selectIntegrationFeedItemDetailModalFeedItemId,
  selectIntegrationFeedItemDetailModalTitle,
  selectIntegrationFeedItemDetailModalVisible,
  selectIntegrationFeedItemDetailModalWorkspace,
} from "@/store/slice/modalSlice";
import { useAppDispatch, useTypedSelector } from "@/store/store";
import useTranslation from "locales/useTranslation";
import React from "react";
import Modal from "../modal/Modal";

interface IntegrationFeedItemDetailModalProps {}

const IntegrationFeedItemDetailModal: React.FC<IntegrationFeedItemDetailModalProps> = ({}) => {
  const { t } = useTranslation();
  const dispatch = useAppDispatch();
  const { isMobile } = useWindowSize();

  const visible = useTypedSelector(selectIntegrationFeedItemDetailModalVisible);
  const workspace = useTypedSelector(selectIntegrationFeedItemDetailModalWorkspace);
  const feedId = useTypedSelector(selectIntegrationFeedItemDetailModalFeedId);
  const itemId = useTypedSelector(selectIntegrationFeedItemDetailModalFeedItemId);
  const title = useTypedSelector(selectIntegrationFeedItemDetailModalTitle);

  const close = () => {
    dispatch(closeIntegrationFeedItemDetailModal());
  };

  return (
    <Modal
      visible={visible}
      width={isMobile ? "fullscreen" : "xxlarge"}
      height={"height-full"}
      title={title || ""}
      hasTitleCloseButton={true}
      requestClose={close}
    >
      {feedId && itemId && workspace && <FeedItemDetail workspace={workspace} feedId={feedId} itemId={itemId} />}
    </Modal>
  );
};

export default IntegrationFeedItemDetailModal;
