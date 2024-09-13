import MiniMonthCalendar from "@/components/miniMonthCalendar/MiniMonthCalendar";
import {
  closeDatePickerModal,
  selectDatePickerModalDateSpanEnd,
  selectDatePickerModalDateSpanStart,
  selectDatePickerModalDisabledAfter,
  selectDatePickerModalDisabledBefore,
  selectDatePickerModalInitialDate,
  selectDatePickerModalOnDateChange,
  selectDatePickerModalTitle, selectDatePickerModalUnpickable,
  selectDatePickerModalVisible
} from "@/store/slice/modalSlice";
import { useAppDispatch, useTypedSelector } from "@/store/store";
import Logger from "@/utils/logger";
import React from "react";
import Modal from "../modal/Modal";
import styles from "./DatePickerModal.module.css";
import useTranslation from "@/locals/useTranslation";
import Button from "@/components/button";
import { LuX } from "react-icons/lu";

interface DatePickerModalProps {
}

const logger = Logger("DatePickerModal");
const DatePickerModal: React.FC<DatePickerModalProps> = ({}) => {
  const { t } = useTranslation();
  const dispatch = useAppDispatch();
  const visible = useTypedSelector(selectDatePickerModalVisible);
  const initialDate = useTypedSelector(selectDatePickerModalInitialDate);
  const dateSpanStart = useTypedSelector(selectDatePickerModalDateSpanStart);
  const dateSpanEnd = useTypedSelector(selectDatePickerModalDateSpanEnd);
  const disabledBefore = useTypedSelector(selectDatePickerModalDisabledBefore);
  const disabledAfter = useTypedSelector(selectDatePickerModalDisabledAfter);
  const title = useTypedSelector(selectDatePickerModalTitle);

  const onDateChange = useTypedSelector(selectDatePickerModalOnDateChange);
  const unpickable = useTypedSelector(selectDatePickerModalUnpickable);

  const setValue = (value?: Date | undefined) => {
    value && onDateChange?.(value);
  };

  const unpick = () => {
    onDateChange?.(null);
  };
  const close = () => {
    dispatch(closeDatePickerModal());
  };

  logger.log({
    visible,
    initialDate,
    dateSpanStart,
    dateSpanEnd
  });

  return (
    <Modal visible={visible} bodyClass={styles.container} requestClose={close} hasTitleCloseButton={true} title={title}>
      <MiniMonthCalendar
        value={initialDate ?? undefined}
        setValue={setValue}
        dateSpanStart={dateSpanStart ?? undefined}
        dateSpanEnd={dateSpanEnd ?? undefined}
        disabledBefore={disabledBefore ?? undefined}
        disabledAfter={disabledAfter ?? undefined}
      />
      <div className={styles.footerContainer}>
        {initialDate && unpickable && <Button onClick={unpick}>{t("datePickerModalUnpickButtonLabel")}</Button>}
      </div>
    </Modal>
  );
};

export default DatePickerModal;
