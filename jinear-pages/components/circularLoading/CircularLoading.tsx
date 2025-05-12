import { CircularProgress } from "@mui/material";
import cn from "classnames";
import React from "react";
import styles from "./CircularLoading.module.css";

interface CircularLoadingProps {
  containerClassName?: string;
  progressClassName?: string;
  size?: number;
}

const CircularLoading: React.FC<CircularLoadingProps> = ({ containerClassName, progressClassName, size = 14 }) => {
  return (
    <div className={cn(styles.container, containerClassName)}>
      <CircularProgress size={size} className={progressClassName} />
    </div>
  );
};

export default CircularLoading;
