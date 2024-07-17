import DateTimeInput from "@/components/dateTimeInput/DateTimeInput";
import Logger from "@/utils/logger";
import useTranslation from "locales/useTranslation";
import React from "react";
import styles from "./EventDateButtons.module.css";
import {
  IoAdd,
  IoCheckboxOutline,
  IoClose,
  IoPlaySkipBackOutline,
  IoPlaySkipForwardOutline, IoSquareOutline,
  IoTimeOutline
} from "react-icons/io5";
import cn from "classnames";

interface EventDateButtonsProps {
  className?: string;
  allDay: boolean,
  assignedDate: Date,
  dueDate: Date,
  onAssignedDateUpdate: (assignedDate: Date) => void,
  onDueDateUpdate: (dueDate: Date) => void,
  onAllDayUpdate: (allDay: boolean) => void,
  withLabels?: boolean
}

const logger = Logger("EventDateButtons2");

const EventDateButtons: React.FC<EventDateButtonsProps> = ({
                                                             allDay,
                                                             assignedDate,
                                                             dueDate,
                                                             onAssignedDateUpdate,
                                                             onDueDateUpdate,
                                                             onAllDayUpdate,
                                                             withLabels = true
                                                           }) => {
  logger.log("EventDateButtons");
  const { t } = useTranslation();

  const changeAssignedDate = (nextAssignedDate?: Date) => {
    if (nextAssignedDate) {
      onAssignedDateUpdate(nextAssignedDate);
    }
  };

  const changeDueDate = (nextDueDate?: Date) => {
    if (nextDueDate) {
      onDueDateUpdate(nextDueDate);
    }
  };

  const handleChecked = (event: React.ChangeEvent<HTMLInputElement>) => {
    onAllDayUpdate(!event.target.checked);
  };

  return (
    <div className={styles.container}>
      <div className={styles.datesContainer}>
        <div className={styles.toggleButtonContainer}>
          <input id={"all-day-checkbox"} type="checkbox" checked={allDay} onChange={handleChecked}
                 className={styles.hidden} />
          <label className={cn(styles.toggleLabel, allDay && styles.toggleLabelChecked)}
                 htmlFor={"all-day-checkbox"}>
            {allDay ? <IoCheckboxOutline size={11} /> : <IoSquareOutline size={11} /> }
            {t("calendarEventAllDayButtonLabel")}
          </label>
        </div>
        <div className={styles.dateInputContainer}>
          <DateTimeInput
            id={`calendarEvent-event-assigned-date-picker"`}
            type={allDay ? "date" : "date-time"}
            allowEmptyDate={false}
            value={assignedDate}
            setValue={changeAssignedDate}
            valuePrecise={allDay}
            contentContainerClassName={styles.contentContainerClassName}
            dateButtonClassName={styles.dateButtonClassName}
            dateInputButtonIcon={IoPlaySkipForwardOutline}
          />
        </div>
        <div className={styles.dateInputContainer}>
          <DateTimeInput
            id={`calendarEvent-event-due-date-picker"`}
            type={allDay ? "date" : "date-time"}
            allowEmptyDate={false}
            value={dueDate}
            setValue={changeDueDate}
            valuePrecise={allDay}
            contentContainerClassName={styles.contentContainerClassName}
            dateButtonClassName={styles.dateButtonClassName}
            dateInputButtonIcon={IoPlaySkipBackOutline}
          />
        </div>
      </div>
    </div>
  );
};

export default EventDateButtons;
