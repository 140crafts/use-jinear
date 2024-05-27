"use client";
import React, { useEffect, useState } from "react";
import styles from "./page.module.css";
import { useParams } from "next/navigation";
import ChannelScreenHeader from "@/components/channelScreen/channelScreenHeader/ChannelScreenHeader";
import { useWorkspaceFromName } from "@/hooks/useWorkspaceFromName";
import ChannelBody from "@/components/channelScreen/channelBody/ChannelBody";
import NewThreadInput from "@/components/channelScreen/newThreadInput/NewThreadInput";
import useElementSize from "@/hooks/useElementSize";
import { PureClientOnly } from "@/components/clientOnly/ClientOnly";
import { useChannelMembership } from "@/hooks/messaging/useChannelMembership";

interface ChannelPageProps {

}

const ChannelPage: React.FC<ChannelPageProps> = () => {
  const params = useParams();
  const workspaceName: string = params?.workspaceName as string;
  const channelId: string = params?.channelId as string;
  const workspace = useWorkspaceFromName(workspaceName);
  const channelMembership = useChannelMembership({ workspaceId: workspace?.workspaceId, channelId });
  const isAdmin = ["ADMIN", "OWNER"].includes(channelMembership?.roleType ?? "");
  const canReplyThreads = "READ_ONLY" != channelMembership?.channel.participationType || isAdmin;

  const [container, setContainer] = useState<HTMLElement | null>();
  const { offsetWidth } = useElementSize(container);

  useEffect(() => {
    setTimeout(() => {
      const container = document.getElementById(`channel-page-container`);
      setContainer(container);
    }, 750);
  }, []);

  return (
    <PureClientOnly>
      <div id={`channel-page-container`} className={styles.container}>
        {workspace &&
          <>
            <ChannelScreenHeader
              channelId={channelId}
              workspaceName={workspaceName}
              workspaceId={workspace.workspaceId}
            />
            <ChannelBody
              channelId={channelId}
              workspaceName={workspaceName}
              workspaceId={workspace.workspaceId}
              canReplyThreads={canReplyThreads}
            />
            <NewThreadInput
              channelId={channelId}
              workspaceName={workspaceName}
              workspaceId={workspace.workspaceId}
              width={offsetWidth || 0}
            />
          </>
        }
      </div>
    </PureClientOnly>
  );
};

export default ChannelPage;