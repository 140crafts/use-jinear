import TeamWeekView from "@/components/teamWeekView/TeamWeekView";
import React from "react";

interface WeeklyPlanTabProps {
  teamId?: string;
  workspaceId?: string;
  viewingWeekStart: Date;
  containerRef?: React.RefObject<HTMLDivElement>;
}

const WeeklyPlanTab: React.FC<WeeklyPlanTabProps> = ({
  teamId,
  workspaceId,
  viewingWeekStart,
  containerRef,
}) => {
  return teamId && workspaceId ? (
    <TeamWeekView
      teamId={teamId}
      workspaceId={workspaceId}
      viewingWeekStart={viewingWeekStart}
      containerRef={containerRef}
    />
  ) : null;
};

export default WeeklyPlanTab;
