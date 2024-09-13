import Button, { ButtonVariants } from "@/components/button";
import Line from "@/components/line/Line";
import TimePicker from "@/components/timePicker/TimePicker";
import { RepeatType, TaskReminderInitializeRequest } from "@/model/be/jinear-core";
import { useInitializeTaskReminderMutation } from "@/store/api/taskReminderApi";
import {
  closeDatePickerModal,
  closeNewReminderModal,
  popDatePickerModal,
  selectNewReminderModalTask,
  selectNewReminderModalVisible
} from "@/store/slice/modalSlice";
import { useAppDispatch, useTypedSelector } from "@/store/store";
import Logger from "@/utils/logger";
import { addDays, format, getHours, getMinutes, setHours, setMinutes, startOfToday } from "date-fns";
import useTranslation from "locales/useTranslation";
import React, { useEffect } from "react";
import { SubmitHandler, useForm } from "react-hook-form";
import toast from "react-hot-toast";
import Modal from "../../modal/Modal";
import styles from "./NewReminderModal.module.css";

interface NewReminderModalProps {
}

interface TaskReminderInitializeForm extends TaskReminderInitializeRequest {
  taskId: string;
  specificDate: boolean;
  repeat: boolean;
}

const REPEAT_TYPES = [
  "NONE",
  "HOURLY",
  "DAILY",
  "WEEKLY",
  "BIWEEKLY",
  "MONTHLY",
  "EVERY_3_MONTHS",
  "EVERY_6_MONTHS",
  "YEARLY"
] as RepeatType[];

const logger = Logger("NewReminderModal");

const formatDate = (dateString: Date, dateFormat: string) => {
  try {
    return format(new Date(dateString), dateFormat);
  } catch (error) {
    console.error({ error });
    return null;
  }
};

