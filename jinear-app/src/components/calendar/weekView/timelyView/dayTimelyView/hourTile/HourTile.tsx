import React from "react";
import styles from "./HourTile.module.css";
import Logger from "@/util/logger";
import cn from "classnames";
import {useSetDraggingOnHourTile} from "@/components/calendar/context/CalendarContext.ts";
import {setHours, setMinutes} from "date-fns";

interface HourTileProps {
    topLabel?: string;
    bottomLabel?: string;
    onClick?: () => void;
    topClassName?: string;
    day?: Date;
    hour?: number;
}

const logger = Logger("HourTile");
export const HOUR_TILE_RESOLUTION = 4;

const HourTile: React.FC<HourTileProps> = ({day, hour, topLabel, bottomLabel, onClick, topClassName}) => {
    const setDraggingOnHourTile = useSetDraggingOnHourTile();

    const _onClick = () => {
        logger.log({msg: "HourTile click", onClick});
        onClick?.();
    };

    const _onDragEnter = (event: React.DragEvent, i: number) => {
        if (day && hour && setDraggingOnHourTile) {
            const minute = i * (60 / HOUR_TILE_RESOLUTION);
            const nextHour = setMinutes(setHours(new Date(day), hour), minute);
            setDraggingOnHourTile(nextHour);
        }
    };

    const _onDragLeave = (event: React.DragEvent) => {
        if (setDraggingOnHourTile) {
            setDraggingOnHourTile(undefined);
        }
    };

    return (
        <div
            className={cn(styles.container, onClick && styles.clickable)}
            onClick={_onClick}
        >
            {topLabel && <div className={cn(styles.label, styles.top, topClassName)}>{topLabel}</div>}
            {bottomLabel && <div className={cn(styles.label, styles.bottom)}>{bottomLabel}</div>}
            <div className={styles.hourResolutionContainer}>
                {[...new Array(HOUR_TILE_RESOLUTION)].map((_, i) => (
                    <div
                        key={`${day}-${hour}-${i}-resolution`}
                        className={styles.hourResolution}
                        onDragEnter={event => _onDragEnter(event, i)}
                        onDragLeave={_onDragLeave}
                    >
                    </div>))}
            </div>
        </div>
    );
};

export default HourTile;
