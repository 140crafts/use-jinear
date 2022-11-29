import Button, { ButtonHeight, ButtonVariants } from "@/components/button";
import React from "react";
import { IoAdd } from "react-icons/io5";
import styles from "./MenuGroupTitle.module.css";

interface MenuGroupTitleProps {
  label: string;
  hasAddButton?: boolean;
  buttonVariant?: string;
}

const MenuGroupTitle: React.FC<MenuGroupTitleProps> = ({
  label,
  hasAddButton = false,
  buttonVariant = ButtonVariants.hoverFilled2,
}) => {
  return (
    <div className={styles.titleContainer}>
      <div className={styles.title}>{label}</div>
      {hasAddButton && (
        <>
          <div className="flex-1" />
          <Button variant={buttonVariant} heightVariant={ButtonHeight.short}>
            <IoAdd />
          </Button>
        </>
      )}
    </div>
  );
};

export default MenuGroupTitle;
