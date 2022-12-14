import { useViewingWeekStart } from "@/store/context/screen/team/weekly/teamWeeklyScreenContext";
import {
  endOfWeek,
  format,
  getISOWeek,
  getWeek,
  isSameYear,
  startOfWeek,
} from "date-fns";
import useTranslation from "locales/useTranslation";
import React from "react";
import DateControl from "./dateControl/DateControl";
import styles from "./YearWeekNo.module.css";

interface YearWeekNoProps {}

const options = { weekStartsOn: 1 } as any;

const YearWeekNo: React.FC<YearWeekNoProps> = ({}) => {
  const { t } = useTranslation();
  const date = useViewingWeekStart();
  const weekStart = startOfWeek(date, options);
  const weekEnd = endOfWeek(date, options);

  const isLastWeekOrFirstWeekOfTheYear = !isSameYear(weekStart, weekEnd);
  const isLastMonth = format(date, "L") == "12";

  const week = getWeek(date, options);
  const isoWeek = getISOWeek(date);

  const humanlyWeekNo =
    isLastMonth && isLastWeekOrFirstWeekOfTheYear ? isoWeek : week;

  const title = t("teamWeeklyScreenWeekNoTitle").replace(
    "{weekNo}",
    isoWeek.toString()
  );

  return (
    <div className={styles.container}>
      <div className={styles.titleContainer}>
        <div className={styles.title}>{title}</div>
        <DateControl />
      </div>

      <div className={styles.weekInfo}>
        {`${format(weekStart, t("dateFormat"))} 
        ${t("teamWeeklyScreenWeekStartEndTo")}
         ${format(weekEnd, t("dateFormat"))}`}
      </div>
    </div>
  );
};

export default YearWeekNo;
