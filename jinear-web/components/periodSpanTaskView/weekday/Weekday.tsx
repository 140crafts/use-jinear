import cn from "classnames";
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
    </div>
  );
};

export default Weekday;
