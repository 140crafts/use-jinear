import cn from "classnames";
import Link from "next/link";
import React from "react";
import styles from "./SingleNumberCard.module.css";

interface SingleNumberCardProps {
  className?: string;
  title: string;
  number: number;
  href: string;
}

const SingleNumberCard: React.FC<SingleNumberCardProps> = ({ className, title, number, href }) => {
  return (
    <Link className={cn(className, styles.container)} href={href}>
      <span className={styles.text}>{title}</span>
      <div className={styles.number}>{number}</div>
    </Link>
  );
};

export default SingleNumberCard;
