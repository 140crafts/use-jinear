import {queryStateShortDateParser, useQueryState} from "@/hooks/useQueryState";
import type {WorkspaceDto} from "@/model/be/jinear-core";
import {eachDayOfInterval, endOfWeek, startOfDay, startOfWeek} from "date-fns";
import React, {useMemo} from "react";
import DayspanTimelyView from "../common/dayspanTimelyView/DayspanTimelyView";
import {useGhostEvent} from "../context/CalendarContext";

interface WeekViewProps {
    workspace: WorkspaceDto;
}

const WeekView: React.FC<WeekViewProps> = ({workspace}) => {
    const defaultDate = useMemo(() => startOfDay(new Date()), []);
    const viewingDate = useQueryState<Date>("viewingDate", queryStateShortDateParser) || defaultDate;

    const {periodStart,periodEnd,days} = useMemo(() => {
        const periodStart = startOfWeek(viewingDate, {weekStartsOn: 1});
        const periodEnd = endOfWeek(viewingDate, {weekStartsOn: 1});
        const days = eachDayOfInterval({start: periodStart, end: periodEnd});
        return {periodStart, periodEnd, days};
    }, [viewingDate]);

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
