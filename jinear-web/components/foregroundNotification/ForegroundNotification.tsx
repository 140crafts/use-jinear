import useTranslation from "locales/useTranslation";
import { useRouter } from "next/router";
import React from "react";
import styles from "./ForegroundNotification.module.css";

interface ForegroundNotificationProps {
  title: string;
  body: string;
  launchUrl?: string;
}

const ForegroundNotification: React.FC<ForegroundNotificationProps> = ({ title, body, launchUrl }) => {
  const { t } = useTranslation();
  const router = useRouter();

  const onClick = () => {
    if (launchUrl) {
      router.push(launchUrl);
    }
  };

  return (
    <div className={styles.container} onClick={onClick}>
      <h2>
        <b>{title}</b>
      </h2>
      <div>{body}</div>
    </div>
  );
};

export default ForegroundNotification;
