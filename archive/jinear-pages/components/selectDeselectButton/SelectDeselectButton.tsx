import React from "react";
import { IoClose } from "react-icons/io5";
import Button, { ButtonHeight, ButtonVariants } from "../button";
import styles from "./SelectDeselectButton.module.css";
import Logger from "@/utils/logger";

interface SelectDeselectButtonProps {
  id?: string;
  hasSelection: boolean;
  onPickClick?: () => void;
  onUnpickClick: () => void;
  selectedComponent: React.ReactNode;
  emptySelectionLabel?: string;
  emptySelectionComponent?: React.ReactNode;
  disabled?: boolean;
  withoutUnpickButton?: boolean;
  buttonVariant?: string;
  loading?: boolean;
}

const logger = Logger("SelectDeselectButton");

const SelectDeselectButton: React.FC<SelectDeselectButtonProps> = ({
                                                                     id,
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
  logger.log({ id, hasSelection });
  return (
    <div id={id} className={styles.container}>
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
