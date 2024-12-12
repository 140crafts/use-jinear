import React from "react";
import styles from "./ConversationScreenHeader.module.css";
import ConversationMembers
  from "@/components/conversationScreen/conversationScreenHeader/conversationMembers/ConversationMembers";
import { useAppDispatch } from "@/store/store";
import { popConversationSettingsModal } from "@/slice/modalSlice";

interface ConversationScreenHeaderProps {
  conversationId: string,
  workspaceId: string,
}

const ConversationScreenHeader: React.FC<ConversationScreenHeaderProps> = ({ conversationId, workspaceId }) => {
  const dispatch = useAppDispatch();

  const popConversationSettings = () => {
    dispatch(popConversationSettingsModal({ workspaceId, conversationId, visible: true }));
  };

  return (
    <div className={styles.container}>
      <div className={styles.titleContainer}>
        {/*<Button className={styles.iconButton} heightVariant={ButtonHeight.short}*/}
        {/*        onClick={popConversationSettings}><LuSettings2 /></Button>*/}
        {/*<div className={"spacer-w-1"} />*/}
        <ConversationMembers conversationId={conversationId} workspaceId={workspaceId} />
      </div>
      <div className={styles.borderBottom} />
    </div>
  );
};

export default ConversationScreenHeader;