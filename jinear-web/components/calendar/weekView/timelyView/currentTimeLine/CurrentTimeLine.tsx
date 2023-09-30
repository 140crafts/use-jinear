import { differenceInMinutes, format, startOfDay } from "date-fns";
import useTranslation from "locales/useTranslation";
import React, { useEffect, useRef, useState } from "react";
import styles from "./CurrentTimeLine.module.css";

interface CurrentTimeLineProps {
  calendarWeekViewDayMinutePixelRatio: number;
}

const CurrentTimeLine: React.FC<CurrentTimeLineProps> = ({ calendarWeekViewDayMinutePixelRatio }) => {
  const { t } = useTranslation();
  const tickerRef = useRef<any>();
  const [now, setNow] = useState(new Date());
  const minutesFromStartOfDay = differenceInMinutes(now, startOfDay(now));

  useEffect(() => {
    tick();
    return () => {
      clearInterval(tickerRef.current);
    };
  }, []);

  const tick = () => {
    tickerRef.current = setTimeout(() => {
      setNow(new Date());
      tick();
    }, 60000);
  };

  return (
    <div
      id="calendar-week-view-current-time-line"
      className={styles.currentTimeLine}
      style={{
        top: minutesFromStartOfDay * calendarWeekViewDayMinutePixelRatio,
      }}
    >
      <div className={styles.label}>{format(now, t("timeFormat"))}</div>
    </div>
  );
};

export default CurrentTimeLine;
