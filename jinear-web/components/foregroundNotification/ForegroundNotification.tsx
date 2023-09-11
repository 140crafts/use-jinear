import useTranslation from "locales/useTranslation";
import { useRouter } from "next/router";
import React from "react";
import { toast } from "react-hot-toast";
import Button, { ButtonVariants } from "../button";
import styles from "./ForegroundNotification.module.css";

interface ForegroundNotificationProps {
  title: string;
  body: string;
  launchUrl?: string;
  buttonLabel?: string;
  closeable?: boolean;
  onClick?: () => void;
  onClose?: () => void;
}

const ForegroundNotification: React.FC<ForegroundNotificationProps> = ({
  title,
  body,
  launchUrl,
  buttonLabel,
  closeable = false,
  onClick,
  onClose,
}) => {
  const { t } = useTranslation();
  const router = useRouter();

  const _onClick = () => {
    if (launchUrl) {
      router.push(launchUrl);
    }
    onClick?.();
  };

  const closeAll = () => {
    toast.dismiss();
    onClose?.();
  };

  return (
    <div className={styles.container}>
      <h2>
        <b>{title}</b>
      </h2>
      <div>{body}</div>
      {launchUrl && (
        <Button variant={ButtonVariants.filled} onClick={_onClick}>
          {buttonLabel ? buttonLabel : t("foregroundNotificationDefaultButtonLabel")}
        </Button>
      )}
      {closeable && (
        <Button variant={ButtonVariants.contrast} onClick={closeAll}>
          {t("foregroundNotificationDefaultCloseButtonLabel")}
        </Button>
      )}
    </div>
  );
};

export default ForegroundNotification;
