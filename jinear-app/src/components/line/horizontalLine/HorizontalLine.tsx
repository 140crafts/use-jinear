import cn from "classnames";
import React from "react";
import styles from "./HorizontalLine.module.css";

interface HorizontalLineProps {
  className?: string;
}

const HorizontalLine: React.FC<HorizontalLineProps> = ({ className }) => {
  return <div className={cn(styles.line, className)} />;
};

export default HorizontalLine;
