import Button, { ButtonVariants } from "@/components/button";
import MiniMonthCalendar from "@/components/miniMonthCalendar/MiniMonthCalendar";
import { startOfToday } from "date-fns";
import useTranslation from "locales/useTranslation";
import React, { useState } from "react";
import Modal from "../modal/Modal";
import styles from "./DatePickerModal.module.css";

interface DatePickerModalProps {}

const DatePickerModal: React.FC<DatePickerModalProps> = ({}) => {
  const { t } = useTranslation();
  const visible = true;
  const [selectedDate, setSelectedDate] = useState(new Date());
  const onDateChange = (day: Date) => {};

  return (
    <Modal visible={visible} bodyClass={styles.container}>
      <MiniMonthCalendar selectedDate={selectedDate} onDateChange={onDateChange} />
      <div className={styles.quickActions}>
        <Button
          variant={ButtonVariants.filled}
          onClick={() => {
            setSelectedDate(startOfToday());
          }}
        >
          {t("datePickerModalQuickActionToday")}
        </Button>
      </div>
    </Modal>
  );
};

export default DatePickerModal;
