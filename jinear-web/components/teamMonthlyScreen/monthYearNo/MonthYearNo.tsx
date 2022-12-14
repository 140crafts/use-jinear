import {
  useViewingPeriodEnd,
  useViewingPeriodOf,
  useViewingPeriodStart,
} from "@/store/context/screen/team/monthly/teamMonthlyScreenContext";
import { format } from "date-fns";
import useTranslation from "locales/useTranslation";
import React from "react";
import DateControl from "./dateControl/DateControl";
import styles from "./MonthYearNo.module.css";

interface MonthYearNoProps {}

const MonthYearNo: React.FC<MonthYearNoProps> = ({}) => {
  const { t } = useTranslation();
  const viewingPeriodOf = useViewingPeriodOf();
  const viewingPeriodStart = useViewingPeriodStart();
  const viewingPeriodEnd = useViewingPeriodEnd();
  const title = !viewingPeriodOf
    ? ""
    : format(viewingPeriodOf, "MMMM yy", {
        locale: t("dateFnsLocale") as any,
      });

  return (
    <div className={styles.container}>
      <div className={styles.titleContainer}>
        <div className={styles.title}>{title}</div>
        <DateControl />
      </div>

      <div className={styles.dateInfo}>
        {viewingPeriodStart &&
          viewingPeriodEnd &&
          `${format(viewingPeriodStart, t("dateFormat"))} 
    ${t("teamMonthlyScreenPeriodStartEndTo")}
     ${format(viewingPeriodEnd, t("dateFormat"))}`}
      </div>
    </div>
  );
};

export default MonthYearNo;
