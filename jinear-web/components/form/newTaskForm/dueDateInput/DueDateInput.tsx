import Button, { ButtonHeight } from "@/components/button";
import TimePicker from "@/components/timePicker/TimePicker";
import { useToggle } from "@/hooks/useToggle";
import { addDays, format, getHours, getMinutes, setHours, setMinutes, startOfToday } from "date-fns";
import useTranslation from "locales/useTranslation";
import React, { useEffect } from "react";
import { UseFormRegister, UseFormSetValue, UseFormWatch } from "react-hook-form";
import { IoAdd, IoClose, IoTimeOutline } from "react-icons/io5";
import { NewTaskExtendedForm } from "../NewTaskForm";
import styles from "./DueDateInput.module.css";

interface DueDateInputProps {
  register: UseFormRegister<NewTaskExtendedForm>;
  labelClass: string;
  watch: UseFormWatch<NewTaskExtendedForm>;
  setValue: UseFormSetValue<NewTaskExtendedForm>;
}

const DueDateInput: React.FC<DueDateInputProps> = ({ register, labelClass, watch, setValue }) => {
  const { t } = useTranslation();
  const { current: hasPreciseDate, toggle: toggleHasPreciseDate } = useToggle(false);

  const dueDate = watch("dueDate");
  const dueDateISO = watch("dueDate_ISO");
  const assignedDateISO = watch("assignedDate_ISO");

  useEffect(() => {
    if (dueDate) {
      const isoValue = new Date(dueDate)?.toISOString();
      setValue("dueDate_ISO", isoValue);
    } else {
      setValue("dueDate_ISO", undefined);
    }
  }, [dueDate]);

  useEffect(() => {
    setValue("hasPreciseDueDate", hasPreciseDate);
  }, [hasPreciseDate]);

  const unsetDate = () => {
    setValue("dueDate", undefined);
  };

  const onHourChange = (val: string) => {
    const date = dueDateISO ? new Date(dueDateISO) : startOfToday();
    const result = setHours(date, parseInt(val));
    setValue?.("dueDate_ISO", result?.toISOString());
  };

  const onMinuteChange = (val: string) => {
    const date = dueDateISO ? new Date(dueDateISO) : startOfToday();
    const result = setMinutes(date, parseInt(val));
    setValue?.("dueDate_ISO", result?.toISOString());
  };

  const setNextDay = () => {
    if (!assignedDateISO) {
      return;
    }
    const assignedDay = new Date(assignedDateISO);
    const nextDay = addDays(assignedDay, 1);
    // @ts-ignore
    setValue("dueDate", format(nextDay, "yyyy-MM-dd"));
  };

  return (
    <label className={styles.container} htmlFor={"new-task-due-date"}>
      <div className={styles.labelContainer}>
        <div className={styles.labelTextContainer}>
          {t("newTaskModalTaskDueDate")}
          {dueDateISO && (
            <Button data-tooltip-right={t("newTaskModalUnsetDate")} onClick={unsetDate}>
              <IoClose size={11} />
            </Button>
          )}
        </div>

        <div className={styles.inputContainer}>
          <input type={"date"} id={"ui_new-task-due-date"} {...register("dueDate")} />
          <input type={"hidden"} id={"new-task-due-date"} {...register("dueDate_ISO")} />
          {dueDateISO && hasPreciseDate && (
            <TimePicker
              id={"new-task-due-date-time"}
              minuteResolution={15}
              onHourChange={onHourChange}
              onMinuteChange={onMinuteChange}
              defaultHours={`${getHours(new Date(dueDateISO))}`.padStart(2, "0")}
              defaultMinutes={`${getMinutes(new Date(dueDateISO))}`.padStart(2, "0")}
            />
          )}
          {dueDateISO && (
            <Button onClick={toggleHasPreciseDate}>
              {hasPreciseDate ? <IoClose size={11} /> : <IoAdd size={11} />}
              {!hasPreciseDate && <IoTimeOutline size={14} />}
            </Button>
          )}

          <input type={"hidden"} value={`${hasPreciseDate}`} {...register("hasPreciseDueDate")} />
        </div>
      </div>
      {assignedDateISO && (
        <div className={styles.quickActionsContainer}>
          <Button className={styles.quickDateButton} heightVariant={ButtonHeight.short} onClick={setNextDay}>
            {t("newTaskModalDayAfterAssignedDate")}
          </Button>
        </div>
      )}
    </label>
  );
};

export default DueDateInput;
