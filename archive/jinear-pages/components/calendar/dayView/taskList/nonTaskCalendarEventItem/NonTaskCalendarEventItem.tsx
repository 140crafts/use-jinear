import Button from "@/components/button";
import { CalendarEventDto } from "@/model/be/jinear-core";
import { popCalendarExternalEventViewModal } from "@/store/slice/modalSlice";
import { useAppDispatch } from "@/store/store";
import cn from "classnames";
import React from "react";
import { IoCalendarOutline } from "react-icons/io5";
import styles from "./NonTaskCalendarEventItem.module.scss";
import useTranslation from "@/locals/useTranslation";

interface NonTaskCalendarEventItemProps {
  calendarEvent: CalendarEventDto;
  className?: string;
  withBottomBorderLine?: boolean;
}

const NonTaskCalendarEventItem: React.FC<NonTaskCalendarEventItemProps> = ({
                                                                             calendarEvent,
                                                                             className,
                                                                             withBottomBorderLine = true
                                                                           }) => {
  const { t } = useTranslation();
  const dispatch = useAppDispatch();

  const openCalendarExternalEventOverviewModal = () => {
    if (calendarEvent) {
      dispatch(popCalendarExternalEventViewModal({ calendarEventDto: calendarEvent, visible: true }));
    }
  };

  return (
    <div className={cn(styles.container, withBottomBorderLine ? styles.bottomBorderLine : null, className)}>
      <div className={styles.leftContainer}>
        <div className={styles.iconContainer}>
          <IoCalendarOutline size={14} />
        </div>
      </div>
      <Button className={styles.button} onClick={openCalendarExternalEventOverviewModal}>
        {calendarEvent && (calendarEvent.title ? calendarEvent.title : t("calendarTitleNotProvided"))}
      </Button>
    </div>
  );
};

export default NonTaskCalendarEventItem;
