import { useToggle } from "@/hooks/useToggle";
import { closeDatePickerModal, popDatePickerModal } from "@/store/slice/modalSlice";
import { useAppDispatch } from "@/store/store";
import Logger from "@/utils/logger";
import cn from "classnames";
import { format, getHours, getMinutes, setHours, setMinutes } from "date-fns";
import useTranslation from "locales/useTranslation";
import React, { useEffect, useState } from "react";
import { IconType } from "react-icons";
import { IoAdd, IoClose, IoTimeOutline } from "react-icons/io5";
import Button, { ButtonHeight, ButtonVariants } from "../button";
import TimePicker from "../timePicker/TimePicker";
import styles from "./DateTimeInput.module.css";

interface DateTimeInputProps {
  id?: string;
  type: "date" | "date-time";
  initialDate?: Date;
  initialDateIsPrecise?: boolean;
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
  initialDate,
  initialDateIsPrecise,
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
  const [selectedDate, setSelectedDate] = useState<Date>();
  const [hasPreciseDate, toggleHasPreciseDate, setHasPreciseDate] = useToggle();

  useEffect(() => {
    if (initialDate) {
      setSelectedDate(initialDate);
    }
    if (initialDateIsPrecise != null) {
      setHasPreciseDate(initialDateIsPrecise);
    }
  }, [initialDate, initialDateIsPrecise]);

  const onDateSelect = (day: Date) => {
    setSelectedDate(day);
    dispatch(closeDatePickerModal());
  };

  const onUnpickClick = () => {
    if (hasPreciseDate) {
      toggleHasPreciseDate();
      return;
    }
    setSelectedDate(undefined);
  };

  const onHourChange = (val: string) => {
    if (selectedDate) {
      const result = setHours(selectedDate, parseInt(val));
      setSelectedDate(result);
    }
  };

  const onMinuteChange = (val: string) => {
    if (selectedDate) {
      const result = setMinutes(selectedDate, parseInt(val));
      setSelectedDate(result);
    }
  };

  const onPickClick = () => {
    dispatch(
      popDatePickerModal({
        visible: true,
        initialDate: selectedDate ? new Date(selectedDate) : new Date(),
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
        {selectedDate ? (
          <div className={styles.labelButton}>
            {Icon && <Icon />}
            {format(selectedDate, t("dateFormat"))}
          </div>
        ) : dateButtonLabel ? (
          dateButtonLabel
        ) : (
          t("genericDateButtonLabel")
        )}
      </Button>

      {selectedDate &&
        type == "date-time" &&
        (hasPreciseDate ? (
          <>
            <div className={styles.divider} />
            <TimePicker
              id={`${id}-time`}
              minuteResolution={minuteResolution}
              onHourChange={onHourChange}
              onMinuteChange={onMinuteChange}
              defaultHours={selectedDate ? `${getHours(selectedDate)}`.padStart(2, "0") : undefined}
              defaultMinutes={selectedDate ? `${getMinutes(selectedDate)}`.padStart(2, "0") : undefined}
            />
          </>
        ) : (
          <>
            <div className={styles.divider} />
            <Button variant={ButtonVariants.filled} onClick={toggleHasPreciseDate}>
              {hasPreciseDate ? <IoClose size={11} /> : <IoAdd size={11} />}
              {!hasPreciseDate && <IoTimeOutline size={14} />}
            </Button>
          </>
        ))}

      {allowEmptyDate && selectedDate && (
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
