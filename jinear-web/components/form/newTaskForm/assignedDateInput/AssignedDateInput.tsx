import Button, { ButtonHeight, ButtonVariants } from "@/components/button";
import TimePicker from "@/components/timePicker/TimePicker";
import { useToggle } from "@/hooks/useToggle";
import { closeDatePickerModal, popDatePickerModal } from "@/store/slice/modalSlice";
import { useAppDispatch } from "@/store/store";
import {
  addWeeks,
  format,
  getHours,
  getMinutes,
  setHours,
  setMinutes,
  startOfToday,
  startOfTomorrow,
  startOfWeek,
} from "date-fns";
import useTranslation from "locales/useTranslation";
import React, { useEffect } from "react";
import { UseFormRegister, UseFormSetValue, UseFormWatch } from "react-hook-form";
import { IoAdd, IoClose, IoTimeOutline } from "react-icons/io5";
import { NewTaskExtendedForm } from "../NewTaskForm";
import styles from "./AssignedDateInput.module.css";

interface AssignedDateInputProps {
  register: UseFormRegister<NewTaskExtendedForm>;
  labelClass: string;
  watch: UseFormWatch<NewTaskExtendedForm>;
  setValue: UseFormSetValue<NewTaskExtendedForm>;
}

const AssignedDateInput: React.FC<AssignedDateInputProps> = ({ register, labelClass, watch, setValue }) => {
  const { t } = useTranslation();
  const dispatch = useAppDispatch();
  const { current: hasPreciseDate, toggle: toggleHasPreciseDate } = useToggle(false);

  const assignedDate = watch("assignedDate");
  const assignedDateISO = watch("assignedDate_ISO");

  useEffect(() => {
    if (assignedDate) {
      const isoValue = new Date(assignedDate)?.toISOString();
      setValue("assignedDate_ISO", isoValue);
    } else {
      setValue("assignedDate_ISO", undefined);
    }
  }, [assignedDate]);

  useEffect(() => {
    setValue("hasPreciseAssignedDate", hasPreciseDate);
  }, [hasPreciseDate]);

  const unsetDate = () => {
    setValue("assignedDate", undefined);
  };

  const onHourChange = (val: string) => {
    const date = assignedDateISO ? new Date(assignedDateISO) : startOfToday();
    const result = setHours(date, parseInt(val));
    setValue?.("assignedDate_ISO", result?.toISOString());
  };

  const onMinuteChange = (val: string) => {
    const date = assignedDateISO ? new Date(assignedDateISO) : startOfToday();
    const result = setMinutes(date, parseInt(val));
    setValue?.("assignedDate_ISO", result?.toISOString());
  };

  const setToday = () => {
    const today = startOfToday();
    // @ts-ignore
    setValue("assignedDate", format(today, "yyyy-MM-dd"));
  };

  const setTomorrow = () => {
    const tomorrow = startOfTomorrow();
    // @ts-ignore
    setValue("assignedDate", format(tomorrow, "yyyy-MM-dd"));
  };

  const setStartOfNextWeek = () => {
    const startOfNextWeek = startOfWeek(addWeeks(startOfToday(), 1), { weekStartsOn: 1 });
    // @ts-ignore
    setValue("assignedDate", format(startOfNextWeek, "yyyy-MM-dd"));
  };

  const onDateSelect = (day: Date) => {
    // @ts-ignore
    setValue("assignedDate", format(day, "yyyy-MM-dd"));
    dispatch(closeDatePickerModal());
  };

  const popDatePickerForAssignedDate = () => {
    dispatch(
      popDatePickerModal({
        visible: true,
        initialDate: assignedDate ? new Date(assignedDate) : new Date(),
        onDateChange: onDateSelect,
      })
    );
  };

  return (
    <label className={styles.container} htmlFor={"new-task-assigned-date"}>
      <div className={styles.labelContainer}>
        <div className={styles.labelTextContainer}>{t("newTaskModalTaskAssignedDate")}</div>
        <div className="flex-1" />
        {assignedDateISO && (
          <Button data-tooltip-right={t("newTaskModalUnsetDate")} onClick={unsetDate} heightVariant={ButtonHeight.short}>
            <IoClose size={11} />
          </Button>
        )}
        <div className={styles.inputContainer}>
          <input type={"hidden"} id={"ui_new-task-assigned-date"} {...register("assignedDate")} />
          <input type={"hidden"} id={"new-task-assigned-date"} {...register("assignedDate_ISO")} />
          <Button variant={ButtonVariants.filled} onClick={popDatePickerForAssignedDate}>
            {assignedDateISO ? format(new Date(assignedDateISO), t("dateFormat")) : t("datePickerSelectDate")}
          </Button>
          {assignedDateISO && hasPreciseDate && (
            <TimePicker
              id={"new-task-assigned-date-time"}
              minuteResolution={15}
              onHourChange={onHourChange}
              onMinuteChange={onMinuteChange}
              defaultHours={`${getHours(new Date(assignedDateISO))}`.padStart(2, "0")}
              defaultMinutes={`${getMinutes(new Date(assignedDateISO))}`.padStart(2, "0")}
            />
          )}
          {assignedDateISO && (
            <Button onClick={toggleHasPreciseDate}>
              {hasPreciseDate ? <IoClose size={11} /> : <IoAdd size={11} />}
              {!hasPreciseDate && <IoTimeOutline size={14} />}
            </Button>
          )}

          <input type={"hidden"} value={`${hasPreciseDate}`} {...register("hasPreciseAssignedDate")} />
        </div>
      </div>
      <div className={styles.quickActionsContainer}>
        <Button className={styles.quickDateButton} heightVariant={ButtonHeight.short} onClick={setToday}>
          {t("newTaskModalToday")}
        </Button>
        <Button className={styles.quickDateButton} heightVariant={ButtonHeight.short} onClick={setTomorrow}>
          {t("newTaskModalTomorrow")}
        </Button>
        <Button className={styles.quickDateButton} heightVariant={ButtonHeight.short} onClick={setStartOfNextWeek}>
          {t("newTaskModalStartOfNextWeek")}
        </Button>
      </div>
    </label>
  );
};

export default AssignedDateInput;
