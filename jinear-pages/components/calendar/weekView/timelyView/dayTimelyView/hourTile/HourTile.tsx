import React from "react";
import styles from "./HourTile.module.css";

interface HourTileProps {
  topLabel?: string;
  bottomLabel?: string;
  onClick?: () => void;
  topClassName?: string;
}

import Logger from "@/utils/logger";
import cn from "classnames";

const logger = Logger("HourTile");
const HourTile: React.FC<HourTileProps> = ({ topLabel, bottomLabel, onClick, topClassName }) => {
  const _onClick = () => {
    logger.log({ msg: "HourTile click", onClick });
    onClick?.();
  };
  return (
    <div className={cn(styles.container, onClick && styles.clickable)} onClick={_onClick}>
      {topLabel && <div className={cn(styles.label, styles.top, topClassName)}>{topLabel}</div>}
      {bottomLabel && <div className={cn(styles.label, styles.bottom)}>{bottomLabel}</div>}
    </div>
  );
};

export default HourTile;
