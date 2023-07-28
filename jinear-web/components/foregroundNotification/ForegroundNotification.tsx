import useTranslation from "locales/useTranslation";
import React from "react";
import styles from "./ForegroundNotification.module.css";

interface ForegroundNotificationProps {
  title: string;
  body: string;
}

const ForegroundNotification: React.FC<ForegroundNotificationProps> = ({ title, body }) => {
  const { t } = useTranslation();

  return (
    <div className={styles.container}>
      <h2>
        <b>{title}</b>
      </h2>
      <div>{body}</div>
    </div>
  );
};

export default ForegroundNotification;
