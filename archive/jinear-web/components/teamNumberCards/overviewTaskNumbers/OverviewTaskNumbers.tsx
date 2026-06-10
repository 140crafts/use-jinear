import HorizontalLine from "@/components/line/horizontalLine/HorizontalLine";
import cn from "classnames";
import useTranslation from "locales/useTranslation";
import Link from "next/link";
import React from "react";
import styles from "./OverviewTaskNumbers.module.scss";

interface OverviewTaskNumbersProps {
  className?: string;
  totalOpenTaskCount: number;
  totalClosedTaskCount: number;
  totalTaskCount: number;
  tasksPath: string;
  tasksFilteredWithUndoneStatus: string;
  tasksFilteredWithClosedStatus: string;
}

const OverviewTaskNumbers: React.FC<OverviewTaskNumbersProps> = ({
  className,
  totalOpenTaskCount = 0,
  totalClosedTaskCount = 0,
  totalTaskCount = 0,
  tasksPath,
  tasksFilteredWithUndoneStatus,
  tasksFilteredWithClosedStatus,
}) => {
  const { t } = useTranslation();
  return (
    <div className={cn(className, styles.container)}>
      <span>{t("taskNumbersOverviewTtitle")}</span>

      <div className={styles.infoContainer}>
        <Link className={styles.numberInfoContainer} href={tasksFilteredWithUndoneStatus}>
          <div className={styles.text}>{t("taskNumbersOverviewTotalOpenTaskCount")}</div>
          <div className={styles.number}>{totalOpenTaskCount}</div>
        </Link>
        <HorizontalLine className={styles.horizontalLine} />
        <Link className={styles.numberInfoContainer} href={tasksFilteredWithClosedStatus}>
          <div className={styles.text}>{t("taskNumbersOverviewTotalClosedTaskCount")}</div>
          <div className={styles.number}>{totalClosedTaskCount}</div>
        </Link>
        <HorizontalLine className={styles.horizontalLine} />
        <Link className={styles.numberInfoContainer} href={tasksPath}>
          <div className={styles.text}>{t("taskNumbersOverviewTotalTaskCount")}</div>
          <div className={styles.number}>{totalTaskCount}</div>
        </Link>
      </div>
    </div>
  );
};

export default OverviewTaskNumbers;
