import React from "react";
import styles from "./FeedUrlInfo.module.css";
import Button, { ButtonHeight, ButtonVariants } from "@/components/button";
import useTranslation from "@/locals/useTranslation";
import { copyTextToClipboard } from "@/utils/clipboard";

interface FeedUrlInfoProps {
  accessKey: string;
  workspaceUserName: string;
}

const FeedUrlInfo: React.FC<FeedUrlInfoProps> = ({ accessKey, workspaceUserName }) => {
  const { t } = useTranslation();
  const url ='/';

  const copyLink = () => {
    copyTextToClipboard(url);
  };

  return (
    <div className={styles.container}>
      <div className={styles.titleContainer}>
        <h3>{t("projectFeedUrlTitle")}</h3>
        <span>{t("projectFeedUrlText")}</span>
      </div>
      <a className={styles.urlInput} href={url} target={"_blank"} rel={"noreferrer"}>{url}</a>
      <div className={styles.actionButtonContainer}>
        <Button heightVariant={ButtonHeight.short} variant={ButtonVariants.filled}
                onClick={copyLink}>{t("projectFeedUrlCopy")}</Button>
      </div>
    </div>
  );
};

export default FeedUrlInfo;