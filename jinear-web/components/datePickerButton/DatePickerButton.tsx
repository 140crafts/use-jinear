import React, { forwardRef, useEffect, useImperativeHandle, useState } from "react";
import styles from "./DatePickerButton.module.css";
import SelectDeselectButton from "@/components/selectDeselectButton/SelectDeselectButton";
import { LuCalendar } from "react-icons/lu";
import { useAppDispatch } from "@/store/store";
import { closeDatePickerModal, popDatePickerModal } from "@/slice/modalSlice";
import useTranslation from "@/locals/useTranslation";
import { format } from "date-fns";
import { IconType } from "react-icons/lib";
import {
  ITeamMemberPickerButtonRef
} from "@/components/form/newTaskForm/teamMemberPickerButton/TeamMemberPickerButton";
import { ButtonVariants } from "@/components/button";

export interface IDatePickerButtonRef {
  reset: () => void;
}

interface DatePickerButtonProps {
  title?: string;
  initialDate?: Date | null;
  dateSpanStart?: Date | null;
  dateSpanEnd?: Date | null;
  disabledBefore?: Date | null;
  disabledAfter?: Date | null;
  onDateChange?: (date?: Date | null) => void;
  icon?: IconType;
  label?: string;
  emptySelectionComponent?: React.ReactNode;
  unpickableFromModal?: boolean;
  dataTooltipRight?: string;
  pickedDateFormat?: string;
  buttonVariant?: string;
  loading?:boolean
}

const DatePickerButton = ({
                            title,
                            initialDate,
                            dateSpanStart,
                            dateSpanEnd,
                            disabledBefore,
                            disabledAfter,
                            onDateChange,
                            icon,
                            label,
                            emptySelectionComponent,
                            unpickableFromModal = false,
                            dataTooltipRight,
                            pickedDateFormat,
                            buttonVariant = ButtonVariants.filled,
                            loading
                          }: DatePickerButtonProps, ref: any) => {
  const { t } = useTranslation();
  const dispatch = useAppDispatch();

  const [currentPick, setCurrentPick] = useState<Date | null>();

  useImperativeHandle(ref, () => ({
    reset: () => setCurrentPick(null)
  }));

  useEffect(() => {
    if (initialDate) {
      setCurrentPick(initialDate);
    }
  }, [initialDate?.getTime?.()]);

  const popPicker = () => {
    dispatch(popDatePickerModal({
      visible: true,
      title,
      initialDate,
      dateSpanStart,
      dateSpanEnd,
      disabledBefore,
      disabledAfter,
      onDateChange: onPickerDateChange,
      unpickable: unpickableFromModal
    }));
  };

  const onPickerDateChange = (date: Date | null) => {
    dispatch(closeDatePickerModal());
    setCurrentPick(date);
    onDateChange?.(date);
  };

  const deselect = () => {
    setCurrentPick(null);
    onDateChange?.(null);
  };

  const ButtonIcon = icon ? icon : LuCalendar;

  return (
    <div className={styles.container} data-tooltip-right={dataTooltipRight}>
      <SelectDeselectButton
        buttonVariant={buttonVariant}
        hasSelection={currentPick != null}
        onPickClick={popPicker}
        loading={loading}
        selectedComponent={
          <div className={styles.selectedContainer}>
            <ButtonIcon className={styles.icon} />
            <span>
              {currentPick ? format(currentPick, pickedDateFormat ?? t("dateFormat")) : ""}
          </span>
          </div>
        }
        emptySelectionLabel={label ?? t("datePickerButtonLabel")}
        emptySelectionComponent={emptySelectionComponent}
        onUnpickClick={deselect}
        withoutUnpickButton={unpickableFromModal}
      />
    </div>
  );
};

export default forwardRef<IDatePickerButtonRef, DatePickerButtonProps>(DatePickerButton);