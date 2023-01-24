import { createContext, useContext } from "react";

interface IPeriodSpanTaskViewContext {
  viewingPeriodStart?: Date;
  viewingPeriodEnd?: Date;
  variant?: "month" | "week";
  showDayOfWeek?: boolean;
}

const PeriodSpanTaskViewContext = createContext<IPeriodSpanTaskViewContext>({});

export default PeriodSpanTaskViewContext;

export function usePeriodStart() {
  const ctx = useContext(PeriodSpanTaskViewContext);
  return ctx.viewingPeriodStart;
}

export function usePeriodEnd() {
  const ctx = useContext(PeriodSpanTaskViewContext);
  return ctx.viewingPeriodEnd;
}

export function useVariant() {
  const ctx = useContext(PeriodSpanTaskViewContext);
  return ctx.variant;
}

export function useShowDayOfWeek() {
  const ctx = useContext(PeriodSpanTaskViewContext);
  return ctx.showDayOfWeek;
}
