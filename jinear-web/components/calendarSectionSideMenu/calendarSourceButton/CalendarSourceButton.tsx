import Button, { ButtonHeight } from "@/components/button";
import { queryStateAnyToStringConverter, queryStateArrayParser, useQueryState, useSetQueryState } from "@/hooks/useQueryState";
import { shortenStringIfMoreThanMaxLength } from "@/utils/textUtil";
import cn from "classnames";
import useTranslation from "locales/useTranslation";
import React from "react";
import { LuEye, LuEyeOff } from "react-icons/lu";
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
  const setQueryState = useSetQueryState();
  const lookUpParam = type == "EXTERNAL-CAL" ? "hiddenCalendars" : "hiddenTeams";
  const lookUpValue = type == "EXTERNAL-CAL" ? calendarSourceId : teamId;
  const hiddenList = useQueryState<string[]>(lookUpParam, queryStateArrayParser) || [];
  const isHidden = hiddenList?.findIndex((val) => val == lookUpValue) != -1;

  const toggleHidden = () => {
    const nextList = isHidden ? hiddenList.filter((val) => val != lookUpValue) : [lookUpValue, ...hiddenList];
    setQueryState(lookUpParam, queryStateAnyToStringConverter(nextList));
  };

  return (
    <Button className={styles.container} heightVariant={ButtonHeight.short2x} onClick={toggleHidden}>
      <span className={cn(styles.calendarSourceName, "single-line", type == "TEAM" && "bold", isHidden && styles.hidden)}>
        {shortenStringIfMoreThanMaxLength({
          text: label,
          maxLength: 29,
        })}
      </span>
      {!isHidden ? (
        <div className={styles.icon} data-tooltip-right={t("sideMenuTeamMembers")}>
          <LuEye size={11} />
        </div>
      ) : (
        <LuEyeOff size={11} />
      )}
    </Button>
  );
};

export default CalendarSourceButton;
