import React from "react";
import styles from "./ConversationsSectionSideMenu.module.css";
import ChannelList from "@/components/conversationsSectionSideMenu/channelList/ChannelList";
import { useParams } from "next/navigation";
import { useTypedSelector } from "@/store/store";
import { selectWorkspaceFromWorkspaceUsername } from "@/slice/accountSlice";

interface ConversationsSectionSideMenuProps {}

const ConversationsSectionSideMenu: React.FC<ConversationsSectionSideMenuProps> = ({}) => {
  const params = useParams();
  const workspaceName = (params?.workspaceName as string) || "";
  const workspace = useTypedSelector(selectWorkspaceFromWorkspaceUsername(workspaceName));

  return <div className={styles.container}>
    {workspace && <ChannelList workspace={workspace} />}
  </div>;
};

export default ConversationsSectionSideMenu;
