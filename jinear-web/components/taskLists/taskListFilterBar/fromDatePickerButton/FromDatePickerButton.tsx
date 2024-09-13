import Button, { ButtonHeight, ButtonVariants } from "@/components/button";
import TimePicker from "@/components/timePicker/TimePicker";
import {
  queryStateAnyToStringConverter,
  queryStateBooleanParser,
  queryStateDateToIsoDateConverter,
  queryStateIsoDateParser,
  useQueryState,
  useSetQueryState,
} from "@/hooks/useQueryState";
import { closeDatePickerModal, popDatePickerModal } from "@/store/slice/modalSlice";
import { useAppDispatch } from "@/store/store";
import { format, getHours, getMinutes, setHours, setMinutes } from "date-fns";
import useTranslation from "locales/useTranslation";
import React from "react";
import { IoAdd, IoClose, IoPlaySkipForwardOutline, IoTimeOutline } from "react-icons/io5";
import styles from "./FromDatePickerButton.module.css";

interface FromDatePickerButtonProps {}

const FromDatePickerButton: React.FC<FromDatePickerButtonProps> = ({}) => {
  const { t } = useTranslation();
  const dispatch = useAppDispatch();
  const setQueryState = useSetQueryState();
  const currentTimespanStart = useQueryState<Date>("timespanStart", queryStateIsoDateParser);
  const currentHasPreciseFromDate = useQueryState<boolean>("hasPreciseFromDate", queryStateBooleanParser);

  const setFromDate = (day?: Date | null) => {
    setQueryState("timespanStart", queryStateDateToIsoDateConverter(day));
  };

  const setHasPreciseFromDate = (hasPreciseFromDate?: boolean) => {
    setQueryState("hasPreciseFromDate", queryStateAnyToStringConverter(hasPreciseFromDate));
  };

  const onDateSelect = (day: Date | null) => {
    setFromDate(day);
    dispatch(closeDatePickerModal());
  };

  const onPickClick = () => {
    dispatch(
      popDatePickerModal({
        visible: true,
        initialDate: currentTimespanStart ? currentTimespanStart : new Date(),
        onDateChange: onDateSelect,
      })
    );
  };

  const onHourChange = (val: string) => {
    if (currentTimespanStart) {
      const result = setHours(currentTimespanStart, parseInt(val));
      setFromDate(result);
    }
  };

  const onMinuteChange = (val: string) => {
    if (currentTimespanStart) {
      const result = setMinutes(currentTimespanStart, parseInt(val));
      setFromDate(result);
    }
  };

  const onUnpickClick = () => {
    if (currentHasPreciseFromDate) {
      setHasPreciseFromDate?.(false);
      return;
    }
    setFromDate(undefined);
  };

  const toggleHasPreciseDate = () => {
    const next = !currentHasPreciseFromDate;
    setHasPreciseFromDate?.(next);
  };

  return (
    <div className={styles.container}>
      <Button
        heightVariant={ButtonHeight.short}
        variant={!currentTimespanStart ? ButtonVariants.filled : ButtonVariants.filled2}
        className={currentTimespanStart ? styles.selectedButton : undefined}
        onClick={onPickClick}
      >
        {currentTimespanStart ? (
          <div className={styles.labelButton}>
            <IoPlaySkipForwardOutline />
            {/* : <IoPlaySkipBackOutline /> */}
            <b>{format(currentTimespanStart, t("dateFormat"))}</b>
          </div>
        ) : (
          t("taskFilterFromDateFilterButtonEmpty")
        )}
      </Button>
      {currentTimespanStart &&
        (currentHasPreciseFromDate ? (
          <TimePicker
            id={`task-filter-from-date-time`}
            minuteResolution={15}
            onHourChange={onHourChange}
            onMinuteChange={onMinuteChange}
            defaultHours={currentTimespanStart ? `${getHours(currentTimespanStart)}`.padStart(2, "0") : undefined}
            defaultMinutes={currentTimespanStart ? `${getMinutes(currentTimespanStart)}`.padStart(2, "0") : undefined}
            hourSelectClassName={styles.hourSelectClassName}
            minuteSelectClassName={styles.minuteSelectClassName}
            containerClassName={styles.timePickerContainerClassName}
          />
        ) : (
          <Button variant={ButtonVariants.filled} className={styles.timePickerToggleButton} onClick={toggleHasPreciseDate}>
            {currentHasPreciseFromDate ? <IoClose size={11} /> : <IoAdd size={11} />}
            {!currentHasPreciseFromDate && <IoTimeOutline size={14} />}
          </Button>
        ))}
      {currentTimespanStart && (
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
