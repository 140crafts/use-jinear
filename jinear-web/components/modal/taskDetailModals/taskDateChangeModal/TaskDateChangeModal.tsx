import Button, { ButtonVariants } from "@/components/button";
import {
  useUpdateTaskAssignedDateMutation,
  useUpdateTaskDueDateMutation,
} from "@/store/api/taskUpdateApi";
import {
  closeChangeTaskDateModal,
  selectChangeTaskDateModalDateType,
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
  const dateType = useTypedSelector(selectChangeTaskDateModalDateType);
  const [dateVal, setDateVal] = useState<Date | null>(new Date());
  const inputRef = useRef<HTMLInputElement>(null);

  const currentAssigned = useTypedSelector(
    selectChangeTaskDateModalTaskCurrentAssignedDate
  );
  const currentDue = useTypedSelector(
    selectChangeTaskDateModalTaskCurrentDueDate
  );
  const [
    updateTaskAssignedDate,
    { isSuccess: isUpdateAssignedSuccess, isLoading: isUpdateAssignedLoading },
  ] = useUpdateTaskAssignedDateMutation();
  const [
    updateTaskDueDate,
    { isSuccess: isUpdateDueSuccess, isLoading: isUpdateDueLoading },
  ] = useUpdateTaskDueDateMutation();

  useEffect(() => {
    if (visible && dateType && dateType == "assigned" && currentAssigned) {
      setDateVal(new Date(currentAssigned));
    } else if (visible && dateType && dateType == "due" && currentDue) {
      setDateVal(new Date(currentDue));
    } else if (!visible) {
      setDateVal(null);
      if (inputRef && inputRef.current && inputRef.current.value) {
        inputRef.current.value = "";
      }
    }
  }, [visible, dateType, currentAssigned, currentDue]);

  useEffect(() => {
    if (isUpdateAssignedSuccess || isUpdateDueSuccess) {
      close();
    }
  }, [isUpdateAssignedSuccess, isUpdateDueSuccess]);

  const onDateChange = (e: ChangeEvent<HTMLInputElement>) => {
    const value = e.target.value;
    try {
      const parsed = parse(value, "yyyy-MM-dd", new Date());
      console.log({ parsed });
      setDateVal(parsed);
    } catch (error) {
      console.error(error);
    }
  };

  const unsetDate = () => {
    setDateVal(null);
    if (inputRef && inputRef.current && inputRef.current.value) {
      inputRef.current.value = "";
    }
  };

  const close = () => {
    dispatch(closeChangeTaskDateModal());
  };

  const save = () => {
    if (!taskId) return;
    const method =
      dateType == "assigned" ? updateTaskAssignedDate : updateTaskDueDate;
    method?.({ taskId, body: { date: dateVal } });
  };

  return (
    <Modal
      visible={visible}
      title={t(`changeTaskDateTitle_${dateType}`)}
      bodyClass={styles.container}
      hasTitleCloseButton={true}
      requestClose={close}
    >
      <label className={styles.label} htmlFor={"new-task-assigned-date"}>
        {t(`changeTaskDateDateLabel_${dateType}`)}
        <input
          ref={inputRef}
          type={"date"}
          id={"new-task-assigned-date"}
          value={dateVal ? format(dateVal, "yyyy-MM-dd") : ""}
          onChange={onDateChange}
        />
      </label>
      <div className={styles.removeContainer}>
        <Button className={styles.unassignButton} onClick={unsetDate}>
          {t("changeTaskDateDateLabelRemove")}
        </Button>
      </div>

      <Button
        loading={isUpdateAssignedLoading || isUpdateDueLoading}
        disabled={isUpdateAssignedLoading || isUpdateDueLoading}
        variant={ButtonVariants.contrast}
        onClick={save}
      >
        {t("changeTaskDateDateLabelSave")}
      </Button>
    </Modal>
  );
};

export default TaskDateChangeModal;
