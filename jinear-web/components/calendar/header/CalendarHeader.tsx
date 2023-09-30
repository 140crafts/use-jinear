import Button, { ButtonHeight, ButtonVariants } from "@/components/button";
import SegmentedControl from "@/components/segmentedControl/SegmentedControl";
import { WorkspaceDto } from "@/model/be/jinear-core";
import { popTeamPickerModal } from "@/store/slice/modalSlice";
import { useAppDispatch } from "@/store/store";
import Logger from "@/utils/logger";
import { addMonths, addWeeks, format, startOfToday, startOfWeek } from "date-fns";
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
      const prev = viewType == "MONTH" ? addMonths(viewingDate, -1) : addWeeks(startOfWeek(viewingDate, { weekStartsOn: 1 }), -1);
      setViewingDate?.(prev);
    }
  };

  const nextPeriod = () => {
    if (viewingDate) {
      const next = viewType == "MONTH" ? addMonths(viewingDate, 1) : addWeeks(startOfWeek(viewingDate, { weekStartsOn: 1 }), 1);
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
    if (value && (value == "MONTH" || value == "WEEK")) {
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
              defaultIndex={["WEEK", "MONTH"].indexOf(viewType)}
              segments={[
                { label: t("calendarViewTypeSegment_Week"), value: "WEEK" },
                { label: t("calendarViewTypeSegment_Month"), value: "MONTH" },
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
        <div className="spacer-h-1" />
        <div className={styles.calendarNavigation}>
          {viewType == "MONTH" && (
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
