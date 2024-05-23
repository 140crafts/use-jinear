"use client";
import React from "react";
import styles from "./page.module.scss";
import ConversationsSectionSideMenu from "@/components/conversationsSectionSideMenu/ConversationsSectionSideMenu";
import useTranslation from "@/locals/useTranslation";
import NewConversationButton
  from "@/components/conversationsSectionSideMenu/conversationList/newConversationButton/NewConversationButton";
import { useParams } from "next/navigation";
import { useTypedSelector } from "@/store/store";
import { selectWorkspaceFromWorkspaceUsername } from "@/slice/accountSlice";

interface ConversationsPageProps {
}

const ConversationsPage: React.FC<ConversationsPageProps> = ({}) => {
  const { t } = useTranslation();
  const params = useParams();
  const workspaceName = (params?.workspaceName as string) || "";
  const workspace = useTypedSelector(selectWorkspaceFromWorkspaceUsername(workspaceName));

  return <div className={styles.container}>
    <ConversationsSectionSideMenu containerClassName={styles.menu} />
    <div className={styles.emptyPageContent}>
      <h1>{t("conversationPageEmptyStateSubTitle")}</h1>
      <span>{t("conversationPageEmptyStateText")}</span>
      <div className={styles.actionButtonsContainer}>
      {workspace && <NewConversationButton workspaceId={workspace.workspaceId} workspaceName={workspace.username} />}
      </div>
    </div>
  </div>;
};

export default ConversationsPage;
