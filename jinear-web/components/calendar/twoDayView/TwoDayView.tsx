import { addDays } from "date-fns";
import React from "react";
import DayspanTimelyView from "../common/dayspanTimelyView/DayspanTimelyView";
import { useViewingDate } from "../context/CalendarContext";

interface TwoDayViewProps {}

const TwoDayView: React.FC<TwoDayViewProps> = ({}) => {
  const viewingDate = useViewingDate();
  const days = [viewingDate, addDays(viewingDate, 1)];
  const periodStart = viewingDate;
  const periodEnd = addDays(viewingDate, 1);
  return <DayspanTimelyView viewingDate={viewingDate} periodStart={periodStart} periodEnd={periodEnd} days={days} />;
};

export default TwoDayView;
