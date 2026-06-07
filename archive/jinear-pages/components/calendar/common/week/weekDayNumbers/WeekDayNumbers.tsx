import React from "react";
import styles from "./WeekDayNumbers.module.css";
import WeekDayTitle from "./weekDayTitle/WeekDayTitle";

interface WeekDayNumbersProps {
  week: Date[];
}

const WeekDayNumbers: React.FC<WeekDayNumbersProps> = ({ week }) => {
  return (
    <div className={styles.container}>
      {week.map((weekDay) => (
        <WeekDayTitle day={weekDay} key={`week-day-title-${weekDay.getTime()}`} />
      ))}
    </div>
  );
};

export default WeekDayNumbers;
