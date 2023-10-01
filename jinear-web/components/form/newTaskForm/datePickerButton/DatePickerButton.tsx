import Button, { ButtonHeight, ButtonVariants } from "@/components/button";
import TimePicker from "@/components/timePicker/TimePicker";
import { useToggle } from "@/hooks/useToggle";
import { TaskInitializeRequest } from "@/model/be/jinear-core";
import { closeDatePickerModal, popDatePickerModal } from "@/store/slice/modalSlice";
import { useAppDispatch } from "@/store/store";
import { format, getHours, getMinutes, setHours, setMinutes } from "date-fns";
import useTranslation from "locales/useTranslation";
import React, { useEffect, useState } from "react";
import { UseFormRegister, UseFormSetValue } from "react-hook-form";
import { IoAdd, IoClose, IoPlaySkipBackOutline, IoPlaySkipForwardOutline, IoTimeOutline } from "react-icons/io5";
import styles from "./DatePickerButton.module.css";

interface DatePickerButtonProps {
  fieldName: "assignedDate" | "dueDate";
  isPreciseFieldName: "hasPreciseAssignedDate" | "hasPreciseDueDate";
  initialDate?: Date;
  initialDateIsPrecise?: boolean;
  register: UseFormRegister<TaskInitializeRequest>;
  setValue: UseFormSetValue<TaskInitializeRequest>;
}

const DatePickerButton: React.FC<DatePickerButtonProps> = ({
  fieldName,
  isPreciseFieldName,
  initialDate,
  initialDateIsPrecise,
  register,
  setValue,
}) => {
  const { t } = useTranslation();
  const dispatch = useAppDispatch();
  const [selectedDate, setSelectedDate] = useState<Date>();
  const { current: hasPreciseDate, toggle: toggleHasPreciseDate } = useToggle(initialDateIsPrecise);

  useEffect(() => {
    if (selectedDate) {
      // @ts-ignore
      setValue(fieldName, selectedDate.toISOString());
    }
    setValue(isPreciseFieldName, hasPreciseDate);
  }, [selectedDate, hasPreciseDate, fieldName, isPreciseFieldName]);

  useEffect(() => {
    if (initialDate) {
      setSelectedDate(initialDate);
    }
  }, [initialDate]);

  const onDateSelect = (day: Date) => {
    setSelectedDate(day);
    dispatch(closeDatePickerModal());
  };

  const onPickClick = () => {
    dispatch(
      popDatePickerModal({
        visible: true,
        initialDate: selectedDate ? new Date(selectedDate) : new Date(),
        onDateChange: onDateSelect,
      })
    );
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

  return (
    <div className={styles.container}>
      <input type="hidden" value={selectedDate?.toISOString() || undefined} {...register(fieldName)} />
      <input type="hidden" value={`${hasPreciseDate}`} {...register(isPreciseFieldName)} />
      <Button
        heightVariant={ButtonHeight.short}
        variant={ButtonVariants.filled}
        className={selectedDate ? styles.selectedButton : undefined}
        onClick={onPickClick}
      >
        {selectedDate ? (
          <div className={styles.labelButton}>
            {fieldName == "assignedDate" ? <IoPlaySkipForwardOutline /> : <IoPlaySkipBackOutline />}
            {format(selectedDate, t("dateFormat"))}
          </div>
        ) : fieldName == "assignedDate" ? (
          t("newTaskFormPickAssignedDateButtonLabel")
        ) : (
          t("newTaskFormPickDueDateButtonLabel")
        )}
      </Button>
      {selectedDate &&
        (hasPreciseDate ? (
          <TimePicker
            id={`new-task-date-time-${fieldName}`}
            minuteResolution={15}
            onHourChange={onHourChange}
            onMinuteChange={onMinuteChange}
            defaultHours={selectedDate ? `${getHours(selectedDate)}`.padStart(2, "0") : undefined}
            defaultMinutes={selectedDate ? `${getMinutes(selectedDate)}`.padStart(2, "0") : undefined}
            hourSelectClassName={styles.hourSelectClassName}
            minuteSelectClassName={styles.minuteSelectClassName}
          />
        ) : (
          <Button variant={ButtonVariants.filled} className={styles.timePickerToggleButton} onClick={toggleHasPreciseDate}>
            {hasPreciseDate ? <IoClose size={11} /> : <IoAdd size={11} />}
            {!hasPreciseDate && <IoTimeOutline size={14} />}
          </Button>
        ))}
      {selectedDate && (
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

export default DatePickerButton;
