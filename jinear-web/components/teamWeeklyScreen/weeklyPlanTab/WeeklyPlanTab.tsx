import TeamWeekView from "@/components/teamWeekView/TeamWeekView";
import React from "react";

interface WeeklyPlanTabProps {
  teamId?: string;
  workspaceId?: string;
  viewingWeekStart: Date;
}

const WeeklyPlanTab: React.FC<WeeklyPlanTabProps> = ({
  teamId,
  workspaceId,
  viewingWeekStart,
}) => {
  return teamId && workspaceId ? (
    <TeamWeekView
      teamId={teamId}
      workspaceId={workspaceId}
      viewingWeekStart={viewingWeekStart}
    />
  ) : null;
};

export default WeeklyPlanTab;
