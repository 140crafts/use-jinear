"use client";
import React, { useRef } from "react";
import styles from "./page.module.css";
import { useParams } from "next/navigation";
import useTranslation from "@/locals/useTranslation";
import ChannelScreenHeader from "@/components/channelScreen/channelScreenHeader/ChannelScreenHeader";
import { useWorkspaceFromName } from "@/hooks/useWorkspaceFromName";
import ChannelBody from "@/components/channelScreen/channelBody/ChannelBody";
import NewThreadInput from "@/components/channelScreen/newThreadInput/NewThreadInput";
import useElementSize from "@/hooks/useElementSize";
import { PureClientOnly } from "@/components/clientOnly/ClientOnly";

interface ChannelPageProps {

}

const ChannelPage: React.FC<ChannelPageProps> = ({}) => {
  const { t } = useTranslation();
  const params = useParams();
  const workspaceName: string = params?.workspaceName as string;
  const channelId: string = params?.channelId as string;
  const workspace = useWorkspaceFromName(workspaceName);
  const containerRef = useRef<HTMLDivElement>(null);
  const { offsetWidth } = useElementSize(containerRef.current);

  return (
    <PureClientOnly>
      <div ref={containerRef} className={styles.container}>
        {workspace &&
          <>
            <ChannelScreenHeader channelId={channelId}
                                 workspaceName={workspaceName}
                                 workspaceId={workspace.workspaceId}
            />
            <ChannelBody
              channelId={channelId}
              workspaceName={workspaceName}
              workspaceId={workspace.workspaceId}
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