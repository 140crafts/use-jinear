import TopicForm from "@/components/form/topicForm/TopicForm";
import { selectCurrentAccountsPreferredTeamId, selectCurrentAccountsPreferredWorkspaceId } from "@/store/slice/accountSlice";
import { closeNewTopicModal, selectNewTopicModalVisible } from "@/store/slice/modalSlice";
import { useAppDispatch, useTypedSelector } from "@/store/store";
import useTranslation from "locales/useTranslation";
import React from "react";
import Modal from "../modal/Modal";
import styles from "./NewTopicModal.module.css";

interface NewTopicModalProps {}

const NewTopicModal: React.FC<NewTopicModalProps> = ({}) => {
  const { t } = useTranslation();
  const dispatch = useAppDispatch();
  const visible = useTypedSelector(selectNewTopicModalVisible);
  const workspaceId = useTypedSelector(selectCurrentAccountsPreferredWorkspaceId);
  const teamId = useTypedSelector(selectCurrentAccountsPreferredTeamId);

  const close = () => {
    dispatch(closeNewTopicModal());
  };

  return (
    <Modal
      visible={visible}
      title={t("newTopicModalTitle")}
      bodyClass={styles.container}
      //   width={isMobile ? "fullscreen" : "large"}
      hasTitleCloseButton={true}
      requestClose={close}
    >
      <TopicForm workspaceId={workspaceId} teamId={teamId} onSuccess={close} />
    </Modal>
  );
};

export default NewTopicModal;
