import React from "react";
import styles from "./HourTile.module.css";
import Logger from "@/util/logger";
import cn from "classnames";

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
export const HOUR_TILE_SLOT_IN_MINUTES = 60 / HOUR_TILE_RESOLUTION;

const HourTile: React.FC<HourTileProps> = ({day, hour, topLabel, bottomLabel, onClick, topClassName}) => {
    const _onClick = () => {
        logger.log({msg: "HourTile click", onClick});
        onClick?.();
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
                    >
                    </div>))}
            </div>
        </div>
    );
};

export default HourTile;
