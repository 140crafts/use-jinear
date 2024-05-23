import React from "react";
import styles from "./ConversationsSectionSideMenu.module.css";
import ChannelList from "@/components/conversationsSectionSideMenu/channelList/ChannelList";
import { useParams } from "next/navigation";
import { useTypedSelector } from "@/store/store";
import { selectWorkspaceFromWorkspaceUsername } from "@/slice/accountSlice";
import ClientOnly from "@/components/clientOnly/ClientOnly";
import ConversationList from "@/components/conversationsSectionSideMenu/conversationList/ConversationList";
import cn from "classnames";

interface ConversationsSectionSideMenuProps {
  containerClassName?: string;
}

const ConversationsSectionSideMenu: React.FC<ConversationsSectionSideMenuProps> = ({ containerClassName }) => {
  const params = useParams();
  const workspaceName = (params?.workspaceName as string) || "";
  const workspace = useTypedSelector(selectWorkspaceFromWorkspaceUsername(workspaceName));

  return <ClientOnly className={cn(styles.container, containerClassName)}>
    {workspace && <>
      <ChannelList workspace={workspace} />
      <ConversationList workspace={workspace} />
    </>}
  </ClientOnly>;
};

export default ConversationsSectionSideMenu;
