import cn from "classnames";
import React from "react";
import styles from "./SingleNumberCard.module.css";
import {Link} from "react-router-dom";

interface SingleNumberCardProps {
    className?: string;
    title: string;
    number: number;
    href: string;
}

const SingleNumberCard: React.FC<SingleNumberCardProps> = ({className, title, number, href}) => {
    return (
        <Link className={cn(className, styles.container)} to={href}>
            <span className={styles.text}>{title}</span>
            <div className={styles.number}>{number}</div>
        </Link>
    );
};

export default SingleNumberCard;
