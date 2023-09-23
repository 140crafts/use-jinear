import { popNewTaskModal } from "@/store/slice/modalSlice";
import { useAppDispatch } from "@/store/store";
import cn from "classnames";
import { isToday } from "date-fns";
import React from "react";
import { useCalendarNewTaskFromTeam, useCalendarWorkspace } from "../../context/CalendarContext";
import styles from "./Tile.module.css";

interface TileProps {
  id: string;
  week: Date[];
}

const Tile: React.FC<TileProps> = ({ id, week }) => {
  const dispatch = useAppDispatch();
  const workspace = useCalendarWorkspace();
  const team = useCalendarNewTaskFromTeam();

  const popNewTaskModalWithAssignedDatePreSelected = (initialAssignedDate: Date) => {
    dispatch(popNewTaskModal({ visible: true, workspace, team, initialAssignedDate }));
  };

  return (
    <div className={styles.calendarLineContainer}>
      {Array.from(Array(7).keys()).map((i) => (
        <div
          key={`${id}-tile-${i}`}
          className={cn(styles.calendarLine, isToday(week[i]) && styles.todayCalendarLine)}
          onClick={() => popNewTaskModalWithAssignedDatePreSelected(week[i])}
        />
      ))}
    </div>
  );
};

export default Tile;
