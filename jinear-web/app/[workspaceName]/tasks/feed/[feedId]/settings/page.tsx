"use client";
import DeleteFeedCard from "@/components/feedSettingsPage/deleteFeedCard/DeleteFeedCard";
import { useParams } from "next/navigation";
import React from "react";
import styles from "./page.module.css";

interface FeedSettingsPageProps {}

const FeedSettingsPage: React.FC<FeedSettingsPageProps> = ({}) => {
  const params = useParams();
  const feedId: string = params?.feedId as string;

  return (
    <div className={styles.container}>
      <DeleteFeedCard feedId={feedId} />
    </div>
  );
};

export default FeedSettingsPage;
