import React from "react";
import styles from "./TargetDateInput.module.css";
import DatePickerButton from "@/components/datePickerButton/DatePickerButton";
import useTranslation from "@/locals/useTranslation";

interface TargetDateInputProps {
  onMilestoneDateChange: (date?: Date | null) => void;
}

const TargetDateInput: React.FC<TargetDateInputProps> = ({ onMilestoneDateChange }) => {
  const { t } = useTranslation();

  return (
    <div className={styles.container}>
      <label className={styles.label}>
        {t("newMilestoneFormTargetDateLabel")}
      </label>
      <DatePickerButton
        label={t("newMilestoneFormTargetDate")}
        onDateChange={onMilestoneDateChange}
      />
    </div>
  );
};

export default TargetDateInput;