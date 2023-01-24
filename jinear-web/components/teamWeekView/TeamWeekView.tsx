import PeriodSpanTaskView from "@/components/periodSpanTaskView/PeriodSpanTaskView";
import { endOfWeek } from "date-fns";
import React from "react";

interface TeamWeekViewProps {
  workspaceId: string;
  teamId: string;
  viewingWeekStart: Date;
  showDayOfWeek?: boolean;
  containerRef?: React.RefObject<HTMLDivElement>;
}

const TeamWeekView: React.FC<TeamWeekViewProps> = ({
  teamId,
  workspaceId,
  viewingWeekStart,
  showDayOfWeek,
  containerRef,
}) => {
  const viewingWeekEnd = endOfWeek(viewingWeekStart, { weekStartsOn: 1 });
  return (
    <PeriodSpanTaskView
      containerRef={containerRef}
      teamId={teamId}
      workspaceId={workspaceId}
      viewingPeriodStart={viewingWeekStart}
      viewingPeriodEnd={viewingWeekEnd}
      showDayOfWeek={showDayOfWeek}
      variant={"week"}
    />
  );
};

export default TeamWeekView;
