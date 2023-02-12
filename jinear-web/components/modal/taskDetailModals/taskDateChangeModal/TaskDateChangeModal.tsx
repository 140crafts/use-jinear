import Button, { ButtonVariants } from "@/components/button";
import { useUpdateTaskDatesMutation } from "@/store/api/taskUpdateApi";
import {
  closeChangeTaskDateModal,
  selectChangeTaskDateModalTaskCurrentAssignedDate,
  selectChangeTaskDateModalTaskCurrentDueDate,
  selectChangeTaskDateModalTaskId,
  selectChangeTaskDateModalVisible,
} from "@/store/slice/modalSlice";
import { useAppDispatch, useTypedSelector } from "@/store/store";
import { format, parse } from "date-fns";
import useTranslation from "locales/useTranslation";
import React, { ChangeEvent, useEffect, useRef, useState } from "react";
import Modal from "../../modal/Modal";
import styles from "./TaskDateChangeModal.module.css";

interface TaskDateChangeModalProps {}

const TaskDateChangeModal: React.FC<TaskDateChangeModalProps> = ({}) => {
  const { t } = useTranslation();
  const dispatch = useAppDispatch();
  const visible = useTypedSelector(selectChangeTaskDateModalVisible);
  const taskId = useTypedSelector(selectChangeTaskDateModalTaskId);
  const [assignedDateVal, setAssignedDateVal] = useState<Date | null>(
    new Date()
  );
  const [dueDateVal, setDueDateVal] = useState<Date | null>(new Date());
  const assignedInputRef = useRef<HTMLInputElement>(null);
  const dueInputRef = useRef<HTMLInputElement>(null);

  const currentAssigned = useTypedSelector(
    selectChangeTaskDateModalTaskCurrentAssignedDate
  );
  const currentDue = useTypedSelector(
    selectChangeTaskDateModalTaskCurrentDueDate
  );

  const [
    updateTaskDates,
    { isSuccess: isUpdateSuccess, isLoading: isUpdateLoading },
  ] = useUpdateTaskDatesMutation();

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
    }
  }, [visible, currentAssigned, currentDue]);

  useEffect(() => {
    if (isUpdateSuccess) {
      close();
    }
  }, [isUpdateSuccess]);

  const onAssignedDateChange = (e: ChangeEvent<HTMLInputElement>) => {
    const value = e.target.value;
    try {
      const parsed = parse(value, "yyyy-MM-dd", new Date());
      setAssignedDateVal(parsed);
    } catch (error) {
      console.error(error);
    }
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

  const unsetAssignedDate = () => {
    setAssignedDateVal(null);
    if (
      assignedInputRef &&
      assignedInputRef.current &&
      assignedInputRef.current.value
    ) {
      assignedInputRef.current.value = "";
    }
  };

  const unsetDueDate = () => {
    setDueDateVal(null);
    if (dueInputRef && dueInputRef.current && dueInputRef.current.value) {
      dueInputRef.current.value = "";
    }
  };

  const close = () => {
    dispatch(closeChangeTaskDateModal());
  };

  const save = () => {
    if (!taskId) return;
    updateTaskDates?.({
      taskId,
      body: {
        assignedDate: assignedDateVal,
        dueDate: dueDateVal,
      },
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
      <label className={styles.label} htmlFor={"task-update-assigned-date"}>
        {t("changeTaskDateDateLabel_assigned")}
        <input
          ref={assignedInputRef}
          type={"date"}
          id={"task-update-assigned-date"}
          value={assignedDateVal ? format(assignedDateVal, "yyyy-MM-dd") : ""}
          onChange={onAssignedDateChange}
        />
      </label>
      <div className={styles.removeContainer}>
        <Button className={styles.unassignButton} onClick={unsetAssignedDate}>
          {t("changeTaskDateDateLabelRemove")}
        </Button>
      </div>

      <label className={styles.label} htmlFor={"task-update-due-date"}>
        {t("changeTaskDateDateLabel_due")}
        <input
          ref={dueInputRef}
          type={"date"}
          id={"task-update-due-date"}
          value={dueDateVal ? format(dueDateVal, "yyyy-MM-dd") : ""}
          onChange={onDueDateChange}
        />
      </label>
      <div className={styles.removeContainer}>
        <Button className={styles.unassignButton} onClick={unsetDueDate}>
          {t("changeTaskDateDateLabelRemove")}
        </Button>
      </div>

      <Button
        loading={isUpdateLoading}
        disabled={isUpdateLoading}
        variant={ButtonVariants.contrast}
        onClick={save}
      >
        {t("changeTaskDateDateLabelSave")}
      </Button>
    </Modal>
  );
};

export default TaskDateChangeModal;
