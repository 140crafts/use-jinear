import React from "react";
import styles from "./MobileAppSection.module.css";
import Button, { ButtonHeight, ButtonVariants } from "@/components/button";
import { APP_STORE_URL, PLAY_STORE_URL } from "@/utils/constants";
import { IoLogoApple, IoLogoGooglePlaystore, IoLogoPwa } from "react-icons/io5";
import { useAppDispatch } from "@/store/store";
import { popInstallPwaInstructionsModal } from "@/slice/modalSlice";
import useTranslation from "@/locals/useTranslation";

interface MobileAppSectionProps {

}

const MobileAppSection: React.FC<MobileAppSectionProps> = ({}) => {
  const { t } = useTranslation();
  const dispatch = useAppDispatch();

  const onPwaButtonClick = () => {
    dispatch(popInstallPwaInstructionsModal());
  };

  return (
    <div className={styles.appstoreContainer}>
      <h1>{t("homePageMobileAppSectionTitle")}</h1>
      <span className={styles.about} dangerouslySetInnerHTML={{ __html: t("homePageMobileAppSectionAbout") }}>
        </span>

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
        <Button
          variant={ButtonVariants.outline}
          heightVariant={ButtonHeight.short}
          onClick={onPwaButtonClick}
          className={styles.storeButton}>
          <IoLogoPwa className={"icon"} />
          PWA
        </Button>
      </div>

    </div>
  );
};

export default MobileAppSection;