import Button, { ButtonHeight, ButtonVariants } from "@/components/button";
import { TeamDto, WorkspaceDto } from "@/model/be/jinear-core";
import { popTeamPickerModal } from "@/store/slice/modalSlice";
import { useAppDispatch } from "@/store/store";
import { addMonths, format, startOfToday } from "date-fns";
import useTranslation from "locales/useTranslation";
import React from "react";
import { IoCaretBack, IoCaretForward, IoClose, IoEllipse } from "react-icons/io5";
import { useSetViewingDate, useViewingDate } from "../context/CalendarContext";
import styles from "./CalendarHeader.module.scss";

interface CalendarHeaderProps {
  days: Date[];
  filterBy?: TeamDto;
  setFilterBy?: React.Dispatch<React.SetStateAction<TeamDto | undefined>>;
  workspace: WorkspaceDto;
  squeezedView: boolean;
  setSqueezedView: React.Dispatch<React.SetStateAction<boolean>>;
}

const CalendarHeader: React.FC<CalendarHeaderProps> = ({
  days,
  filterBy,
  setFilterBy,
  workspace,
  squeezedView,
  setSqueezedView,
}) => {
  const { t } = useTranslation();
  const dispatch = useAppDispatch();
  const viewingDate = useViewingDate();
  const setViewingDate = useSetViewingDate();
  const title = !viewingDate
    ? ""
    : format(viewingDate, "MMMM yy", {
        locale: t("dateFnsLocale") as any,
      });

  const prevMonth = () => {
    if (viewingDate) {
      setViewingDate?.(addMonths(viewingDate, -1));
    }
  };

  const nextMonth = () => {
    if (viewingDate) {
      setViewingDate?.(addMonths(viewingDate, 1));
    }
  };

  const thisMonth = () => {
    setViewingDate?.(startOfToday());
  };

  const popFilterTeamModal = () => {
    dispatch(popTeamPickerModal({ workspaceId: workspace.workspaceId, visible: true, onPick: setFilterBy }));
  };

  const clearFilter = () => {
    setFilterBy?.(undefined);
  };

  const toggleSquezeedView = () => {
    setSqueezedView(!squeezedView);
  };

  return (
    <>
      <div className={styles.mainCalendarHeader}>
        <div className={styles.headerInfoContainer}>
          <h1 className={styles.monthHeader}>{title}</h1>
          <div
            dangerouslySetInnerHTML={{
              __html: filterBy
                ? t("calendarHeaderFilteredTeamSubtitle").replace("${teamName}", filterBy.name)
                : t("calendarHeaderAllTeamsSubtitle"),
            }}
          />
        </div>
        <div className="spacer-h-1" />
        <div className={styles.calendarNavigation}>
          <Button
            heightVariant={ButtonHeight.short}
            variant={squeezedView ? ButtonVariants.filled2 : ButtonVariants.filled}
            onClick={toggleSquezeedView}
            className={styles.squeezedViewToggleButton}
          >
            {squeezedView ? <IoCaretBack /> : <IoCaretForward />}
            {!squeezedView ? <IoCaretBack /> : <IoCaretForward />}
          </Button>
          <div className="spacer-w-1" />
          <Button
            onClick={filterBy ? clearFilter : popFilterTeamModal}
            heightVariant={ButtonHeight.short}
            variant={ButtonVariants.filled}
            className={filterBy ? styles.filterButtonWithActiveFilter : undefined}
          >
            {filterBy ? (
              <>
                <IoClose />
                <div className="spacer-w-1" />
                {t("calendarClearFilterButton")}
              </>
            ) : (
              t("calendarFilterButton")
            )}
          </Button>
          <div className="spacer-w-2" />

          <Button onClick={prevMonth}>
            <IoCaretBack />
          </Button>
          <Button onClick={thisMonth}>
            <IoEllipse size={7} />
          </Button>
          <Button onClick={nextMonth}>
            <IoCaretForward />
          </Button>
        </div>
      </div>
    </>
  );
};

export default CalendarHeader;
