import { startOfToday, startOfWeek } from "date-fns";
import { createContext, useContext } from "react";

interface ITeamWeeklyScreenContext {
  viewingWeekStart: Date;
  setViewingWeekStart?: React.Dispatch<React.SetStateAction<Date>>;
}

const TeamWeeklyScreenContext = createContext<ITeamWeeklyScreenContext>({
  viewingWeekStart: startOfWeek(startOfToday(), { weekStartsOn: 1 }),
});

export default TeamWeeklyScreenContext;

export function useViewingWeekStart() {
  const ctx = useContext(TeamWeeklyScreenContext);
  return ctx.viewingWeekStart;
}

export function useSetViewingWeekStart() {
  const ctx = useContext(TeamWeeklyScreenContext);
  return ctx.setViewingWeekStart;
}
