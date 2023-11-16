"use client";
import cn from "classnames";
import useTranslation from "locales/useTranslation";
import React from "react";
import { LuChevronLeft, LuChevronRight } from "react-icons/lu";
import Button, { ButtonHeight, ButtonVariants } from "../button";
import styles from "./SecondLevelSideMenu.module.scss";

interface SecondLevelSideMenuProps {
  className?: string;
  children: React.ReactNode;
  open: boolean;
  toggle: () => void;
}

const SecondLevelSideMenu: React.FC<SecondLevelSideMenuProps> = ({ className, children, open = true, toggle }) => {
  const { t } = useTranslation();

  const MenuIcon = open ? LuChevronLeft : LuChevronRight;
  const menuVariant = open ? ButtonVariants.default : ButtonVariants.blur;

  return (
    <div className={cn(styles.sideMenu, open && styles.sideMenuVisible, className)}>
      <div id="second-level-side-menu-action-bar" className={styles.sideMenuActionBar}>
        <Button className={styles.menuToggleButton} heightVariant={ButtonHeight.short} onClick={toggle} variant={menuVariant}>
          <div className={cn(styles.sideMenuCollapsedLabel, !open && styles.sideMenuCollapsedLabelClosed)}>
            {t("tasksLayoutSideMenuCollapsedLabel")}
          </div>
          <MenuIcon size={14} className={styles.menuToggleIcon} />
        </Button>
      </div>
      <div id="second-level-side-menu-content" className={cn(styles.sideMenuContent, open && styles.sideMenuContentVisible)}>
        {children}
      </div>
    </div>
  );
};

export default SecondLevelSideMenu;
