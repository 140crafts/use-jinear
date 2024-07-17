import React, { useMemo } from "react";
import styles from "./ExternalCalendarAndSourcePicker.module.css";
import { CalendarDto } from "@/be/jinear-core";
import { LuCalendar, LuChevronRight } from "react-icons/lu";
import useTranslation from "@/locals/useTranslation";
import { shortenStringIfMoreThanMaxLength } from "@/utils/textUtil";

interface ExternalCalendarAndSourcePickerProps {
  calendarList: CalendarDto[],
  selectedCalendarId: string,
  selectedCalendarSourceId: string,
  onCalendarSelect: (calendarId: string) => void,
  onCalendarSourceSelect: (calendarSourceId: string) => void,
  hasNewEventText?: boolean,
  calendarChangeable?: boolean
}

const ExternalCalendarAndSourcePicker: React.FC<ExternalCalendarAndSourcePickerProps> = ({
                                                                                           calendarList,
                                                                                           selectedCalendarId,
                                                                                           selectedCalendarSourceId,
                                                                                           onCalendarSelect,
                                                                                           onCalendarSourceSelect,
                                                                                           hasNewEventText = true,
                                                                                           calendarChangeable = true
                                                                                         }) => {
  const { t } = useTranslation();
  const selectedCalendar = useMemo(() => calendarList.find(calendar => calendar.calendarId == selectedCalendarId), [JSON.stringify(calendarList), selectedCalendarId]);
  const selectedCalendarSource = useMemo(() => selectedCalendar?.calendarSources?.find(calendarSource => calendarSource.externalCalendarSourceId == selectedCalendarSourceId), [JSON.stringify(selectedCalendar), selectedCalendarSourceId]);
  const selectedCalendarNameTooltip = selectedCalendar?.name ? (selectedCalendar.name.length) > 32 ? selectedCalendar?.name : undefined : undefined;
  const selectedCalendarSourceTooltip = selectedCalendarSource?.summary ? (selectedCalendarSource.summary.length) > 32 ? selectedCalendarSource?.summary : undefined : undefined;

  const onCalendarChange = (event: React.ChangeEvent<HTMLSelectElement>) => {
    const calendarId = event.target.value;
    onCalendarSelect(calendarId);
  };

  const onChangeCalendarSourceSelect = (event: React.ChangeEvent<HTMLSelectElement>) => {
    const externalCalendarSourceId = event.target.value;
    onCalendarSourceSelect(externalCalendarSourceId);
  };

  return (
    <div className={styles.container}>

      <div className={styles.selectContainer}
           data-tooltip-multiline={selectedCalendarNameTooltip}
      >
        <LuCalendar className={styles.icon} />
        <select onChange={onCalendarChange} className={styles.calendarSelect} value={selectedCalendar?.calendarId}
                disabled={!calendarChangeable}>
          {calendarList?.map(calendar =>
            <option key={"external-calendar-and-source-picker" + calendar.calendarId} value={calendar.calendarId}>
              {shortenStringIfMoreThanMaxLength({ text: calendar.name ?? "", maxLength: 32 })}
            </option>
          )}
        </select>
      </div>

      <LuChevronRight className={styles.icon} />

      <div className={styles.selectContainer}
           data-tooltip-multiline={selectedCalendarSourceTooltip}
      >
        <select
          onChange={onChangeCalendarSourceSelect}
          value={selectedCalendarSource?.externalCalendarSourceId}
        >
          {selectedCalendar?.calendarSources?.map(calendarSource =>
            <option key={selectedCalendar?.calendarId + calendarSource.externalCalendarSourceId}
                    disabled={calendarSource.readOnly ? calendarSource.readOnly : undefined}
                    value={calendarSource.externalCalendarSourceId}>
              {shortenStringIfMoreThanMaxLength({ text: calendarSource.summary ?? "", maxLength: 32 })}
            </option>
          )}
        </select>
      </div>

      {hasNewEventText &&
        <div className={styles.container}>
          <LuChevronRight className={styles.icon} />
          <span>{t("externalCalendarAndSourcePickerNewEvent")}</span>
        </div>
      }

    </div>
  );
};

export default ExternalCalendarAndSourcePicker;