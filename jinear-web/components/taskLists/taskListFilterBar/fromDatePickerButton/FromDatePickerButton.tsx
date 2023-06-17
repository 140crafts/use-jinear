import Button, { ButtonHeight, ButtonVariants } from "@/components/button";
import TimePicker from "@/components/timePicker/TimePicker";
import { closeDatePickerModal, popDatePickerModal } from "@/store/slice/modalSlice";
import { useAppDispatch } from "@/store/store";
import { format, getHours, getMinutes, setHours, setMinutes } from "date-fns";
import useTranslation from "locales/useTranslation";
import React from "react";
import { IoAdd, IoClose, IoPlaySkipForwardOutline, IoTimeOutline } from "react-icons/io5";
import {
  useFromDate,
  useHasPreciseFromDate,
  useSetFromDate,
  useSetHasPreciseFromDate,
} from "../context/TaskListFilterBarContext";
import styles from "./FromDatePickerButton.module.css";

interface FromDatePickerButtonProps {}

const FromDatePickerButton: React.FC<FromDatePickerButtonProps> = ({}) => {
  const { t } = useTranslation();
  const dispatch = useAppDispatch();
  const fromDate = useFromDate();
  const setFromDate = useSetFromDate();
  const hasPreciseFromDate = useHasPreciseFromDate();
  const setHasPreciseFromDate = useSetHasPreciseFromDate();

  const onDateSelect = (day: Date) => {
    setFromDate?.(day);
    dispatch(closeDatePickerModal());
  };

  const onPickClick = () => {
    dispatch(
      popDatePickerModal({
        visible: true,
        initialDate: fromDate ? fromDate : new Date(),
        onDateChange: onDateSelect,
      })
    );
  };

  const onHourChange = (val: string) => {
    if (fromDate) {
      const result = setHours(fromDate, parseInt(val));
      setFromDate?.(result);
    }
  };

  const onMinuteChange = (val: string) => {
    if (fromDate) {
      const result = setMinutes(fromDate, parseInt(val));
      setFromDate?.(result);
    }
  };

  const onUnpickClick = () => {
    if (hasPreciseFromDate) {
      setHasPreciseFromDate?.(false);
      return;
    }
    setFromDate?.(undefined);
  };

  const toggleHasPreciseDate = () => {
    const next = !hasPreciseFromDate;
    setHasPreciseFromDate?.(next);
  };

  return (
    <div className={styles.container}>
      <Button
        heightVariant={ButtonHeight.short}
        variant={ButtonVariants.filled}
        className={fromDate ? styles.selectedButton : undefined}
        onClick={onPickClick}
      >
        {fromDate ? (
          <div className={styles.labelButton}>
            <IoPlaySkipForwardOutline />
            {/* : <IoPlaySkipBackOutline /> */}
            {format(fromDate, t("dateFormat"))}
          </div>
        ) : (
          t("taskFilterFromDateFilterButtonEmpty")
        )}
      </Button>
      {fromDate &&
        (hasPreciseFromDate ? (
          <TimePicker
            id={`task-filter-from-date-time`}
            minuteResolution={15}
            onHourChange={onHourChange}
            onMinuteChange={onMinuteChange}
            defaultHours={fromDate ? `${getHours(fromDate)}`.padStart(2, "0") : undefined}
            defaultMinutes={fromDate ? `${getMinutes(fromDate)}`.padStart(2, "0") : undefined}
            hourSelectClassName={styles.hourSelectClassName}
            minuteSelectClassName={styles.minuteSelectClassName}
          />
        ) : (
          <Button variant={ButtonVariants.filled} className={styles.timePickerToggleButton} onClick={toggleHasPreciseDate}>
            {hasPreciseFromDate ? <IoClose size={11} /> : <IoAdd size={11} />}
            {!hasPreciseFromDate && <IoTimeOutline size={14} />}
          </Button>
        ))}
      {fromDate && (
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

export default FromDatePickerButton;
