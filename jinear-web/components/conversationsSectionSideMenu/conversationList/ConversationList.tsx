import React, { useMemo } from "react";
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
import { selectConversationLastCheck, selectConversationLastCheckMap } from "@/slice/messagingSlice";

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
  const conversationLastCheckMap = useTypedSelector(selectConversationLastCheckMap(workspace.workspaceId));

  const sortedParticipatedConversations = useMemo(() => {
    if (conversationLastCheckMap && participatedConversationsResponse) {
      const mergedData = participatedConversationsResponse.data.map((conversationParticipantDto) => {
        const localLastCheck = conversationLastCheckMap[conversationParticipantDto.conversationId] ? conversationLastCheckMap[conversationParticipantDto.conversationId] : new Date();
        return { conversationParticipantDto: conversationParticipantDto, localLastCheck };
      });
      return mergedData.sort((a, b) => new Date(a.localLastCheck).getTime() - new Date(b.localLastCheck).getTime()).map(val => val.conversationParticipantDto);
    }
    return [];
  }, [participatedConversationsResponse, conversationLastCheckMap]);

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