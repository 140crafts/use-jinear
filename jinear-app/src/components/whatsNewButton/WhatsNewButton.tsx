import Button, {ButtonHeight, ButtonVariants} from "@/components/button";
import useTranslation from "@/locales/useTranslation";
import {CHANGELOG} from "@/util/constants";
import React from "react";
import {LuNewspaper} from "react-icons/lu";
import styles from "./WhatsNewButton.module.css";

const WhatsNewButton: React.FC = () => {
    const {t} = useTranslation();

    return (
        <Button
            href={CHANGELOG}
            target="_blank"
            variant={ButtonVariants.outline}
            heightVariant={ButtonHeight.short}
            className={styles.button}
            shortcut="whatsNew"
            data-tooltip={t("whatsNewButtonLabel")}
        >
            <LuNewspaper className={'icon'} size={14}/>
        </Button>
    );
};

export default WhatsNewButton;
