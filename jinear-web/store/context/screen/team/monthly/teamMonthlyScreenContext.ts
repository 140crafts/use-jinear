import { createContext, useContext } from "react";

interface ITeamMonthlyScreenContext {
  viewingPeriodOf?: Date;
  viewingPeriodStart?: Date;
  viewingPeriodEnd?: Date;
  setViewingPeriodOf?: React.Dispatch<React.SetStateAction<Date>>;
}

const TeamMonthlyScreenContext = createContext<ITeamMonthlyScreenContext>({});

export default TeamMonthlyScreenContext;

export function useSetViewingPeriodOf() {
  const ctx = useContext(TeamMonthlyScreenContext);
  return ctx.setViewingPeriodOf;
}

export function useViewingPeriodStart() {
  const ctx = useContext(TeamMonthlyScreenContext);
  return ctx.viewingPeriodStart;
}

export function useViewingPeriodEnd() {
  const ctx = useContext(TeamMonthlyScreenContext);
  return ctx.viewingPeriodEnd;
}

export function useViewingPeriodOf() {
  const ctx = useContext(TeamMonthlyScreenContext);
  return ctx.viewingPeriodOf;
}
