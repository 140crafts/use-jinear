import Button, { ButtonVariants } from "@/components/button";
import TimePicker from "@/components/timePicker/TimePicker";
import { useUpdateTaskDatesMutation } from "@/store/api/taskUpdateApi";
import {
  closeChangeTaskDateModal,
  closeDatePickerModal,
  popDatePickerModal,
  selectChangeTaskDateModalHasPreciseAssignedDate,
  selectChangeTaskDateModalHasPreciseDueDate,
  selectChangeTaskDateModalTaskCurrentAssignedDate,
  selectChangeTaskDateModalTaskCurrentDueDate,
  selectChangeTaskDateModalTaskId,
  selectChangeTaskDateModalVisible,
} from "@/store/slice/modalSlice";
import { useAppDispatch, useTypedSelector } from "@/store/store";
import Logger from "@/utils/logger";
import {
  addDays,
  addHours,
  endOfWeek,
  format,
  getHours,
  getMinutes,
  isBefore,
  isEqual,
  isSameDay,
  parse,
  setHours,
  setMinutes,
  startOfDay,
  startOfToday,
  startOfTomorrow,
  startOfWeek,
} from "date-fns";
import useTranslation from "locales/useTranslation";
import React, { ChangeEvent, useEffect, useRef, useState } from "react";
import toast from "react-hot-toast";
import { IoAdd, IoClose, IoTimeOutline } from "react-icons/io5";
import Modal from "../../modal/Modal";
import styles from "./TaskDateChangeModal.module.css";
import QuickDateActionBar from "./quickDateActionBar/QuickDateActionBar";

interface TaskDateChangeModalProps {}

const logger = Logger("TaskDateChangeModal");

const options = { weekStartsOn: 1 } as any;

