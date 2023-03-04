import Button from "@/components/button";
import { useSetViewingWeekStart, useViewingWeekStart } from "@/store/context/screen/team/weekly/teamWeeklyScreenContext";
import { addWeeks, startOfToday, startOfWeek } from "date-fns";
import React from "react";
import { IoCaretBack, IoCaretForward, IoEllipse } from "react-icons/io5";
import styles from "./DateControl.module.css";

interface DateControlProps {}

const DateControl: React.FC<DateControlProps> = ({}) => {
  const date = useViewingWeekStart();
  const setViewingWeekStart = useSetViewingWeekStart();

  const prevWeek = () => {
    setViewingWeekStart?.(addWeeks(date, -1));
  };

  const nextWeek = () => {
    setViewingWeekStart?.(addWeeks(date, 1));
  };

  const thisWeek = () => {
    setViewingWeekStart?.(startOfWeek(startOfToday(), { weekStartsOn: 1 }));
  };

  return (
    <div className={styles.container}>
      <Button onClick={prevWeek}>
        <IoCaretBack />
      </Button>
      <Button onClick={thisWeek}>
        <IoEllipse size={7} />
      </Button>
      <Button onClick={nextWeek}>
        <IoCaretForward />
      </Button>
    </div>
  );
};

export default DateControl;
