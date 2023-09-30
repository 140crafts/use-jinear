import React from "react";
import styles from "./Tile.module.css";

interface TileProps {
  topLabel?: string;
  bottomLabel?: string;
  onClick?: () => void;
}

import Logger from "@/utils/logger";
import cn from "classnames";

const logger = Logger("Tile");
const Tile: React.FC<TileProps> = ({ topLabel, bottomLabel, onClick }) => {
  const _onClick = () => {
    logger.log("tile click");
    onClick?.();
  };
  return (
    <div className={styles.container} onClick={_onClick}>
      {topLabel && <div className={cn(styles.label, styles.top)}>{topLabel}</div>}
      {bottomLabel && <div className={cn(styles.label, styles.bottom)}>{bottomLabel}</div>}
    </div>
  );
};

export default Tile;
