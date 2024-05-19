import React, { useEffect, useState } from "react";
import styles from "./NewConversationModal.module.css";
import Modal from "@/components/modal/modal/Modal";
import {
  closeNewConversationModal,
  selectNewConversationModalVisible,
  selectNewConversationModalWorkspaceId, selectNewConversationModalWorkspaceName
} from "@/slice/modalSlice";
import useTranslation from "@/locals/useTranslation";
import { useAppDispatch, useTypedSelector } from "@/store/store";
import useWindowSize from "@/hooks/useWindowSize";
import { WorkspaceMemberDto } from "@/be/jinear-core";
import WorkspaceMemberInputPicker from "@/components/workspaceMemberInputPicker/WorkspaceMemberInputPicker";
import NewConversationInput from "@/components/modal/newConversationModal/newConversationInput/NewConversationInput";
import { useInitializeConversationMutation } from "@/api/conversationApi";
import { toast } from "react-hot-toast";
import { useRouter } from "next/navigation";
import { selectCurrentAccountId } from "@/slice/accountSlice";

interface NewConversationModalProps {

}

const NewConversationModal: React.FC<NewConversationModalProps> = ({}) => {
  const { t } = useTranslation();
  const router = useRouter();
  const dispatch = useAppDispatch();
  const { isMobile } = useWindowSize();
  const visible = useTypedSelector(selectNewConversationModalVisible);
  const workspaceId = useTypedSelector(selectNewConversationModalWorkspaceId);
  const workspaceName = useTypedSelector(selectNewConversationModalWorkspaceName);

  const [selectedWorkspaceMembers, setSelectedWorkspaceMembers] = useState<WorkspaceMemberDto[]>([]);
  const [initializeConversation, {
    data: initializeConversationResponse,
    isLoading
  }] = useInitializeConversationMutation();

  const close = () => {
    dispatch(closeNewConversationModal());
  };

  const onSelectionChange = (pickedList: WorkspaceMemberDto[]) => {
    setSelectedWorkspaceMembers(pickedList);
  };

  const initialize = (message: string) => {
    if (selectedWorkspaceMembers.length <= 0) {
      toast(t("newConversationSendToEmpty"));
      return;
    }
    if (workspaceId) {
      const participantAccountIds = selectedWorkspaceMembers.map(wm => wm.accountId);
      initializeConversation({ workspaceId, initialMessageBody: message, participantAccountIds });
    }
  };

  useEffect(() => {
    if (initializeConversationResponse?.responseStatusType == "SUCCESS" && initializeConversationResponse?.data && workspaceName) {
      const conversationId = initializeConversationResponse?.data;
      router.replace(`/${workspaceName}/conversations/${conversationId}`);
      setTimeout(() => close(), 500);
    }
  }, [router, workspaceName, initializeConversationResponse, close]);

  return (
    <Modal
      visible={visible}
      width={isMobile ? "fullscreen" : "default"}
      title={t("newConversationModalTitle")}
      hasTitleCloseButton={true}
      requestClose={close}
      bodyClass={styles.modalBody}
      contentContainerClass={styles.modal}
      containerClassName={styles.modalContainer}
    >
      {workspaceId && <WorkspaceMemberInputPicker disabled={isLoading} workspaceId={workspaceId}
                                                  onSelectionChange={onSelectionChange} />}
      <div className={"flex-1"} />
      <NewConversationInput onSubmit={initialize} isLoading={isLoading} />
    </Modal>
  );
};

export default NewConversationModal;