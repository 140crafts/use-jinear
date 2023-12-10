import { FeedDto } from "@/model/be/jinear-core";
import useTranslation from "locales/useTranslation";
import React from "react";
import styles from "./FeedInfo.module.css";

interface FeedInfoProps {
  feed: FeedDto;
}

const FeedInfo: React.FC<FeedInfoProps> = ({ feed }) => {
  const { t } = useTranslation();
  return (
    <div className={styles.container}>
      <h3>{feed.name}</h3>
    </div>
  );
};

export default FeedInfo;
