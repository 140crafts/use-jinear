import Button, { ButtonHeight, ButtonVariants } from "@/components/button";
import TimePicker from "@/components/timePicker/TimePicker";
import {
  queryStateAnyToStringConverter,
  queryStateBooleanParser,
  queryStateDateToIsoDateConverter,
  queryStateIsoDateParser,
  useQueryState,
  useSetQueryState
} from "@/hooks/useQueryState";
import { closeDatePickerModal, popDatePickerModal } from "@/store/slice/modalSlice";
import { useAppDispatch } from "@/store/store";
import { format, getHours, getMinutes, setHours, setMinutes } from "date-fns";
import useTranslation from "locales/useTranslation";
import React from "react";
import { IoAdd, IoClose, IoPlaySkipBackOutline, IoTimeOutline } from "react-icons/io5";
import styles from "./ToDatePickerButton.module.css";

interface ToDatePickerButtonProps {
}

const ToDatePickerButton: React.FC<ToDatePickerButtonProps> = ({}) => {
  const { t } = useTranslation();
  const dispatch = useAppDispatch();
  const setQueryState = useSetQueryState();
  const toDate = useQueryState<Date>("timespanEnd", queryStateIsoDateParser);
  const hasPreciseToDate = useQueryState<boolean>("hasPreciseToDate", queryStateBooleanParser);

  const setToDate = (day?: Date | null) => {
    setQueryState("timespanEnd", queryStateDateToIsoDateConverter(day));
  };

  const setHasPreciseToDate = (hasPreciseToDate?: boolean) => {
    setQueryState("hasPreciseToDate", queryStateAnyToStringConverter(hasPreciseToDate));
  };

  const onDateSelect = (day: Date | null) => {
    setToDate?.(day);
    dispatch(closeDatePickerModal());
  };

  const onPickClick = () => {
    dispatch(
      popDatePickerModal({
        visible: true,
        initialDate: toDate ? toDate : new Date(),
        onDateChange: onDateSelect
      })
    );
  };

  const onHourChange = (val: string) => {
    if (toDate) {
      const result = setHours(toDate, parseInt(val));
      setToDate?.(result);
    }
  };

  const onMinuteChange = (val: string) => {
    if (toDate) {
      const result = setMinutes(toDate, parseInt(val));
      setToDate?.(result);
    }
  };

  const onUnpickClick = () => {
    if (hasPreciseToDate) {
      setHasPreciseToDate?.(false);
      return;
    }
    setToDate?.(undefined);
  };

  const toggleHasPreciseDate = () => {
    const next = !hasPreciseToDate;
    setHasPreciseToDate?.(next);
  };

  return (
    <div className={styles.container}>
      <Button
        heightVariant={ButtonHeight.short}
        variant={!toDate ? ButtonVariants.filled : ButtonVariants.filled2}
        className={toDate ? styles.selectedButton : undefined}
        onClick={onPickClick}
      >
        {toDate ? (
          <div className={styles.labelButton}>
            <IoPlaySkipBackOutline />
            <b>{format(toDate, t("dateFormat"))}</b>
          </div>
        ) : (
          t("taskFilterToDateFilterButtonEmpty")
        )}
      </Button>
      {toDate &&
        (hasPreciseToDate ? (
          <TimePicker
            id={`task-filter-to-date-time`}
            minuteResolution={15}
            onHourChange={onHourChange}
            onMinuteChange={onMinuteChange}
            defaultHours={toDate ? `${getHours(toDate)}`.padStart(2, "0") : undefined}
            defaultMinutes={toDate ? `${getMinutes(toDate)}`.padStart(2, "0") : undefined}
            hourSelectClassName={styles.hourSelectClassName}
            minuteSelectClassName={styles.minuteSelectClassName}
            containerClassName={styles.timePickerContainerClassName}
          />
        ) : (
          <Button variant={ButtonVariants.filled} className={styles.timePickerToggleButton}
                  onClick={toggleHasPreciseDate}>
            {hasPreciseToDate ? <IoClose size={11} /> : <IoAdd size={11} />}
            {!hasPreciseToDate && <IoTimeOutline size={14} />}
          </Button>
        ))}
      {toDate && (
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

export default ToDatePickerButton;
