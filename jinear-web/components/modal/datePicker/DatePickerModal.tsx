import MiniMonthCalendar from "@/components/miniMonthCalendar/MiniMonthCalendar";
import {
  closeDatePickerModal,
  selectDatePickerModalDateSpanEnd,
  selectDatePickerModalDateSpanStart,
  selectDatePickerModalDisabledAfter,
  selectDatePickerModalDisabledBefore,
  selectDatePickerModalInitialDate,
  selectDatePickerModalOnDateChange,
  selectDatePickerModalTitle,
  selectDatePickerModalVisible,
} from "@/store/slice/modalSlice";
import { useAppDispatch, useTypedSelector } from "@/store/store";
import Logger from "@/utils/logger";
import React from "react";
import Modal from "../modal/Modal";
import styles from "./DatePickerModal.module.css";

interface DatePickerModalProps {}

const logger = Logger("DatePickerModal");
const DatePickerModal: React.FC<DatePickerModalProps> = ({}) => {
  const dispatch = useAppDispatch();
  const visible = useTypedSelector(selectDatePickerModalVisible);
  const initialDate = useTypedSelector(selectDatePickerModalInitialDate);
  const dateSpanStart = useTypedSelector(selectDatePickerModalDateSpanStart);
  const dateSpanEnd = useTypedSelector(selectDatePickerModalDateSpanEnd);
  const disabledBefore = useTypedSelector(selectDatePickerModalDisabledBefore);
  const disabledAfter = useTypedSelector(selectDatePickerModalDisabledAfter);
  const title = useTypedSelector(selectDatePickerModalTitle);

  const onDateChange = useTypedSelector(selectDatePickerModalOnDateChange);

  const close = () => {
    dispatch(closeDatePickerModal());
  };

  logger.log({
    visible,
    initialDate,
    dateSpanStart,
    dateSpanEnd,
  });

  return (
    <Modal visible={visible} bodyClass={styles.container} requestClose={close} hasTitleCloseButton={true} title={title}>
      <MiniMonthCalendar
        initialDate={initialDate}
        dateSpanStart={dateSpanStart}
        dateSpanEnd={dateSpanEnd}
        disabledBefore={disabledBefore}
        disabledAfter={disabledAfter}
        onDateChange={onDateChange}
      />
    </Modal>
  );
};

export default DatePickerModal;
