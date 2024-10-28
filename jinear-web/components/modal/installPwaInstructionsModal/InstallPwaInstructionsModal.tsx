import React from "react";
import useTranslation from "@/locals/useTranslation";
import { useAppDispatch, useTypedSelector } from "@/store/store";
import { closeInstallPwaInstructionsModal, selectInstallPwaInstructionsModalVisible } from "@/slice/modalSlice";
import Modal from "@/components/modal/modal/Modal";
import styles from "./InstallPwaInstructionsModal.module.css";
import OrLine from "@/components/orLine/OrLine";
import Button, { ButtonHeight, ButtonVariants } from "@/components/button";
import { IoLogoApple, IoLogoGooglePlaystore } from "react-icons/io5";
import { useOnInstallPromptEvent } from "@/components/onInstallPromptEventProvider/OnInstallPromptEventProvider";
import { APP_STORE_URL, PLAY_STORE_URL } from "@/utils/constants";

interface InstallPwaInstructionsModalProps {

}

const InstallPwaInstructionsModal: React.FC<InstallPwaInstructionsModalProps> = ({}) => {
  const { t } = useTranslation();
  const dispatch = useAppDispatch();
  const onInstallPromptEvent = useOnInstallPromptEvent();

  const visible = useTypedSelector(selectInstallPwaInstructionsModalVisible);

  const close = () => {
    dispatch(closeInstallPwaInstructionsModal());
  };

  const onInstallAsPWA = async () => {
    if (onInstallPromptEvent != null) {
      // @ts-ignore
      const result = await onInstallPromptEvent?.prompt?.();
      if (result?.outcome == "accepted") {
        return;
      }
    }
  };

  return (
    <Modal
      visible={visible}
      height={"height-full"}
      width={"large"}
      title={t("installPwaInstructionsModalTitle")}
      hasTitleCloseButton={true}
      requestClose={close}
    >
      <div className={styles.container}>

        <div className={styles.appstoreContainer}>

          <h3>{t("installFromAppStores")}</h3>

          <div className={styles.appstoreButtonsContainer}>
            <Button
              variant={ButtonVariants.outline}
              heightVariant={ButtonHeight.short}
              href={APP_STORE_URL}
              target={"_blank"}
              className={styles.storeButton}>
              <IoLogoApple className={"icon"} />
              App Store
            </Button>
            <Button
              variant={ButtonVariants.outline}
              heightVariant={ButtonHeight.short}
              href={PLAY_STORE_URL}
              target={"_blank"}
              className={styles.storeButton}>
              <IoLogoGooglePlaystore className={"icon"} />
              Play Store
            </Button>
          </div>
        </div>

        <OrLine />

        {onInstallPromptEvent != null &&
          <Button
            variant={ButtonVariants.outline}
            heightVariant={ButtonHeight.short}
            onClick={onInstallAsPWA}
          >
            {t("installPwaInstructionsAsPWAButton")}
          </Button>
        }

        <div className={styles.pwaContainer}>
          <h3>{t("installPwaInstructionsModalIOSTitle")}</h3>
          <span className={styles.instructionsContainer}>
          <ul>
            <li dangerouslySetInnerHTML={{ __html: t("installPwaInstructionsModalIOS_step1") }}></li>
            <li>
            {t("installPwaInstructionsModalIOS_step2")}
            </li>
            <li>
            {t("installPwaInstructionsModalIOS_step3")}
            </li>
            <li>
            {t("installPwaInstructionsModalIOS_step4")}
            </li>
          </ul>
        </span>
          <div className={styles.videoContainer}>
            <iframe
              src={`https://player.vimeo.com/video/987965789?badge=0&autoplay=1&loop=1&background=1&amp;autopause=0&muted=1&amp;player_id=0&amp;app_id=58479`}
              frameBorder="0" allow="autoplay; fullscreen; picture-in-picture; clipboard-write"
              className={styles.vimeoIframe} title="task-create-dark"></iframe>
          </div>

          <div className={"spacer-h-2"} />

          <h3>{t("installPwaInstructionsModalAndroidTitle")}</h3>
          <span className={styles.instructionsContainer}>
          <ul>
            <li dangerouslySetInnerHTML={{ __html: t("installPwaInstructionsModalAndroid_step1") }}></li>
            <li>
            {t("installPwaInstructionsModalAndroid_step2")}
            </li>
            <li>
            {t("installPwaInstructionsModalAndroid_step3")}
            </li>
          </ul>
        </span>
          <div className={styles.videoContainer}>
            <iframe
              src={`https://player.vimeo.com/video/1002123801?badge=0&autoplay=1&loop=1&background=1&amp;autopause=0&muted=1&amp;player_id=0&amp;app_id=58479`}
              frameBorder="0" allow="autoplay; fullscreen; picture-in-picture; clipboard-write"
              className={styles.vimeoIframe} title="task-create-dark"></iframe>
          </div>

        </div>
      </div>
    </Modal>
  )
    ;
};

export default InstallPwaInstructionsModal;