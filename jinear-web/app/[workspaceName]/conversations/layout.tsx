"use client";
import ConversationsSectionSideMenu from "@/components/conversationsSectionSideMenu/ConversationsSectionSideMenu";
import SecondLevelSideMenu from "@/components/secondLevelSideMenu/SecondLevelSideMenu";
import { selectConversationsMenuVisible, toggleConversationsMenu } from "@/store/slice/displayPreferenceSlice";
import { useAppDispatch, useTypedSelector } from "@/store/store";
import cn from "classnames";
import React from "react";
import styles from "./layout.module.scss";

interface ConversationsLayoutProps {
  children: React.ReactNode;
}

const ConversationsLayout: React.FC<ConversationsLayoutProps> = ({ children }) => {
  const dispatch = useAppDispatch();
  const conversationsMenuVisible = useTypedSelector(selectConversationsMenuVisible);

  const toggleMenu = () => {
    dispatch(toggleConversationsMenu());
  };

  return (
    <div id="conversations-layout-container" className={styles.container}>
      <SecondLevelSideMenu open={conversationsMenuVisible} toggle={toggleMenu} className={styles.secondLevelSideMenu}>
        <ConversationsSectionSideMenu />
      </SecondLevelSideMenu>
      <div
        id="conversations-layout-content"
        className={cn(styles.contentContainer, conversationsMenuVisible && styles.contentContainerWithSideMenu)}
      >
        {children}
      </div>
    </div>
  );
};

export default ConversationsLayout;
