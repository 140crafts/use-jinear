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
      <span
        className={progressClassName}
        style={{
          display: "inline-block",
          width: size,
          height: size,
          border: "2px solid currentColor",
          borderTopColor: "transparent",
          borderRadius: "50%",
          animation: "jinear-spin 0.7s linear infinite",
        }}
      />
    </div>
  );
};

export default CircularLoading;
