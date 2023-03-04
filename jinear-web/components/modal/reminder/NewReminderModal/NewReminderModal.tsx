import Button, { ButtonVariants } from "@/components/button";
import Line from "@/components/line/Line";
import { RepeatType, TaskReminderInitializeRequest } from "@/model/be/jinear-core";
import { useInitializeTaskReminderMutation } from "@/store/api/taskReminderApi";
import { closeNewReminderModal, selectNewReminderModalTask, selectNewReminderModalVisible } from "@/store/slice/modalSlice";
import { useAppDispatch, useTypedSelector } from "@/store/store";
import Logger from "@/utils/logger";
import { addDays, format, setHours, setMinutes } from "date-fns";
import useTranslation from "locales/useTranslation";
import React, { useEffect } from "react";
import { SubmitHandler, useForm } from "react-hook-form";
import toast from "react-hot-toast";
import Modal from "../../modal/Modal";
import styles from "./NewReminderModal.module.css";

interface NewReminderModalProps {}

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
  "YEARLY",
] as RepeatType[];

const logger = Logger("NewReminderModal");

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

  const hasAnyAssignedDateReminder = task?.taskReminders?.find(
    (taskReminder) => taskReminder.taskReminderType == "ASSIGNED_DATE"
  );
  const taskHasAssignedDate = task?.assignedDate;

  const formattedAssignedDate =
    taskHasAssignedDate &&
    format(new Date(task.assignedDate), task.hasPreciseAssignedDate ? t("dateTimeFormat") : t("dateFormat"));

  const hasAnyDueDateReminder = task?.taskReminders?.find((taskReminder) => taskReminder.taskReminderType == "DUE_DATE");
  const taskHasDueDate = task?.dueDate;

  const formattedDueDate =
    taskHasDueDate && format(new Date(task.dueDate), task.hasPreciseDueDate ? t("dateTimeFormat") : t("dateFormat"));

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
      const date = format(setMinutes(setHours(addDays(new Date(), 1), 9), 0), "yyyy-MM-dd HH:mm");
      //@ts-ignore
      setValue("specificRemindDate", date);
    }
  }, [hasAnyAssignedDateReminder, hasAnyDueDateReminder]);

  return (
    <Modal visible={visible} title={t("taskNewReminderModalTitle")} bodyClass={styles.container} hasTitleCloseButton={true}>
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
            <label className={styles.label} htmlFor={"specific-remind-date"}>
              <input
                id={"specific-remind-date"}
                type={"datetime-local"}
                //@ts-ignore
                value={specificRemindDate}
                {...register("specificRemindDate")}
              />
            </label>

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
                  <label className={styles.label} htmlFor={"remind-end-date"}>
                    {t("taskNewReminderModalRepeatEndDateLabel")}
                    <input id={"remind-end-date"} type={"date"} {...register("specificRemindRepeatEnd")} />
                  </label>
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
