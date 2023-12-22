import React from "react";
import styles from "./Tile.module.css";

interface TileProps {
  topLabel?: string;
  bottomLabel?: string;
  onClick?: () => void;
  topClassName?: string;
}

import Logger from "@/utils/logger";
import cn from "classnames";

const logger = Logger("Tile");
const Tile: React.FC<TileProps> = ({ topLabel, bottomLabel, onClick, topClassName }) => {
  const _onClick = () => {
    logger.log({ msg: "tile click", onClick });
    onClick?.();
  };
  return (
    <div className={cn(styles.container, onClick && styles.clickable)} onClick={_onClick}>
      {topLabel && <div className={cn(styles.label, styles.top, topClassName)}>{topLabel}</div>}
      {bottomLabel && <div className={cn(styles.label, styles.bottom)}>{bottomLabel}</div>}
    </div>
  );
};

export default Tile;
