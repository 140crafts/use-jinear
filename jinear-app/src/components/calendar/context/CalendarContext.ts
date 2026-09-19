import type {CalendarEventDto, TeamDto, WorkspaceDto} from "@/model/be/jinear-core";
import {createContext, useContext} from "react";
import type {ICalendarDayRowCell} from "@/components/calendar/calendarUtils.ts";

interface ICalendarContext {
    highlightedEventId: string;
    setHighlightedEventId?: React.Dispatch<React.SetStateAction<string>>;

    squeezedView: boolean;
    setSqueezedView?: React.Dispatch<React.SetStateAction<boolean>>;

    workspace?: WorkspaceDto;
    newTasksFromTeam?: TeamDto;

    draggingEvent?: CalendarEventDto;
    setDraggingEvent?: React.Dispatch<React.SetStateAction<CalendarEventDto | undefined>>;

    ghostEvent?: CalendarEventDto;
    setGhostEvent?: React.Dispatch<React.SetStateAction<CalendarEventDto | undefined>>;

    calenderLoading?: boolean;
    setCalenderLoading?: React.Dispatch<React.SetStateAction<boolean>>;

    dayTimelyViewDraggingEvent?: ICalendarDayRowCell;
    setDayTimelyViewDraggingEvent?: React.Dispatch<React.SetStateAction<ICalendarDayRowCell | undefined>>;
    draggingOnHourTile?: Date
    setDraggingOnHourTile?: React.Dispatch<React.SetStateAction<Date | undefined>>;
}

const CalendarContext = createContext<ICalendarContext>({
    highlightedEventId: "",
    squeezedView: true,
    workspace: undefined,
    newTasksFromTeam: undefined,
    ghostEvent: undefined,
    setGhostEvent: undefined,
    calenderLoading: false,
    setCalenderLoading: undefined,
    dayTimelyViewDraggingEvent: undefined,
    setDayTimelyViewDraggingEvent: undefined,
    draggingOnHourTile: undefined,
    setDraggingOnHourTile: undefined,
});

export default CalendarContext;

export function useHighlightedEventId() {
    const ctx = useContext(CalendarContext);
    return ctx.highlightedEventId;
}

export function useSetHighlightedEventId() {
    const ctx = useContext(CalendarContext);
    return ctx.setHighlightedEventId;
}

export function useSqueezedView() {
    const ctx = useContext(CalendarContext);
    return ctx.squeezedView;
}

export function useSetSqueezedView() {
    const ctx = useContext(CalendarContext);
    return ctx.setSqueezedView;
}

export function useCalendarWorkspace() {
    const ctx = useContext(CalendarContext);
    return ctx.workspace;
}

export function useCalendarNewTaskFromTeam() {
    const ctx = useContext(CalendarContext);
    return ctx.newTasksFromTeam;
}

export function useDraggingEvent() {
    const ctx = useContext(CalendarContext);
    return ctx.draggingEvent;
}

export function useSetDraggingEvent() {
    const ctx = useContext(CalendarContext);
    return ctx.setDraggingEvent;
}

export function useGhostEvent() {
    const ctx = useContext(CalendarContext);
    return ctx.ghostEvent;
}

export function useSetGhostEvent() {
    const ctx = useContext(CalendarContext);
    return ctx.setGhostEvent;
}

export function useCalenderLoading() {
    const ctx = useContext(CalendarContext);
    return ctx.calenderLoading || false;
}

export function useSetCalenderLoading() {
    const ctx = useContext(CalendarContext);
    return ctx.setCalenderLoading;
}

export function useDayTimelyViewDraggingEvent() {
    const ctx = useContext(CalendarContext);
    return ctx.dayTimelyViewDraggingEvent;
}

export function useSetDayTimelyViewDraggingEvent() {
    const ctx = useContext(CalendarContext);
    return ctx.setDayTimelyViewDraggingEvent;
}

export function useDraggingOnHourTile() {
    const ctx = useContext(CalendarContext);
    return ctx.draggingOnHourTile;
}

export function useSetDraggingOnHourTile() {
    const ctx = useContext(CalendarContext);
    return ctx.setDraggingOnHourTile;
}

