import React from "react";
import styles from "./Footer.module.css";
import { LuGlobe, LuMail } from "react-icons/lu";
import { SiApple, SiGoogleplay, SiPwa, SiReddit, SiTelegram, SiX } from "react-icons/si";
import cn from "classnames";
import { useAppDispatch } from "@/store/store";
import { popInstallPwaInstructionsModal } from "@/slice/modalSlice";
import { APP_STORE_URL, PLAY_STORE_URL } from "@/utils/constants";
import Button, { ButtonHeight, ButtonVariants } from "@/components/button";
import { IoLogoPwa } from "react-icons/io5";
import useTranslation from "@/locals/useTranslation";

interface FooterProps {

}

const Footer: React.FC<FooterProps> = ({}) => {
  const { t } = useTranslation();
  const dispatch = useAppDispatch();

  const onPwaButtonClick = () => {
    dispatch(popInstallPwaInstructionsModal());
  };

  return (
    <div className={styles.container}>
      <div className={styles.column}>
        <b>Jinear</b>
        <div className={"spacer-h-1"} />
        <a
          href={`https://jinear.co/pricing`}
          className={styles.link}
        >
          {t("landingPageFooterPricing")}
        </a>
        <a
          href={`https://jinear.co/terms`}
          className={styles.link}
        >
          {t("landingPageFooterTerms")}
        </a>
      </div>

      <div className={styles.column}>
        <b>{t("landingPageFooterAccess")}</b>
        <div className={"spacer-h-1"} />
        <a
          href={"/login"}
          className={styles.link}
        >
          <LuGlobe className={"icon"} /> {t("landingPageFooterAccessBrowser")}
        </a>
        <a
          href={APP_STORE_URL}
          target={"_blank"}
          rel={"noreferrer"}
          className={styles.link}
        >
          <SiApple className={"icon"} /> Apple Appstore
        </a>
        <a
          href={PLAY_STORE_URL}
          target={"_blank"}
          rel={"noreferrer"}
          className={styles.link}
        >
          <SiGoogleplay className={"icon"} /> Google Play
        </a>
        <a
          onClick={onPwaButtonClick}
          className={styles.link}
        >
          <SiPwa className={"icon"} />
          PWA
        </a>
      </div>
      <div className={styles.column}>
        <b>Contact</b>
        <div className={"spacer-h-1"} />
        <a
          href={`https://jinear.co/shared/cgdstnc-personal/feed/jinear-01j862vw8t9mt1zar7vgw14y5t`}
          target={"_blank"}
          rel={"noreferrer"}
          className={styles.link}
        >
          <span className={cn("jinear-logo", styles.jinearLogo)}>J</span> Jinear Project Feed
        </a>
        <a
          href={`https://twitter.com/usejinear`}
          target={"_blank"}
          rel={"noreferrer"}
          className={styles.link}
        >
          <SiX className={"icon"} /> x.com/usejinear
        </a>
        <a
          href={`https://www.reddit.com/r/jinear`}
          target={"_blank"}
          rel={"noreferrer"}
          className={styles.link}
        >
          <SiReddit className={"icon"} /> r/jinear
        </a>
        <a
          href={`https://t.me/usejinear`}
          target={"_blank"}
          rel={"noreferrer"}
          className={styles.link}
        >
          <SiTelegram className={"icon"} /> Telegram
        </a>
        <a
          href={`mailto:cgdstnc@gmail.com`}
          target={"_blank"}
          rel={"noreferrer"}
          className={styles.link}
        >
          <LuMail className={"icon"} /> Email
        </a>
      </div>
    </div>
  );
};

export default Footer;