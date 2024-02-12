import useTranslation from "locales/useTranslation";
import React from "react";
import { ICalendarWeekRowCell } from "../../calendarUtils";
import Week from "../../common/week/Week";
import styles from "./AllDayTasks.module.css";

interface AllDayTasksProps {
  days: Date[];
  weekTable: ICalendarWeekRowCell[][][] | undefined;
}

import Logger from "@/utils/logger";

const logger = Logger("AllDayTasks");

const AllDayTasks: React.FC<AllDayTasksProps> = ({ days, weekTable }) => {
  const { t } = useTranslation();
  logger.log({ weekTable, days });
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
            weekEvents={week}
            weekIndex={weekIndex}
            week={days.slice(weekIndex * (days.length + 1), weekIndex * (days.length + 1) + (days.length + 1))}
            className={styles.week}
            omitNumbers={true}
          />
        ))}
      </div>
    </div>
  );
};

export default AllDayTasks;
