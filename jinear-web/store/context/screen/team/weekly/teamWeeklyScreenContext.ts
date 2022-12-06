import { startOfToday, startOfWeek } from "date-fns";
import { createContext, useContext } from "react";

interface ITeamWeeklyScreenContext {
  viewingWeekStart: Date;
}

const TeamWeeklyScreenContext = createContext<ITeamWeeklyScreenContext>({
  viewingWeekStart: startOfWeek(startOfToday(), { weekStartsOn: 1 }),
});

export default TeamWeeklyScreenContext;

export function useViewingWeekStart() {
  const ctx = useContext(TeamWeeklyScreenContext);
  return ctx.viewingWeekStart;
}
