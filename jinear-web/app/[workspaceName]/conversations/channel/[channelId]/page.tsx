"use client";
import React from "react";
import styles from "./page.module.css";
import { useParams } from "next/navigation";
import useTranslation from "@/locals/useTranslation";
import ChannelScreenHeader from "@/components/channelScreen/channelScreenHeader/ChannelScreenHeader";
import { useWorkspaceFromName } from "@/hooks/useWorkspaceFromName";

interface ChannelPageProps {

}

const ChannelPage: React.FC<ChannelPageProps> = ({}) => {
  const { t } = useTranslation();
  const params = useParams();
  const workspaceName: string = params?.workspaceName as string;
  const channelId: string = params?.channelId as string;
  const workspace = useWorkspaceFromName(workspaceName);

  return (
    <div className={styles.container}>
      {workspace &&
        <ChannelScreenHeader channelId={channelId} workspaceName={workspaceName} workspaceId={workspace.workspaceId} />}
    </div>
  );
};

export default ChannelPage;