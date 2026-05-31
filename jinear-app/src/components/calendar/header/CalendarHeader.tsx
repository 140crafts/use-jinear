import Button from "@/components/button";
import {
    queryStateAnyToStringConverter,
    queryStateDateToShortDateConverter,
    queryStateShortDateParser,
    useQueryState,
    useSetQueryState,
} from "@/hooks/useQueryState";
import Logger from "@/util/logger";
import {addDays, addMonths, addWeeks, format, startOfDay, startOfMonth, startOfToday, startOfWeek} from "date-fns";
import strings from "@/locales/strings";
import useTranslation, {type StringKeys} from "@/locales/useTranslation";
import React from "react";
import {IoCaretBack, IoCaretForward, IoEllipse} from "react-icons/io5";
import {LuCalendar} from "react-icons/lu";
import styles from "./CalendarHeader.module.scss";
import {type CalendarViewType, storeCalendarViewType} from "@/components/calendar/calendarUtils";

interface CalendarHeaderProps {
}

interface ICalendarViewType {
    label: StringKeys;
    value: "d" | "2d" | "w" | "m";
}

const viewTypes: ICalendarViewType[] = [
    {label: "calendarViewTypeSegment_Day", value: "d"},
    {label: "calendarViewTypeSegment_2Day", value: "2d"},
    {label: "calendarViewTypeSegment_Week", value: "w"},
    {label: "calendarViewTypeSegment_Month", value: "m"},
];

const logger = Logger("CalendarHeader");
const CalendarHeader: React.FC<CalendarHeaderProps> = ({}) => {
    const {t, dateFnsLocale} = useTranslation();
    const setQueryState = useSetQueryState();
    const viewingDate = useQueryState<Date>("viewingDate", queryStateShortDateParser) || startOfDay(new Date());
    const viewType = useQueryState<CalendarViewType>("viewType");

    const setViewingDate = (viewingDate: Date) => {
        logger.log({setViewingDate: viewingDate});
        setQueryState("viewingDate", queryStateDateToShortDateConverter(viewingDate));
    };

    const setViewType = (viewType: CalendarViewType) => {
        setQueryState("viewType", queryStateAnyToStringConverter(viewType));
        storeCalendarViewType(queryStateAnyToStringConverter(viewType));
    };

    const title =
        viewingDate == null
            ? ""
            : format(viewingDate, "MMMM yy", {
                locale: dateFnsLocale,
            });

    const prevPeriod = () => {
        if (viewingDate) {
            const prev =
                viewType == "m"
                    ? addMonths(startOfMonth(viewingDate), -1)
                    : viewType == "w"
                        ? addWeeks(startOfWeek(viewingDate, {weekStartsOn: 1}), -1)
                        : viewType == "d"
                            ? addDays(startOfDay(viewingDate), -1)
                            : addDays(startOfDay(viewingDate), -2);
            setViewingDate?.(prev);
        }
    };

    const nextPeriod = () => {
        if (viewingDate) {
            const next =
                viewType == "m"
                    ? addMonths(startOfMonth(viewingDate), 1)
                    : viewType == "w"
                        ? addWeeks(startOfWeek(viewingDate, {weekStartsOn: 1}), 1)
                        : viewType == "d"
                            ? addDays(startOfDay(viewingDate), 1)
                            : addDays(startOfDay(viewingDate), 2);
            logger.log({nextPeriod: next});
            setViewingDate?.(next);
        }
    };

    const today = () => {
        setViewingDate?.(startOfToday());
    };

    const changeViewType = (event: React.ChangeEvent<HTMLSelectElement>) => {
        const value = event.target.value;
        if (value && (value == "m" || value == "w" || value == "d" || value == "2d")) {
            setViewType?.(value);
        }
    };

    return (
        <>
            <div className={styles.mainCalendarHeader}>

                <div className={styles.headerInfoContainer}>
                    <div className={styles.headerLabelContainer}>
                        <h1 className={styles.monthHeader}>{title}</h1>
                    </div>
                </div>

                <div className={styles.calendarNavigation}>

                    <div className={styles.viewTypeContainer}>
                        <label htmlFor="calendar-view-type-select" className={styles.viewTypeIconContainer}>
                            <LuCalendar size={11} className={'icon'}/>
                        </label>

                        <select
                            id="calendar-view-type-select"
                            className={styles.viewTypeSelect}
                            onChange={changeViewType}
                            value={viewType || "m"}
                        >
                            {viewTypes.map((viewType) => (
                                <option key={`calendar-veiw-type-${viewType.value}`} value={viewType.value}>
                                    {t(viewType.label)}
                                </option>
                            ))}
                        </select>
                    </div>

                    <div className="spacer-w-1"/>

                    <Button onClick={prevPeriod}>
                        <IoCaretBack/>
                    </Button>
                    <Button onClick={today}>
                        <IoEllipse size={7}/>
                    </Button>
                    <Button onClick={nextPeriod}>
                        <IoCaretForward/>
                    </Button>
                </div>
            </div>

        </>
    );
};

export default CalendarHeader;
