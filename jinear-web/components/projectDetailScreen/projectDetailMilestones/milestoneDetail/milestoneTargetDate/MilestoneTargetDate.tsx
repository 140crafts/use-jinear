import React from "react";
import styles from "./MilestoneTargetDate.module.css";
import DatePickerButton from "@/components/datePickerButton/DatePickerButton";
import useTranslation from "@/locals/useTranslation";
import { useUpdateMilestoneTargetDateMutation } from "@/api/projectMilestoneApi";

interface TargetDateInputProps {
  milestoneId: string;
  targetDate?: Date | null;
}

const MilestoneTargetDate: React.FC<TargetDateInputProps> = ({ milestoneId, targetDate }) => {
  const { t } = useTranslation();

  const [updateMilestoneTargetDate, { isLoading }] = useUpdateMilestoneTargetDateMutation();

  const onMilestoneDateChange = (targetDate?: Date | null) => {
    updateMilestoneTargetDate?.({ milestoneId, targetDate });
  };

  return (
    <div className={styles.container}>
      <DatePickerButton
        disabled={isLoading}
        label={t("newMilestoneFormTargetDate")}
        onDateChange={onMilestoneDateChange}
        initialDate={targetDate ? new Date(targetDate) : undefined}
        unpickableFromModal={true}
      />
    </div>
  );
};

export default MilestoneTargetDate;