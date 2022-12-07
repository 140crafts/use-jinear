import Button, { ButtonHeight, ButtonVariants } from "@/components/button";
import React from "react";
import { IoAdd, IoChevronDownSharp } from "react-icons/io5";
import styles from "./MenuGroupTitle.module.css";

interface MenuGroupTitleProps {
  label: string;
  hasAddButton?: boolean;
  hasDetailButton?: boolean;
  buttonVariant?: string;
  onAddButtonClick?: () => void;
}

const MenuGroupTitle: React.FC<MenuGroupTitleProps> = ({
  label,
  hasAddButton = false,
  hasDetailButton = false,
  buttonVariant = ButtonVariants.hoverFilled2,
  onAddButtonClick,
}) => {
  const _addClick = () => {
    onAddButtonClick?.();
  };

  return (
    <div className={styles.titleContainer}>
      <div className={styles.title}>{label}</div>
      {hasAddButton && (
        <>
          <div className="flex-1" />
          <Button
            onClick={_addClick}
            variant={buttonVariant}
            heightVariant={ButtonHeight.short}
          >
            <IoAdd />
          </Button>
        </>
      )}
      {hasDetailButton && (
        <>
          <div className="flex-1" />
          <Button variant={buttonVariant} heightVariant={ButtonHeight.short}>
            <IoChevronDownSharp />
          </Button>
        </>
      )}
    </div>
  );
};

export default MenuGroupTitle;
