import cn from "classnames";
import React from "react";
import styles from "./Line.module.css";

interface LineProps {
  className?: string;
}

const Line: React.FC<LineProps> = ({ className }) => {
  return <div className={cn(styles.line, className)} />;
};

export default Line;
