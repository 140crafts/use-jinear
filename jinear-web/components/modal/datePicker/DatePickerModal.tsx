import MiniMonthCalendar from "@/components/miniMonthCalendar/MiniMonthCalendar";
import {
  closeDatePickerModal,
  selectDatePickerModalInitialDate,
  selectDatePickerModalOnDateChange,
  selectDatePickerModalVisible,
} from "@/store/slice/modalSlice";
import { useAppDispatch, useTypedSelector } from "@/store/store";
import React from "react";
import Modal from "../modal/Modal";
import styles from "./DatePickerModal.module.css";

interface DatePickerModalProps {}

const DatePickerModal: React.FC<DatePickerModalProps> = ({}) => {
  const dispatch = useAppDispatch();
  const visible = useTypedSelector(selectDatePickerModalVisible);
  const initialDate = useTypedSelector(selectDatePickerModalInitialDate);
  const onDateChange = useTypedSelector(selectDatePickerModalOnDateChange);

  const close = () => {
    dispatch(closeDatePickerModal());
  };

  return (
    <Modal visible={visible} bodyClass={styles.container} requestClose={close} hasTitleCloseButton={true}>
      <MiniMonthCalendar initialDate={initialDate} onDateChange={onDateChange} />
    </Modal>
  );
};

export default DatePickerModal;
