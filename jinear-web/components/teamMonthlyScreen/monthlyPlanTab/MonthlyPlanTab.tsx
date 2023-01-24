import PeriodSpanTaskView from "@/components/periodSpanTaskView/PeriodSpanTaskView";
import React from "react";

interface MonthlyPlanTabProps {
  teamId?: string;
  workspaceId?: string;
  viewingPeriodStart: Date;
  viewingPeriodEnd: Date;
  containerRef?: React.RefObject<HTMLDivElement>;
}

const MonthlyPlanTab: React.FC<MonthlyPlanTabProps> = ({
  teamId,
  workspaceId,
  viewingPeriodStart,
  viewingPeriodEnd,
  containerRef,
}) => {
  return teamId && workspaceId ? (
    <PeriodSpanTaskView
      containerRef={containerRef}
      teamId={teamId}
      workspaceId={workspaceId}
      viewingPeriodStart={viewingPeriodStart}
      viewingPeriodEnd={viewingPeriodEnd}
      showDayOfWeek={false}
      variant={"month"}
    />
  ) : null;
};

export default MonthlyPlanTab;
