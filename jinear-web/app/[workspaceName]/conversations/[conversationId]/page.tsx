"use client";
import React, { useEffect, useState } from "react";
import styles from "./page.module.css";
import { useWorkspaceFromName } from "@/hooks/useWorkspaceFromName";
import { useParams } from "next/navigation";
import ConversationScreenHeader
  from "@/components/conversationScreen/conversationScreenHeader/ConversationScreenHeader";
import ConversationBody from "@/components/conversationScreen/conversationBody/ConversationBody";
import ReplyConversationInput from "@/components/conversationScreen/replyConversationInput/ReplyConversationInput";
import useElementSize from "@/hooks/useElementSize";
import { PureClientOnly } from "@/components/clientOnly/ClientOnly";

interface ConversationPageProps {

}

const ConversationPage: React.FC<ConversationPageProps> = ({}) => {
  const params = useParams();
  const workspaceName: string = params?.workspaceName as string;
  const conversationId: string = params?.conversationId as string;
  const workspace = useWorkspaceFromName(workspaceName);

  const [container, setContainer] = useState<HTMLElement | null>();
  const { offsetWidth } = useElementSize(container);

  useEffect(() => {
    setTimeout(() => {
      const container = document.getElementById(`conversation-page-container`);
      setContainer(container);
    }, 750);
  }, []);

  return (
    <PureClientOnly>
      <div id={`conversation-page-container`} className={styles.container}>
        {workspace && conversationId &&
          <>
            <ConversationScreenHeader conversationId={conversationId} workspaceId={workspace.workspaceId} />
            <ConversationBody conversationId={conversationId} workspaceId={workspace.workspaceId} />
            <ReplyConversationInput
              workspaceId={workspace.workspaceId}
              conversationId={conversationId}
              width={offsetWidth}
            />
          </>
        }
      </div>
    </PureClientOnly>
  );
};

export default ConversationPage;