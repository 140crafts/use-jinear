import Button, {ButtonVariants} from "@/components/button";
import {useThemeSettings} from "@/components/themeProvider/ThemeProvider";
import useTranslation from "@/locales/useTranslation";
import {useAppDispatch} from "@/store";
import {popThemePickerModal} from "@/store/slice/modalSlice";
import React from "react";
import styles from "./AppearanceSettingsButton.module.css";

interface AppearanceSettingsButtonProps {
}

const AppearanceSettingsButton: React.FC<AppearanceSettingsButtonProps> = ({}) => {
    const {t} = useTranslation();
    const dispatch = useAppDispatch();
    const {theme} = useThemeSettings();

    const openThemePicker = () => {
        dispatch(popThemePickerModal());
    };

    return (
        <div className={styles.container}>
            <h2>{t("appearanceSettingsTitle")}</h2>
            <span className={styles.text}>{t("appearanceSettingsText")}</span>
            <div className={styles.contentContainer}>
                <span className={styles.currentTheme}>{theme.name}</span>
                <Button variant={ButtonVariants.filled} onClick={openThemePicker}>
                    {t("appearanceSettingsButtonLabel")}
                </Button>
            </div>
        </div>
    );
};

export default AppearanceSettingsButton;
