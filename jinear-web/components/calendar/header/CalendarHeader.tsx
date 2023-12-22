import Button, { ButtonHeight, ButtonVariants } from "@/components/button";
import SegmentedControl from "@/components/segmentedControl/SegmentedControl";
import { WorkspaceDto } from "@/model/be/jinear-core";
import { popTeamPickerModal } from "@/store/slice/modalSlice";
import { useAppDispatch } from "@/store/store";
import Logger from "@/utils/logger";
import { addDays, addMonths, addWeeks, format, startOfDay, startOfToday, startOfWeek } from "date-fns";
import useTranslation from "locales/useTranslation";
import React from "react";
import { IoCaretBack, IoCaretForward, IoClose, IoEllipse, IoScan } from "react-icons/io5";
import {
  useFilterBy,
  useSetFilterBy,
  useSetSqueezedView,
  useSetViewType,
  useSetViewingDate,
  useSqueezedView,
  useViewType,
  useViewingDate,
} from "../context/CalendarContext";
import styles from "./CalendarHeader.module.scss";

interface CalendarHeaderProps {
  workspace: WorkspaceDto;
}

const logger = Logger("CalendarHeader");
const CalendarHeader: React.FC<CalendarHeaderProps> = ({ workspace }) => {
  const { t } = useTranslation();
  const dispatch = useAppDispatch();
  const filterBy = useFilterBy();
  const setFilterBy = useSetFilterBy();
  const viewingDate = useViewingDate();
  const setViewingDate = useSetViewingDate();
  const squeezedView = useSqueezedView();
  const setSqueezedView = useSetSqueezedView();
  const viewType = useViewType();
  const setViewType = useSetViewType();

  const title = !viewingDate
    ? ""
    : format(viewingDate, "MMMM yy", {
        locale: t("dateFnsLocale") as any,
      });

  const prevPeriod = () => {
    if (viewingDate) {
      const prev =
        viewType == "m"
          ? addMonths(viewingDate, -1)
          : viewType == "w"
          ? addWeeks(startOfWeek(viewingDate, { weekStartsOn: 1 }), -1)
          : viewType == "d"
          ? addDays(startOfDay(viewingDate), -1)
          : addDays(startOfDay(viewingDate), -2);
      setViewingDate?.(prev);
    }
  };

  const nextPeriod = () => {
    if (viewingDate) {
      const next =
        viewType == "m"
          ? addMonths(viewingDate, 1)
          : viewType == "w"
          ? addWeeks(startOfWeek(viewingDate, { weekStartsOn: 1 }), 1)
          : viewType == "d"
          ? addDays(startOfDay(viewingDate), 1)
          : addDays(startOfDay(viewingDate), 2);
      logger.log({ nextPeriod: next });
      setViewingDate?.(next);
    }
  };

  const today = () => {
    setViewingDate?.(startOfToday());
  };

  const popFilterTeamModal = () => {
    dispatch(popTeamPickerModal({ workspaceId: workspace.workspaceId, visible: true, onPick: setFilterBy }));
  };

  const clearFilter = () => {
    setFilterBy?.(undefined);
  };

  const toggleSquezeedView = () => {
    setSqueezedView?.(!squeezedView);
  };

  const changeViewType = (value: string, index: number) => {
    if (value && (value == "m" || value == "w" || value == "d" || value == "2d")) {
      setViewType?.(value);
    }
  };

  return (
    <>
      <div className={styles.mainCalendarHeader}>
        <div className={styles.headerInfoContainer}>
          <div className={styles.headerLabelContainer}>
            <h1 className={styles.monthHeader}>{title}</h1>
            <SegmentedControl
              name="calendar-view-type-segment-control"
              defaultIndex={["d", "2d", "w", "m"].indexOf(viewType)}
              segments={[
                { label: t("calendarViewTypeSegment_Day"), value: "d" },
                { label: t("calendarViewTypeSegment_2Day"), value: "2d" },
                { label: t("calendarViewTypeSegment_Week"), value: "w" },
                { label: t("calendarViewTypeSegment_Month"), value: "m" },
              ]}
              segmentLabelClassName={styles.viewTypeSegmentLabel}
              callback={changeViewType}
            />
          </div>
          <div
            dangerouslySetInnerHTML={{
              __html: filterBy
                ? t("calendarHeaderFilteredTeamSubtitle").replace("${teamName}", filterBy.name)
                : t("calendarHeaderAllTeamsSubtitle"),
            }}
          />
        </div>
        <div className="spacer-h-2" />
        <div className={styles.calendarNavigation}>
          {viewType == "m" && (
            <>
              <Button
                heightVariant={ButtonHeight.short}
                variant={squeezedView ? ButtonVariants.filled2 : ButtonVariants.filled}
                onClick={toggleSquezeedView}
                className={styles.squeezedViewToggleButton}
              >
                <IoScan />
              </Button>
              <div className="spacer-w-1" />
            </>
          )}

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

          <Button onClick={prevPeriod}>
            <IoCaretBack />
          </Button>
          <Button onClick={today}>
            <IoEllipse size={7} />
          </Button>
          <Button onClick={nextPeriod}>
            <IoCaretForward />
          </Button>
        </div>
      </div>
    </>
  );
};

export default CalendarHeader;
