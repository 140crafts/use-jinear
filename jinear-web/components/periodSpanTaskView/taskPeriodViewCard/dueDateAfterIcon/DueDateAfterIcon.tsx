import { format } from "date-fns";
import useTranslation from "locales/useTranslation";
import React from "react";
import { IoArrowForward } from "react-icons/io5";
import styles from "./DueDateAfterIcon.module.css";

interface DueDateAfterIconProps {
  dueDate: Date;
  isDueDateAfter: boolean;
}

const DueDateAfterIcon: React.FC<DueDateAfterIconProps> = ({
  dueDate,
  isDueDateAfter,
}) => {
  const { t } = useTranslation();
  const tooltip = !dueDate
    ? null
    : t("taskWeekCardTaskDueThisWeekTooltip")?.replace(
        "${date}",
        format(new Date(dueDate), t("dateFormat"))
      );

  return dueDate && isDueDateAfter ? (
    <div className={styles.iconWrapper} data-tooltip-right={tooltip}>
      <IoArrowForward />
    </div>
  ) : null;
};

export default DueDateAfterIcon;
