import React, { useMemo } from "react";
import styles from "./ConversationList.module.css";
import MenuGroupTitle from "@/components/sideMenu/menuGroupTitle/MenuGroupTitle";
import CircularLoading from "@/components/circularLoading/CircularLoading";
import useTranslation from "@/locals/useTranslation";
import { useRetrieveParticipatedConversationsQuery } from "@/api/conversationApi";
import { ConversationParticipantDto, WorkspaceDto } from "@/be/jinear-core";
import Logger from "@/utils/logger";
import ConversationButton
  from "@/components/conversationsSectionSideMenu/conversationList/conversationButton/ConversationButton";
import { useTypedSelector } from "@/store/store";
import { selectCurrentAccountId } from "@/slice/accountSlice";
import NewConversationButton
  from "@/components/conversationsSectionSideMenu/conversationList/newConversationButton/NewConversationButton";
import { useLiveQuery } from "dexie-react-hooks";
import { getSortedConversationIds } from "../../../repository/MessageRepository";

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
  const distinctSortedConversationIds = useLiveQuery(() => getSortedConversationIds());

  const sortedParticipatedConversations = useMemo(() => {
    const participatedConversations = participatedConversationsResponse?.data;
    if (distinctSortedConversationIds && participatedConversations) {
      return distinctSortedConversationIds
        .map(conversationId =>
          participatedConversations.find(participatedConversation => participatedConversation.conversationId == conversationId) || -1)
        .filter(participantDto => participantDto != -1) as ConversationParticipantDto[];
    }
    return [];
  }, [participatedConversationsResponse, distinctSortedConversationIds]);

  logger.log({ sortedParticipatedConversations, participatedConversationsResponse, distinctSortedConversationIds });

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
          {sortedParticipatedConversations
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