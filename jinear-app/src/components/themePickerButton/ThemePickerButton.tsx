import cn from "classnames";
import React from "react";
import { LuPalette } from "react-icons/lu";
import Button, { ButtonHeight, ButtonVariants } from "../button";

import useTranslation from "@/locales/useTranslation";
import { useAppDispatch } from "@/store";
import { popThemePickerModal } from "@/store/slice/modalSlice";
import styles from "./ThemePickerButton.module.css";

interface ThemePickerButtonProps {
  variant?: keyof typeof ButtonVariants | string;
  iconSize?: number;
  buttonStyle?: string;
}

const ThemePickerButton: React.FC<ThemePickerButtonProps> = ({ variant = ButtonVariants.default, buttonStyle, iconSize = 13 }) => {
  const { t } = useTranslation();
  const dispatch = useAppDispatch();

  const openThemePicker = () => {
    dispatch(popThemePickerModal());
  };

  return (
    <div>
      <Button
        heightVariant={ButtonHeight.short}
        variant={variant}
        onClick={openThemePicker}
        title={t("themePickerOpenButtonTitle")}
        className={cn(styles.iconButton, buttonStyle)}
      >
        <LuPalette size={iconSize} />
      </Button>
    </div>
  );
};

export default ThemePickerButton;
