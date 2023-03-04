import { format } from "date-fns";
import useTranslation from "locales/useTranslation";
import React from "react";
import { IoArrowBack } from "react-icons/io5";
import styles from "./StartDateBeforeIcon.module.css";

interface StartDateBeforeIconProps {
  assignedDate: Date;
  isStartDateBefore: boolean;
}

const StartDateBeforeIcon: React.FC<StartDateBeforeIconProps> = ({ assignedDate, isStartDateBefore }) => {
  const { t } = useTranslation();
  const tooltip = !assignedDate
    ? null
    : t("taskWeekCardTaskStartedBeforeThisWeekTooltip")?.replace("${date}", format(new Date(assignedDate), t("dateFormat")));

  return isStartDateBefore ? (
    <div className={styles.iconWrapper} data-tooltip={tooltip}>
      <IoArrowBack />
    </div>
  ) : null;
};

export default StartDateBeforeIcon;
