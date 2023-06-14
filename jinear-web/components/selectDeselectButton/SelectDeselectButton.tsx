import React from "react";
import { IoClose } from "react-icons/io5";
import Button, { ButtonHeight, ButtonVariants } from "../button";
import styles from "./SelectDeselectButton.module.css";

interface SelectDeselectButtonProps {
  hasSelection: boolean;
  onPickClick: () => void;
  onUnpickClick: () => void;
  selectedComponent: React.ReactNode;
  emptySelectionLabel: string;
}

const SelectDeselectButton: React.FC<SelectDeselectButtonProps> = ({
  hasSelection,
  onPickClick,
  selectedComponent,
  emptySelectionLabel,
  onUnpickClick,
}) => {
  return (
    <div className={styles.container}>
      <Button
        heightVariant={ButtonHeight.short}
        variant={ButtonVariants.filled}
        className={hasSelection ? styles.selectedButton : undefined}
        onClick={onPickClick}
      >
        {hasSelection ? selectedComponent : emptySelectionLabel}
      </Button>
      {hasSelection && (
        <Button
          heightVariant={ButtonHeight.short}
          variant={ButtonVariants.filled2}
          className={styles.deselectButton}
          onClick={onUnpickClick}
        >
          <IoClose />
        </Button>
      )}
    </div>
  );
};

export default SelectDeselectButton;
