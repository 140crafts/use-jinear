import cn from "classnames";
import { format, isToday } from "date-fns";
import useTranslation from "locales/useTranslation";
import React from "react";
import styles from "./WeekdayTitle.module.css";

interface WeekdayTitleProps {
  day: Date;
  taskCount?: number;
}

const WeekdayTitle: React.FC<WeekdayTitleProps> = ({ day, taskCount }) => {
  const { t } = useTranslation();
  return (
    <div className={styles.titleContiner}>
      <h2
        className={cn(styles.dayTitle, isToday(day) ? styles.today : undefined)}
      >
        {format(day, "dd EEE", { locale: t("dateFnsLocale") as any })}
      </h2>
      <div className={styles.taskCount}>{taskCount}</div>
    </div>
  );
};

export default WeekdayTitle;
