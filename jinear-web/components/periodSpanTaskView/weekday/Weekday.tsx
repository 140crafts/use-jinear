import cn from "classnames";
import { isToday } from "date-fns";
import React from "react";
import { useVariant } from "../context/PeriodSpanTaskViewContext";
import WeekdayTitle from "./title/WeekdayTitle";
import styles from "./Weekday.module.css";

interface WeekdayProps {
  day: Date;
}

const Weekday: React.FC<WeekdayProps> = ({ day }) => {
  const variant = useVariant() || "week";
  return (
    <div className={cn(styles.container, styles[`width-${variant}`])}>
      <WeekdayTitle day={day} />
      {isToday(day) && (
        <div className={cn(styles.todayLine, styles[`width-${variant}`])} />
      )}
    </div>
  );
};

export default Weekday;
