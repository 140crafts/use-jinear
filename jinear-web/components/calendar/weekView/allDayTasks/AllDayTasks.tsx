import useTranslation from "locales/useTranslation";
import React from "react";
import { ICalendarWeekRowCell } from "../../calendarUtils";
import Week from "../../common/week/Week";
import styles from "./AllDayTasks.module.css";

interface AllDayTasksProps {
  days: Date[];
  weekTable: ICalendarWeekRowCell[][][] | undefined;
}

const AllDayTasks: React.FC<AllDayTasksProps> = ({ days, weekTable }) => {
  const { t } = useTranslation();
  return (
    <div className={styles.container}>
      <div className={styles.allDayLabelContainer}>
        <div className={styles.allDayLabel}>{t("calendarWeekViewAllDayLabel")}</div>
      </div>
      <div className={styles.weekContainer}>
        {weekTable?.map((week, weekIndex) => (
          <Week
            id={`week-view-${weekIndex}`}
            key={`week-view-${weekIndex}`}
            weekTasks={week}
            weekIndex={weekIndex}
            week={days.slice(weekIndex * 7, weekIndex * 7 + 7)}
            className={styles.week}
            omitNumbers={true}
          />
        ))}
      </div>
    </div>
  );
};

export default AllDayTasks;
