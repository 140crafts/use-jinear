import HorizontalLine from "@/components/line/horizontalLine/HorizontalLine";
import cn from "classnames";
import useTranslation from "locales/useTranslation";
import React from "react";
import styles from "./OverviewTaskNumbers.module.scss";

interface OverviewTaskNumbersProps {
  className?: string;
  totalOpenTaskCount: number;
  totalClosedTaskCount: number;
  totalTaskCount: number;
}

const OverviewTaskNumbers: React.FC<OverviewTaskNumbersProps> = ({
  className,
  totalOpenTaskCount = 0,
  totalClosedTaskCount = 0,
  totalTaskCount = 0,
}) => {
  const { t } = useTranslation();
  return (
    <div className={cn(className, styles.container)}>
      <span>{t("taskNumbersOverviewTtitle")}</span>

      <div className={styles.infoContainer}>
        <div className={styles.numberInfoContainer}>
          <div className={styles.text}>{t("taskNumbersOverviewTotalOpenTaskCount")}</div>
          <div className={styles.number}>{totalOpenTaskCount}</div>
        </div>
        <HorizontalLine className={styles.horizontalLine} />
        <div className={styles.numberInfoContainer}>
          <div className={styles.text}>{t("taskNumbersOverviewTotalClosedTaskCount")}</div>
          <div className={styles.number}>{totalClosedTaskCount}</div>
        </div>
        <HorizontalLine className={styles.horizontalLine} />
        <div className={styles.numberInfoContainer}>
          <div className={styles.text}>{t("taskNumbersOverviewTotalTaskCount")}</div>
          <div className={styles.number}>{totalTaskCount}</div>
        </div>
      </div>
    </div>
  );
};

export default OverviewTaskNumbers;
