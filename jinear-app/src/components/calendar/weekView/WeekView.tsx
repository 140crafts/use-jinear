import {queryStateShortDateParser, useQueryState} from "@/hooks/useQueryState";
import type {WorkspaceDto} from "@/model/be/jinear-core";
import {startOfDay} from "date-fns";
import React, {useMemo} from "react";
import {computeWeekViewPeriod} from "../calendarUtils";
import DayspanTimelyView from "../common/dayspanTimelyView/DayspanTimelyView";
import {useGhostEvent} from "../context/CalendarContext";

interface WeekViewProps {
    workspace: WorkspaceDto;
}

const WeekView: React.FC<WeekViewProps> = ({workspace}) => {
    const defaultDate = useMemo(() => startOfDay(new Date()), []);
    const viewingDate = useQueryState<Date>("viewingDate", queryStateShortDateParser) || defaultDate;

    const {periodStart, periodEnd, days} = useMemo(() => computeWeekViewPeriod(viewingDate), [viewingDate]);

    const ghostEvent = useGhostEvent();

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

export default WeekView;
