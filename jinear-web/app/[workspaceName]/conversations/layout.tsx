"use client";
import ConversationsSectionSideMenu from "@/components/conversationsSectionSideMenu/ConversationsSectionSideMenu";
import cn from "classnames";
import React from "react";
import styles from "./layout.module.scss";
import SecondLevelSideMenuV2 from "@/components/secondLevelSideMenuV2/SecondLevelSideMenuV2";

interface ConversationsLayoutProps {
  children: React.ReactNode;
}

const ConversationsLayout: React.FC<ConversationsLayoutProps> = ({ children }) => {

  return (
    <div id="conversations-layout-container" className={styles.container}>
      <SecondLevelSideMenuV2>
        <ConversationsSectionSideMenu />
      </SecondLevelSideMenuV2>
      <div
        id="conversations-layout-content"
        className={cn(styles.contentContainer, styles.contentContainerWithSideMenu)}
      >
        {children}
      </div>
    </div>
  );
};

export default ConversationsLayout;
