import Button, { ButtonHeight } from "@/components/button";
import { shortenStringIfMoreThanMaxLength } from "@/utils/textUtil";
import cn from "classnames";
import useTranslation from "locales/useTranslation";
import React from "react";
import { LuEye } from "react-icons/lu";
import styles from "./CalendarSourceButton.module.css";

interface CalendarSourceButtonProps {
  teamId?: string;
  calendarId?: string;
  calendarSourceId?: string;
  label: string;
  type: "TEAM" | "EXTERNAL-CAL";
}

const CalendarSourceButton: React.FC<CalendarSourceButtonProps> = ({ teamId, calendarId, calendarSourceId, label, type }) => {
  const { t } = useTranslation();

  return (
    <Button className={styles.container} heightVariant={ButtonHeight.short2x}>
      <span className={cn(styles.calendarSourceName, "single-line", type == "TEAM" && "bold")}>
        {shortenStringIfMoreThanMaxLength({
          text: label,
          maxLength: 29,
        })}
      </span>
      <div className={styles.icon} data-tooltip-right={t("sideMenuTeamMembers")}>
        <LuEye size={11} />
      </div>
    </Button>
  );
};

export default CalendarSourceButton;