const NewReminderModal: React.FC<NewReminderModalProps> = ({}) => {
  const { t } = useTranslation();
  const dispatch = useAppDispatch();
  const visible = useTypedSelector(selectNewReminderModalVisible);
  const task = useTypedSelector(selectNewReminderModalTask);
  const { register, reset, watch, setValue, handleSubmit } = useForm<TaskReminderInitializeForm>();
  const repeat = watch("repeat");
  const specificDate = watch("specificDate");
  const specificRemindDate = watch("specificRemindDate");
  const specificRemindDateRepeatType = watch("specificRemindDateRepeatType");
  const specificRemindRepeatEnd = watch("specificRemindRepeatEnd");

  const hasAnyAssignedDateReminder = task?.taskReminders?.find(
    (taskReminder) => taskReminder.taskReminderType == "ASSIGNED_DATE"
  );

  const taskHasAssignedDate = task?.assignedDate;
  const formattedAssignedDate =
    taskHasAssignedDate && formatDate(task.assignedDate, task.hasPreciseAssignedDate ? t("dateTimeFormat") : t("dateFormat"));

  const hasAnyDueDateReminder = task?.taskReminders?.find((taskReminder) => taskReminder.taskReminderType == "DUE_DATE");

  const taskHasDueDate = task?.dueDate;
  const formattedDueDate =
    taskHasDueDate && formatDate(task.dueDate, task.hasPreciseDueDate ? t("dateTimeFormat") : t("dateFormat"));

  const [initializeTaskReminder, { isSuccess, isLoading, isError }] = useInitializeTaskReminderMutation();

  const close = () => {
    dispatch(closeNewReminderModal());
  };

  const submit: SubmitHandler<TaskReminderInitializeForm> = (data) => {
    if (isLoading || !task) {
      return;
    }
    logger.log({ submit: data });
    const req = { ...data };
    if (data.specificDate && !data.specificRemindDate) {
      toast(t("taskNewReminderProvideSpecificDate"));
      return;
    }
    if (data.specificRemindDate) {
      req.specificRemindDate = new Date(data.specificRemindDate);
    }
    if (data.specificRemindRepeatEnd) {
      req.specificRemindRepeatEnd = new Date(data.specificRemindRepeatEnd);
    }
    if (!specificDate) {
      req.specificRemindDate = undefined;
      req.specificRemindDateRepeatType = undefined;
      req.specificRemindRepeatStart = undefined;
      req.specificRemindRepeatEnd = undefined;
    }
    if (!repeat) {
      req.specificRemindRepeatStart = undefined;
      req.specificRemindRepeatEnd = undefined;
    }
    logger.log({ submit: data, req });
    initializeTaskReminder(req);
  };

  useEffect(() => {
    if (!visible) {
      reset();
    }
  }, [visible]);

  useEffect(() => {
    if (isSuccess) {
      close();
    }
  }, [isSuccess]);

  useEffect(() => {
    if (!hasAnyAssignedDateReminder && !hasAnyDueDateReminder) {
      setValue("specificDate", true);
      try {
        const date = format(setMinutes(setHours(addDays(new Date(), 1), 9), 0), "yyyy-MM-dd HH:mm");
        // @ts-ignore
        setValue("specificRemindDate", date);
      } catch (error) {
        console.error(error);
      }
    }
  }, [hasAnyAssignedDateReminder, hasAnyDueDateReminder]);

  const popDatePickerForSpecificRemindDate = () => {
    dispatch(
      popDatePickerModal({
        visible: true,
        initialDate: specificRemindDate ? new Date(specificRemindDate) : new Date(),
        onDateChange: onSpecificDateChange
      })
    );
  };

  const onSpecificDateChange = (date: Date | null) => {
    setValue("specificRemindDate", date);
    dispatch(closeDatePickerModal());
  };

  const popDatePickerForSpecificRemindRepeatEndDate = () => {
    dispatch(
      popDatePickerModal({
        visible: true,
        initialDate: specificRemindRepeatEnd ? new Date(specificRemindRepeatEnd) : new Date(),
        onDateChange: onSpecificRemindRepeatEndDateChange
      })
    );
  };

  const onSpecificRemindRepeatEndDateChange = (date: Date | null) => {
    setValue("specificRemindRepeatEnd", date);
    dispatch(closeDatePickerModal());
  };

  const onSpecificDateHoursChange = (val: string) => {
    const current = specificRemindDate ? new Date(specificRemindDate) : startOfToday();
    const result = setHours(current, parseInt(val));
    setValue("specificRemindDate", result);
  };

  const onSpecificDateMinutesChange = (val: string) => {
    const current = specificRemindDate ? new Date(specificRemindDate) : startOfToday();
    const result = setMinutes(current, parseInt(val));
    setValue("specificRemindDate", result);
  };

  const onSpecificDateRemindRepeatEndHoursChange = (val: string) => {
    const current = specificRemindRepeatEnd ? new Date(specificRemindRepeatEnd) : startOfToday();
    const result = setHours(current, parseInt(val));
    setValue("specificRemindRepeatEnd", result);
  };

  const onSpecificDateRemindRepeatEndMinutesChange = (val: string) => {
    const current = specificRemindRepeatEnd ? new Date(specificRemindRepeatEnd) : startOfToday();
    const result = setMinutes(current, parseInt(val));
    setValue("specificRemindRepeatEnd", result);
  };

  return (
    <Modal visible={visible} title={t("taskNewReminderModalTitle")} bodyClass={styles.container}>
      <form
        autoComplete="off"
        id={"initialize-task-reminder-form"}
        className={styles.form}
        onSubmit={handleSubmit(submit)}
        action="#"
      >
        <input type={"hidden"} value={task?.taskId} {...register("taskId")} />
        {!hasAnyAssignedDateReminder && taskHasAssignedDate && (
          <>
            <label className={styles.labelRow} htmlFor={"before-assigned-date"}>
              <input id={"before-assigned-date"} type={"checkbox"} {...register("beforeAssignedDate")} />
              <b>{t("taskNewReminderModalOnAssignedDateLabel")}</b>
              <div className={styles.formattedDateLabel}>{`${formattedAssignedDate}`}</div>
            </label>
            <Line />
          </>
        )}

        {!hasAnyDueDateReminder && taskHasDueDate && (
          <>
            <label className={styles.labelRow} htmlFor={"before-due-date"}>
              <input id={"before-due-date"} type={"checkbox"} {...register("beforeDueDate")} />
              <b>{t("taskNewReminderModalOnDueDateLabel")}</b>
              <div className={styles.formattedDateLabel}>{`${formattedDueDate}`}</div>
            </label>
            <Line />
          </>
        )}

        <label className={styles.labelRow} htmlFor={"on-specific-date"}>
          <input id={"on-specific-date"} type={"checkbox"} {...register("specificDate")} />
          <b>{t("taskNewReminderModalSpecificDateLabel")}</b>
        </label>

        {specificDate && (
          <div className={styles.specificDateContainer}>
            <div className={styles.dateTimePickerContainer}>
              <Button variant={ButtonVariants.filled} onClick={popDatePickerForSpecificRemindDate}>
                {specificRemindDate ? formatDate(specificRemindDate, t("dateFormat")) : t("datePickerSelectDate")}
              </Button>
              {specificRemindDate && (
                <TimePicker
                  id={"task-specific-date-reminder-time"}
                  minuteResolution={15}
                  onHourChange={onSpecificDateHoursChange}
                  onMinuteChange={onSpecificDateMinutesChange}
                  defaultHours={`${getHours(specificRemindDate ? new Date(specificRemindDate) : startOfToday())}`.padStart(
                    2,
                    "0"
                  )}
                  defaultMinutes={`${getMinutes(specificRemindDate ? new Date(specificRemindDate) : startOfToday())}`.padStart(
                    2,
                    "0"
                  )}
                />
              )}
            </div>

            <label className={styles.label} htmlFor={"repeat-type"}>
              <b>{t("taskNewReminderModalRepeat")}</b>
              <select id="repeat-type" {...register("specificRemindDateRepeatType")}>
                {REPEAT_TYPES.map((type: RepeatType) => (
                  <option key={`repeat-type-${type}`} value={type}>
                    {t(`taskNewReminderModalReminderRepeatType_${type}`)}
                  </option>
                ))}
              </select>
            </label>

            {specificRemindDateRepeatType && specificRemindDateRepeatType != "NONE" && (
              <div className={styles.repeatContainer}>
                <label className={styles.labelRow} htmlFor={"end-repeat-checkbox"}>
                  <div className="flex-1" />
                  <input id={"end-repeat-checkbox"} type={"checkbox"} {...register("repeat")} />
                  {t("taskNewReminderModalRepeatEndLabel")}
                </label>

                {repeat && (
                  <div>
                    {t("taskNewReminderModalRepeatEndDateLabel")}
                    <div className={styles.dateTimePickerContainer}>
                      <input id={"remind-end-date"} type={"hidden"} {...register("specificRemindRepeatEnd")} />
                      <Button variant={ButtonVariants.filled} onClick={popDatePickerForSpecificRemindRepeatEndDate}>
                        {specificRemindRepeatEnd
                          ? formatDate(specificRemindRepeatEnd, t("dateFormat"))
                          : t("datePickerSelectDate")}
                      </Button>
                      {specificRemindRepeatEnd && (
                        <TimePicker
                          id={"task-specific-date-reminder-repeat-end-time"}
                          minuteResolution={15}
                          onHourChange={onSpecificDateRemindRepeatEndHoursChange}
                          onMinuteChange={onSpecificDateRemindRepeatEndMinutesChange}
                          defaultHours={`${getHours(
                            specificRemindRepeatEnd ? new Date(specificRemindRepeatEnd) : startOfToday()
                          )}`.padStart(2, "0")}
                          defaultMinutes={`${getMinutes(
                            specificRemindDate ? new Date(specificRemindDate) : startOfToday()
                          )}`.padStart(2, "0")}
                        />
                      )}
                    </div>
                  </div>
                )}
              </div>
            )}
          </div>
        )}
        <div className="flex-1" />
        <div className={styles.footer}>
          <Button
            disabled={isLoading}
            loading={isLoading}
            type="submit"
            className={styles.button}
            variant={ButtonVariants.contrast}
          >
            <div>{t("taskNewReminderCreateButton")}</div>
          </Button>

          <Button className={styles.button} variant={ButtonVariants.filled} onClick={close}>
            <div>{t("taskNewReminderCloseButton")}</div>
          </Button>
        </div>
      </form>
    </Modal>
  );
};

export default NewReminderModal;
