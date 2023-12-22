import { TaskDto } from "@/model/be/jinear-core";
import getCssVariable from "@/utils/cssHelper";
import Logger from "@/utils/logger";
import { tryCatch } from "@/utils/tryCatch";
import { addHours, format, startOfDay } from "date-fns";
import useTranslation from "locales/useTranslation";
import React from "react";
import styles from "./TimelyView.module.css";
import CurrentTimeLine from "./currentTimeLine/CurrentTimeLine";
import DayTimelyView from "./dayTimelyView/DayTimelyView";
import Tile from "./dayTimelyView/tile/Tile";

interface TimelyViewProps {
  days: Date[];
  tasks: TaskDto[];
}

const MINUTES_IN_A_DAY = 24 * 60;

const logger = Logger("TimelyView");

const TimelyView: React.FC<TimelyViewProps> = ({ days, tasks }) => {
  const { t } = useTranslation();
  const calendarWeekViewDayMinutePixelRatio = tryCatch(() =>
    parseInt(getCssVariable("--calendar-week-view-day-minute-pixel-ratio"))
  )?.result;

  return (
    <div className={styles.container} style={{ minHeight: MINUTES_IN_A_DAY * calendarWeekViewDayMinutePixelRatio }}>
      <div className={styles.hourLabelContainer}>
        {[...new Array(24)].map((_, i) => (
          <Tile
            key={`week-time-tile-${i}`}
            topLabel={format(addHours(startOfDay(new Date()), i), t("timeFormatShort"))}
            topClassName={i == 0 ? styles.zeroHoursLabel : undefined}
            bottomLabel={i == 23 ? format(addHours(startOfDay(new Date()), i + 1), t("timeFormatShort")) : undefined}
          />
        ))}
      </div>
      {days.map((day) => (
        <DayTimelyView
          key={`day-timely-view-${day.getTime()}`}
          day={day}
          tasks={tasks}
          minuteInPx={calendarWeekViewDayMinutePixelRatio}
        />
      ))}
      <CurrentTimeLine calendarWeekViewDayMinutePixelRatio={calendarWeekViewDayMinutePixelRatio} />
    </div>
  );
};

export default TimelyView;
