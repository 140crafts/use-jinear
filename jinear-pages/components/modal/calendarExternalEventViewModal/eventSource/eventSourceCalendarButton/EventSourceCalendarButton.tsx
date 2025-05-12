import Button, { ButtonHeight, ButtonVariants } from "@/components/button";
import { CalendarEventDto } from "@/model/be/jinear-core";
import useTranslation from "locales/useTranslation";
import React from "react";
import { IoCalendarOutline } from "react-icons/io5";

interface EventSourceCalendarButtonProps {
  className?: string;
  calendarEvent: CalendarEventDto;
}

const EventSourceCalendarButton: React.FC<EventSourceCalendarButtonProps> = ({ className, calendarEvent }) => {
  const { t } = useTranslation();

  return (
    <div>
      <Button
        //onClick={changeSourceCalendar}
        variant={ButtonVariants.filled}
        heightVariant={ButtonHeight.short}
        className={className}
        data-tooltip-right={t("calendarEventCalendarExternalSourceChange")}
      >
        <IoCalendarOutline size={14} />
        {calendarEvent.externalCalendarSourceDto?.summary || ""}
      </Button>
    </div>
  );
};

export default EventSourceCalendarButton;
