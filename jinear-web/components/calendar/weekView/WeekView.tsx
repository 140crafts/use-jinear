import Logger from "@/utils/logger";
import React from "react";
import DayspanTimelyView from "../common/dayspanTimelyView/DayspanTimelyView";
import { useViewingDate, useWeekDays, useWeekViewPeriodEnd, useWeekViewPeriodStart } from "../context/CalendarContext";

interface WeekViewProps {}

const logger = Logger("WeekView");

const WeekView: React.FC<WeekViewProps> = ({}) => {
  const periodStart = useWeekViewPeriodStart();
  const periodEnd = useWeekViewPeriodEnd();
  const days = useWeekDays();
  const viewingDate = useViewingDate();

  return <DayspanTimelyView viewingDate={viewingDate} periodStart={periodStart} periodEnd={periodEnd} days={days} />;
};

export default WeekView;
