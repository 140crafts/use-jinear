import useTranslation from "locales/useTranslation";
import { useRouter } from "next/navigation";
import React from "react";
import { toast } from "react-hot-toast";
import Button, { ButtonHeight, ButtonVariants } from "../button";
import styles from "./ForegroundNotification.module.css";
import { IoArrowForward, IoClose } from "react-icons/io5";

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
                                                                         onClose
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
      <h3>
        <b>{title}</b>
      </h3>
      <div>{body}</div>
      <div className={styles.buttonContainer}>
        {closeable && (
          <Button variant={ButtonVariants.contrast} heightVariant={ButtonHeight.short2x} onClick={closeAll}>
            {/*{t("foregroundNotificationDefaultCloseButtonLabel")}*/}
            <IoClose />
          </Button>
        )}
        {launchUrl && (
          <Button variant={ButtonVariants.filled} heightVariant={ButtonHeight.short2x} onClick={_onClick}>
            {buttonLabel ? buttonLabel : <IoArrowForward />}
          </Button>
        )}
      </div>
    </div>
  );
};

export default ForegroundNotification;
