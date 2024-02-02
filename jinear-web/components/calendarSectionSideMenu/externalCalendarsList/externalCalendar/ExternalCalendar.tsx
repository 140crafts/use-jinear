import { CalendarDto, WorkspaceDto } from "@/model/be/jinear-core";
import useTranslation from "locales/useTranslation";
import React from "react";
import CalendarButton from "../../calendarButton/CalendarButton";
import CalendarSourceButton from "../../calendarSourceButton/CalendarSourceButton";
import styles from "./ExternalCalendar.module.css";

interface ExternalCalendarProps {
  workspace: WorkspaceDto;
  calendar: CalendarDto;
}

const ExternalCalendar: React.FC<ExternalCalendarProps> = ({ workspace, calendar }) => {
  const { t } = useTranslation();
  return (
    <div className={styles.container}>
      <CalendarButton calendar={calendar} workspace={workspace} />
      <div className={styles.calendarSourcesContainer}>
        {calendar.calendarSources?.map((calendarSource) => (
          <CalendarSourceButton
            key={`${calendar.calendarId}-${calendarSource.id}`}
            calendarId={calendar.calendarId}
            calendarSourceId={calendarSource.id}
            label={calendarSource.summary || t("unnamedCalendar")}
            type={"EXTERNAL-CAL"}
          />
        ))}
      </div>
    </div>
  );
};

export default ExternalCalendar;
