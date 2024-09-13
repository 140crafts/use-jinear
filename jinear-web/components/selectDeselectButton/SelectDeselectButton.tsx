import React from "react";
import { IoClose } from "react-icons/io5";
import Button, { ButtonHeight, ButtonVariants } from "../button";
import styles from "./SelectDeselectButton.module.css";

interface SelectDeselectButtonProps {
  hasSelection: boolean;
  onPickClick?: () => void;
  onUnpickClick: () => void;
  selectedComponent: React.ReactNode;
  emptySelectionLabel?: string;
  emptySelectionComponent?: React.ReactNode;
  disabled?: boolean;
  withoutUnpickButton?: boolean;
  buttonVariant?: string;
  loading?:boolean
}

const SelectDeselectButton: React.FC<SelectDeselectButtonProps> = ({
                                                                     hasSelection,
                                                                     onPickClick,
                                                                     selectedComponent,
                                                                     emptySelectionLabel,
                                                                     emptySelectionComponent,
                                                                     onUnpickClick,
                                                                     withoutUnpickButton = false,
                                                                     buttonVariant = ButtonVariants.filled,
                                                                     disabled = false,
                                                                     loading
                                                                   }) => {
  return (
    <div className={styles.container}>
      <Button
        heightVariant={ButtonHeight.short}
        variant={buttonVariant}
        className={hasSelection && !withoutUnpickButton ? styles.selectedButton : undefined}
        onClick={onPickClick}
        disabled={disabled}
        loading={loading}
      >
        {hasSelection ? selectedComponent : emptySelectionComponent ? emptySelectionComponent : emptySelectionLabel}
      </Button>
      {hasSelection && !withoutUnpickButton && (
        <Button
          heightVariant={ButtonHeight.short}
          variant={ButtonVariants.filled2}
          className={styles.deselectButton}
          onClick={onUnpickClick}
        >
          <IoClose className={styles.icon} />
        </Button>
      )}
    </div>
  );
};

export default SelectDeselectButton;
