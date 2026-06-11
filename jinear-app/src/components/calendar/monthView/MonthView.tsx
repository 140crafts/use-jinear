import {queryStateArrayParser, queryStateShortDateParser, useQueryState} from "@/hooks/useQueryState";
import type {WorkspaceDto} from "@/model/be/jinear-core";
import {useFilterCalendarEventsQuery} from "@/store/api/calendarEventApi";
import Logger from "@/util/logger";
import {startOfDay} from "date-fns";
import React, {useMemo} from "react";
import {calculateHitMissTable, computeMonthViewPeriod, type ICalendarWeekRowCell} from "../calendarUtils";
import OverlayLoading from "../common/overlayLoading/OverlayLoading";
import {useCalenderLoading, useGhostEvent, useSqueezedView} from "../context/CalendarContext";
import Month from "./month/Month";

interface MonthViewProps {
    workspace: WorkspaceDto;
}

const logger = Logger("MonthView");

const EMPTY_ARRAY: string[] = [];
const EMPTY_MONTH_TABLE: ICalendarWeekRowCell[][][] = [[], [], [], [], []];

const MonthView: React.FC<MonthViewProps> = ({workspace}) => {
    const squeezedView = useSqueezedView();
    const hiddenCalendars = useQueryState<string[]>("hiddenCalendars", queryStateArrayParser) || EMPTY_ARRAY;
    const hiddenTeams = useQueryState<string[]>("hiddenTeams", queryStateArrayParser) || EMPTY_ARRAY;
    const taskBoards = useQueryState<string[]>("taskBoards", queryStateArrayParser) || EMPTY_ARRAY;

    const defaultDate = useMemo(() => startOfDay(new Date()), []);
    const viewingDate = useQueryState<Date>("viewingDate", queryStateShortDateParser) || defaultDate;

    const {periodStart, periodEnd, days} = useMemo(() => computeMonthViewPeriod(viewingDate), [viewingDate]);

    const ghostEvent = useGhostEvent();
    const calendarLoading = useCalenderLoading();

    const {data: filterResponse, isFetching} = useFilterCalendarEventsQuery({
        workspaceId: workspace?.workspaceId || "",
        taskboardIds: taskBoards,
        timespanStart: periodStart,
        timespanEnd: periodEnd
    });

    const monthTable: ICalendarWeekRowCell[][][] | undefined = useMemo(() => {
        if (!filterResponse?.data) {
            return;
        }
        const responseEvents = filterResponse.data.filter((val) => {
            const lookUpSource = val.calendarEventSourceType == "TASK" ? hiddenTeams : hiddenCalendars;
            const lookUpValue =
                val.calendarEventSourceType == "TASK" ? val.relatedTask?.teamId : val.externalCalendarSourceDto?.externalCalendarSourceId;
            return lookUpSource.findIndex((value) => value == lookUpValue) == -1;
        });

        const events = [...responseEvents];
        if (ghostEvent) {
            events.unshift(ghostEvent);
        }

        return calculateHitMissTable({events, days}) ?? EMPTY_MONTH_TABLE;
    }, [days, filterResponse, ghostEvent, hiddenTeams, hiddenCalendars]);

    return (
        <>
            <Month monthTable={monthTable ?? EMPTY_MONTH_TABLE} days={days} squeezedView={squeezedView}/>
            <OverlayLoading isFetching={isFetching || calendarLoading}/>
        </>
    );
};

export default MonthView;
