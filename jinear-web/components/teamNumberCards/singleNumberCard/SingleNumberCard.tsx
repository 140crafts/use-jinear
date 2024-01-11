import cn from "classnames";
import React from "react";
import styles from "./SingleNumberCard.module.css";

interface SingleNumberCardProps {
  className?: string;
  title: string;
  number: number;
}

const SingleNumberCard: React.FC<SingleNumberCardProps> = ({ className, title, number }) => {
  return (
    <div className={cn(className, styles.container)}>
      <span className={styles.text}>{title}</span>
      <div className={styles.number}>{number}</div>
    </div>
  );
};

export default SingleNumberCard;
