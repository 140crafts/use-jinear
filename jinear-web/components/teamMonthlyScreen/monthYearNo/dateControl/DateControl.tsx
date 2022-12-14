import Button from "@/components/button";
import {
  useSetViewingPeriodOf,
  useViewingPeriodOf,
} from "@/store/context/screen/team/monthly/teamMonthlyScreenContext";
import { addMonths, startOfToday } from "date-fns";
import React from "react";
import { IoCaretBack, IoCaretForward, IoEllipse } from "react-icons/io5";
import styles from "./DateControl.module.css";

interface DateControlProps {}

const DateControl: React.FC<DateControlProps> = ({}) => {
  const viewingPeriodOf = useViewingPeriodOf();
  const setViewingPeriodOf = useSetViewingPeriodOf();

  const prevMonth = () => {
    if (viewingPeriodOf) {
      setViewingPeriodOf?.(addMonths(viewingPeriodOf, -1));
    }
  };

  const nextMonth = () => {
    if (viewingPeriodOf) {
      setViewingPeriodOf?.(addMonths(viewingPeriodOf, 1));
    }
  };

  const thisMonth = () => {
    setViewingPeriodOf?.(startOfToday());
  };

  return (
    <div className={styles.container}>
      <Button onClick={prevMonth}>
        <IoCaretBack />
      </Button>
      <Button onClick={thisMonth}>
        <IoEllipse size={7} />
      </Button>
      <Button onClick={nextMonth}>
        <IoCaretForward />
      </Button>
    </div>
  );
};

export default DateControl;
