import { queryStateShortDateParser, useQueryState } from "@/hooks/useQueryState";
import { WorkspaceDto } from "@/model/be/jinear-core";
import { addDays, startOfDay } from "date-fns";
import React from "react";
import DayspanTimelyView from "../common/dayspanTimelyView/DayspanTimelyView";
import { useGhostEvent } from "../context/CalendarContext";

interface TwoDayViewProps {
  workspace: WorkspaceDto;
}

const TwoDayView: React.FC<TwoDayViewProps> = ({ workspace }) => {
  const ghostEvent = useGhostEvent();
  const viewingDate = useQueryState<Date>("viewingDate", queryStateShortDateParser) || startOfDay(new Date());

  const periodStart = viewingDate;
  const periodEnd = addDays(viewingDate, 1);
  const days = [periodStart, periodEnd];

  return (
    <DayspanTimelyView
      workspace={workspace}
      ghostEvent={ghostEvent}
      viewingDate={viewingDate}
      periodStart={periodStart}
      periodEnd={periodEnd}
      days={days}
    />
  );
};

export default TwoDayView;
