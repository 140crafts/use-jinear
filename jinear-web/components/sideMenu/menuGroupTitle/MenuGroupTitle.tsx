import Button, { ButtonHeight, ButtonVariants } from "@/components/button";
import cn from "classnames";
import React from "react";
import { IoAdd, IoEllipsisHorizontal } from "react-icons/io5";
import styles from "./MenuGroupTitle.module.css";

interface MenuGroupTitleProps {
  label: string;
  hasAddButton?: boolean;
  hasDetailButton?: boolean;
  buttonVariant?: string;
  onAddButtonClick?: () => void;
  onDetailButtonClick?: () => void;
  titleClassName?: string;
}

const MenuGroupTitle: React.FC<MenuGroupTitleProps> = ({
  label,
  hasAddButton = false,
  hasDetailButton = false,
  buttonVariant = ButtonVariants.hoverFilled2,
  onAddButtonClick,
  onDetailButtonClick,
  titleClassName,
}) => {
  const _detailClick = () => {
    onDetailButtonClick?.();
  };

  const _addClick = () => {
    onAddButtonClick?.();
  };

  return (
    <div className={styles.titleContainer}>
      <div className={cn(styles.title, titleClassName)}>{label}</div>
      <div className="flex-1" />
      {hasDetailButton && (
        <Button onClick={_detailClick} variant={buttonVariant} heightVariant={ButtonHeight.short}>
          <IoEllipsisHorizontal />
        </Button>
      )}
      {hasAddButton && (
        <Button onClick={_addClick} variant={buttonVariant} heightVariant={ButtonHeight.short}>
          <IoAdd />
        </Button>
      )}
    </div>
  );
};

export default MenuGroupTitle;
