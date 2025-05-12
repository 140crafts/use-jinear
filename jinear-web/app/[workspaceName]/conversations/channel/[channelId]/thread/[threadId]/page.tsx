"use client";
import React from "react";
import styles from "./page.module.css";
import { useParams } from "next/navigation";
import { useWorkspaceFromName } from "@/hooks/useWorkspaceFromName";
import { useChannelMembership } from "@/hooks/messaging/useChannelMembership";
import ChannelScreenHeader from "@/components/channelScreen/channelScreenHeader/ChannelScreenHeader";
import { PureClientOnly } from "@/components/clientOnly/ClientOnly";
import { useRetrieveThreadQuery } from "@/api/threadApi";
import CircularLoading from "@/components/circularLoading/CircularLoading";
import Thread from "@/components/channelScreen/channelBody/thread/Thread";
import { getThreadWithMessages } from "../../../../../../../repository/IndexedDbRepository";
import { useLiveQuery } from "dexie-react-hooks";

interface ThreadDetailScreenProps {

}

const ThreadDetailScreen: React.FC<ThreadDetailScreenProps> = ({}) => {
  const params = useParams();
  const workspaceName: string = params?.workspaceName as string;
  const channelId: string = params?.channelId as string;
  const threadId: string = params?.threadId as string;
  const workspace = useWorkspaceFromName(workspaceName);

  const { isFetching } = useRetrieveThreadQuery({
    workspaceId: workspace?.workspaceId || "",
    threadId
  }, { skip: threadId == null || workspace == null });
  const channelMembership = useChannelMembership({ workspaceId: workspace?.workspaceId, channelId });
  const isAdmin = ["ADMIN", "OWNER"].includes(channelMembership?.roleType ?? "");
  const canReplyThreads = "READ_ONLY" != channelMembership?.channel.participationType || isAdmin;
  const threadWithMessages = useLiveQuery(() => getThreadWithMessages(threadId));

  return (
    <PureClientOnly>
      <div id={`channel-page-container`} className={styles.container}>
        {workspace &&
          <>
            <ChannelScreenHeader channelId={channelId}
                                 workspaceName={workspaceName}
                                 workspaceId={workspace.workspaceId}
            />
            {isFetching && <CircularLoading />}
            <div className={"spacer-h-2"} />
            {threadWithMessages &&
              <Thread
                threadId={threadWithMessages.threadId}
                channelId={threadWithMessages.channelId}
                canReplyThreads={canReplyThreads}
                workspaceName={workspaceName}
                workspaceId={workspace.workspaceId}
                viewingAsDetail={true}
                threadWithMessages={threadWithMessages}
              />
            }
          </>
        }
      </div>
    </PureClientOnly>
  );
};

export default ThreadDetailScreen;