const TaskDateChangeModal: React.FC<TaskDateChangeModalProps> = ({}) => {
  const { t } = useTranslation();
  const dispatch = useAppDispatch();
  const visible = useTypedSelector(selectChangeTaskDateModalVisible);
  const taskId = useTypedSelector(selectChangeTaskDateModalTaskId);

  const hasPreciseAssignedDate = useTypedSelector(selectChangeTaskDateModalHasPreciseAssignedDate) || false;
  const hasPreciseDueDate = useTypedSelector(selectChangeTaskDateModalHasPreciseDueDate) || false;

  const [assignedDateVal, setAssignedDateVal] = useState<Date | null>(new Date());
  const [dueDateVal, setDueDateVal] = useState<Date | null>(new Date());

  const [assignedDateTimePickerVisible, setAssignedDateTimePickerVisible] = useState<boolean>(hasPreciseAssignedDate);
  const [dueDateTimePickerVisible, setDueDateTimePickerVisible] = useState<boolean>(hasPreciseDueDate);

  const assignedInputRef = useRef<HTMLInputElement>(null);
  const dueInputRef = useRef<HTMLInputElement>(null);

  const currentAssigned = useTypedSelector(selectChangeTaskDateModalTaskCurrentAssignedDate);
  const currentDue = useTypedSelector(selectChangeTaskDateModalTaskCurrentDueDate);

  const [updateTaskDates, { isSuccess: isUpdateSuccess, isLoading: isUpdateLoading }] = useUpdateTaskDatesMutation();

  useEffect(() => {
    if (visible && currentAssigned) {
      setAssignedDateVal(new Date(currentAssigned));
    }
    if (visible && currentDue) {
      setDueDateVal(new Date(currentDue));
    }
    if (!visible) {
      unsetAssignedDate();
      unsetDueDate();
      setAssignedDateTimePickerVisible(false);
      setDueDateTimePickerVisible(false);
    }
  }, [visible, currentAssigned, currentDue]);

  useEffect(() => {
    if (isUpdateSuccess) {
      close();
    }
  }, [isUpdateSuccess]);

  useEffect(() => {
    logger.log({
      assignedDateVal,
      dueDateVal,
    });
    if (visible && assignedDateVal && dueDateVal) {
      const dueDateIsBeforeAssignedDate = isBefore(dueDateVal, assignedDateVal) || isEqual(dueDateVal, assignedDateVal);
      const datesAreSameDay = isSameDay(assignedDateVal, dueDateVal);
      const isDueDatePrecise = dueDateTimePickerVisible;
      const isDueDatePreciseAndBothDatesAreSameDay = datesAreSameDay && !isDueDatePrecise;
      if (dueDateIsBeforeAssignedDate && !isDueDatePreciseAndBothDatesAreSameDay) {
        toast(t("changeTaskDateDueDateIsBeforeAssignedDate"));
        isSameDay(assignedDateVal, dueDateVal) ? setDueDateOneHourAfterAssignedDate() : setDueDateDayAfterAssignedDate();
      }
    }
  }, [visible, assignedDateVal, dueDateVal, dueDateTimePickerVisible]);

  useEffect(() => {
    if (hasPreciseAssignedDate) {
      setAssignedDateTimePickerVisible(hasPreciseAssignedDate);
    }
    if (hasPreciseDueDate) {
      setDueDateTimePickerVisible(hasPreciseDueDate);
    }
  }, [hasPreciseAssignedDate, hasPreciseDueDate]);

  const popDatePickerForAssignedDate = () => {
    dispatch(
      popDatePickerModal({
        visible: true,
        initialDate: assignedDateVal ? assignedDateVal : new Date(),
        onDateChange: onAssignedDateChangeFromModal,
        dateSpanStart: assignedDateVal && dueDateVal ? new Date(assignedDateVal) : undefined,
        dateSpanEnd: dueDateVal ? new Date(dueDateVal) : undefined,
        disabledAfter: dueDateVal ? new Date(dueDateVal) : undefined,
      })
    );
  };

  const popDatePickerForDueDate = () => {
    dispatch(
      popDatePickerModal({
        visible: true,
        initialDate: dueDateVal ? dueDateVal : new Date(),
        onDateChange: onDueDateChangeFromModal,
        dateSpanStart: assignedDateVal ? new Date(assignedDateVal) : undefined,
        dateSpanEnd: dueDateVal ? new Date(dueDateVal) : undefined,
        disabledBefore: assignedDateVal ? new Date(assignedDateVal) : undefined,
      })
    );
  };

  const onAssignedDateChangeFromModal = (date: Date) => {
    setAssignedDateVal(date);
    dispatch(closeDatePickerModal());
  };

  const onDueDateChangeFromModal = (date: Date) => {
    setDueDateVal(date);
    dispatch(closeDatePickerModal());
  };

  const onDueDateChange = (e: ChangeEvent<HTMLInputElement>) => {
    const value = e.target.value;
    try {
      const parsed = parse(value, "yyyy-MM-dd", new Date());
      setDueDateVal(parsed);
    } catch (error) {
      console.error(error);
    }
  };

  const onAssignedDateHoursChange = (val: string) => {
    const date = assignedDateVal ? assignedDateVal : startOfToday();
    const result = setHours(date, parseInt(val));
    setAssignedDateVal(result);
  };

  const onAssignedDateMinutesChange = (val: string) => {
    const date = assignedDateVal ? assignedDateVal : startOfToday();
    const result = setMinutes(date, parseInt(val));
    setAssignedDateVal(result);
  };

  const onDueDateHoursChange = (val: string) => {
    if (dueDateVal) {
      const result = setHours(dueDateVal, parseInt(val));
      setDueDateVal(result);
    }
  };

  const onDueDateMinutesChange = (val: string) => {
    if (dueDateVal) {
      const result = setMinutes(dueDateVal, parseInt(val));
      setDueDateVal(result);
    }
  };

  const unsetAssignedDate = () => {
    setAssignedDateVal(null);
    if (assignedInputRef && assignedInputRef.current && assignedInputRef.current.value) {
      assignedInputRef.current.value = "";
    }
  };

  const unsetDueDate = () => {
    setDueDateVal(null);
    if (dueInputRef && dueInputRef.current && dueInputRef.current.value) {
      dueInputRef.current.value = "";
    }
  };

  const setAssignedDateToday = () => {
    const today = startOfToday();
    setAssignedDateVal(today);
  };

  const setAssignedDateTomorrow = () => {
    const tomorrow = startOfTomorrow();
    setAssignedDateVal(tomorrow);
  };

  const setDueDateDayAfterAssignedDate = () => {
    if (!assignedDateVal) {
      return;
    }
    setDueDateVal(addDays(assignedDateVal, 1));
  };

  const setDueDateOneHourAfterAssignedDate = () => {
    if (!assignedDateVal) {
      return;
    }
    setDueDateVal(addHours(assignedDateVal, 1));
  };

  const resetAssignedDateHoursAndMinutes = () => {
    if (assignedDateVal) {
      setAssignedDateVal(setMinutes(setHours(assignedDateVal, 0), 0));
    }
  };

  const resetDueDateHoursAndMinutes = () => {
    if (dueDateVal) {
      setDueDateVal(setMinutes(setHours(dueDateVal, 0), 0));
    }
  };

  const setDatesThisWeek = () => {
    const today = startOfToday();
    const weekStart = startOfWeek(today, options);
    const weekEnd = startOfDay(endOfWeek(today, options));
    setAssignedDateVal(weekStart);
    setDueDateVal(weekEnd);
  };

  const setDatesNextWeek = () => {
    const today = startOfToday();
    const date = addDays(today, 7);
    const weekStart = startOfWeek(date, options);
    const weekEnd = startOfDay(endOfWeek(date, options));
    setAssignedDateVal(weekStart);
    setDueDateVal(weekEnd);
  };

  const toggleAssignedDateTimePicker = () => {
    const next = !assignedDateTimePickerVisible;
    setAssignedDateTimePickerVisible(next);
    if (!next) {
      resetAssignedDateHoursAndMinutes();
    }
  };

  const toggleDueDateTimePicker = () => {
    const next = !dueDateTimePickerVisible;
    setDueDateTimePickerVisible(next);
    if (!next) {
      resetDueDateHoursAndMinutes();
    }
  };

  const close = () => {
    dispatch(closeChangeTaskDateModal());
  };

  const save = () => {
    if (!taskId) return;
    const req = {
      assignedDate: assignedDateVal,
      dueDate: dueDateVal,
      hasPreciseAssignedDate: assignedDateTimePickerVisible,
      hasPreciseDueDate: dueDateTimePickerVisible,
    };
    logger.log(req);
    updateTaskDates?.({
      taskId,
      body: req,
    });
  };

  return (
    <Modal
      visible={visible}
      title={t("changeTaskDateTitle")}
      bodyClass={styles.container}
      hasTitleCloseButton={true}
      requestClose={close}
    >
      <div className={styles.label}>
        {t("changeTaskDateDateLabel_assigned")}
        <div className={styles.inputContainer}>
          <Button className={styles.dateInput} variant={ButtonVariants.filled} onClick={popDatePickerForAssignedDate}>
            <b>{assignedDateVal ? format(new Date(assignedDateVal), t("dateFormat")) : t("datePickerSelectDate")}</b>
          </Button>
          {assignedDateVal && assignedDateTimePickerVisible && (
            <TimePicker
              id={"task-date-change-assigned-date-time"}
              minuteResolution={15}
              onHourChange={onAssignedDateHoursChange}
              onMinuteChange={onAssignedDateMinutesChange}
              defaultHours={`${getHours(assignedDateVal)}`.padStart(2, "0")}
              defaultMinutes={`${getMinutes(assignedDateVal)}`.padStart(2, "0")}
            />
          )}
          {assignedDateVal && (
            <Button onClick={toggleAssignedDateTimePicker}>
              {assignedDateTimePickerVisible ? <IoClose size={11} /> : <IoAdd size={11} />}
              {!assignedDateTimePickerVisible && <IoTimeOutline size={14} />}
            </Button>
          )}
        </div>
      </div>

      <div className={styles.dateActionContainer}>
        <Button className={styles.actionButton} onClick={setAssignedDateToday}>
          {t("changeTaskDateToday")}
        </Button>
        <Button className={styles.actionButton} onClick={setAssignedDateTomorrow}>
          {t("changeTaskDateTomorrow")}
        </Button>
        <div className="flex-1" />
        <Button className={styles.actionButton} onClick={unsetAssignedDate}>
          {t("changeTaskDateDateLabelRemove")}
        </Button>
      </div>

      <div className={styles.label}>
        {t("changeTaskDateDateLabel_due")}
        <div className={styles.inputContainer}>
          <Button className={styles.dateInput} variant={ButtonVariants.filled} onClick={popDatePickerForDueDate}>
            <b>{dueDateVal ? format(new Date(dueDateVal), t("dateFormat")) : t("datePickerSelectDate")}</b>
          </Button>
          {dueDateVal && dueDateTimePickerVisible && (
            <TimePicker
              id={"task-date-change-due-date-time"}
              minuteResolution={15}
              onHourChange={onDueDateHoursChange}
              onMinuteChange={onDueDateMinutesChange}
              defaultHours={`${getHours(dueDateVal)}`.padStart(2, "0")}
              defaultMinutes={`${getMinutes(dueDateVal)}`.padStart(2, "0")}
            />
          )}
          {dueDateVal && (
            <Button onClick={toggleDueDateTimePicker}>
              {dueDateTimePickerVisible ? <IoClose size={11} /> : <IoAdd size={11} />}
              {!dueDateTimePickerVisible && <IoTimeOutline size={14} />}
            </Button>
          )}
        </div>
      </div>

      <div className={styles.dateActionContainer}>
        <Button className={styles.actionButton} onClick={setDueDateDayAfterAssignedDate}>
          {t("changeTaskDateDayAfter")}
        </Button>
        <div className="flex-1" />
        <Button className={styles.actionButton} onClick={unsetDueDate}>
          {t("changeTaskDateDateLabelRemove")}
        </Button>
      </div>

      <QuickDateActionBar setDatesThisWeek={setDatesThisWeek} setDatesNextWeek={setDatesNextWeek} />

      <Button loading={isUpdateLoading} disabled={isUpdateLoading} variant={ButtonVariants.contrast} onClick={save}>
        {t("changeTaskDateDateLabelSave")}
      </Button>
    </Modal>
  );
};

export default TaskDateChangeModal;
