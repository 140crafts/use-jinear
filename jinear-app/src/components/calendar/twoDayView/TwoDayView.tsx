import {queryStateShortDateParser, useQueryState} from "@/hooks/useQueryState";
import type {WorkspaceDto} from "@/model/be/jinear-core";
import {startOfDay} from "date-fns";
import React, {useMemo} from "react";
import {computeTwoDayViewPeriod} from "../calendarUtils";
import DayspanTimelyView from "../common/dayspanTimelyView/DayspanTimelyView";
import {useGhostEvent} from "../context/CalendarContext";

interface TwoDayViewProps {
    workspace: WorkspaceDto;
}

const TwoDayView: React.FC<TwoDayViewProps> = ({workspace}) => {
    const ghostEvent = useGhostEvent();
    const viewingDate = useQueryState<Date>("viewingDate", queryStateShortDateParser) || startOfDay(new Date());

    const {periodStart, periodEnd, days} = useMemo(() => computeTwoDayViewPeriod(viewingDate), [viewingDate]);

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
