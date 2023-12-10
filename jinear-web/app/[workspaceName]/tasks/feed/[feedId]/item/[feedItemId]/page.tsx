"use client";
import FeedItemDetail from "@/components/feedItemDetail/FeedItemDetail";
import { selectWorkspaceFromWorkspaceUsername } from "@/store/slice/accountSlice";
import { useTypedSelector } from "@/store/store";
import useTranslation from "locales/useTranslation";
import { useParams } from "next/navigation";
import React from "react";
import styles from "./page.module.css";

interface FeedItemDetailPageProps {}

const FeedItemDetailPage: React.FC<FeedItemDetailPageProps> = ({}) => {
  const { t } = useTranslation();
  const params = useParams();
  const workspaceName: string = params?.workspaceName as string;
  const feedId: string = params?.feedId as string;
  const feedItemId: string = params?.feedItemId as string;
  const workspace = useTypedSelector(selectWorkspaceFromWorkspaceUsername(workspaceName));

  return (
    <div className={styles.container}>
      {feedId && feedItemId && workspace && (
        <FeedItemDetail workspaceId={workspace.workspaceId} feedId={feedId} itemId={feedItemId} />
      )}
    </div>
  );
};

export default FeedItemDetailPage;
