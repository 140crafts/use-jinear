import useWindowSize from "@/hooks/useWindowSize";
import Logger from "@/utils/logger";
import cn from "classnames";
import { isToday } from "date-fns";
import React, { useEffect, useRef, useState } from "react";
import { useVariant } from "../context/PeriodSpanTaskViewContext";
import WeekdayTitle from "./title/WeekdayTitle";
import styles from "./Weekday.module.css";

interface WeekdayProps {
  day: Date;
}
const logger = Logger("Weekday");
const Weekday: React.FC<WeekdayProps> = ({ day }) => {
  const variant = useVariant() || "week";
  const windowSize = useWindowSize();
  const parentRef = useRef<HTMLDivElement>(null);
  const [width, setWidth] = useState<string>();

  useEffect(() => {
    if (parentRef && parentRef.current) {
      const width = parentRef.current.offsetWidth;
      logger.log({ width });
      setWidth(`${width}px`);
    }
  }, [windowSize.width]);

  return (
    <div ref={parentRef} className={cn(styles.container, styles[`width-${variant}`])}>
      <WeekdayTitle day={day} />
      {isToday(day) && <div style={{ width }} className={cn(styles.todayLine, styles[`width-${variant}`])} />}
    </div>
  );
};

export default Weekday;
