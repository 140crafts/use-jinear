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
    // <>
    //   <TeamWeekView
    //     teamId={teamId}
    //     workspaceId={workspaceId}
    //     viewingWeekStart={viewingPeriodStart}
    //     showDayOfWeek={true}
    //   />
    //   <TeamWeekView
    //     teamId={teamId}
    //     workspaceId={workspaceId}
    //     viewingWeekStart={addWeeks(viewingPeriodStart, 1)}
    //     showDayOfWeek={false}
    //   />
    //   <TeamWeekView
    //     teamId={teamId}
    //     workspaceId={workspaceId}
    //     viewingWeekStart={addWeeks(viewingPeriodStart, 2)}
    //     showDayOfWeek={false}
    //   />
    //   <TeamWeekView
    //     teamId={teamId}
    //     workspaceId={workspaceId}
    //     viewingWeekStart={addWeeks(viewingPeriodStart, 3)}
    //     showDayOfWeek={false}
    //   />
    // </>
    <PeriodSpanTaskView
      containerRef={containerRef}
      teamId={teamId}
      workspaceId={workspaceId}
      viewingPeriodStart={viewingPeriodStart}
      viewingPeriodEnd={viewingPeriodEnd}
      showDayOfWeek={true}
    />
  ) : null;
};

export default MonthlyPlanTab;
