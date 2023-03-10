import React from "react";
import styles from "./Tile.module.css";

interface TileProps {
  id: string;
}

const Tile: React.FC<TileProps> = ({ id }) => {
  return (
    <div className={styles.calendarLineContainer}>
      {Array.from(Array(7).keys()).map((i) => (
        <div key={`${id}-tile-${i}`} className={styles.calendarLine} />
      ))}
    </div>
  );
};

export default Tile;
