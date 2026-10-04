import Button from "@/components/button";
import cn from "classnames";
import useTranslation from "@/locales/useTranslation";
import {CHANGELOG} from "@/util/constants";
import React from "react";
import {LuNewspaper} from "react-icons/lu";
import styles from "./WhatsNewMoreMenuButton.module.css";

interface WhatsNewMoreMenuButtonProps {
    requestClose: () => void;
}

const WhatsNewMoreMenuButton: React.FC<WhatsNewMoreMenuButtonProps> = ({requestClose}) => {
    const {t} = useTranslation();

    return (
        <div className={styles.infoContainer}>
            <div className={cn(styles.profilePicContainer, styles["profilePicContainer-without-profile-pic"])}>
                <div className={styles.firstLetter}>
                    <LuNewspaper/>
                </div>
            </div>
            <Button className={styles.nameContainer} href={CHANGELOG} target="_blank" onClick={requestClose}>
                <span className={styles.title}>{t("whatsNewButtonLabel")}</span>
            </Button>
        </div>
    );
};

export default WhatsNewMoreMenuButton;
