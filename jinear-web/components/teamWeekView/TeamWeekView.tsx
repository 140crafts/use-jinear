import PeriodSpanTaskView from "@/components/periodSpanTaskView/PeriodSpanTaskView";
import { endOfWeek } from "date-fns";
import React from "react";

interface TeamWeekViewProps {
  workspaceId: string;
  teamId: string;
  viewingWeekStart: Date;
  showDayOfWeek?: boolean;
}

const TeamWeekView: React.FC<TeamWeekViewProps> = ({
  teamId,
  workspaceId,
  viewingWeekStart,
  showDayOfWeek,
}) => {
  const viewingWeekEnd = endOfWeek(viewingWeekStart, { weekStartsOn: 1 });
  return (
    <PeriodSpanTaskView
      teamId={teamId}
      workspaceId={workspaceId}
      viewingPeriodStart={viewingWeekStart}
      viewingPeriodEnd={viewingWeekEnd}
      showDayOfWeek={showDayOfWeek}
    />
  );
};

export default TeamWeekView;
