import React from "react";
import styles from "./ConversationList.module.css";
import MenuGroupTitle from "@/components/sideMenu/menuGroupTitle/MenuGroupTitle";
import CircularLoading from "@/components/circularLoading/CircularLoading";
import useTranslation from "@/locals/useTranslation";
import { useRetrieveParticipatedConversationsQuery } from "@/api/conversationApi";
import { WorkspaceDto } from "@/be/jinear-core";
import Logger from "@/utils/logger";
import ConversationButton
  from "@/components/conversationsSectionSideMenu/conversationList/conversationButton/ConversationButton";
import { useTypedSelector } from "@/store/store";
import { selectCurrentAccountId } from "@/slice/accountSlice";
import NewConversationButton
  from "@/components/conversationsSectionSideMenu/conversationList/newConversationButton/NewConversationButton";

interface ConversationListProps {
  workspace: WorkspaceDto;
}

const logger = Logger("ConversationList");

const ConversationList: React.FC<ConversationListProps> = ({ workspace }) => {
  const { t } = useTranslation();
  const currentAccountId = useTypedSelector(selectCurrentAccountId);
  const {
    data: participatedConversationsResponse,
    isLoading
  } = useRetrieveParticipatedConversationsQuery({ workspaceId: workspace.workspaceId });

  return (
    <div className={styles.container}>
      <div className="spacer-h-1" />
      <div className={styles.titleContainer}>
        <MenuGroupTitle label={t("sideMenuConversations")} />
      </div>
      {isLoading && <CircularLoading />}
      <div className="spacer-h-1" />
      {!isLoading && currentAccountId && <>
        <div className={styles.conversationsListContainer}>
          {participatedConversationsResponse?.data
            ?.map(conversationParticipant => conversationParticipant.conversation)
            ?.filter(conversation => conversation != null)
            ?.map(conversation => <ConversationButton
              key={conversation.conversationId}
              conversation={conversation}
              currentAccountId={currentAccountId}
              workspaceUsername={workspace.username} />
            )
          }
          <NewConversationButton workspaceId={workspace.workspaceId} workspaceName={workspace.username} />
        </div>
      </>}

    </div>);
};

export default ConversationList;