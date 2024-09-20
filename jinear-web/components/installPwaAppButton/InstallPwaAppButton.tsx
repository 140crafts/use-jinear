import React from "react";
import styles from "./InstallPwaAppButton.module.scss";
import Button, { ButtonHeight, ButtonVariants } from "@/components/button";
import { LuDownload } from "react-icons/lu";
import useTranslation from "@/locals/useTranslation";
import { useAppDispatch } from "@/store/store";
import { popInstallPwaInstructionsModal } from "@/slice/modalSlice";
import Logger from "@/utils/logger";
import { useOnInstallPromptEvent } from "@/components/onInstallPromptEventProvider/OnInstallPromptEventProvider";
import cn from "classnames";

interface InstallPwaAppButtonProps {
  className?: string;
  withLabel?: boolean;
}

const logger = Logger("InstallPwaAppButton");

const InstallPwaAppButton: React.FC<InstallPwaAppButtonProps> = ({ className, withLabel = true }) => {
  const { t } = useTranslation();
  const dispatch = useAppDispatch();
  const onInstallPromptEvent = useOnInstallPromptEvent();

  const popPwaInstructionModal = async () => {
    if (onInstallPromptEvent != null) {
      // @ts-ignore
      const result = await onInstallPromptEvent?.prompt?.();
      logger.log({ popPwaInstructionModal: result });
      if (result?.outcome == "accepted") {
        return;
      }
    }
    dispatch(popInstallPwaInstructionsModal());
  };

  return (
    <Button
      onClick={popPwaInstructionModal}
      className={cn(styles.pwaInstallButton, className)}
      heightVariant={ButtonHeight.short}
      variant={ButtonVariants.outline}
      data-tooltip-right={onInstallPromptEvent == null ? t("headerInstallPwaButtonTooltip") : undefined}
    >
      <LuDownload />
      {withLabel &&
        <p className={styles.installButtonLabel}>
          <b>{t("headerInstallPwaButtonLabel")}</b>
        </p>}
    </Button>
  );
};

export default InstallPwaAppButton;