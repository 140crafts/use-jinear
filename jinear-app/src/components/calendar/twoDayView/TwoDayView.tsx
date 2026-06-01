import {queryStateShortDateParser, useQueryState} from "@/hooks/useQueryState";
import type {WorkspaceDto} from "@/model/be/jinear-core";
import {addDays, endOfDay, startOfDay} from "date-fns";
import React, {useMemo} from "react";
import DayspanTimelyView from "../common/dayspanTimelyView/DayspanTimelyView";
import {useGhostEvent} from "../context/CalendarContext";

interface TwoDayViewProps {
    workspace: WorkspaceDto;
}

const TwoDayView: React.FC<TwoDayViewProps> = ({workspace}) => {
    const ghostEvent = useGhostEvent();
    const viewingDate = useQueryState<Date>("viewingDate", queryStateShortDateParser) || startOfDay(new Date());

    const periodStart = viewingDate;
    const {periodEnd, days} = useMemo(() => {
        const periodEnd = endOfDay(addDays(viewingDate, 1));
        const days = [periodStart, periodEnd];
        return {periodEnd, days};
    }, [viewingDate]);

    return (
        <DayspanTimelyView
            workspace={workspace}
            ghostEvent={ghostEvent}
            viewingDate={viewingDate}
            periodStart={periodStart}
            periodEnd={periodEnd}
            days={days}
        />
    );
};

export default TwoDayView;
