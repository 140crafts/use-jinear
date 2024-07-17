import { closeDatePickerModal, popDatePickerModal } from "@/store/slice/modalSlice";
import { useAppDispatch } from "@/store/store";
import Logger from "@/utils/logger";
import cn from "classnames";
import { format, getHours, getMinutes, setHours, setMinutes } from "date-fns";
import useTranslation from "locales/useTranslation";
import React from "react";
import { IconType } from "react-icons";
import { IoClose } from "react-icons/io5";
import Button, { ButtonHeight, ButtonVariants } from "../button";
import TimePicker from "../timePicker/TimePicker";
import styles from "./DateTimeInput.module.css";

interface DateTimeInputProps {
  id?: string;
  type: "date" | "date-time";
  value: Date;
  setValue: (date?: Date) => void;
  valuePrecise: boolean;
  toggleValuePrecise?: () => void;
  dateSpanStart?: Date;
  dateSpanEnd?: Date;
  disabledBefore?: Date;
  disabledAfter?: Date;
  datePickerModalTitle?: string;
  dateInputButtonIcon?: IconType;
  dateButtonLabel?: string;
  allowEmptyDate?: boolean;
  minuteResolution?: 1 | 5 | 15 | 30 | 60;
  contentContainerClassName?: string;
  dateButtonClassName?: string;
}

const logger = Logger("DateTimeInput");
const DateTimeInput: React.FC<DateTimeInputProps> = ({
  id,
  type,
  value,
  setValue,
  valuePrecise,
  toggleValuePrecise,
  dateSpanStart,
  dateSpanEnd,
  disabledBefore,
  disabledAfter,
  datePickerModalTitle,
  dateInputButtonIcon,
  dateButtonLabel,
  allowEmptyDate = true,
  minuteResolution = 1,
  contentContainerClassName,
  dateButtonClassName,
}) => {
  const { t } = useTranslation();
  const dispatch = useAppDispatch();

  const onDateSelect = (day: Date) => {
    setValue(day);
    dispatch(closeDatePickerModal());
  };

  const onUnpickClick = () => {
    if (valuePrecise) {
      toggleValuePrecise?.();
      return;
    }
    setValue(undefined);
  };

  const onHourChange = (val: string) => {
    if (value) {
      const result = setHours(value, parseInt(val));
      setValue(result);
    }
  };

  const onMinuteChange = (val: string) => {
    if (value) {
      const result = setMinutes(value, parseInt(val));
      setValue(result);
    }
  };

  const onPickClick = () => {
    dispatch(
      popDatePickerModal({
        visible: true,
        initialDate: value ? new Date(value) : new Date(),
        onDateChange: onDateSelect,
        dateSpanStart,
        dateSpanEnd,
        disabledBefore,
        disabledAfter,
        title: datePickerModalTitle ? datePickerModalTitle : t("calendarEventCalendarExternalSourceChange"),
      })
    );
  };

  const Icon = dateInputButtonIcon ? dateInputButtonIcon : undefined;

  return (
    <div id={id} className={cn(styles.container, contentContainerClassName)}>
      <Button
        heightVariant={ButtonHeight.short}
        variant={ButtonVariants.filled}
        onClick={onPickClick}
        className={dateButtonClassName}
      >
        {value ? (
          <div className={styles.labelButton}>
            {Icon && <Icon />}
            {format(value, t("dateFormat"))}
          </div>
        ) : dateButtonLabel ? (
          dateButtonLabel
        ) : (
          t("genericDateButtonLabel")
        )}
      </Button>

      {value && type == "date-time" && (
        <>
          <div className={styles.divider} />
          <TimePicker
            id={`${id}-time`}
            minuteResolution={minuteResolution}
            onHourChange={onHourChange}
            onMinuteChange={onMinuteChange}
            defaultHours={value ? `${getHours(value)}`.padStart(2, "0") : undefined}
            defaultMinutes={value ? `${getMinutes(value)}`.padStart(2, "0") : undefined}
          />
        </>
      )}

      {allowEmptyDate && valuePrecise && (
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

export default DateTimeInput;
