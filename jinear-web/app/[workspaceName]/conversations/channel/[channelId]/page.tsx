"use client";
import React from "react";
import styles from "./page.module.css";
import { useParams } from "next/navigation";
import useTranslation from "@/locals/useTranslation";
import ChannelScreenHeader from "@/components/channelScreen/channelScreenHeader/ChannelScreenHeader";

interface ChannelPageProps {

}

const ChannelPage: React.FC<ChannelPageProps> = ({}) => {
  const { t } = useTranslation();
  const params = useParams();
  const workspaceName: string = params?.workspaceName as string;
  const channelId: string = params?.channelId as string;

  return (
    <div className={styles.container}>
      <ChannelScreenHeader channelId={channelId} workspaceName={workspaceName} />
    </div>
  );
};

export default ChannelPage;